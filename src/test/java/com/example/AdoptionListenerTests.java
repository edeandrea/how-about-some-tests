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
	InMemoryConnector emitterConnector;

	@BeforeEach
	public void beforeEach() {
		// Clear the emitter sink between tests
		this.emitterConnector.sink(AdoptionListener.ADOPTIONS_CHANNEL_NAME).clear();
	}

	@Test
	public void adoptablePetFound() {
		var pet = new Pet(1L, "fluffy", "cat", "Eric");
		var adoptionRequest = new AdoptionRequest("Eric", pet.getKind());

		when(this.petRepository.adoptPetIfFound(pet.getKind(), adoptionRequest.owner()))
			.thenReturn(Optional.of(pet));

		this.emitterConnector.source(AdoptionListener.ADOPTION_REQUESTS_CHANNEL_NAME).send(adoptionRequest);
		var sink = this.emitterConnector.sink(AdoptionListener.ADOPTIONS_CHANNEL_NAME);

		await()
			.atMost(Duration.ofSeconds(10))
			.until(() -> sink.received().size() == 1);

		assertThat(sink.received())
			.isNotNull()
			.singleElement()
			.extracting(Message::getPayload)
			.usingRecursiveComparison()
			.isEqualTo(new Pet(pet.getId(), pet.getName(), pet.getKind(), adoptionRequest.owner()));

		verify(this.petRepository).adoptPetIfFound(pet.getKind(), adoptionRequest.owner());
		verify(this.adoptionListener).handleAdoption(any(AdoptionRequest.class));
		verifyNoMoreInteractions(this.petRepository);
	}

	@Test
	public void adoptablePetNotFound() {
		var pet = new Pet(1L, "fluffy", "cat");
		var adoptionRequest = new AdoptionRequest("Eric", pet.getKind());

		when(this.petRepository.adoptPetIfFound(pet.getKind(), adoptionRequest.owner()))
			.thenReturn(Optional.empty());

		this.emitterConnector.source(AdoptionListener.ADOPTION_REQUESTS_CHANNEL_NAME).send(adoptionRequest);

		verify(this.petRepository, timeout(10 * 1000)).adoptPetIfFound(pet.getKind(), adoptionRequest.owner());
		verify(this.adoptionListener, timeout(10 * 1000)).handleAdoption(any(AdoptionRequest.class));
		verifyNoMoreInteractions(this.petRepository);
	}
}