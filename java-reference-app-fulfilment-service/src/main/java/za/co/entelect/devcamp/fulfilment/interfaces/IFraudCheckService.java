
package za.co.entelect.devcamp.fulfilment.interfaces;

import java.io.IOException;
import za.co.entelect.devcamp.fulfilment.dto.FraudCheckResponseDto;

public interface IFraudCheckService
{
    boolean DoFraudCheck(Long customerId, String idNumber) throws Exception;
}

