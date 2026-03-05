package com.tihuz.indentity_service.mapper;


import com.tihuz.indentity_service.dto.request.UserCreationRequest;
import com.tihuz.indentity_service.dto.request.UserUpdateRequest;
import com.tihuz.indentity_service.dto.response.UserResponse;
import com.tihuz.indentity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

// báo đây là 1 Mapper quản lý bởi Spring
@Mapper(componentModel = "spring")
public interface UserMapper {

//    @Mapping(source = "username", target = "username")
//    @Mapping(source = "firstname", target = "firstname")
//    @Mapping(source = "lastname", target = "lastname")
//    @Mapping(source = "dob", target = "dob")




    //method map các field từ UserCreationRequest sang entity User
    User toUser(UserCreationRequest request);

// lấy từ source gán và target
  //  @Mapping(source = "firstname",target = "lastname")

    // ignore =true thì target= null
    //   @Mapping(target = "lastname",ignore = true)



    //method map các field từ User sang entity UserResponse
    UserResponse toUserResponse(User user);



    @Mapping(target = "roles", ignore = true)

    //method map các field từ UserUpdateRequest sang entity User
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

}
