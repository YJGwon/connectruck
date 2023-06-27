package com.connectruck.foodtruck.user.domain;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface AccountRepository extends Repository<Account, Long> {

    Account save(final Account account);

    Optional<Account> findById(Long id);

    Optional<Account> findByUsername(final String username);

    boolean existsByUsername(final String username);

    boolean existsByPhone(final String phone);
}
