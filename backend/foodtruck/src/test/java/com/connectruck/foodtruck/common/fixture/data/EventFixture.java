package com.connectruck.foodtruck.common.fixture.data;

import com.connectruck.foodtruck.event.domain.Event;

public enum EventFixture {
    밤도깨비_야시장("여의도 밤도깨비 야시장", "서울 영등포구 여의동 여의동로 330"),
    서울FC_경기("서울FC 경기", "서울 마포구 성산동 509-7");

    private final String name;
    private final String location;

    EventFixture(final String name, final String location) {
        this.name = name;
        this.location = location;
    }

    public Event create() {
        return Event.ofNew(name, location);
    }
}
