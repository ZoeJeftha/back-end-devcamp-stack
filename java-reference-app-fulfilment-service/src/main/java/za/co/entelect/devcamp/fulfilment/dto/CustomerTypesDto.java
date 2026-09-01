package za.co.entelect.devcamp.fulfilment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTypesDto {

    private Long id;
    private String name;
    private String description;
}