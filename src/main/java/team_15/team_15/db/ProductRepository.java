package team_15.team_15.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team_15.team_15.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);
}
