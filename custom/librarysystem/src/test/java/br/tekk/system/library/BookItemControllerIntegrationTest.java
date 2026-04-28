package br.tekk.system.library;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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

import br.tekk.system.library.model.Status;
import br.tekk.system.library.request.BookItemRequest;
import br.tekk.system.library.request.BookRequest;
import br.tekk.system.library.request.UserRequest;
import net.datafaker.Faker;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class BookItemControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private UserRequest userRequest;

	private BookRequest bookRequest;

	private BookItemRequest bookItemRequest;

	private String userId;

	private final Faker faker = new Faker();

	@BeforeEach
	void setUp() throws Exception {
		userRequest = new UserRequest(faker.name().name(), faker.number().numberBetween(6, 70),
				faker.internet().emailAddress(), faker.internet().password());

		bookRequest = new BookRequest(faker.book().title(), faker.book().author(), faker.number().numberBetween(1, 100),
				faker.number().randomDouble(2, 10, 80),
				LocalDate.now().minusYears(faker.number().numberBetween(1, 100)));

		userId = mockMvc
				.perform(post("/users/create").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userRequest)))
				.andReturn().getResponse().getContentAsString();

		String bookId = mockMvc
				.perform(post("/books/create").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(bookRequest)))
				.andReturn().getResponse().getContentAsString();

		bookItemRequest = new BookItemRequest(Integer.parseInt(bookId),
				UUID.fromString(objectMapper.readTree(userId).asText()), Status.AVAILABLE, null, null);
	}

	@Test
	@Order(1)
	@DisplayName("1 - Create Book Item")
	public void testCreateBookItem() throws Exception {
		mockMvc.perform(post("/items/create").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(bookItemRequest))).andExpect(status().isCreated())
				.andExpect(jsonPath("$").isNumber());
	}

	@Test
	@Order(2)
	@DisplayName("2 - Find Book Item By ID")
	public void testFindBookItemById() throws Exception {
		String bookItemId = mockMvc
				.perform(post("/items/create").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(bookItemRequest)))
				.andReturn().getResponse().getContentAsString();

		mockMvc.perform(get("/items/get/{id}", Integer.parseInt(bookItemId)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@Order(3)
	@DisplayName("3 - Update Book Item ")
	public void testUpdateBookItem() throws Exception {
		String bookItemId = mockMvc
				.perform(post("/items/create").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(bookItemRequest)))
				.andReturn().getResponse().getContentAsString();

		BookItemRequest bookItemRequest = new BookItemRequest(1,
				UUID.fromString(objectMapper.readTree(userId).asText()), Status.RENT, LocalDate.now(),
				LocalDate.now().plusDays(7));

		mockMvc.perform(put("/items/update/{id}", Integer.parseInt(bookItemId)).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(bookItemRequest))).andExpect(status().isAccepted());
	}

	@Test
	@Order(4)
	@DisplayName("4 - Delete Book Item")
	public void testDeleteBookItem() throws Exception {
		String bookItemId = mockMvc
				.perform(post("/items/create").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(bookItemRequest)))
				.andReturn().getResponse().getContentAsString();

		mockMvc.perform(
				delete("/items/delete/{id}", Integer.parseInt(bookItemId)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted());

		mockMvc.perform(get("/items/get/{id}", Integer.parseInt(bookItemId))).andExpect(status().isNotFound());
	}

	/*
	 * @Test public void testSellBookItem() throws Exception { String bookItemId =
	 * mockMvc
	 * .perform(post("/items/create").contentType(MediaType.APPLICATION_JSON)
	 * .content(objectMapper.writeValueAsString(bookItemRequest)))
	 * .andReturn().getResponse().getContentAsString();
	 * 
	 * mockMvc.perform(put("/items/sell/{id}",
	 * Integer.parseInt(bookItemId)).contentType(MediaType.APPLICATION_JSON))
	 * .andExpect(status().isOk()); }
	 * 
	 * @Test public void testStartRentBookItem() throws Exception { String
	 * bookItemId = mockMvc
	 * .perform(post("/items/create").contentType(MediaType.APPLICATION_JSON)
	 * .content(objectMapper.writeValueAsString(bookItemRequest)))
	 * .andReturn().getResponse().getContentAsString();
	 * 
	 * mockMvc.perform( put("/items/rent/start/{id}",
	 * Integer.parseInt(bookItemId)).contentType(MediaType.APPLICATION_JSON))
	 * .andExpect(status().isOk()).andExpect(jsonPath("$").isNotEmpty()); }
	 * 
	 * @Test public void testReturnBookItem() throws Exception { String bookItemId =
	 * mockMvc
	 * .perform(post("/items/create").contentType(MediaType.APPLICATION_JSON)
	 * .content(objectMapper.writeValueAsString(bookItemRequest)))
	 * .andReturn().getResponse().getContentAsString();
	 * 
	 * mockMvc.perform(put("/items/return/{id}",
	 * Integer.parseInt(bookItemId)).contentType(MediaType.APPLICATION_JSON))
	 * .andExpect(status().isOk()).andExpect(jsonPath("$").isNotEmpty()); }
	 */
}