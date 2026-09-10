package za.co.entelect.devcamp.fraudcheck.service;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/FraudCheck")
public class FraudCheckService {

    @PostMapping(
            consumes = MediaType.TEXT_XML_VALUE,
            produces = MediaType.TEXT_XML_VALUE
    )
    public ResponseEntity<String> fraudCheck(@RequestBody String soapRequest) {

        String response =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                        "<soap:Envelope " +
                        "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<soap:Body>" +
                        "<fraudCheckResponse xmlns=\"http://localhost:8085/fraudcheck\">" +
                        "<bankStatus>PASS</bankStatus>" +
                        "<nationalStatus>PASS</nationalStatus>" +
                        "</fraudCheckResponse>" +
                        "</soap:Body>" +
                        "</soap:Envelope>";

        return ResponseEntity.ok(response);
    }
}