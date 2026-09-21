package za.co.entelect.devcamp.productcatalog.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.entelect.devcamp.productcatalog.client.IAuthApiClient;
import za.co.entelect.devcamp.productcatalog.requests.LoginRequest;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private IAuthApiClient authApiClient;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp()
    {
        authService = new AuthService("system-user", "system-password", authApiClient);
    }

    @Test
    void getSystemToken_shouldReturnToken() throws Exception
    {
        when(authApiClient.GetSystemToken(any(LoginRequest.class)))
                .thenReturn("test-token");

        String result = authService.GetSystemToken();

        assertEquals("test-token", result);

        verify(authApiClient).GetSystemToken(any(LoginRequest.class));
    }

    @Test
    void getSystemToken_shouldSendCorrectCredentials() throws Exception {

        when(authApiClient.GetSystemToken(
                org.mockito.ArgumentMatchers.any(LoginRequest.class)
        )).thenReturn("test-token");

        authService.GetSystemToken();

        ArgumentCaptor<LoginRequest> captor =
                ArgumentCaptor.forClass(LoginRequest.class);

        verify(authApiClient).GetSystemToken(captor.capture());

        LoginRequest request = captor.getValue();

        assertEquals("system-user", request.getUsername());
        assertEquals("system-password", request.getPassword());
    }
}