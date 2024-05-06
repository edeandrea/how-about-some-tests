package com.example;

import java.util.List;
import java.util.Optional;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import io.smallrye.common.annotation.RunOnVirtualThread;

@Path("/pets")
@Produces(MediaType.APPLICATION_JSON)
@RunOnVirtualThread
public class PetResource {
	// tag::petResourceContents[]
	private final PetRepository petRepository;

	public PetResource(PetRepository petRepository) {
		this.petRepository = petRepository;
	}

	@GET
	public List<Pet> getAll(@QueryParam("kind") Optional<String> kind) {
		return kind.map(this.petRepository::findPetsByKind)
			.orElseGet(this.petRepository::listAll);
	}

	@GET
	@Path("/{id}")
	public Response getPetById(@PathParam("id") Long id) {
		return this.petRepository.findByIdOptional(id)
			.map(Response::ok)
			.orElseGet(() -> Response.status(Status.NOT_FOUND))
			.build();
	}
	// end::petResourceContents[]
}
