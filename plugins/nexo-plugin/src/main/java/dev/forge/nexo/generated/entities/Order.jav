package dev.forge.nexo.generated.entities;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import dev.forge.nexo.generated.entities.User;

@Table(name = "orders")
@Entity(name = "Order")
@Getter
@Setter
public class Order {

	@Id
	private Long id;

	private String orderNumber;

	private BigDecimal totalAmount;

	private String status;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	User user;

	@Override
	public String toString() {
		return "Order [id=" + id + "]";
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Order other))
			return false;
		return id != null && id.equals(other.id);
	}

}