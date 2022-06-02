package com.example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pets")
public class Pet {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String kind;

	private String adoptedBy;

	public Pet(Long id, String name, String kind, String adoptedBy) {
		this.id = id;
		this.name = name;
		this.kind = kind;
		this.adoptedBy = adoptedBy;
	}

	public Pet() {
	}

	public Pet(Long id, String name, String kind) {
		this(id, name, kind, null);
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
