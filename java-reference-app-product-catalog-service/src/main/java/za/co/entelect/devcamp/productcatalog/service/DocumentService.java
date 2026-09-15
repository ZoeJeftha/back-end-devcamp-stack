package za.co.entelect.devcamp.productcatalog.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Phrase;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.responses.CustomerChecksResponse;
import za.co.entelect.devcamp.productcatalog.responses.OrderResponse;

@Slf4j
@Service
public class DocumentService implements IDocumentService {

    @Autowired
    public DocumentService()
    {
    }

    public ResponseEntity<byte[]> CreateDocument(List<OrderResponse> orders, CustomerDto customer) throws FileNotFoundException,DocumentException,IOException,Exception
    {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("iTextHelloWorld.pdf"));
            log.info("---------------------------document: "+ document);
            document.open();

            Font headingFont = FontFactory.getFont(
                    FontFactory.TIMES_ROMAN,
                    20,
                    Font.BOLD,
                    BaseColor.BLACK
            );

            document.add(new Paragraph("\n"));
            Chunk headingChunk = new Chunk("Entelect Product Shop", headingFont);

            document.add(headingChunk);
            document.add(new Paragraph("\n"));

            Font subHeadingFont = FontFactory.getFont(
                    FontFactory.TIMES_ROMAN,
                    16,
                    Font.BOLD,
                    BaseColor.BLACK
            );

            Chunk subHeadingChunk = new Chunk("Customer Contract", subHeadingFont);
            document.add(subHeadingChunk);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Dear "+ customer.getFirstName() + " " +  customer.getLastName()));
            document.add(new Paragraph("The status of your orders are as follows:"));
            document.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(3);

            table.setWidthPercentage(100);
            table.setWidths(new float[]{1f, 2f, 2f});

            //addTableHeader(table);
            addRows(table, customer,orders);

            document.add(table);
            document.add(new Paragraph("\n"));
            Chunk orderDetailsChunk= new Chunk("Order details", subHeadingFont);
            document.add(orderDetailsChunk);
            document.add(new Paragraph("Please note orders in a pending state will not be included in this list."));
            document.add(new Paragraph("\n"));
            addOrderDetailsTables(document,orders);

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
        catch(Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

//    private void addTableHeader(PdfPTable table) {
//        Stream.of("column header 1", "column header 2", "column header 3")
//                .forEach(columnTitle -> {
//                    PdfPCell header = new PdfPCell();
//                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
//                    header.setBorderWidth(2);
//                    header.setPhrase(new Phrase(columnTitle));
//                    table.addCell(header);
//                });
//    }

    private void addRows(PdfPTable table, CustomerDto customer, List<OrderResponse> orders) {
        Stream.of("Order Number", "Product Name", "Status")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });

        for(OrderResponse order: orders)
        {
            table.addCell(""+ order.getOrderId());
            table.addCell(order.getProduct().getName());
            table.addCell(order.getStatus());
        }
    }

    private void addOrderDetailsTables(Document document,List<OrderResponse> orders) throws DocumentException
    {
        try {
            for (OrderResponse order : orders) {

                if(order.getCustomerChecks().size() >0) {
                    Font orderHeadingFont = FontFactory.getFont(
                            FontFactory.TIMES_ROMAN,
                            12,
                            Font.BOLD,
                            BaseColor.BLACK
                    );

                    Chunk orderHeading = new Chunk("Order #" + order.getOrderId() + ": " + order.getProduct().getName(), orderHeadingFont);

                    document.add(orderHeading);

                    PdfPTable table = new PdfPTable(2);
                    table.setWidthPercentage(100);
                    table.setWidths(new float[]{1f, 2f});

                    for (CustomerChecksResponse check : order.getCustomerChecks()) {
                        table.addCell(check.getCustomerCheck());
                        if (check.getHasPassed()) {
                            table.addCell("PASSED");
                        } else {
                            table.addCell("FAILED");
                        }
                    }

                    document.add(table);
                    document.add(new Paragraph("\n"));
                }
            }
        }
        catch(DocumentException e)
        {
            throw new DocumentException(e.getMessage());
        }
    }
}