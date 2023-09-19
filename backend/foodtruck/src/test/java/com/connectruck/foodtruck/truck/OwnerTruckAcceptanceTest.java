package com.connectruck.foodtruck.truck;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.OK;

import com.connectruck.foodtruck.common.testbase.AcceptanceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.Role;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OwnerTruckAcceptanceTest extends AcceptanceTestBase {

    private static final String BASE_URI = "/api/owner/trucks";

    @DisplayName("소유 푸드트럭의 정보를 사장님 id로 조회한다.")
    @Test
    void findMyTruck() {
        // given
        // 사장님 계정 생성, 로그인
        final String username = "test";
        final String password = "test1234!";
        final Account owner = dataSetup.saveAccount(Account.ofNew(username, password, "01000000000", Role.OWNER));
        final String token = loginAndGetToken(username, password);

        // 소유 푸드트럭 1개 저장
        final Event event = dataSetup.saveEvent(밤도깨비_야시장.create());
        final Truck owningTruck = dataSetup.saveTruck(event, owner.getId());

        // 해당 계정의 소유 아닌 푸드트럭 1개 존재
        dataSetup.saveTruck(event);

        // when
        final ValidatableResponse response = getWithToken(BASE_URI + "/my", token);

        // then
        response.statusCode(OK.value())
                .body("id", equalTo(owningTruck.getId().intValue()))
                .body("name", equalTo(owningTruck.getName()));
    }
}
