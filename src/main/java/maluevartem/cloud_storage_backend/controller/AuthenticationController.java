package maluevartem.cloud_storage_backend.controller;

import lombok.RequiredArgsConstructor;
import maluevartem.cloud_storage_backend.dto.UserDto;
import maluevartem.cloud_storage_backend.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<String> authenticationLogin(@RequestBody UserDto userDto) {
        String token = authenticationService.readUserByLogin(userDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = "auth-token") String authToken,
                                       HttpServletRequest request, HttpServletResponse response) {
        authenticationService.logout(authToken, request, response);
        // TODO реализовать
        return null;
    }
}
