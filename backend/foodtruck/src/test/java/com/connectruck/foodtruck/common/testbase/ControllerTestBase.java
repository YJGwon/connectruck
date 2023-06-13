package com.connectruck.foodtruck.common.testbase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.connectruck.foodtruck.truck.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest
public abstract class ControllerTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected TruckService truckService;

    protected ResultActions performGet(final String uri) throws Exception {
        return mockMvc.perform(get(uri)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
