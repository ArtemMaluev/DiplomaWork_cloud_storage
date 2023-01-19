package maluevartem.cloud_storage_backend.service;

import maluevartem.cloud_storage_backend.dto.UserDto;
import maluevartem.cloud_storage_backend.entity.UserEntity;
import maluevartem.cloud_storage_backend.repository.UserRepository;
import maluevartem.cloud_storage_backend.utils.MapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RegistrationServiceTest {

    @Autowired
    private RegistrationService registrationService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private MapperUtils mapperUtils;

    private UserEntity user;
    private UserDto userDto;

    @BeforeEach
    public void init() {
        userDto = UserDto.builder()
                .login("Login")
                .password("Password")
                .build();
        user = UserEntity.builder()
                .id(1L)
                .login("Login")
                .password("Password")
                .build();
    }

    @Test
    public void test_registerUser() {
        Mockito.when(mapperUtils.toUserEntity(userDto)).thenReturn(user);
        Mockito.when(userRepository.findUserByLogin(user.getLogin())).thenReturn(Optional.empty());

        registrationService.registerUser(userDto);

        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin("Login");
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void test_getUser() {

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        registrationService.getUser(1L);

        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void test_deleteUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        registrationService.deleteUser(1L);

        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
    }
}
