
package za.co.entelect.devcamp.fulfilment.interfaces;

import java.io.IOException;

public interface ICreditChecksApiClient
{
    String DoCreditCheck(Long customerId) throws IOException;
}

