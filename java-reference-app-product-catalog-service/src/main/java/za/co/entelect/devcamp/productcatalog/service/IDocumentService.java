
package za.co.entelect.devcamp.productcatalog.service;

import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface IDocumentService
{
    ResponseEntity<byte[]> CreateDocument() throws FileNotFoundException,DocumentException,IOException;
}

