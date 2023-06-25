package com.connectruck.foodtruck.user.service;

import static org.assertj.core.api.Assertions.assertThatNoException;

import com.connectruck.foodtruck.user.domain.Role;
import com.connectruck.foodtruck.user.dto.UserRequest;
import com.connectruck.foodtruck.user.sevice.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("classpath:truncate.sql")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @DisplayName("회원 정보 생성")
    @Nested
    class create {

        @DisplayName("사장님 회원 정보를 생성한다.")
        @Test
        void asOwner() {
            // given
            final UserRequest request = new UserRequest("test", "test1234!", "01012345678", Role.OWNER);

            // when & then
            assertThatNoException()
                    .isThrownBy(() -> userService.create(request));
        }
    }
}
