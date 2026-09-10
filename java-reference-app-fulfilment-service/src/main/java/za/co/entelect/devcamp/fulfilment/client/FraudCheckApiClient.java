package za.co.entelect.devcamp.fulfilment.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import za.co.entelect.devcamp.fulfilment.dto.FraudCheckResponseDto;
import za.co.entelect.devcamp.fulfilment.interfaces.IFraudCheckApiClient;

@Slf4j
@Component
public class FraudCheckApiClient implements IFraudCheckApiClient {

    @Value("${fraud-check.url}")
    private String fraudCheckUrl;

    @Override
    public FraudCheckResponseDto DoFraudCheck(Long customerId, String idNumber) throws IOException {

        log.info("FraudCheckApiClient customerId: {}", customerId);
        log.info("FraudCheckApiClient idNumber: {}", idNumber);
        log.info("FraudCheckApiClient fraudCheckUrl: {}", fraudCheckUrl);

        String xmlInput =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                        + "<soap:Envelope "
                        + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" "
                        + "xmlns:web=\"entelect:devcamp:fraudcheckservice\">"
                        + "<soap:Body>"
                        + "<web:FraudCheck>"
                        + "<customerId>" + customerId + "</customerId>"
                        + "<idNumber>" + idNumber + "</idNumber>"
                        + "</web:FraudCheck>"
                        + "</soap:Body>"
                        + "</soap:Envelope>";

        URL url = new URL(fraudCheckUrl);

        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.setRequestProperty(
                "Content-Type",
                "text/xml; charset=utf-8"
        );

        connection.setRequestProperty(
                "SOAPAction",
                "FraudCheck"
        );

        try (OutputStream os = connection.getOutputStream()) {
            os.write(xmlInput.getBytes());
        }

        int responseCode = connection.getResponseCode();

        log.info("FraudCheck response code: {}", responseCode);

        InputStream responseStream;

        if (responseCode >= 200 && responseCode < 300) {
            responseStream = connection.getInputStream();
        } else {
            responseStream = connection.getErrorStream();
        }

        String responseXml = new String(responseStream.readAllBytes());

        log.info("FraudCheck response: {}", responseXml);

        return parseFraudCheckResponse(responseXml);
    }

    private FraudCheckResponseDto parseFraudCheckResponse(String responseXml) throws IOException {
        try {
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            factory.setNamespaceAware(true);

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(
                    new java.io.ByteArrayInputStream(responseXml.getBytes()));

            NodeList bankStatusNodes = document.getElementsByTagNameNS("*", "bankStatus");

            NodeList nationalStatusNodes = document.getElementsByTagNameNS("*", "nationalStatus");

            String bankStatus = bankStatusNodes.item(0).getTextContent();

            String nationalStatus = nationalStatusNodes.item(0).getTextContent();

            return new FraudCheckResponseDto(bankStatus, nationalStatus);

        } catch (Exception e) {
            throw new IOException("Failed to parse Fraud Check response: " + e.getMessage());
        }
    }
}
