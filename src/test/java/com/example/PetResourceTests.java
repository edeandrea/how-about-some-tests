package com.example;

import static io.restassured.RestAssured.get;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

import io.restassured.http.ContentType;

@QuarkusTest
class PetResourceTests {
	@InjectMock
	PetRepository petRepository;

	@Test
	public void getAll() {
		Mockito.when(petRepository.listAll()).thenReturn(List.of(new Pet(1L, "fluffy", "cat")));

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
		Mockito.when(petRepository.findPetsByKind(eq("cat")))
			.thenReturn(List.of(new Pet(1L, "fluffy", "cat", "Eric")));

		get("/pets?kind={kind}", "cat")
			.then()
			.contentType(ContentType.JSON)
			.statusCode(OK.getStatusCode())
			.body("$.size()", is(1))
			.body("[0].name", is("fluffy"))
			.body("[0].kind", is("cat"))
			.body("[0].adoptedBy", is("Eric"));

		verify(this.petRepository).findPetsByKind(eq("cat"));
		verifyNoMoreInteractions(this.petRepository);
	}

	@Test
	public void getByIdFound() {
		Mockito.when(petRepository.findByIdOptional(eq(1L)))
			.thenReturn(Optional.of(new Pet(1L, "fluffy", "cat", "Eric")));

		get("/pets/{id}", 1L)
			.then()
			.contentType(ContentType.JSON)
			.statusCode(OK.getStatusCode())
			.body("name", is("fluffy"))
			.body("kind", is("cat"))
			.body("adoptedBy", is("Eric"));

		verify(this.petRepository).findByIdOptional(eq(1L));
		verifyNoMoreInteractions(this.petRepository);
	}

	@Test
	public void getByIdNotFound() {
		Mockito.when(petRepository.findByIdOptional(eq(1L)))
			.thenReturn(Optional.empty());

		get("/pets/{id}", 1L)
			.then()
			.statusCode(NOT_FOUND.getStatusCode())
			.body(blankOrNullString());

		verify(this.petRepository).findByIdOptional(eq(1L));
		verifyNoMoreInteractions(this.petRepository);
	}
}