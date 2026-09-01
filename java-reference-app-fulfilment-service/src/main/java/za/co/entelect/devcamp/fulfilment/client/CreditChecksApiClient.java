package za.co.entelect.devcamp.fulfilment.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import za.co.entelect.devcamp.fulfilment.interfaces.ICreditChecksApiClient;


@Slf4j
@Component
public class CreditChecksApiClient implements ICreditChecksApiClient {

    @Override
    public String DoCreditCheck(Long customerId) throws IOException
    {
        String creditCheckUrl = "http://devcamp-creditcheck-service:80/CreditCheck";

        String xmlInput =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                        + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" "
                        + "xmlns:web=\"entelect:devcamp:creditcheckservice\">"
                        + "  <soap:Body>"
                        + "    <web:CreditCheck>"
                        + "      <customerId>" + customerId + "</customerId>"
                        + "    </web:CreditCheck>"
                        + "  </soap:Body>"
                        + "</soap:Envelope>";

        URL url = new URL(creditCheckUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        String auth = "user:password";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        connection.setRequestProperty("Authorization", "Basic " + encodedAuth);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(xmlInput.getBytes());
        }

        InputStream responseStream = connection.getInputStream();
        String responseXml = new String(responseStream.readAllBytes());
        System.out.println("Raw SOAP Response: " + responseXml);

        String result = responseXml.replaceAll("(?s).*<CreditCheckResult>(.*?)</CreditCheckResult>.*", "$1");
        System.out.println("CreditCheck returned: " + result);
        return result;
    }
}