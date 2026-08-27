package za.co.entelect.devcamp.productcatalog.requests;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProductEligibilityRequest {
    public Long productId;
    public Long customerTypesId;
}