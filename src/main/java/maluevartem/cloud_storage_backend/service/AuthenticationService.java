package maluevartem.cloud_storage_backend.service;

import lombok.RequiredArgsConstructor;
import maluevartem.cloud_storage_backend.dto.UserDto;
import maluevartem.cloud_storage_backend.entity.UserEntity;
import maluevartem.cloud_storage_backend.exception.IncorrectDataEntry;
import maluevartem.cloud_storage_backend.exception.UserNotFoundException;
import maluevartem.cloud_storage_backend.model.Token;
import maluevartem.cloud_storage_backend.repository.UserRepository;
import maluevartem.cloud_storage_backend.security.JWTToken;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JWTToken jwtToken;

    public Token readUserByLogin(UserDto user) {
        final UserEntity userFromDatabase = userRepository.findUserByLogin(user.getLogin()).orElseThrow(()
                -> new UserNotFoundException("Пользователь не найден", 0));
        if (userFromDatabase.getPassword().equals(user.getPassword())) {
            final String token = jwtToken.generateToken(userFromDatabase);
            return new Token(token);
        } else {
            throw new IncorrectDataEntry("Неправильный пароль", 0);
        }
    }

    public String logout(String authToken, HttpServletRequest request, HttpServletResponse response) {
        // TODO реализовать
        return null;
    }

}
