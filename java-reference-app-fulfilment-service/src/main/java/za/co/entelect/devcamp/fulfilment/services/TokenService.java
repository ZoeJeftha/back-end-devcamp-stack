package za.co.entelect.devcamp.fulfilment.services;

import java.time.Instant;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.fulfilment.dto.KycDto;
import za.co.entelect.devcamp.fulfilment.interfaces.ITokenService;

@Service
public class TokenService implements ITokenService
{
    public final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder)
    {
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public String GetToken(String username)
    {
        Instant now = Instant.now();
        Long expiry = 3600L;
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(username)
                .claim("role", "customer")
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return token;
    }

}