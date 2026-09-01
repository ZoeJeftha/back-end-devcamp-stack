
package za.co.entelect.devcamp.fulfilment.interfaces;

import java.io.IOException;
import za.co.entelect.devcamp.fulfilment.dto.DuplicateIdStatusDto;
import za.co.entelect.devcamp.fulfilment.dto.LivingStatusDto;
import za.co.entelect.devcamp.fulfilment.dto.MaritalStatusesDto;

public interface IDhaChecksApiClient
{
    MaritalStatusesDto DoMaritalCheck(String token, Long idNumber);

    DuplicateIdStatusDto DoDuplicateIdCheck(String token, Long idNumber);

    LivingStatusDto DoLivingStatusCheck(String token, Long idNumber);
}

