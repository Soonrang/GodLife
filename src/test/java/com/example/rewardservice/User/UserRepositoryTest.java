package com.example.rewardservice.User;

import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testInserts() {
        IntStream.rangeClosed(1,100).forEach(i->{
            User user = User.builder()
                    .userId("apiuser"+i)
                    .userPassword(passwordEncoder.encode("1111"))
                    .build();

            userRepository.save(user);
        });
    }

}
