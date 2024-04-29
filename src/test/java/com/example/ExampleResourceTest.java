package com.example;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ExampleResourceTest {
	@Test
	public void testHelloEndpoint() {
		get("/hello").then()
			.statusCode(200)
			.body(is("Hello from RESTEasy Reactive"));
	}
}