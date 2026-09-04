package za.co.entelect.devcamp.productcatalog.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private Long customerId;
    private String status;
    private String contractUrl;
    private Long productId;
}