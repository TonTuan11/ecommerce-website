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

        // Map user
        User user=userMapper.toUser(request);
        user.setUsername(userName);

        // Hash the password using BCrypt.
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        // Set role default is "USER"
        var roleUser=roleRepository.findById(RoleType.USER.name())
                .orElseThrow(()-> new AppException(ErrorCode.ROLE_NOTEXISTED));
        user.setRoles(Set.of(roleUser));

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




    // SELECT ALL
    @PreAuthorize("hasRole('ADMIN')")
   // @PreAuthorize("hasAuthority('APPROVE_DATA')")
    public List<UserResponse> getAllUser() {


        return userRepository.findAll()              //  List<User>
                .stream()               // convert List<User> -> Stream<User>
                .map(userMapper::toUserResponse) // map  User -> UserResponse
                .toList();              // return List<UserResponse>
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
