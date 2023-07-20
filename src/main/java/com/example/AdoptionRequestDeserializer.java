package com.example;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class AdoptionRequestDeserializer extends ObjectMapperDeserializer<AdoptionRequest> {
	public AdoptionRequestDeserializer() {
		super(AdoptionRequest.class);
	}
}
