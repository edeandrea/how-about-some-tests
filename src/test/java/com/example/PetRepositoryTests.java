package com.example;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestTransaction
class PetRepositoryTests {
	@Inject
	PetRepository petRepository;

	@Test
	public void petsByKindFound() {
		this.petRepository.deleteAll();
		assertThat(this.petRepository.count()).isZero();

		this.petRepository.persist(new Pet("fluffy", "cat"));

		assertThat(this.petRepository.findPetsByKind("cat"))
			.isNotNull()
			.hasSize(1)
			.flatExtracting(Pet::getKind, Pet::getName)
			.containsExactly("cat", "fluffy");
	}

	@Test
	public void noPetsByKindFound() {
		this.petRepository.deleteAll();
		assertThat(this.petRepository.count()).isZero();
		assertThat(this.petRepository.findPetsByKind("cat"))
			.isNotNull()
			.isEmpty();
	}

	@Test
	public void adoptFoundPet() {
		this.petRepository.deleteAll();
		assertThat(this.petRepository.count()).isZero();

		this.petRepository.persist(
			new Pet("fluffy", "cat"),
			new Pet("harry", "dog")
		);

		assertThat(this.petRepository.adoptPetIfFound("cat", "Eric"))
			.isNotNull()
			.isPresent()
			.get()
			.extracting(Pet::getKind, Pet::getName, Pet::getAdoptedBy)
			.containsExactly("cat", "fluffy", "Eric");
	}

	@Test
	public void noAdoptablePetFound() {
		this.petRepository.deleteAll();
		assertThat(this.petRepository.count()).isZero();

		this.petRepository.persist(
			new Pet(null, "fluffy", "cat", "Eric"),
			new Pet("harry", "dog")
		);
		assertThat(this.petRepository.adoptPetIfFound("cat", "Eric"))
			.isNotNull()
			.isEmpty();
	}
}