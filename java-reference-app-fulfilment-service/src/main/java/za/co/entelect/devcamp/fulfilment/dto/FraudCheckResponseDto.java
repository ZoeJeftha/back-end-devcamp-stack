package za.co.entelect.devcamp.fulfilment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FraudCheckResponseDto {

    private String bankStatus;
    private String nationalStatus;
}