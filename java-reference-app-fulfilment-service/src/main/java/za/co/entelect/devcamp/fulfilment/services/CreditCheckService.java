package za.co.entelect.devcamp.fulfilment.services;

import java.io.IOException;
import java.time.Instant;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.fulfilment.dto.KycDto;
import za.co.entelect.devcamp.fulfilment.interfaces.ICreditCheckService;
import za.co.entelect.devcamp.fulfilment.interfaces.ICreditChecksApiClient;

@Service
public class CreditCheckService implements ICreditCheckService
{
    public final ICreditChecksApiClient creditChecksApiClient;

    public CreditCheckService(ICreditChecksApiClient creditChecksApiClient)
    {
        this.creditChecksApiClient = creditChecksApiClient;
    }

    @Override
    public String DoCreditCheck(Long customerId) throws IOException, Exception
    {
        try {
            return creditChecksApiClient.DoCreditCheck(customerId);
        }
        catch (IOException e) {
            throw new IOException(e.getMessage());
        }
        catch(Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }
}