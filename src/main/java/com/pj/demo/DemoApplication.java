package com.pj.demo;

import com.pj.demo.data.User;
import com.pj.demo.data.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@ComponentScan({"com.pj.demo.data"})
@SpringBootApplication
@RestController
public class DemoApplication {
	@Autowired
	private UserRepository repository;
	private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/{name}")
	public ResponseEntity<String> askQuestion(@PathVariable(value = "name") String name) {
		return ResponseEntity.ok("Hey " + name + ", How are you?");
	}

	@GetMapping("/hello")
	public String sayHello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

	@GetMapping("/{call}")
	public ResponseEntity<String> callRest() throws IOException {

		URL url = new URL("");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("accept", "application/json");
		InputStream responseStream = conn.getInputStream();
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));
		String output="NO DATA";

		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}


		return ResponseEntity.ok(output);
	}
/*
	public void run(String... var1) {
		this.repository.deleteAll().block();
		LOGGER.info("Deleted all data in container.");

		final User testUser = new User("testId", "testFirstName", "testLastName", "test address line one");

		// Save the User class to Azure Cosmos DB database.
		final Mono<User> saveUserMono = repository.save(testUser);

		final Flux<User> firstNameUserFlux = repository.findByFirstName("testFirstName");

		//  Nothing happens until we subscribe to these Monos.
		//  findById will not return the user as user is not present.
		final Mono<User> findByIdMono = repository.findById(testUser.getId());
		final User findByIdUser = findByIdMono.block();
		Assert.isNull(findByIdUser, "User must be null");

		final User savedUser = saveUserMono.block();
		Assert.state(savedUser != null, "Saved user must not be null");
		Assert.state(savedUser.getFirstName().equals(testUser.getFirstName()), "Saved user first name doesn't match");

		firstNameUserFlux.collectList().block();

		final Optional<User> optionalUserResult = repository.findById(testUser.getId()).blockOptional();
		Assert.isTrue(optionalUserResult.isPresent(), "Cannot find user.");

		final User result = optionalUserResult.get();
		Assert.state(result.getFirstName().equals(testUser.getFirstName()), "query result firstName doesn't match!");
		Assert.state(result.getLastName().equals(testUser.getLastName()), "query result lastName doesn't match!");

		LOGGER.info("findOne in User collection get result: {}", result.toString());
	}*/
}

