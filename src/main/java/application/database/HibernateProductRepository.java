package application.database;

import application.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HibernateProductRepository extends JpaRepository<Product, Long> {
}
