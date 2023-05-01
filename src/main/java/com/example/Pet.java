package com.example;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "pets")
public class Pet {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pets_seq")
	@SequenceGenerator(name = "pets_seq", allocationSize = 1, sequenceName = "pets_seq")
	private Long id;
	private String name;
	private String kind;
	private String adoptedBy;

	public Pet() {

	}

	public Pet(String name, String kind) {
		this(null, name, kind);
	}

	public Pet(Long id, String name, String kind) {
		this(id, name, kind, null);
	}

	public Pet(Long id, String name, String kind, String adoptedBy) {
		this.id = id;
		this.name = name;
		this.kind = kind;
		this.adoptedBy = adoptedBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getAdoptedBy() {
		return adoptedBy;
	}

	public void setAdoptedBy(String adoptedBy) {
		this.adoptedBy = adoptedBy;
	}

	@Override
	public String toString() {
		return "Pet{" + "id=" + id + ", name='" + name + '\'' + ", kind='" + kind + '\'' + ", adoptedBy='" + adoptedBy + '\'' + '}';
	}
}
