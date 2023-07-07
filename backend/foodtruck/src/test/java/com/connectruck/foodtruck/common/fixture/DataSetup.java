package com.connectruck.foodtruck.common.fixture;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import com.connectruck.foodtruck.common.fixture.repository.TestEventRepository;
import com.connectruck.foodtruck.common.fixture.repository.TestMenuRepository;
import com.connectruck.foodtruck.common.fixture.repository.TestParticipationRepository;
import com.connectruck.foodtruck.common.fixture.repository.TestScheduleRepository;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.event.domain.Schedule;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.truck.domain.Participation;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.AccountRepository;
import com.connectruck.foodtruck.user.domain.Role;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(propagation = REQUIRES_NEW)
public class DataSetup {

    private final TestEventRepository testEventRepository;
    private final TestScheduleRepository testScheduleRepository;
    private final TestParticipationRepository testParticipationRepository;
    private final TestMenuRepository testMenuRepository;
    private final AccountRepository accountRepository;

    public DataSetup(final TestEventRepository testEventRepository,
                     final TestScheduleRepository testScheduleRepository,
                     final TestParticipationRepository testParticipationRepository,
                     final TestMenuRepository testMenuRepository,
                     final AccountRepository accountRepository) {
        this.testEventRepository = testEventRepository;
        this.testScheduleRepository = testScheduleRepository;
        this.testParticipationRepository = testParticipationRepository;
        this.testMenuRepository = testMenuRepository;
        this.accountRepository = accountRepository;
    }

    public Event saveEvent(final Event event) {
        return testEventRepository.save(event);
    }

    public Schedule saveSchedule(final Schedule schedule) {
        return testScheduleRepository.save(schedule);
    }

    public Participation saveParticipation(final Event event) {
        return testParticipationRepository.save(
                Participation.ofNew(event.getId(), Truck.ofNewWithNoThumbnail("핫도그쿨냥이", "00가0001")));
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
