package za.co.entelect.devcamp.productcatalog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.entelect.devcamp.productcatalog.dto.UserDto;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;
import za.co.entelect.devcamp.productcatalog.responses.ApiResponse;
import za.co.entelect.devcamp.productcatalog.service.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/user")
public class UserController {

    public final IUserService userService;

    public UserController(IUserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse<UserDto>> GetUserByEmail(@PathVariable String email)
    {
        try
        {
            UserDto user = userService.LoadUserByUsername(email);
            ApiResponse<UserDto> response = new ApiResponse<UserDto>(true, "User found successfully", user);
            return ResponseEntity.ok(response);
        }
        catch(NotFoundException e)
        {
            ApiResponse<UserDto> response = new ApiResponse<UserDto>(false, "Order not found",null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch(Exception e) {
            ApiResponse<UserDto> response = new ApiResponse<UserDto>(false, "Failed to retrieve order: " + e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
