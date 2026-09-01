package za.co.entelect.devcamp.fulfilment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RestController
@RequestMapping("/v1")
public class FulfilmentController {

    public FulfilmentController()
    {
    }

    @GetMapping("/stuff")
    public String getProducts()
    {
        return "Temp";
    }
}
