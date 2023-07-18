package com.connectruck.foodtruck.user.domain;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface AccountRepository extends Repository<Account, Long> {

    Account save(Account account);

    Optional<Account> findById(Long id);

    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByPhone(String phone);
}
