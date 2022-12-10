package maluevartem.cloud_storage_backend.controller;

import maluevartem.cloud_storage_backend.service.RegistrationService;
import org.springframework.web.bind.annotation.*;
@RestController
public class RegistrationController{
    private RegistrationService registrationService;
    @PostMapping("/create")
    public String registerUser() {
        return "register User";
    }
    @PutMapping("/update/{id}")
    public String updateUser() {
        return "updateUser";
    }
    @GetMapping("/{id}")
    public String getUserById() {
        return "getUserById";
    }
    @DeleteMapping("/delete/{id}")
    public void deleteUserById() {
    }
}

