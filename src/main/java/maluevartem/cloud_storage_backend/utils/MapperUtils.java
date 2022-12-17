package maluevartem.cloud_storage_backend.utils;

import maluevartem.cloud_storage_backend.dto.UserDto;
import maluevartem.cloud_storage_backend.entity.UserEntity;

public interface MapperUtils {

    UserEntity toUserEntity(UserDto userDto);

    UserDto toUserDto(UserEntity userEntity);

}
