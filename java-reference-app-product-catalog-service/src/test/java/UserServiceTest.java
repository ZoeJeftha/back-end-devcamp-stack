package za.co.entelect.devcamp.productcatalog.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.productcatalog.dto.UserDto;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;
import za.co.entelect.devcamp.productcatalog.model.User;
import za.co.entelect.devcamp.productcatalog.repository.UserRepository;
import za.co.entelect.devcamp.productcatalog.requests.CreateUserRequest;
import za.co.entelect.devcamp.productcatalog.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest
{
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void CreateUser_shouldReturnCreatedUser() throws Exception
    {
        CreateUserRequest request = new CreateUserRequest();

        request.setEmail("abc@gmail.com");
        request.setPassword("password");
        request.setRole("customer");

        User user = new User();
        user.setEmail("abc@gmail.com");
        user.setPassword("encoded-password");
        user.setRole("customer");

        when(passwordEncoder.encode("password"))
                .thenReturn("encoded-password");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserDto result = userService.CreateUser(request);

        assertEquals("abc@gmail.com", result.getEmail());
        assertEquals("customer", result.getRole());

        verify(userRepository).save(user);
    }

    @Test
    void LoadByUsername_shouldReturnUserDto()
    {
        String username = "abc@gmail.com";

        User user = new User();
        user.setUserId(1L);
        user.setEmail("abc@gmail.com");
        user.setRole("customer");

       when(userRepository.findFirstByEmailIgnoreCase(username))
               .thenReturn(Optional.of(user));

       UserDto result = userService.LoadUserByUsername(username);

       assertEquals(1L, result.getId());
       assertEquals("abc@gmail.com", result.getEmail());
       assertEquals("customer", result.getRole());

        verify(userRepository).findFirstByEmailIgnoreCase(username);
    }

    @Test
    void LoadByUsername_shouldThrowExceptionWhenUserDoesNotExist()
    {
        String username = "abc@gmail.com";
        when(userRepository.findFirstByEmailIgnoreCase(username))
                .thenReturn(Optional.empty());

        NotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.LoadUserByUsername(username));

        assertEquals("User not found", exception.getMessage());
    }

}