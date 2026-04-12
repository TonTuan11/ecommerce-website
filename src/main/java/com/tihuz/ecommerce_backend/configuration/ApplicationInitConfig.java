package com.tihuz.ecommerce_backend.configuration;

import com.tihuz.ecommerce_backend.base.BaseEntity;
import com.tihuz.ecommerce_backend.entity.Role;
import com.tihuz.ecommerce_backend.entity.User;
import com.tihuz.ecommerce_backend.enums.RoleType;
import com.tihuz.ecommerce_backend.repository.RoleRepository;
import com.tihuz.ecommerce_backend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Configuration
@Slf4j

// this class automatically creates a unique administrator user at startup
public class ApplicationInitConfig extends BaseEntity
{
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Bean
    // đảm bảo runner chỉ chạy khi DB driver là MySQL (một safeguard môi trường).
    @ConditionalOnProperty(prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository)
     {  log.info("Init application.....");

         return args ->
         {
             // Seed roles từ enum nếu chưa có
             for(RoleType roleType : RoleType.values())
             {
                 roleRepository.findById(roleType.name())
                         .orElseGet(() -> roleRepository.save(new Role(roleType.name(), "Default " + roleType.name(), new HashSet<>())));
             }
             // nếu chưa có admin thì tạo
           if(  userRepository.findByUsername("admin").isEmpty())
           {
               LocalDate dob=LocalDate.of(1992,8,7);

               Role adminRole=roleRepository.findById(RoleType.ADMIN.name()).orElseThrow();

               User user=User.builder()
                       .username("admin")
                       .password(passwordEncoder.encode("admin")) // mã hóa pass trước khi save
                       .lastname("min")
                       .dob(dob)
                       .firstname("ad")
                       .email("adminadmin@gmail.com")
                       .roles(Set.of(adminRole))
                       .build();

               userRepository.save(user);

               log.warn("admin user has been created with defaul password: admin, pleas change it");
           }
         };
    }

}
