package maluevartem.cloud_storage_backend.service;

import lombok.RequiredArgsConstructor;
import maluevartem.cloud_storage_backend.entity.UserEntity;
import maluevartem.cloud_storage_backend.exception.UserNotFoundException;
import maluevartem.cloud_storage_backend.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<UserEntity> userRes = userRepository.findUserByLogin(login);
        if(userRes.isEmpty()) {
            throw new UserNotFoundException("Пользователь с таким логином: { " + login + " } не найден", 0);
        }
        UserEntity user = userRes.get();
        return new User(user.getLogin(), user.getPassword(), Collections.emptyList());
    }
}
