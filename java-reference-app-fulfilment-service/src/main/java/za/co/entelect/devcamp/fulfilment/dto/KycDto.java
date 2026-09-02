package za.co.entelect.devcamp.fulfilment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KycDto {

    private Boolean primaryIndicator;
    private Boolean secondaryIndicator;
    private String taxCompliance;
}