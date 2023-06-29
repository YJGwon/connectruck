package com.connectruck.foodtruck.common.fixture;

import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.AccountRepository;
import com.connectruck.foodtruck.user.domain.Role;
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
        final Truck truck = Truck.ofNewWithNoThumbnail("핫도그쿨냥이", "00가0001");
        return testTruckRepository.save(truck);
    }

    public Account saveAccount() {
        final Account account = Account.ofNew("test", "test1234!", "01000000000", Role.OWNER);
        return saveAccount(account);
    }

    public Account saveAccount(final Account account) {
        return accountRepository.save(account);
    }
}
