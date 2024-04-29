package com.example;

import static io.restassured.RestAssured.get;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

import io.restassured.http.ContentType;

@QuarkusTest
class PetResourceTests {
	@InjectMock
	PetRepository petRepository;

	@Test
	public void getAll() {
		when(this.petRepository.listAll())
			.thenReturn(List.of(new Pet(1L, "fluffy", "cat")));

		get("/pets")
			.then()
			.contentType(ContentType.JSON)
			.statusCode(OK.getStatusCode())
			.body("$.size()", is(1))
			.body("[0].name", is("fluffy"))
			.body("[0].kind", is("cat"))
			.body("[0].adoptedBy", blankOrNullString());

		verify(this.petRepository).listAll();
		verifyNoMoreInteractions(this.petRepository);
	}

	@Test
	public void getByKind() {
		when(petRepository.findPetsByKind("cat"))
			.thenReturn(List.of(new Pet(1L, "fluffy", "cat", "Eric")));

		get("/pets?kind={kind}", "cat")
			.then()
			.contentType(ContentType.JSON)
			.statusCode(OK.getStatusCode())
			.body("$.size()", is(1))
			.body("[0].name", is("fluffy"))
			.body("[0].kind", is("cat"))
			.body("[0].adoptedBy", is("Eric"));

		verify(this.petRepository).findPetsByKind("cat");
		verifyNoMoreInteractions(this.petRepository);
	}

	@Test
	public void getByIdFound() {
		when(petRepository.findByIdOptional(1L))
			.thenReturn(Optional.of(new Pet(1L, "fluffy", "cat", "Eric")));

		get("/pets/{id}", 1L)
			.then()
			.contentType(ContentType.JSON)
			.statusCode(OK.getStatusCode())
			.body("name", is("fluffy"))
			.body("kind", is("cat"))
			.body("adoptedBy", is("Eric"));

		verify(this.petRepository).findByIdOptional(1L);
		verifyNoMoreInteractions(this.petRepository);
	}

	@Test
	public void getByIdNotFound() {
		when(petRepository.findByIdOptional(1L))
			.thenReturn(Optional.empty());

		get("/pets/{id}", 1L)
			.then()
			.statusCode(NOT_FOUND.getStatusCode())
			.body(blankOrNullString());

		verify(this.petRepository).findByIdOptional(1L);
		verifyNoMoreInteractions(this.petRepository);
	}
}