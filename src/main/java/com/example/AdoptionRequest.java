package com.example;

import io.quarkus.runtime.annotations.RegisterForReflection;

// tag::adoptionRequest[]
@RegisterForReflection
public record AdoptionRequest(String owner, String kind) { }
// end::adoptionRequest[]
