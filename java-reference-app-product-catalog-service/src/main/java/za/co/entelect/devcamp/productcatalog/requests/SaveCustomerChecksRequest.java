package za.co.entelect.devcamp.productcatalog.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.entelect.devcamp.productcatalog.enums.CustomerChecksEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveCustomerChecksRequest {
    private CustomerChecksEnum customerCheck;
    private boolean hasPassed;
    private Long orderId;
}