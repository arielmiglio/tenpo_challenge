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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Test de integración - Se testea completa la aplicación sin Mocks de servicios ni repositorios, tomando como punto de inicio de testing los controllers.
 * Se testea también el funcionamiento del Aspecto que registra las invocaciones a las APIs, chequeando que el número de registros en BD se incremente.
 *
 * @author Ariel Miglio
 * @date 25/8/2021
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ChallengeApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class UserControllerTest {

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
    public void signUpTest() throws Exception {
        CredentialsDTO credentialsDTO = new CredentialsDTO("ariel", "12358ariel");
        long countUsers = userRepository.count();

        mockMvc.perform(post("/api/v1/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentialsDTO)))
                .andExpect(status().isCreated());

        long countUsersAfter = userRepository.count();
        Assert.assertEquals(countUsers + 1, countUsersAfter);

    }

    @Test
    public void loginTestFailure() throws Exception {
        //Creo un usuario en base
        userService.signUpUser(User.builder().username("pachi").password("12358Pachi").build());

        //Creo credenciales con una password errónea
        CredentialsDTO credentialsDto = new CredentialsDTO("pachi", "12358pachi");
        String jsonCredentialsDto = objectMapper.writeValueAsString(credentialsDto);

        //Ejecuto el endpoint y espero un 401
        mockMvc.perform(post("/api/v1/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCredentialsDto))
                .andExpect(status().is(401));
    }

    @Test
    public void loginTestSuccess() throws Exception {
        userService.signUpUser(User.builder().username("ana").password("12358ana").build());

        CredentialsDTO credentialsDTO = new CredentialsDTO("ana", "12358ana");
        String jsonCredentialsDto = objectMapper.writeValueAsString(credentialsDTO);

        ResultActions result = mockMvc.perform(post("/api/v1/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCredentialsDto))
                .andExpect(status().isOk());

        String resultString = result.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String token = jsonParser.parseMap(resultString).get("token").toString();
        Assert.assertNotNull(token);

    }

}
