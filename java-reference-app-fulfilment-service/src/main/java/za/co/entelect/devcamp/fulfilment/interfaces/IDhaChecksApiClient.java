
package za.co.entelect.devcamp.fulfilment.interfaces;

import java.io.IOException;
import za.co.entelect.devcamp.fulfilment.dha.model.DuplicateIDDocumentCheckResponse;
import za.co.entelect.devcamp.fulfilment.dha.model.LivingStatusResponse;
import za.co.entelect.devcamp.fulfilment.dha.model.MaritalStatusResponse;

public interface IDhaChecksApiClient
{
    MaritalStatusResponse DoMaritalCheck(String token, Long idNumber);

    DuplicateIDDocumentCheckResponse DoDuplicateIdCheck(String token, Long idNumber);

    LivingStatusResponse DoLivingStatusCheck(String token, Long idNumber);
}

