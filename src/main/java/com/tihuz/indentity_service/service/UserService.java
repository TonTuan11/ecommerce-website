package com.tihuz.indentity_service.service;

import com.tihuz.indentity_service.dto.request.UserCreationRequest;
import com.tihuz.indentity_service.dto.request.UserUpdateRequest;
import com.tihuz.indentity_service.dto.response.UserResponse;
import com.tihuz.indentity_service.entity.User;

import com.tihuz.indentity_service.enums.RoleType;
import com.tihuz.indentity_service.exception.AppException;
import com.tihuz.indentity_service.exception.ErrorCode;
import com.tihuz.indentity_service.mapper.UserMapper;
import com.tihuz.indentity_service.repository.RoleRepository;
import com.tihuz.indentity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j


@Service

// thay vì dùng  @Autowired cho từng biến thì dùng RequiredArgsConstructor
@RequiredArgsConstructor

// makeFinal = true field không khai báo thì thành final
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {


    // gọi UserRepository để thao tác với csdl
     UserRepository userRepository;

     RoleRepository roleRepository;
     // gọi UserMapper
     UserMapper userMapper;

     PasswordEncoder passwordEncoder;


    // CREATE
    public UserResponse createUser(UserCreationRequest request)
    {

        log.info("Service: Create User");

        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);


        // Map user
        User user=userMapper.toUser(request);


        // mã hóa  password bằng BCrypt
        // interface cung cấp PasswordEncoder
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        // Tạo roles mặc định là "USER"
        var roleUser=roleRepository.findById(RoleType.USER.name())
                .orElseThrow(()-> new AppException(ErrorCode.ROLE_NOTEXISTED));
        user.setRoles(Set.of(roleUser));


        // trả về userRepository lưu xuống database
       return userMapper.toUserResponse(userRepository.save(user));

    }




    // SELECT ALL
    @PreAuthorize("hasRole('ADMIN')")
   // @PreAuthorize("hasAuthority('APPROVE_DATA')")
    public List<UserResponse> getAllUser() {


        return userRepository.findAll()              // trả về List<User>
                .stream()               // chuyển List<User> thành Stream<User>
                .map(userMapper::toUserResponse) // map từng User thành UserResponse
                .toList();              // gom lại thành List<UserResponse>
    }



    //returnObject.username → username của user vừa lấy trong DB
    //authentication.name → username trong token JWT (người đang login)
     // @PostAuthorize(" hasRole('ADMIN') or returnObject.id == authentication.name")
    // SELECT BY ID
//    @PreAuthorize("hasRole('ADMIN')")
//    @PostAuthorize("returnObject.id.equals(authentication.name)")
    @PreAuthorize("hasRole('ADMIN') or #id.equals(authentication.name)")
    public UserResponse getUser (String id)
    {
        log.info(" In method get user by Id");

        return userMapper.toUserResponse( userRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOTEXISTED) ));
        // trả về 1 Optional nên phải thêm orElse


    }




    public UserResponse getMyInfo()
    {
       var context= SecurityContextHolder.getContext().getAuthentication();

        log.info("User Name này: {}", context.getName());


        User user= userRepository.findById(context.getName())
               .orElseThrow( ()-> new AppException(ErrorCode.USER_NOTEXISTED));

       return userMapper.toUserResponse(user);
    }

    // UPDATE BY ID

    public UserResponse updateUser (String userId, UserUpdateRequest request)
    {
        User user =userRepository.findById(userId)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOTEXISTED) );


        // Map user và mã hóa pass
        userMapper.updateUser(user,request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        var roles= roleRepository.findAllById(request.getRoles());

            user.setRoles(new HashSet<>(roles));


        return userMapper.toUserResponse(userRepository.save(user));

    }

    public void deleteUser(String userId)
    {
        userRepository.deleteById(userId);

    }



    public String checkName(String name) {
        name = name.trim();

        char prev = 0;

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);

            if (!(Character.isLetter(c) )) {
                throw new AppException(ErrorCode.CATE_NAME);
            }

            if (c == ' ')
            {
                if (prev == ' ')   {throw new AppException(ErrorCode.CATE_NAME);}
            }
            prev = c;
        }

        return name;
    }


}
