package cl.com.challenge.test.integration;

import cl.tenpo.challenge.ChallengeApplication;
import cl.tenpo.challenge.model.User;
import cl.tenpo.challenge.service.OperationService;
import cl.tenpo.challenge.service.UserService;
import cl.tenpo.challenge.web.model.CredentialsDTO;
import cl.tenpo.challenge.web.model.OperatorsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ariel Miglio
 * @date 26/8/2021
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ChallengeApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class OperationControllerTest {

    MockMvc mockMvc;

    @Value("${bigdecimal.scale}")
    private Integer bigDecimalScale;

    @Autowired
    OperationService operationService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Autowired
    private ObjectMapper objectMapper;

    private CredentialsDTO credentials = new CredentialsDTO("jorge", "ariel");
    private String token;

    @BeforeAll
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .addFilter(springSecurityFilterChain).build();

        userService.signUpUser(User.builder()
                .username(credentials.getUsername())
                .password(credentials.getPassword())
                .build());

        ResultActions result = mockMvc.perform(post("/api/v1/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isOk());

        String resultString = result.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        token = jsonParser.parseMap(resultString).get("token").toString();
    }

    @Test
    public void sumNumbersSuccessfully() throws Exception {

        OperatorsDTO operators = OperatorsDTO.builder().operator1(new BigDecimal(2.3)).operator2(new BigDecimal(2.3)).build();
        BigDecimal op1 = new BigDecimal(2.3);
        BigDecimal op2 = new BigDecimal(3.1);
        mockMvc.perform(get("/api/v1/operation/sum")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .param("operator1", op1.toString())
                .param("operator2", op2.toString())
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(new BigDecimal(5.4).setScale(bigDecimalScale, RoundingMode.HALF_UP)));
    }

    @Test
    public void sumNumbersWithWrongParameters() throws Exception {

        OperatorsDTO operators = OperatorsDTO.builder().operator1(new BigDecimal(2.3)).operator2(new BigDecimal(2.3)).build();


        mockMvc.perform(get("/api/v1/operation/sum")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .param("operator1", "q")
                .param("operator2", "1")
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

}
