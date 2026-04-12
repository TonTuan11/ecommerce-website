package com.tihuz.ecommerce_backend.mapper;

import com.tihuz.ecommerce_backend.dto.request.UserCreationRequest;
import com.tihuz.ecommerce_backend.dto.request.UserUpdateRequest;
import com.tihuz.ecommerce_backend.dto.response.UserResponse;
import com.tihuz.ecommerce_backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

}
