package com.example;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record AdoptionRequest(String owner, String kind) { }
