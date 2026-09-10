package za.co.entelect.devcamp.fraudservice.service;

import org.springframework.stereotype.Service;

@Service
public class FraudCheckService {

    private final FraudCheckSoapClient fraudCheckSoapClient;

    public FraudCheckService(FraudCheckSoapClient fraudCheckSoapClient) {
        this.fraudCheckSoapClient = fraudCheckSoapClient;
    }

    public FraudCheckResponse fraudCheck(FraudCheckRequest request) {

        // Call external SOAP service
        // Map SOAP response to our response

        return fraudCheckSoapClient.fraudCheck(request);
    }
}