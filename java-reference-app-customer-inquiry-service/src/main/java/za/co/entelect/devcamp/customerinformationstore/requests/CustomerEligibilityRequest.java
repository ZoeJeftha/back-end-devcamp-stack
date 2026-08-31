package za.co.entelect.devcamp.customerinformationstore.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEligibilityRequest {
    public Long productId;
    public List<Long> accountIds;
    public Long customerTypesId;
}