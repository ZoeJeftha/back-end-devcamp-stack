
package za.co.entelect.devcamp.fulfilment.interfaces;

import java.io.IOException;

public interface ICreditCheckService
{
    boolean DoCreditCheck(Long customerId) throws IOException, Exception;
}

