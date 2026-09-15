package za.co.entelect.devcamp.productcatalog.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DocumentService implements IDocumentService {

    @Autowired
    public DocumentService()
    {
    }

    public ResponseEntity<byte[]> CreateDocument() throws FileNotFoundException,DocumentException,IOException
    {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("iTextHelloWorld.pdf"));
            log.info("---------------------------document: "+ document);
            document.open();

            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            log.info("---------------------------font: "+ font);
            Chunk chunk = new Chunk("Hello World", font);
            log.info("---------------------------chunk: "+ chunk);
            document.add(chunk);
            document.close();

            log.info("---------------------File(\"iTextHelloWorld.pdf\").getAbsolutePath()"+ new File("iTextHelloWorld.pdf").getAbsolutePath());
            Path path = Paths.get("/opt/app/iTextHelloWorld.pdf");
            byte[] document2 = Files.readAllBytes(path);

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"iTextHelloWorld.pdf\""
                    )
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(document2);

        }
        catch(FileNotFoundException e)
        {
            throw new FileNotFoundException(e.getMessage());
        }
        catch(DocumentException e)
        {
            throw new DocumentException(e.getMessage());
        }
        catch(IOException e)
        {
            throw new IOException(e.getMessage());
        }
    }
}