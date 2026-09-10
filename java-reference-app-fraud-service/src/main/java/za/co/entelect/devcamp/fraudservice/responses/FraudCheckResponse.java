package za.co.entelect.devcamp.fraudservice.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FraudCheckResponse {

    private String bankStatus;
    private String nationalStatus;
}