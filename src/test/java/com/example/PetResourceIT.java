package com.example;

import static io.restassured.RestAssured.get;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.blankOrNullString;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusIntegrationTest;

import io.restassured.http.ContentType;

@QuarkusIntegrationTest
public class PetResourceIT {
	private static final int NUM_RECORDS = 3;

	@Test
	public void getAll() {
		var pets = get("/pets")
			.then()
			.contentType(ContentType.JSON)
			.statusCode(OK.getStatusCode())
			.extract()
			.body()
			.jsonPath().getList(".", Pet.class);

		assertThat(pets)
			.isNotNull()
			.hasSize(NUM_RECORDS)
			.extracting(Pet::getName, Pet::getKind)
			.containsExactlyInAnyOrder(
				tuple("fluffy", "cat"),
				tuple("herman", "dog"),
				tuple("cutiepie", "monkey")
			);
	}

	@Test
	public void getByKind() {
		get("/pets?kind={kind}", "cat")
			.then()
			.contentType(ContentType.JSON)
			.statusCode(OK.getStatusCode())
			.body("$.size()", is(1))
			.body("[0].name", is("fluffy"))
			.body("[0].kind", is("cat"));
	}

	@Test
	public void getByIdFound() {
		get("/pets/{id}", 1L)
			.then()
			.contentType(ContentType.JSON)
			.statusCode(OK.getStatusCode())
			.body("name", is("fluffy"))
			.body("kind", is("cat"));
	}

	@Test
	public void getByIdNotFound() {
		get("/pets/{id}", NUM_RECORDS + 1)
			.then()
			.statusCode(NOT_FOUND.getStatusCode())
			.body(blankOrNullString());
	}
}
