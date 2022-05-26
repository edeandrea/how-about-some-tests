package com.example;

public class AdoptionRequest {
	private String owner;
	private String kind;

	public AdoptionRequest() {
	}

	public AdoptionRequest(String owner, String kind) {
		this.owner = owner;
		this.kind = kind;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	@Override
	public String toString() {
		return "AdoptionRequest{" + "owner='" + owner + '\'' + ", kind='" + kind + '\'' + '}';
	}
}
