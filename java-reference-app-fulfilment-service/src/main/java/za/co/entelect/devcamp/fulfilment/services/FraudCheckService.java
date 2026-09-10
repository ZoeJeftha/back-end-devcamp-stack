package za.co.entelect.devcamp.fulfilment.services;

import java.io.IOException;
import java.time.Instant;
import za.co.entelect.devcamp.fulfilment.dto.FraudCheckResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.fulfilment.interfaces.IFraudCheckService;
import za.co.entelect.devcamp.fulfilment.interfaces.IFraudCheckApiClient;

@Slf4j
@Service
public class FraudCheckService implements IFraudCheckService
{
    public final IFraudCheckApiClient fraudCheckApiClient;

    public FraudCheckService(IFraudCheckApiClient fraudCheckApiClient)
    {
        this.fraudCheckApiClient = fraudCheckApiClient;
    }

    @Override
    public boolean DoFraudCheck(Long customerId, String idNumber) throws Exception
    {
        try {
            FraudCheckResponseDto fraudCheck = fraudCheckApiClient.DoFraudCheck(customerId, idNumber);
            return fraudCheck.getBankStatus().equalsIgnoreCase("PASS") && fraudCheck.getNationalStatus().equalsIgnoreCase("PASS");
        }
        catch(Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }
}