package br.tekk.system.library;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import br.tekk.system.library.model.Book;
import br.tekk.system.library.model.BookItem;
import br.tekk.system.library.model.Status;
import br.tekk.system.library.model.User;
import br.tekk.system.library.repository.BookItemRepository;
import br.tekk.system.library.repository.BookRepository;
import br.tekk.system.library.repository.UserRepository;
import br.tekk.system.library.request.BookItemResponse;
import br.tekk.system.library.request.BookResponse;
import br.tekk.system.library.request.UserResponse;
import br.tekk.system.library.service.BookItemService;
import br.tekk.system.library.service.BookService;
import br.tekk.system.library.service.UserService;
import net.datafaker.Faker;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class DataPersistenceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookItemRepository bookItemRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private BookService bookService;

	@Autowired
	private BookItemService bookItemService;

	private User user;

	private Book book;

	private BookItem bookItem;

	private final int ITEM_QUANTITY = 100;

	private final Faker faker = new Faker();

	@Test
	@Transactional
	@Order(1)
	@DisplayName("1 - Data Insertion Test")
	void dataInsertionTest() throws Exception {
		for (int i = 0; i < ITEM_QUANTITY; i++) {
			user = new User();
			user.setUsername(faker.name().name());
			user.setEmail(faker.internet().emailAddress());
			user.setPassword(faker.internet().password());
			user.setAge(faker.number().numberBetween(6, 70));
			userRepository.save(user);

			book = new Book();
			book.setTitle(faker.book().title());
			book.setAuthor(faker.book().author());
			book.setQuantity(faker.number().numberBetween(1, 100));
			book.setPrice(faker.number().randomDouble(2, 10, 100));
			book.setAno(LocalDate.now().minusYears(faker.number().numberBetween(1, 50)));
			bookRepository.save(book);

			bookItem = new BookItem();
			bookItem.setBook(book);
			bookItem.setUser(user);

			if (i < 50) {
				bookItem.setStatus(Status.SOLD);
			} else if (i < 75) {
				LocalDate rentStart = getRandomDate();
				bookItem.setStatus(Status.RENT);
				bookItem.setRentStart(rentStart);
				bookItem.setRentEnd(rentStart.plus(30, ChronoUnit.DAYS));
			} else {
				LocalDate rentStart = getRandomDate();
				bookItem.setStatus(Status.RETURNED);
				bookItem.setRentStart(rentStart);
				bookItem.setRentEnd(rentStart.plus(30, ChronoUnit.DAYS));
			}
			bookItemRepository.save(bookItem);
		}

		assertThat(userRepository.count()).isEqualTo(ITEM_QUANTITY);
		assertThat(bookRepository.count()).isEqualTo(ITEM_QUANTITY);
		assertThat(bookItemRepository.count()).isEqualTo(ITEM_QUANTITY);
	}

	@Test
	@Order(2)
	@Transactional
	@DisplayName("2 - Find Data Test")
	void findDataTest() throws Exception {
		for (int i = 0; i < ITEM_QUANTITY; i++) {
			user = new User();
			user.setUsername(faker.name().name());
			user.setEmail(faker.internet().emailAddress());
			user.setPassword(faker.internet().password());
			user.setAge(faker.number().numberBetween(6, 70));
			userRepository.save(user);

			book = new Book();
			book.setTitle(faker.book().title());
			book.setAuthor(faker.book().author());
			book.setQuantity(faker.number().numberBetween(1, 100));
			book.setPrice(faker.number().randomDouble(2, 10, 100));
			book.setAno(LocalDate.now().minusYears(faker.number().numberBetween(1, 50)));
			bookRepository.save(book);

			bookItem = new BookItem();
			bookItem.setBook(book);
			bookItem.setUser(user);

			if (i < 50) {
				bookItem.setStatus(Status.SOLD);
			} else if (i < 75) {
				LocalDate rentStart = getRandomDate();
				bookItem.setStatus(Status.RENT);
				bookItem.setRentStart(rentStart);
				bookItem.setRentEnd(rentStart.plus(30, ChronoUnit.DAYS));
			} else {
				LocalDate rentStart = getRandomDate();
				bookItem.setStatus(Status.RETURNED);
				bookItem.setRentStart(rentStart);
				bookItem.setRentEnd(rentStart.plus(30, ChronoUnit.DAYS));
			}
			bookItemRepository.save(bookItem);
		}
		List<UserResponse> users = userService.findAllUsers();
		List<BookResponse> books = bookService.findAllBooks();
		List<BookItemResponse> bookItems = bookItemService.findAllBookItems();

		assertThat(users).hasSize(ITEM_QUANTITY);
		assertThat(books).hasSize(ITEM_QUANTITY);
		assertThat(bookItems).hasSize(ITEM_QUANTITY);
	}

	@Test
	@Order(3)
	@Transactional
	@DisplayName("3 - Update Data Test")
	void dataUpdateTest() throws Exception {
		userRepository.findAll().forEach(user -> {
			user.setUsername(faker.name().name());
			user.setEmail(faker.internet().emailAddress());
			user.setPassword(faker.internet().password());
			user.setAge(faker.number().numberBetween(6, 70));
			userRepository.save(user);
		});
		bookRepository.findAll().forEach(book -> {
			book.setPrice(book.getPrice() + 10);
			book.setTitle(faker.book().title());
			book.setAuthor(faker.book().author());
			book.setQuantity(faker.number().numberBetween(1, 100));
			book.setPrice(faker.number().randomDouble(2, 10, 100));
			book.setAno(LocalDate.now().minusYears(faker.number().numberBetween(1, 50)));
			bookRepository.save(book);
		});
		List<Book> allBooks = bookRepository.findAll();
		List<User> allUsers = userRepository.findAll();
		bookItemRepository.findAll().forEach(bookItem -> {
			bookItem.setBook(allBooks.get(faker.number().numberBetween(0, allBooks.size())));
			bookItem.setUser(allUsers.get(faker.number().numberBetween(0, allUsers.size())));
			bookItemRepository.save(bookItem);
		});

	}

	@Test
	@Order(4)
	@Transactional
	@DisplayName("4 - Delete Data Test")
	void dataDeleteTest() throws Exception {
		bookItemRepository.findAll().forEach(bookItemRepository::delete);
		userRepository.findAll().forEach(userRepository::delete);
		bookRepository.findAll().forEach(bookRepository::delete);

		assertThat(bookItemRepository.count()).isZero();
		assertThat(userRepository.count()).isZero();
		assertThat(bookRepository.count()).isZero();
	}

	private LocalDate getRandomDate() {
		long minDay = LocalDate.now().minusYears(5).toEpochDay();
		long maxDay = LocalDate.now().toEpochDay();
		long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
		return LocalDate.ofEpochDay(randomDay);
	}
}
