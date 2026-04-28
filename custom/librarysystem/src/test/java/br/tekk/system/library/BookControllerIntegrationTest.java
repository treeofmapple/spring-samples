package br.tekk.system.library;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

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

import br.tekk.system.library.request.BookRequest;
import net.datafaker.Faker;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class BookControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private BookRequest bookRequest;

	private final Faker faker = new Faker();

	private final String bookTitle = faker.book().title();
	private final String bookAuthor = faker.book().author();

	@BeforeEach
	void setUp() {
		bookRequest = new BookRequest(bookTitle, bookAuthor, faker.number().numberBetween(1, 100),
				faker.number().randomDouble(2, 10, 80),
				LocalDate.now().minusYears(faker.number().numberBetween(1, 100)));
	}

	@Test
	@Order(1)
	@DisplayName("1 - Create Book")
	public void testCreateBook() throws Exception {
		mockMvc.perform(post("/books/create").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(bookRequest))).andExpect(status().isCreated())
				.andExpect(jsonPath("$").isNumber());
	}

	@Test
	@Order(2)
	@DisplayName("2 - FindBookById")
	public void testFindBookById() throws Exception {
		String bookId = mockMvc
				.perform(post("/books/create").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(bookRequest)))
				.andReturn().getResponse().getContentAsString();

		mockMvc.perform(get("/books/get/{id}", Integer.parseInt(bookId)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.titulo").value(bookTitle))
				.andExpect(jsonPath("$.author").value(bookAuthor));
	}

	@Test
	@Order(3)
	@DisplayName("3 - Update Book")
	public void testUpdateBook() throws Exception {
		String bookId = mockMvc
				.perform(post("/books/create").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(bookRequest)))
				.andReturn().getResponse().getContentAsString();

		BookRequest updatedRequest = new BookRequest(faker.book().title(), faker.book().author(),
				faker.number().numberBetween(1, 100), faker.number().randomDouble(2, 10, 80),
				LocalDate.now().minusYears(faker.number().numberBetween(1, 100)));

		mockMvc.perform(put("/books/update/{id}", Integer.parseInt(bookId)).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedRequest))).andExpect(status().isNoContent());
	}

	@Test
	@Order(4)
	@DisplayName("4 - Delete Book")
	public void testDeleteBook() throws Exception {
		String bookId = mockMvc
				.perform(post("/books/create").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(bookRequest)))
				.andReturn().getResponse().getContentAsString();

		mockMvc.perform(delete("/books/delete/{id}", Integer.parseInt(bookId)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

}
