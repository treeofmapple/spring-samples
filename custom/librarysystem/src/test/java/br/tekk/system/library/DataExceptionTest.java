package br.tekk.system.library;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.tekk.system.library.exception.BookAlreadyExistsException;
import br.tekk.system.library.exception.BookItemAlreadyExistsException;
import br.tekk.system.library.exception.BookItemNotFoundException;
import br.tekk.system.library.exception.BookNotFoundException;
import br.tekk.system.library.exception.MaxBookRentException;
import br.tekk.system.library.exception.UserAlreadyExistsException;
import br.tekk.system.library.exception.UserNotFoundException;
import br.tekk.system.library.model.Book;
import br.tekk.system.library.model.Status;
import br.tekk.system.library.model.User;
import br.tekk.system.library.request.BookItemRequest;
import br.tekk.system.library.request.BookRequest;
import br.tekk.system.library.request.UserRequest;
import br.tekk.system.library.service.BookItemService;
import br.tekk.system.library.service.BookService;
import br.tekk.system.library.service.UserService;
import net.datafaker.Faker;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class DataExceptionTest {

	@MockBean
	private UserService userService;

	@MockBean
	private BookService bookService;

	@MockBean
	private BookItemService bookItemService;

	private UserRequest userRequest;

	private BookRequest bookRequest;

	private BookItemRequest bookItemRequest;

	private User user;

	private Book book;

	private final Faker faker = new Faker();

	@BeforeEach
	void setup() {
		userRequest = new UserRequest(faker.name().name(), faker.number().numberBetween(6, 70),
				faker.internet().emailAddress(), faker.internet().password());

		bookRequest = new BookRequest(faker.book().title(), faker.book().author(), faker.number().numberBetween(1, 100),
				faker.number().randomDouble(2, 10, 100),
				LocalDate.now().minusYears(faker.number().numberBetween(1, 50)));

		user = new User();
		user.setUsername(faker.name().name());
		user.setEmail(faker.internet().emailAddress());
		user.setPassword(faker.internet().password());
		user.setAge(faker.number().numberBetween(6, 70));

		book = new Book();
		book.setTitle(faker.book().title());
		book.setAuthor(faker.book().author());
		book.setQuantity(faker.number().numberBetween(1, 100));
		book.setPrice(faker.number().randomDouble(2, 10, 100));
		book.setAno(LocalDate.now().minusYears(faker.number().numberBetween(1, 50)));

		bookItemRequest = new BookItemRequest(book.getId(), user.getId(), Status.RENT, LocalDate.now(),
				LocalDate.now());
	}

	@Test
	@Order(1)
	@DisplayName("1 - User Not Found Exception")
	void testUserNotFoundException() throws Exception {
		UUID nonExistentUserId = UUID.randomUUID();
		when(userService.findById(nonExistentUserId))
				.thenThrow(new UserNotFoundException("No user found with ID: " + nonExistentUserId));

		assertThrows(UserNotFoundException.class, () -> userService.findById(nonExistentUserId),
				"No user found with ID: " + nonExistentUserId);
	}

	@Test
	@Order(2)
	@DisplayName("2 - User Already Exists Exception")
	void testUserAlreadyExistsException() throws Exception {
		when(userService.createUser(userRequest)).thenThrow(
				new UserAlreadyExistsException("User with same email already exists " + userRequest.email()));

		assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userRequest),
				"User with same email already exists " + userRequest.email());
	}

	@Test
	@Order(3)
	@DisplayName("3 - Book Not Found Exception")
	void testBookNotFoundException() throws Exception {
		int nonExistentBookId = 99999;
		when(bookService.findById(nonExistentBookId))
				.thenThrow(new BookNotFoundException("No books found with the provided ID: " + nonExistentBookId));

		assertThrows(BookNotFoundException.class, () -> bookService.findById(nonExistentBookId),
				"No books found with the provided ID: " + nonExistentBookId);
	}

	@Test
	@Order(4)
	@DisplayName("4 - Book Already Exists Exception")
	void testBookAlreadyExistsException() throws Exception {
		when(bookService.createBook(bookRequest))
				.thenThrow(new BookAlreadyExistsException("Book with the same title already exists"));

		assertThrows(BookAlreadyExistsException.class, () -> bookService.createBook(bookRequest),
				"Book with the same title already exists");
	}

	@Test
	@Order(5)
	@DisplayName("5 - Book Item Not Found Exception")
	void testBookItemNotFoundException() throws Exception {
		int nonExistentBookItemId = 99999;
		when(bookItemService.findById(nonExistentBookItemId)).thenThrow(
				new BookItemNotFoundException("No book item found with the provided ID: " + nonExistentBookItemId));

		assertThrows(BookItemNotFoundException.class, () -> bookItemService.findById(nonExistentBookItemId),
				"No book item found with the provided ID: " + nonExistentBookItemId);
	}

	@Test
	@Order(6)
	@DisplayName("6 - Book Item Already Exists Exception")
	void testBookItemAlreadyExistsException() throws Exception {
		when(bookItemService.createBookItem(bookItemRequest))
				.thenThrow(new BookItemAlreadyExistsException("Book item with the same title already exists"));

		assertThrows(BookItemAlreadyExistsException.class, () -> bookItemService.createBookItem(bookItemRequest),
				"Book item with the same title already exists");
	}

	@Test
	@Order(7)
	@DisplayName("7 - Max Book Rent Exception")
	void testMaxBookRentException() throws Exception {
		for (int i = 0; i < 4; i++) {
			when(bookItemService.createBookItem(bookItemRequest)).thenReturn(i + 1);
		}

		when(bookItemService.createBookItem(bookItemRequest)).thenThrow(
				new MaxBookRentException("User reached max rent limit for book ID: " + bookItemRequest.book()));

		assertThrows(MaxBookRentException.class, () -> bookItemService.createBookItem(bookItemRequest),
				"User reached max rent limit for book ID: " + bookItemRequest.book());
	}

}