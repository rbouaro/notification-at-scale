package com.rbouaro.notificationatscale.user.model.mapper;

import com.rbouaro.notificationatscale.user.model.dto.UserProfile;
import com.rbouaro.notificationatscale.user.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "uuid", target = "id")
    UserProfile toProfile(User user);
}
