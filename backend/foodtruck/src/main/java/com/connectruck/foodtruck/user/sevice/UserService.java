package com.connectruck.foodtruck.user.sevice;

import com.connectruck.foodtruck.common.exception.AlreadyExistException;
import com.connectruck.foodtruck.user.domain.AccountRepository;
import com.connectruck.foodtruck.user.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AccountRepository accountRepository;

    public void create(final UserRequest request) {
        checkUsernameExists(request.username());
        checkPhoneExists(request.phone());
        accountRepository.save(request.toEntity());
    }

    private void checkUsernameExists(final String username) {
        if (accountRepository.existsByUsername(username)) {
            throw AlreadyExistException.withInputValue("아이디", username);
        }
    }

    private void checkPhoneExists(final String phone) {
        if (accountRepository.existsByPhone(phone)) {
            throw AlreadyExistException.withInputValue("휴대폰 번호", phone);
        }
    }
}
