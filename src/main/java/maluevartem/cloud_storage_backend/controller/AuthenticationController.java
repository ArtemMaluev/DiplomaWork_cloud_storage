package maluevartem.cloud_storage_backend.controller;

import maluevartem.cloud_storage_backend.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class AuthenticationController {

    public AuthenticationService authenticationService;
    @PostMapping("/login")
    public String authenticationLogin() {
        return "auth-token";
    }

    @PostMapping("/logout")
    public void logout() {

    }
}
