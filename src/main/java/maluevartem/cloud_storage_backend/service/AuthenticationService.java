package maluevartem.cloud_storage_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maluevartem.cloud_storage_backend.dto.UserDto;
import maluevartem.cloud_storage_backend.entity.UserEntity;
import maluevartem.cloud_storage_backend.exception.IncorrectDataEntry;
import maluevartem.cloud_storage_backend.exception.UserNotFoundException;
import maluevartem.cloud_storage_backend.model.Token;
import maluevartem.cloud_storage_backend.repository.UserRepository;
import maluevartem.cloud_storage_backend.security.JWTToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JWTToken jwtToken;
    private final PasswordEncoder passwordEncoder;

    public Token authenticationLogin(UserDto userDto) {
        log.info("Поиск пользователя в базе данных по логину: {}", userDto.getLogin());
        final UserEntity userFromDatabase = userRepository.findUserByLogin(userDto.getLogin()).orElseThrow(()
                -> new UserNotFoundException("Пользователь не найден", 0));
        if (passwordEncoder.matches(userDto.getPassword(), userFromDatabase.getPassword())) {
            final String token = jwtToken.generateToken(userFromDatabase);
            return new Token(token);
        } else {
            throw new IncorrectDataEntry("Неправильный пароль", 0);
        }
    }

    public String logout(String authToken, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepository.findUserByLogin(auth.getPrincipal().toString()).orElseThrow(()
                -> new UserNotFoundException("Пользователь не найден", 0));
        log.info("Пользователь начал процедуру выхода из системы: {}", userEntity);
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        if (userEntity == null) {
            return null;
        }
        securityContextLogoutHandler.logout(request, response, auth);
        jwtToken.removeToken(authToken);
        log.info("Токен пользователя: {} удален из списка активных токеннов. Auth-token: {}", userEntity, authToken);
        return userEntity.getLogin();
    }
}
