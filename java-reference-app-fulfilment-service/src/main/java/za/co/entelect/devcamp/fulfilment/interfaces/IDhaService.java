
package za.co.entelect.devcamp.fulfilment.interfaces;

import za.co.entelect.devcamp.fulfilment.dha.model.DuplicateIDDocumentCheckResponse;
import za.co.entelect.devcamp.fulfilment.dha.model.LivingStatusResponse;
import za.co.entelect.devcamp.fulfilment.dha.model.MaritalStatusResponse;

public interface IDhaService
{
    boolean DoMaritalCheck(Long idNumber) throws Exception ;

    boolean DoDuplicateIdCheck(Long idNumber) throws Exception ;

    boolean DoLivingStatusCheck(Long idNumber) throws Exception ;
}

