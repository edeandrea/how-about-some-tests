package com.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.Optional;

import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;

import io.smallrye.reactive.messaging.memory.InMemoryConnector;

@QuarkusTest
class AdoptionListenerTests {
	@InjectSpy
	AdoptionListener adoptionListener;

	@InjectMock
	PetRepository petRepository;

	@Inject
	@Any
	InMemoryConnector inMemoryConnector;

	@BeforeEach
	public void beforeEach() {
		// Clear the emitter sink between tests
		this.inMemoryConnector.sink(AdoptionListener.ADOPTIONS_CHANNEL_NAME).clear();
	}

	@Test
	public void adoptablePetFound() {
		var pet = new Pet(1L, "fluffy", "cat", "Eric");
		var adoptionRequest = new AdoptionRequest("Eric", pet.getKind());

		// tag::adoptablePetFound[]
		// Set up mock
		when(this.petRepository.adoptPetIfFound(adoptionRequest.kind(), adoptionRequest.owner()))
			.thenReturn(Optional.of(pet));

		// Send request to channel
		this.inMemoryConnector.source(AdoptionListener.ADOPTION_REQUESTS_CHANNEL_NAME)
			.send(adoptionRequest);

		// Create sink
		var sink = this.inMemoryConnector.sink(AdoptionListener.ADOPTIONS_CHANNEL_NAME);

		// Wait for messages to arrive in sink
		await()
			.atMost(Duration.ofSeconds(10))
			.until(() -> sink.received().size() == 1);

		// Perform assertions on received message(s)
		assertThat(sink.received())
			.isNotNull()
			.singleElement()
			.extracting(Message::getPayload)
			.usingRecursiveComparison()
			.isEqualTo(new Pet(pet.getId(), pet.getName(), pet.getKind(), adoptionRequest.owner()));

		// Verify interactions
		verify(this.petRepository).adoptPetIfFound(adoptionRequest.kind(), adoptionRequest.owner());
		verify(this.adoptionListener).handleAdoption(any(AdoptionRequest.class));
		verifyNoMoreInteractions(this.petRepository);
		// end::adoptablePetFound[]
	}

	@Test
	public void adoptablePetNotFound() {
		var pet = new Pet(1L, "fluffy", "cat");
		var adoptionRequest = new AdoptionRequest("Eric", pet.getKind());

		// tag::adoptablePetNotFound[]
		// Set up mock
		when(this.petRepository.adoptPetIfFound(adoptionRequest.kind()  , adoptionRequest.owner()))
			.thenReturn(Optional.empty());

		// Send request to channel
		this.inMemoryConnector.source(AdoptionListener.ADOPTION_REQUESTS_CHANNEL_NAME)
			.send(adoptionRequest);

		// Verify interactions (with timeout)
		verify(this.petRepository, timeout(10_000)).adoptPetIfFound(adoptionRequest.kind(), adoptionRequest.owner());
		verify(this.adoptionListener, timeout(10_000)).handleAdoption(any(AdoptionRequest.class));
		verifyNoMoreInteractions(this.petRepository);
		// end::adoptablePetNotFound[]
	}
}