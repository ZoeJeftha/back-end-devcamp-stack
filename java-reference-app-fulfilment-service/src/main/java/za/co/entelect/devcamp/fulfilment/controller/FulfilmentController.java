package za.co.entelect.devcamp.fulfilment.controller;

import java.io.IOException;
import localhost._8085.fraudcheck.FraudCheckRequest;
import localhost._8085.fraudcheck.FraudCheckResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.entelect.devcamp.fulfilment.dha.model.DuplicateIDDocumentCheckResponse;
import za.co.entelect.devcamp.fulfilment.dto.FraudCheckResponseDto;
import za.co.entelect.devcamp.fulfilment.dha.model.LivingStatusResponse;
import za.co.entelect.devcamp.fulfilment.dha.model.MaritalStatusResponse;
import za.co.entelect.devcamp.fulfilment.dto.DuplicateIdStatusDto;
import za.co.entelect.devcamp.fulfilment.dto.KycDto;
import za.co.entelect.devcamp.fulfilment.enums.OrderStatusEnum;
import za.co.entelect.devcamp.fulfilment.interfaces.ICreditCheckService;
import za.co.entelect.devcamp.fulfilment.interfaces.IDhaService;
import za.co.entelect.devcamp.fulfilment.interfaces.IFraudCheckService;
import za.co.entelect.devcamp.fulfilment.interfaces.IKycCheckService;
import za.co.entelect.devcamp.fulfilment.interfaces.IProductService;
import za.co.entelect.devcamp.fulfilment.requests.OrderStatusUpdateRequest;
import za.co.entelect.devcamp.fulfilment.responses.OrderResponse;

@Slf4j
@RestController
@RequestMapping("/v1")
public class FulfilmentController {

    public final ICreditCheckService creditCheckService;
    public final IDhaService dhaService;
    public final IKycCheckService kycCheckService;
    public final IFraudCheckService fraudCheckService;
    public final IProductService productService;

    public FulfilmentController(ICreditCheckService creditCheckService,
                                IDhaService dhaService,
                                IKycCheckService kycCheckService,
                                IFraudCheckService fraudCheckService,
                                IProductService productService)
    {
        this.creditCheckService = creditCheckService;
        this.dhaService = dhaService;
        this.kycCheckService = kycCheckService;
        this.fraudCheckService = fraudCheckService;
        this.productService = productService;
    }

    @GetMapping("/credit-check")
    public boolean DoCreditCheck()
    {
        try {
            return creditCheckService.DoCreditCheck(1L);
        }
        catch (IOException e) {
            log.info("Credit Check failed IOException: " + e.getMessage());
            return false;
        }
        catch(Exception e)
        {
            log.info("Credit Check failed: " + e.getMessage());
            return false;
        }
    }

    @GetMapping("/dha-marital-check")
    public boolean DoDhaMaritalCheck()
    {
        try
        {
            return dhaService.DoMaritalCheck(9001010000081L);
        }
        catch(Exception e)
        {
            log.info("Marital Status Check failed: " + e.getMessage());
            return false;
        }
    }

    @GetMapping("/dha-duplicate-id-check")
    public boolean DoDhaDuplicateIdCheck()
    {
        try {
            return dhaService.DoDuplicateIdCheck(9001010000081L);
        }
        catch(Exception e)
        {
            log.info("Duplicate id check failed: " + e.getMessage());
            return false;
        }
    }


    @GetMapping("/dha-living-status-check")
    public boolean DoLivingStatusCheck()
    {
        try {
            return dhaService.DoLivingStatusCheck(9001010000081L);
        }
        catch(Exception e)
        {
            log.info("Living status check failed: " + e.getMessage());
            return false;
        }
    }

    @GetMapping("/kyc-check")
    public boolean DoKycCheck()
    {
        try {
            return kycCheckService.DoKycCheck(1L);
        }
        catch(Exception e)
        {
            log.info("Kyc Check failed: " + e.getMessage());
            return false;
        }
    }

    @PostMapping("/fraud-check")
    public boolean DoFraudCheck()
    {
        try {
            return fraudCheckService.DoFraudCheck(1L, "9808030138082L");
        }
        catch(IOException e)
        {
            log.info("Fraud Check failed: " + e.getMessage());
            return false;
        }
        catch(Exception e)
        {
            log.info("Fraud Check failed: " + e.getMessage());
            return false;
        }
    }

    @PostMapping("/update-order/{status}")
    public OrderResponse UpdateOrder(@PathVariable int status)
    {
        try {
            OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();

            if(status == 1) {
                request.setStatus(OrderStatusEnum.ACCEPTED);
            }
            else if(status == 2)
            {
            request.setStatus(OrderStatusEnum.REJECTED);
            }
            else
            {
                request.setStatus(OrderStatusEnum.PENDING);
            }
            request.setOrderId(6L);

            return productService.UpdateOrder(request);
        }
        catch(Exception e)
        {
            log.info("Update order failed: " + e.getMessage());
            return null;
        }
    }
}
