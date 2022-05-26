package com.example;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;

@ApplicationScoped
public class PetRepository implements PanacheRepository<Pet> {
	public List<Pet> findPetsByKind(String kind) {
		Log.infof("Looking for all pets of kind '%s'", kind);
		return list("kind", kind);
	}

	@Transactional
	public Optional<Pet> adoptPetIfFound(String kind, String owner) {
		Log.infof("Looking for an adoptable pet of kind '%s'", kind);
		var pet = find("kind = ?1 AND adoptedBy IS NULL ORDER BY RAND()", kind)
			.page(0, 1)
			.withLock(LockModeType.PESSIMISTIC_WRITE)
			.firstResultOptional();

		pet.ifPresentOrElse(
			p -> {
				Log.infof("Found pet for adoption: %s", pet);
				p.setAdoptedBy(owner);
				persist(p);
			},
			() -> Log.infof("No pet of kind '%s' available for adoption", kind)
		);

		return pet;
	}
}
