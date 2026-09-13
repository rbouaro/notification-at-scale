package com.rbouaro.notificationatscale.user;

import com.rbouaro.notificationatscale.user.persistence.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "uuid", target = "id")
    UserProfile toProfile(User user);
}
