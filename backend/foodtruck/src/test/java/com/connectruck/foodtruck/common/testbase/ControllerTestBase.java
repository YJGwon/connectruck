package com.connectruck.foodtruck.common.testbase;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.connectruck.foodtruck.auth.service.AuthService;
import com.connectruck.foodtruck.auth.support.JwtTokenProvider;
import com.connectruck.foodtruck.event.service.EventService;
import com.connectruck.foodtruck.menu.service.MenuService;
import com.connectruck.foodtruck.notification.service.PushNotificationService;
import com.connectruck.foodtruck.order.service.OrderService;
import com.connectruck.foodtruck.truck.service.TruckService;
import com.connectruck.foodtruck.user.sevice.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest
public abstract class ControllerTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected EventService eventService;

    @MockBean
    protected TruckService truckService;

    @MockBean
    protected MenuService menuService;

    @MockBean
    protected OrderService orderService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected PushNotificationService pushNotificationService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    protected ResultActions performGet(final String uri) throws Exception {
        return mockMvc.perform(get(uri)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    protected ResultActions performGetWithToken(final String uri) throws Exception {
        mockToken();
        return mockMvc.perform(get(uri)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer fakeToken")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    protected ResultActions performPost(final String uri, final Object request) throws Exception {
        return mockMvc.perform(post(uri)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    protected ResultActions performPostWithToken(final String uri, final Object request) throws Exception {
        mockToken();
        return mockMvc.perform(post(uri)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer fakeToken")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    protected ResultActions performPutWithToken(final String uri, final Object request) throws Exception {
        mockToken();
        return mockMvc.perform(put(uri)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer fakeToken")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    protected ResultActions performDeleteWithToken(final String uri, final Object request) throws Exception {
        mockToken();
        return mockMvc.perform(delete(uri)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer fakeToken")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private void mockToken() {
        doReturn("0")
                .when(jwtTokenProvider)
                .getSubject(anyString());
    }
}
