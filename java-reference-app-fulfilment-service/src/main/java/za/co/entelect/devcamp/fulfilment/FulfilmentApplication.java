package za.co.entelect.devcamp.fulfilment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import za.co.entelect.devcamp.fulfilment.requests.FulfilmentRequest;
import za.co.entelect.devcamp.fulfilment.requests.SaveCustomerChecksRequest;

import java.util.List;

@SpringBootApplication
public class FulfilmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(FulfilmentApplication.class, args);
    }
}
