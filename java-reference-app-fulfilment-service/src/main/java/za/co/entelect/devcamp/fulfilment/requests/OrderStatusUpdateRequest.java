package za.co.entelect.devcamp.fulfilment.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.entelect.devcamp.fulfilment.enums.OrderStatusEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusUpdateRequest {
    private Long orderId;
    private OrderStatusEnum status;
}