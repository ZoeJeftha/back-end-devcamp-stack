package za.co.entelect.devcamp.fulfilment.responses;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.entelect.devcamp.fulfilment.requests.SaveCustomerChecksRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StrategyResponse {

    List<SaveCustomerChecksRequest> saveCustomerChecksRequest;

    boolean passed;
}
