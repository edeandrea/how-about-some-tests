package com.example;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.quarkus.logging.Log;

import io.smallrye.common.annotation.Blocking;

@ApplicationScoped
public class AdoptionListener {
	static final String ADOPTIONS_CHANNEL_NAME = "adoptions";
	static final String ADOPTION_REQUESTS_CHANNEL_NAME = "adoption-requests";

	private final PetRepository petRepository;
	private final Emitter<Pet> petEmitter;

	public AdoptionListener(PetRepository petRepository, @Channel(ADOPTIONS_CHANNEL_NAME) Emitter<Pet> petEmitter) {
		this.petRepository = petRepository;
		this.petEmitter = petEmitter;
	}

	@Incoming(ADOPTION_REQUESTS_CHANNEL_NAME)
	@Blocking
	public void handleAdoption(AdoptionRequest adoptionRequest) {
		Log.infof("Handling adoption for request: %s", adoptionRequest);
		this.petRepository.adoptPetIfFound(adoptionRequest.getKind(), adoptionRequest.getOwner())
			.ifPresent(this.petEmitter::send);
	}
}
