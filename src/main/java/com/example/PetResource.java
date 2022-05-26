package com.example;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/pets")
@Produces(MediaType.APPLICATION_JSON)
public class PetResource {
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
			.map(pet -> Response.ok(pet).build())
			.orElseGet(() -> Response.status(Status.NOT_FOUND).build());
	}
}
