
package za.co.entelect.devcamp.fulfilment.interfaces;

import java.io.IOException;
import za.co.entelect.devcamp.fulfilment.dto.FraudCheckResponseDto;


public interface IFraudCheckApiClient
{
    FraudCheckResponseDto DoFraudCheck(Long customerId, String idNumber) throws IOException;
}