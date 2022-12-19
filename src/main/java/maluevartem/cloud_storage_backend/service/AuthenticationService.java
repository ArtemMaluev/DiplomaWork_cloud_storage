package maluevartem.cloud_storage_backend.service;

import lombok.RequiredArgsConstructor;
import maluevartem.cloud_storage_backend.dto.UserDto;
import maluevartem.cloud_storage_backend.entity.UserEntity;
import maluevartem.cloud_storage_backend.exception.UserNotFoundException;
import maluevartem.cloud_storage_backend.repository.UserRepository;
import maluevartem.cloud_storage_backend.security.JWTUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    public String readUserByLogin(UserDto user) {
        UserEntity userEntity = userRepository.findUserByLogin(user.getLogin()).orElseThrow(()
                -> new UserNotFoundException("Пользователь не найден", 0));
        return jwtUtil.generateToken(userEntity);
    }

    public String logout(String authToken, HttpServletRequest request,
                         HttpServletResponse response) {
        // TODO реализовать
        return null;
    }

}
