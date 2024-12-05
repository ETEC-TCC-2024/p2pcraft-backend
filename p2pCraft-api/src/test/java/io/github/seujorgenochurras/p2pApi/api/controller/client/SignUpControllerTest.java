package io.github.seujorgenochurras.p2pApi.api.controller.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientRegisterDto;
import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientTokenDto;
import io.github.seujorgenochurras.p2pApi.api.security.detail.UserDetailsImplService;
import io.github.seujorgenochurras.p2pApi.domain.service.JwtService;
import io.github.seujorgenochurras.p2pApi.domain.service.client.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SignUpController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class SignUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @MockBean
    private UserDetailsImplService userDetailsImplService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void register_ShouldReturnToken_WhenSuccessful() throws Exception {
        ClientRegisterDto clientRegisterDto = new ClientRegisterDto();

        clientRegisterDto.setName("jhon")
            .setEmail("jhon@gmai.com")
            .setPassword("12345567");

        when(clientService.register(any())).thenReturn(new ClientTokenDto("clientToken"));

        mockMvc.perform(post("/signup").contentType("application/json")
            .content(objectMapper.writeValueAsString(clientRegisterDto)))
            .andExpect(status().isCreated())
            .andExpect(content().string(containsString("clientToken")));

    }

}
