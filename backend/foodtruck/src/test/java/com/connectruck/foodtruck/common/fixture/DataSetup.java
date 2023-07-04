package com.connectruck.foodtruck.common.fixture;

import com.connectruck.foodtruck.common.fixture.repository.TestEventRepository;
import com.connectruck.foodtruck.common.fixture.repository.TestMenuRepository;
import com.connectruck.foodtruck.common.fixture.repository.TestParticipationRepository;
import com.connectruck.foodtruck.common.fixture.repository.TestTruckRepository;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.truck.domain.Participation;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.AccountRepository;
import com.connectruck.foodtruck.user.domain.Role;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class DataSetup {

    private final TestTruckRepository testTruckRepository;
    private final TestEventRepository testEventRepository;
    private final TestParticipationRepository testParticipationRepository;
    private final TestMenuRepository testMenuRepository;
    private final AccountRepository accountRepository;

    public DataSetup(final TestTruckRepository testTruckRepository,
                     final TestEventRepository testEventRepository,
                     final TestParticipationRepository testParticipationRepository,
                     final TestMenuRepository testMenuRepository,
                     final AccountRepository accountRepository) {
        this.testTruckRepository = testTruckRepository;
        this.testEventRepository = testEventRepository;
        this.testParticipationRepository = testParticipationRepository;
        this.testMenuRepository = testMenuRepository;
        this.accountRepository = accountRepository;
    }

    public Event saveEvent(final Event event) {
        return testEventRepository.save(event);
    }

    public Truck saveTruck() {
        final Truck truck = Truck.ofNewWithNoThumbnail("핫도그쿨냥이", "00가0001");
        return testTruckRepository.save(truck);
    }

    public Participation saveParticipation(final Event event) {
        return testParticipationRepository.save(Participation.ofNew(event.getId(), saveTruck()));
    }

    public Menu saveMenu(final Participation participation) {
        return testMenuRepository.save(Menu.ofNew("핫도그", BigDecimal.valueOf(8000), participation.getId()));
    }

    public Account saveAccount() {
        final Account account = Account.ofNew("test", "test1234!", "01000000000", Role.OWNER);
        return saveAccount(account);
    }

    public Account saveAccount(final Account account) {
        return accountRepository.save(account);
    }
}
