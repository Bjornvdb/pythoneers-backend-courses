package team_15.team_15.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team_15.team_15.db.ProductRepository;
import team_15.team_15.model.Product;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Iterable<Product> getAll() {
        return productRepository.findAll();
    }

    public void add(Product product) throws ServiceException {
        if (productRepository.findByName(product.getTitle()) != null) throw new ServiceException("Product already in database");
        productRepository.save(product);
    }

    public void delete(Product product) {
        productRepository.delete(product);
    }

    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    public void update(Product product) throws ServiceException {
        if (product == null) throw new ServiceException("Invalid product id");

        Product productByName = productRepository.findByName(product.getTitle());
        if (productByName != null && productByName.getId() != product.getId()) throw new ServiceException("Product already in database");

        productRepository.save(product);
    }

    public void deleteById(long id) {
        productRepository.deleteById(id);
    }
}
