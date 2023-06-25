package com.connectruck.foodtruck.user.sevice;

import com.connectruck.foodtruck.user.domain.AccountRepository;
import com.connectruck.foodtruck.user.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AccountRepository accountRepository;

    public void create(final UserRequest request) {
        accountRepository.save(request.toEntity());
    }
}
