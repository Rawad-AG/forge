package dev.forge.nexo.generated.entities;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import dev.forge.nexo.generated.entities.Order;
import java.util.Set;

@Table(name = "users", indexes = {@Index(name = "idx_email", columnList = "email")}, uniqueConstraints = {
		@UniqueConstraint(name = "uk_email", columnNames = {"email"})})
@Entity(name = "User")
@Getter
@Setter
public class User {

	@Id
	private Long id;

	private String username;

	private String email;

	private Integer age;

	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	Set<Order> orders = new HashSet<>();

	@Override
	public String toString() {
		return "User [id=" + id + "]";
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof User other))
			return false;
		return id != null && id.equals(other.id);
	}

}