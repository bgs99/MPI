package ru.itmo.hungergames.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.repository.SponsorRepository;
import ru.itmo.hungergames.service.SecurityService;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class SecurityControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    SecurityService securityService;
    @Autowired
    SponsorRepository sponsorRepository;

    @Test
    @Transactional
    public void createSponsor() throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "hoho",
                                    "password": "haha",
                                    "name": "hihi-name",
                                    "avatarUri": ""
                                }""")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
        sponsorRepository.deleteByUsername("hoho");
    }

    @Test
    @Transactional
    public void generateToken() throws Exception {
        securityService.createSponsor(new Sponsor("testsponsor", "testpassword", "testname", null));
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "testsponsor",
                                    "password": "testpassword"
                                }""")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", notNullValue()))
                .andExpect(jsonPath("token", notNullValue()))
                .andExpect(jsonPath("type", notNullValue()))
                .andExpect(jsonPath("username", notNullValue()))
                .andExpect(jsonPath("roles", notNullValue()));
        sponsorRepository.deleteByUsername("testsponsor");
    }

    @Test
    public void badCredentials() throws Exception {
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "hahahaha",
                                    "password": "hohohoh"
                                }""")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void userExists() throws Exception {
        securityService.createSponsor(new Sponsor("testsponsor", "testpassword", "testname", null));
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "testsponsor",
                                    "password": "ahaha",
                                    "name": "hihi-name",
                                    "avatarUri": ""
                                }""")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isConflict())
                .andExpect(content().string(containsString("User with id=testsponsor already exists.")));
        sponsorRepository.deleteByUsername("testsponsor");
    }

    @Test
    @Transactional
    public void useTokenTest() throws Exception {
        securityService.createSponsor(new Sponsor("testsponsor", "testpassword", "testname", null));
        mockMvc.perform(get("/api/mentor/all")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "hihi",
                            "password": "haha"
                        }""")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
        MvcResult tokenAnswer = mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "testsponsor",
                            "password": "testpassword"
                        }""")
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn();
        JSONObject jsonResponse = new JSONObject(tokenAnswer.getResponse().getContentAsString());
        mockMvc.perform(get("/api/mentor/all")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "hihi",
                            "password": "haha"
                        }""")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jsonResponse.getString("token"))
        ).andExpect(status().isOk());
        sponsorRepository.deleteByUsername("testsponsor");
    }
}