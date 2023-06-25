package com.connectruck.foodtruck.user.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.connectruck.foodtruck.common.exception.AlreadyExistException;
import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.user.domain.Role;
import com.connectruck.foodtruck.user.dto.UserRequest;
import com.connectruck.foodtruck.user.sevice.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends ServiceTestBase {

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

        @DisplayName("사용중인 아이디일 경우 예외가 발생한다.")
        @Test
        void throwsException_whenUsernameExist() {
            // given
            final String existingUsername = "test";
            dataSetup.saveAccountOfName(existingUsername);

            final UserRequest request = new UserRequest(existingUsername, "test1234!", "01012345678", Role.OWNER);

            // when & then
            assertThatExceptionOfType(AlreadyExistException.class)
                    .isThrownBy(() -> userService.create(request))
                    .withMessageContaining("존재하는 아이디");
        }
    }
}
