package maluevartem.cloud_storage_backend.service;

import lombok.RequiredArgsConstructor;
import maluevartem.cloud_storage_backend.dto.UserDto;
import maluevartem.cloud_storage_backend.entity.UserEntity;
import maluevartem.cloud_storage_backend.repository.UserRepository;
import maluevartem.cloud_storage_backend.utils.MapperUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final MapperUtils mapperUtils;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserDto registerUser(UserDto userDto) {
        UserEntity userEntity = mapperUtils.toUserEntity(userDto);

        userRepository.findUserByLogin(userEntity.getLogin()).orElseThrow();
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        return mapperUtils.toUserDto(userRepository.save(userEntity));
    }

    public UserDto getUser(Long id) {
        UserEntity userFound = userRepository.findById(id).orElseThrow();
        return mapperUtils.toUserDto(userFound);
    }

    public void deleteUser(Long id) {
        userRepository.findById(id).orElseThrow();
        userRepository.deleteById(id);
    }
}