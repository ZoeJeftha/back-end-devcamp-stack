package za.co.entelect.devcamp.fulfilment.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.entelect.devcamp.fulfilment.enums.CustomerChecksEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveCustomerChecksRequest {
    private CustomerChecksEnum customerCheck;
    private boolean hasPassed;
    private Long orderId;
}