package com.connectruck.foodtruck.auth.service;

import com.connectruck.foodtruck.auth.dto.SignInRequest;
import com.connectruck.foodtruck.auth.dto.TokenResponse;
import com.connectruck.foodtruck.auth.exception.SignInFailedException;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;

    public TokenResponse signIn(final SignInRequest request) {
        final Account account = accountRepository.findByUsername(request.username())
                .orElseThrow(SignInFailedException::new);
        checkPassword(request.password(), account);

        return new TokenResponse("accessToken");
    }

    private void checkPassword(final String password, final Account account) {
        if (!account.isPassword(password)) {
            throw new SignInFailedException();
        }
    }
}
