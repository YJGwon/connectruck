package com.connectruck.foodtruck.common.fixture;

import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.AccountRepository;
import com.connectruck.foodtruck.user.domain.Role;
import java.time.LocalTime;
import org.springframework.stereotype.Component;

@Component
public class DataSetup {

    private final TestTruckRepository testTruckRepository;
    private final AccountRepository accountRepository;

    public DataSetup(final TestTruckRepository testTruckRepository, final AccountRepository accountRepository) {
        this.testTruckRepository = testTruckRepository;
        this.accountRepository = accountRepository;
    }

    public Truck saveTruck() {
        final Truck truck = Truck.ofNewWithNoThumbnail(
                "핫도그쿨냥이",
                "서울 마포구 성산동 509-7",
                LocalTime.of(11, 0),
                LocalTime.of(21, 0)
        );
        return testTruckRepository.save(truck);
    }

    public Account saveAccountOfName(final String username) {
        final Account account = Account.ofNew(username, "test1234!", "01012341234", Role.OWNER);
        return accountRepository.save(account);
    }
}
