package br.tekk.system.library;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.tekk.system.library.request.UserRequest;
import net.datafaker.Faker;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class UserControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private UserRequest userRequest;

	private final Faker faker = new Faker();

	@BeforeEach
	void setUp() {
		userRequest = new UserRequest(faker.name().name(), faker.number().numberBetween(6, 70),
				faker.internet().emailAddress(), faker.internet().password());
	}

	@Test
	@Order(1)
	@DisplayName("1 - Create User")
	public void testCreateUser() throws Exception {
		mockMvc.perform(post("/users/create").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userRequest))).andExpect(status().isCreated())
				.andExpect(jsonPath("$").isString());
	}

	@Test
	@Order(2)
	@DisplayName("2 - FindUserById")
	public void testFindUserById() throws Exception {
		String userId = mockMvc
				.perform(post("/users/create").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userRequest)))
				.andReturn().getResponse().getContentAsString();

		mockMvc.perform(get("/users/get/{id}", UUID.fromString(objectMapper.readTree(userId).asText()))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(objectMapper.readTree(userId).asText()));
	}

	@Test
	@Order(3)
	@DisplayName("3 - Update User")
	public void testUpdateUser() throws Exception {
		String userId = mockMvc
				.perform(post("/users/create").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userRequest)))
				.andReturn().getResponse().getContentAsString();

		UserRequest updatedRequest = new UserRequest(faker.name().name(), faker.number().numberBetween(6, 70),
				faker.internet().emailAddress(), faker.internet().password());

		mockMvc.perform(put("/users/update/{id}", UUID.fromString(objectMapper.readTree(userId).asText()))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedRequest)))
				.andExpect(status().isNoContent());
	}

	@Test
	@Order(4)
	@DisplayName("4 - DeleteUser")
	public void testDeleteUser() throws Exception {
		String userId = mockMvc
				.perform(post("/users/create").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userRequest)))
				.andReturn().getResponse().getContentAsString();

		mockMvc.perform(delete("/users/delete/{id}", UUID.fromString(objectMapper.readTree(userId).asText()))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
	}

}
