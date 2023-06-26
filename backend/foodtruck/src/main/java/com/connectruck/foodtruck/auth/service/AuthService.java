package com.connectruck.foodtruck.auth.service;

import com.connectruck.foodtruck.auth.dto.SignInRequest;
import com.connectruck.foodtruck.auth.dto.TokenResponse;
import com.connectruck.foodtruck.auth.exception.AuthorizationException;
import com.connectruck.foodtruck.auth.exception.SignInFailedException;
import com.connectruck.foodtruck.auth.support.JwtTokenProvider;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.AccountRepository;
import com.connectruck.foodtruck.user.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AccountRepository accountRepository;

    public TokenResponse signIn(final SignInRequest request) {
        final Account account = accountRepository.findByUsername(request.username())
                .orElseThrow(SignInFailedException::new);
        checkPassword(request.password(), account);

        final String accessToken = jwtTokenProvider.create(Long.toString(account.getId()), account.getRole().name());
        return new TokenResponse(accessToken);
    }

    public void validateToken(final String token) {
        jwtTokenProvider.validateToken(token);
    }

    public void validateRole(final String token, final Role requiredRole) {
        final String role = jwtTokenProvider.getRole(token);
        if (!requiredRole.name().equals(role)) {
            throw new AuthorizationException(role);
        }
    }

    private void checkPassword(final String password, final Account account) {
        if (!account.isPassword(password)) {
            throw new SignInFailedException();
        }
    }
}
