package com.example;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.quarkus.logging.Log;

import io.smallrye.common.annotation.RunOnVirtualThread;

@ApplicationScoped
public class AdoptionListener {
	static final String ADOPTIONS_CHANNEL_NAME = "adoptions";
	static final String ADOPTION_REQUESTS_CHANNEL_NAME = "adoption-requests";

	// tag::AdoptionListenerAttributes[]
	private final PetRepository petRepository;
	private final Emitter<Pet> petEmitter;

	public AdoptionListener(PetRepository petRepository, @Channel(ADOPTIONS_CHANNEL_NAME) Emitter<Pet> petEmitter) {
		this.petRepository = petRepository;
		this.petEmitter = petEmitter;
	}
	// end::AdoptionListenerAttributes[]

	@Incoming(ADOPTION_REQUESTS_CHANNEL_NAME)
	@RunOnVirtualThread
	public void handleAdoption(AdoptionRequest adoptionRequest) {
		// tag::handleAdoption[]
		Log.infof("Handling adoption for request: %s", adoptionRequest);
		this.petRepository.adoptPetIfFound(adoptionRequest.kind(), adoptionRequest.owner())
			.ifPresent(this.petEmitter::send);
		// end::handleAdoption[]
	}
}
