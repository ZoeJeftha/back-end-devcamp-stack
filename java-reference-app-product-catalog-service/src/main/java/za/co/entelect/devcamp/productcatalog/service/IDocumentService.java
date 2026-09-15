
package za.co.entelect.devcamp.productcatalog.service;

import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import za.co.entelect.devcamp.productcatalog.responses.OrderResponse;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;

public interface IDocumentService
{
    ResponseEntity<byte[]> CreateDocument(List<OrderResponse> orders, CustomerDto customerDto) throws FileNotFoundException,DocumentException,IOException, Exception;
}

