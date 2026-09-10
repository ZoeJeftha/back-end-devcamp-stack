package za.co.entelect.devcamp.fraudservice.controller;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/v1")
public class FraudCheckController {

    private final FraudCheckService fraudCheckService;

    public FraudCheckController(FraudCheckService fraudCheckService) {
        this.fraudCheckService = fraudCheckService;
    }

    @PostMapping("/fraud-check")
    public ResponseEntity<FraudCheckResponse> fraudCheck(
            @RequestBody FraudCheckRequest request) {

        FraudCheckResponse response =
                fraudCheckService.fraudCheck(request);

        return ResponseEntity.ok(response);
    }
}