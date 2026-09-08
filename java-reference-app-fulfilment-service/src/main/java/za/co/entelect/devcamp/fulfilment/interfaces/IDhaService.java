
package za.co.entelect.devcamp.fulfilment.interfaces;

import za.co.entelect.devcamp.fulfilment.dha.model.DuplicateIDDocumentCheckResponse;
import za.co.entelect.devcamp.fulfilment.dha.model.LivingStatusResponse;
import za.co.entelect.devcamp.fulfilment.dha.model.MaritalStatusResponse;

public interface IDhaService
{
    MaritalStatusResponse DoMaritalCheck(Long idNumber) throws Exception ;

    DuplicateIDDocumentCheckResponse DoDuplicateIdCheck(Long idNumber) throws Exception ;

    LivingStatusResponse DoLivingStatusCheck(Long idNumber) throws Exception ;
}

