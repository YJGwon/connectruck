package com.connectruck.foodtruck.common.fixture;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import com.connectruck.foodtruck.common.fixture.repository.TestEventRepository;
import com.connectruck.foodtruck.common.fixture.repository.TestMenuRepository;
import com.connectruck.foodtruck.common.fixture.repository.TestScheduleRepository;
import com.connectruck.foodtruck.common.fixture.repository.TestTruckRepository;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.event.domain.Schedule;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.order.domain.OrderInfo;
import com.connectruck.foodtruck.order.domain.OrderInfoRepository;
import com.connectruck.foodtruck.order.domain.OrderLine;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.AccountRepository;
import com.connectruck.foodtruck.user.domain.Role;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(propagation = REQUIRES_NEW)
public class DataSetup {

    private final TestEventRepository testEventRepository;
    private final TestScheduleRepository testScheduleRepository;
    private final TestTruckRepository testTruckRepository;
    private final TestMenuRepository testMenuRepository;
    private final OrderInfoRepository orderInfoRepository;
    private final AccountRepository accountRepository;

    public DataSetup(final TestEventRepository testEventRepository,
                     final TestScheduleRepository testScheduleRepository,
                     final TestTruckRepository testTruckRepository,
                     final TestMenuRepository testMenuRepository,
                     final OrderInfoRepository orderInfoRepository,
                     final AccountRepository accountRepository) {
        this.testEventRepository = testEventRepository;
        this.testScheduleRepository = testScheduleRepository;
        this.testTruckRepository = testTruckRepository;
        this.testMenuRepository = testMenuRepository;
        this.orderInfoRepository = orderInfoRepository;
        this.accountRepository = accountRepository;
    }

    public Event saveEvent(final Event event) {
        return testEventRepository.save(event);
    }

    public Schedule saveSchedule(final Schedule schedule) {
        return testScheduleRepository.save(schedule);
    }

    public Truck saveTruck(final Event event) {
        return saveTruck(event, null);
    }

    public Truck saveTruck(final Event event, final Long ownerId) {
        return testTruckRepository.save(
                Truck.ofNew(event.getId(), "핫도그쿨냥이", "00가0001", null, ownerId));
    }

    public Menu saveMenu(final Truck truck) {
        return testMenuRepository.save(Menu.ofNew("핫도그", BigDecimal.valueOf(8000), truck.getId()));
    }

    public OrderInfo saveOrderInfo(final Truck truck, final Menu menu) {
        final OrderInfo orderInfo = OrderInfo.ofNew(truck.getId(), "01000000000");
        final OrderLine orderLine = OrderLine.ofNew(
                menu.getId(), menu.getName(), menu.getPrice(), 1, orderInfo
        );
        orderInfo.changeOrderLine(List.of(orderLine));
        return orderInfoRepository.save(orderInfo);
    }

    public Account saveOwnerAccount() {
        final Account account = Account.ofNew("test", "test1234!", "01000000000", Role.OWNER);
        return saveAccount(account);
    }

    public Account saveAccount(final Account account) {
        return accountRepository.save(account);
    }
}
