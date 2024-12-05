package io.github.seujorgenochurras.p2pApi.api.controller.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SignUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SignUpController signUpController;

    void givenRegisterDto_whenSigningUp_thenReturnToken() {
        
    }

}
