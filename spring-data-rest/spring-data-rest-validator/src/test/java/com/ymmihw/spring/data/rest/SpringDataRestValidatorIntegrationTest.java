package com.ymmihw.spring.data.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymmihw.spring.data.rest.models.WebsiteUser;

@SpringBootTest(classes = SpringDataRestApplication.class)
@WebAppConfiguration
public class SpringDataRestValidatorIntegrationTest {
  public static final String URL = "http://localhost";

  private MockMvc mockMvc;

  @Autowired
  protected WebApplicationContext wac;

  @BeforeEach
  public void setup() {
    mockMvc = webAppContextSetup(wac).build();
  }

  @Test
  public void whenStartingApplication_thenCorrectStatusCode() throws Exception {
    mockMvc.perform(get("/users")).andExpect(status().is2xxSuccessful());
  };

  @Test
  public void whenAddingNewCorrectUser_thenCorrectStatusCodeAndResponse() throws Exception {
    WebsiteUser user = new WebsiteUser();
    user.setEmail("john.doe@john.com");
    user.setName("John Doe");

    mockMvc
        .perform(post("/users", user).contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(user)))
        .andExpect(status().is2xxSuccessful()).andExpect(redirectedUrl("http://localhost/users/1"));
  }

  @Test
  public void whenAddingNewUserWithoutName_thenErrorStatusCodeAndResponse() throws Exception {
    WebsiteUser user = new WebsiteUser();
    user.setEmail("john.doe@john.com");

    mockMvc
        .perform(post("/users", user).contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(user)))
        .andExpect(status().isNotAcceptable()).andExpect(redirectedUrl(null));
  }

  @Test
  public void whenAddingNewUserWithEmptyName_thenErrorStatusCodeAndResponse() throws Exception {
    WebsiteUser user = new WebsiteUser();
    user.setEmail("john.doe@john.com");
    user.setName("");
    mockMvc
        .perform(post("/users", user).contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(user)))
        .andExpect(status().isNotAcceptable()).andExpect(redirectedUrl(null));
  }

  @Test
  public void whenAddingNewUserWithoutEmail_thenErrorStatusCodeAndResponse() throws Exception {
    WebsiteUser user = new WebsiteUser();
    user.setName("John Doe");

    mockMvc
        .perform(post("/users", user).contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(user)))
        .andExpect(status().isNotAcceptable()).andExpect(redirectedUrl(null));
  }

  @Test
  public void whenAddingNewUserWithEmptyEmail_thenErrorStatusCodeAndResponse() throws Exception {
    WebsiteUser user = new WebsiteUser();
    user.setName("John Doe");
    user.setEmail("");
    mockMvc
        .perform(post("/users", user).contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(user)))
        .andExpect(status().isNotAcceptable()).andExpect(redirectedUrl(null));
  }

}
