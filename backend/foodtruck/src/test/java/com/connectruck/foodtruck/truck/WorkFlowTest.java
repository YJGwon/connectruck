package com.connectruck.foodtruck.truck;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WorkFlowTest {

    @DisplayName("fail test")
    @Test
    void fails() {
        assertThat(true).isFalse();
    }
}
