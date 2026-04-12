package com.tihuz.ecommerce_backend.service;

import com.tihuz.ecommerce_backend.dto.request.UserCreationRequest;
import com.tihuz.ecommerce_backend.dto.request.UserUpdateRequest;
import com.tihuz.ecommerce_backend.dto.response.UserResponse;
import com.tihuz.ecommerce_backend.entity.User;

import com.tihuz.ecommerce_backend.enums.RoleType;
import com.tihuz.ecommerce_backend.exception.AppException;
import com.tihuz.ecommerce_backend.exception.ErrorCode;
import com.tihuz.ecommerce_backend.mapper.UserMapper;
import com.tihuz.ecommerce_backend.repository.RoleRepository;
import com.tihuz.ecommerce_backend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

     EmailService emailService;
     UserRepository userRepository;
     RoleRepository roleRepository;
     UserMapper userMapper;
     PasswordEncoder passwordEncoder;

    // CREATE
    public UserResponse createUser(UserCreationRequest request)
    {
        String userName= checkName(request.getUsername());

        if(userRepository.existsByUsername(userName))
            throw new AppException(ErrorCode.USER_EXISTED);


        if(userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_EXISTED);


        // Map user
        User user=userMapper.toUser(request);
        user.setUsername(userName);

        // Hash the password using BCrypt.
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        // Set role default is "USER"
        var roleUser=roleRepository.findById(RoleType.USER.name())
                .orElseThrow(()-> new AppException(ErrorCode.ROLE_NOTEXISTED));
        user.setRoles(Set.of(roleUser));

//        var roleAdmin = roleRepository.findById(RoleType.ADMIN.name())
//                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOTEXISTED));
//        var roleUser = roleRepository.findById(RoleType.USER.name())
//                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOTEXISTED));
//
//// Tạo Set chứa cả 2 role
//        Set<Role> roles = Set.of(roleAdmin, roleUser);
//        user.setRoles(roles);



        try {
            emailService.sendEmail(
                    request.getEmail(),
                    "Welcome to Our App",
                    """
                    <html>
                    <body style="font-family: Arial, sans-serif;">
                        <h2 style="color:#2c3e50;">Welcome to WebSite</h2>
                        <p>Xin chào <b>%s</b>,</p>
                        <p>Cảm ơn bạn đã đăng ký tài khoản.</p>
                        <p>Chúc bạn có trải nghiệm tuyệt vời với hệ thống của chúng tôi.</p>
                        <br>
                        <p>Best regards,<br>Our Team</p>
                    </body>
                    </html>
                    """.formatted(userName)
            );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

       return userMapper.toUserResponse(userRepository.save(user));

    }


    public List<UserResponse> getAllUser()
        {
            return userRepository.findAll()              //  List<User>
                    .stream()               // convert List<User> -> Stream<User>
                    .map(userMapper::toUserResponse) // map  User -> UserResponse
                    .toList();              // return List<UserResponse>
        }




    public UserResponse getUser (String id)
    {

        log.info(" In method get user by Id");

        return userMapper.toUserResponse( userRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOTEXISTED) ));

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
                throw new AppException(ErrorCode.USERNAME_INVALID3);
            }

            if (c == ' ')
            {
                if (prev == ' ')   {throw new AppException(ErrorCode.USERNAME_INVALID3);}
            }
            prev = c;
        }

        return name;
    }


}
