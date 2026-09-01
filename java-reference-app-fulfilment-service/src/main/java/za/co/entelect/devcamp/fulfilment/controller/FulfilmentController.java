package za.co.entelect.devcamp.fulfilment.controller;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.entelect.devcamp.fulfilment.interfaces.ICreditChecksApiClient;

@Slf4j
@RestController
@RequestMapping("/v1")
public class FulfilmentController {

    public final ICreditChecksApiClient creditChecksApiClient;

    public FulfilmentController(ICreditChecksApiClient creditChecksApiClient)
    {
        this.creditChecksApiClient = creditChecksApiClient;
    }

    @GetMapping("/stuff")
    public String DoCreditCheck()
    {
        try {
            return creditChecksApiClient.DoCreditCheck(1L);
        }
        catch (IOException e) {
            return "IOException thrown: "+ e.getMessage();
        }
        catch(Exception e)
        {
            return "Exception thrown: "+ e.getMessage();
        }
    }
}
