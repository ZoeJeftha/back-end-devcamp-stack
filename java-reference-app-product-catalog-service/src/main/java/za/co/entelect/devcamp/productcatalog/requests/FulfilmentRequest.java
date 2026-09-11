package za.co.entelect.devcamp.productcatalog.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FulfilmentRequest {

    private Long id;
    private String idNumber;
    private String fulfilmentType;
    private String username;
    private Long orderId;
}