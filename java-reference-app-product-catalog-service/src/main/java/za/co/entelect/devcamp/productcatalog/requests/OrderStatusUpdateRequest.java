package za.co.entelect.devcamp.productcatalog.requests;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.entelect.devcamp.productcatalog.enums.OrderStatusEnum;
import za.co.entelect.devcamp.productcatalog.requests.SaveCustomerChecksRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusUpdateRequest {
    private Long orderId;
    private OrderStatusEnum status;
    private List<SaveCustomerChecksRequest> saveCustomerChecks;
}