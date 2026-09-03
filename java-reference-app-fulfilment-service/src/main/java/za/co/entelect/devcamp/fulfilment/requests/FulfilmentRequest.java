package za.co.entelect.devcamp.fulfilment.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FulfilmentRequest {

    private Long id;
    private String idNumber;
    private String fulfilmentType;
    private String username;
}