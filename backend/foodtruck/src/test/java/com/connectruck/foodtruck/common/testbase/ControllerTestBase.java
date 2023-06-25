package com.connectruck.foodtruck.common.testbase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.connectruck.foodtruck.truck.service.TruckService;
import com.connectruck.foodtruck.user.sevice.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @MockBean
    protected UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    protected ResultActions performGet(final String uri) throws Exception {
        return mockMvc.perform(get(uri)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    protected ResultActions performPost(final String uri, final Object request) throws Exception {
        return mockMvc.perform(post(uri)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
