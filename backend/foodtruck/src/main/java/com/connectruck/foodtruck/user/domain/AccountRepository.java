package com.connectruck.foodtruck.user.domain;

import org.springframework.data.repository.Repository;

public interface AccountRepository extends Repository<Account, Long> {

    Account save(final Account account);

    boolean existsByUsername(final String username);

    boolean existsByPhone(final String phone);
}
