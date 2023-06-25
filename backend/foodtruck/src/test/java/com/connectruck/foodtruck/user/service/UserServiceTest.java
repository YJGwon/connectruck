package com.connectruck.foodtruck.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.connectruck.foodtruck.common.exception.AlreadyExistException;
import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.Role;
import com.connectruck.foodtruck.user.dto.CheckAvailableResponse;
import com.connectruck.foodtruck.user.dto.PhoneRequest;
import com.connectruck.foodtruck.user.dto.UserRequest;
import com.connectruck.foodtruck.user.dto.UsernameRequest;
import com.connectruck.foodtruck.user.sevice.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends ServiceTestBase {


    private final String username = "test";
    private final String password = "test1234!";
    private final String phone = "01000000000";

    @Autowired
    private UserService userService;

    @DisplayName("아이디 검사")
    @Nested
    class checkUsername {

        @DisplayName("사용 가능한 아이디일 경우 true를 반환한다.")
        @Test
        void returnTrue_whenUsernameAvailable() {
            // given
            final UsernameRequest request = new UsernameRequest(username);

            // when
            final CheckAvailableResponse response = userService.checkUsername(request);

            // then
            assertThat(response.isAvailable()).isTrue();
        }

        @DisplayName("사용 중인 아이디일 경우 false를 반환한다.")
        @Test
        void returnFalse_whenUsernameAlreadyExists() {
            // given
            final String existingUsername = "exists";
            dataSetup.saveAccount(Account.ofNew(existingUsername, password, phone, Role.OWNER));

            final UsernameRequest request = new UsernameRequest(existingUsername);

            // when
            final CheckAvailableResponse response = userService.checkUsername(request);

            // then
            assertThat(response.isAvailable()).isFalse();
        }
    }

    @DisplayName("휴대폰 검사")
    @Nested
    class checkPhone {

        @DisplayName("사용 가능한 아이디일 경우 true를 반환한다.")
        @Test
        void returnTrue_whenPhoneAvailable() {
            // given
            final PhoneRequest request = new PhoneRequest(phone);

            // when
            final CheckAvailableResponse response = userService.checkPhone(request);

            // then
            assertThat(response.isAvailable()).isTrue();
        }

        @DisplayName("사용 중인 휴대폰 번호일 경우 false를 반환한다.")
        @Test
        void returnFalse_whenPhoneAlreadyExists() {
            // given
            final String existingPhone = "01000000001";
            dataSetup.saveAccount(Account.ofNew(username, password, existingPhone, Role.OWNER));

            final PhoneRequest request = new PhoneRequest(existingPhone);

            // when
            final CheckAvailableResponse response = userService.checkPhone(request);

            // then
            assertThat(response.isAvailable()).isFalse();
        }
    }

    @DisplayName("회원 정보 생성")
    @Nested
    class create {

        @DisplayName("사장님 회원 정보를 생성한다.")
        @Test
        void asOwner() {
            // given
            final UserRequest request = new UserRequest(username, password, phone, Role.OWNER);

            // when & then
            assertThatNoException()
                    .isThrownBy(() -> userService.create(request));
        }

        @DisplayName("사용중인 아이디일 경우 예외가 발생한다.")
        @Test
        void throwsException_whenUsernameExist() {
            // given
            final String existingUsername = "exists";
            dataSetup.saveAccount(Account.ofNew(existingUsername, password, "01000000001", Role.OWNER));

            final UserRequest request = new UserRequest(existingUsername, password, phone, Role.OWNER);

            // when & then
            assertThatExceptionOfType(AlreadyExistException.class)
                    .isThrownBy(() -> userService.create(request))
                    .withMessageContaining("존재하는 아이디");
        }

        @DisplayName("사용중인 휴대폰 번호일 경우 예외가 발생한다.")
        @Test
        void throwsException_whenPhoneExist() {
            // given
            final String existingPhone = "01000000001";
            dataSetup.saveAccount(Account.ofNew("test1", password, existingPhone, Role.OWNER));

            final UserRequest request = new UserRequest(username, password, existingPhone, Role.OWNER);

            // when & then
            assertThatExceptionOfType(AlreadyExistException.class)
                    .isThrownBy(() -> userService.create(request))
                    .withMessageContaining("존재하는 휴대폰 번호");
        }
    }
}
