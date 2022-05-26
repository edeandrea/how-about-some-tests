package com.example;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import javax.ws.rs.core.Response.Status;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.kafka.InjectKafkaCompanion;
import io.quarkus.test.kafka.KafkaCompanionResource;

import io.restassured.http.ContentType;
import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion;

@QuarkusIntegrationTest
@QuarkusTestResource(KafkaCompanionResource.class)
public class AdoptionListenerIT {
	@InjectKafkaCompanion
	KafkaCompanion kafkaCompanion;

	@Test
	public void adoptablePetFound() {
		var pet = getPet();
		var adoptionRequest = new AdoptionRequest("Eric", pet.getKind());

		this.kafkaCompanion.produceWithSerializers(new StringSerializer(), new ObjectMapperSerializer<AdoptionRequest>())
			.fromRecords(new ProducerRecord<>(AdoptionListener.ADOPTION_REQUESTS_CHANNEL_NAME, adoptionRequest))
			.awaitCompletion(Duration.ofSeconds(10));

		var adoptionConsumer = this.kafkaCompanion.consumeWithDeserializers(new StringDeserializer(), new PetDeserializer())
			.fromTopics(AdoptionListener.ADOPTIONS_CHANNEL_NAME, 1)
			.awaitNextRecords(1, Duration.ofSeconds(20));

		assertThat(adoptionConsumer.count())
			.isEqualTo(1L);

		assertThat(adoptionConsumer.getLastRecord().value())
			.isNotNull()
			.usingRecursiveComparison()
			.isEqualTo(new Pet(pet.getId(), pet.getName(), adoptionRequest.getKind(), adoptionRequest.getOwner()));

		assertThat(getPet())
			.isNotNull()
			.usingRecursiveComparison()
			.isEqualTo(new Pet(pet.getId(), pet.getName(), adoptionRequest.getKind(), adoptionRequest.getOwner()));
	}

	private Pet getPet() {
		return get("/pets/{id}", 1L)
			.then()
			.statusCode(Status.OK.getStatusCode())
			.contentType(ContentType.JSON)
			.extract().body()
			.as(Pet.class);
	}

	public static class PetDeserializer extends ObjectMapperDeserializer<Pet> {
		public PetDeserializer() {
			super(Pet.class);
		}
	}
}
