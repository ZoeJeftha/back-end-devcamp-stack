package za.co.entelect.devcamp.productcatalog.controller;

import com.itextpdf.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;
import za.co.entelect.devcamp.productcatalog.responses.ApiResponse;
import za.co.entelect.devcamp.productcatalog.responses.OrderResponse;
import za.co.entelect.devcamp.productcatalog.service.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/document")
public class DocumentController {

    public final ICustomerService customerService;
    public final IOrderService orderService;
    public final IDocumentService documentService;

    public DocumentController(ICustomerService customerService,
                              IOrderService orderService,
                              IDocumentService documentService) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.documentService = documentService;
    }

    @PostMapping()
    public ResponseEntity<?> CreateDocument(@AuthenticationPrincipal Jwt jwt) {
        try {
            String token = jwt.getTokenValue();
            String username = jwt.getSubject();
            CustomerDto customer = customerService.GetMyProfile(token,username);

            List<OrderResponse> orderResponse = orderService.GetMyOrders(customer);
            return documentService.CreateDocument(orderResponse, customer);
        }
        catch(FileNotFoundException e) {
            log.info("CreateDocument FileNotFoundException: "+ e.getMessage());
            ApiResponse<CustomerDto> response = new ApiResponse<CustomerDto>(false, "Failed to retreve document, file not found: "+ e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch(DocumentException e) {
            log.info("CreateDocument DocumentException: "+ e.getMessage());
            ApiResponse<CustomerDto> response = new ApiResponse<CustomerDto>(false, "Failed to retreve document, DocumentException thrown: "+ e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
        catch(IOException e) {
            log.info("CreateDocument IOException: "+ e.getMessage());
            ApiResponse<CustomerDto> response = new ApiResponse<CustomerDto>(false, "Failed to retreve document, IOException thrown: "+ e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
        catch(Exception e) {
            log.info("CreateDocument Exception: "+ e.getMessage());
            ApiResponse<CustomerDto> response = new ApiResponse<CustomerDto>(false, "Failed to create document: "+ e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
    }


    @GetMapping()
    public ResponseEntity<?> getOrderDocument(@AuthenticationPrincipal Jwt jwt) {
        try {
            String username = jwt.getSubject();
            String token = jwt.getTokenValue();
            CustomerDto customer = customerService.GetMyProfile(token, username);

            byte[] pdf = documentService.GetOrderDocument(customer.getId());

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"order_document.pdf\""
                    )
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        }
        catch(NotFoundException e) {
            ApiResponse<CustomerDto> response = new ApiResponse<CustomerDto>(false, "Failed to retreve document: "+ e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch(Exception e) {
            ApiResponse<CustomerDto> response = new ApiResponse<CustomerDto>(false, "Failed to retreve document: "+ e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
