package com.connectruck.foodtruck.notification.domain.sse;

import static lombok.AccessLevel.PROTECTED;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Getter
@EqualsAndHashCode
public class SseEventGroup {

    private SseEventGroupType type;
    private Long id;
}
