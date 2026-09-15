package za.co.entelect.devcamp.productcatalog.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.entelect.devcamp.productcatalog.dto.ProductDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerChecksResponse {
    public String customerCheck;
    public Boolean hasPassed;
}
