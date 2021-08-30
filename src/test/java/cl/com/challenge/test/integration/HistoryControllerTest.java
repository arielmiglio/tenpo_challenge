package cl.com.challenge.test.integration;

import cl.tenpo.challenge.ChallengeApplication;
import cl.tenpo.challenge.model.User;
import cl.tenpo.challenge.repository.RequestHistoryRepository;
import cl.tenpo.challenge.repository.UserRepository;
import cl.tenpo.challenge.service.UserService;
import cl.tenpo.challenge.web.model.CredentialsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test de integración para que chequea que ante cada llamado a un endpoint, se registra el mismo en la base de datos
 * También se testea el listado de request del mismo controller.
 * @author Ariel Miglio
 * @date 26/8/2021
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ChallengeApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class HistoryControllerTest {
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RequestHistoryRepository historyRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void signUpRequestRegistrationTest() throws Exception {
        CredentialsDTO credentialsDTO = new CredentialsDTO("ariel", "12358ariel");

        //Obtengo la cantidad de request previos en BD
        long requestHistoryCount = historyRepository.count();
        mockMvc.perform(post("/api/v1/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentialsDTO)));

        //Obtengo la cantidad de request luego de la ejecución del controller en BD
        long requestHistoryAfter = historyRepository.count();
        Assert.assertEquals(requestHistoryCount + 1, requestHistoryAfter);
    }


    @Test
    public void loginRequestRegistrationTest() throws Exception {
        userService.signUpUser(User.builder().username("ana").password("12358ana").build());

        CredentialsDTO credentialsDTO = new CredentialsDTO("ana", "12358ana");
        String jsonCredentialsDto = objectMapper.writeValueAsString(credentialsDTO);

        //Obtengo la cantidad de request previos en BD
        long requestHistoryCount = historyRepository.count();
        ResultActions result = mockMvc.perform(post("/api/v1/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCredentialsDto));

        long requestHistoryAfter = historyRepository.count();
        Assert.assertEquals(requestHistoryCount + 1, requestHistoryAfter);
    }

    @Test
    public void logOutRequestRegistrationTest() throws Exception {
        //Obtengo la cantidad de request previos en BD
        long requestHistoryCount = historyRepository.count();
        ResultActions result = mockMvc.perform(post("/api/v1/user/logout")
                .contentType(MediaType.APPLICATION_JSON));

        long requestHistoryAfter = historyRepository.count();
        Assert.assertEquals(requestHistoryCount + 1, requestHistoryAfter);
    }

    public void plusOperationRequestRegistrationTest() throws Exception{
        //Obtengo la cantidad de request previos en BD
        long requestHistoryCount = historyRepository.count();
        ResultActions result = mockMvc.perform(post("/api/v1/operation/plus")
                .contentType(MediaType.APPLICATION_JSON));

        long requestHistoryAfter = historyRepository.count();
        Assert.assertEquals(requestHistoryCount + 1, requestHistoryAfter);
    }



}
