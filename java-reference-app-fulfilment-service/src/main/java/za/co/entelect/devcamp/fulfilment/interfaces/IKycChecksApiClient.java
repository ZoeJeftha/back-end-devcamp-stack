
package za.co.entelect.devcamp.fulfilment.interfaces;

import java.io.IOException;
import za.co.entelect.devcamp.fulfilment.dto.KycDto;

public interface IKycChecksApiClient
{
    KycDto DoKycCheck(String token, String customerId);
}

