package maluevartem.cloud_storage_backend.controller;

import lombok.RequiredArgsConstructor;
import maluevartem.cloud_storage_backend.config.AuthenticationConfigConstants;
import maluevartem.cloud_storage_backend.dto.UserDto;
import maluevartem.cloud_storage_backend.model.Token;
import maluevartem.cloud_storage_backend.service.AuthenticationService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Token> authenticationLogin(@RequestBody UserDto userDto) {
        Token token = authenticationService.authenticationLogin(userDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = AuthenticationConfigConstants.AUTH_TOKEN) String authToken,
                                       HttpServletRequest request, HttpServletResponse response) {
        String userLogout = authenticationService.logout(authToken, request, response);
        if (userLogout == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
