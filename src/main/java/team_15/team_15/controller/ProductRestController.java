package team_15.team_15.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import team_15.team_15.model.Product;
import team_15.team_15.service.ProductService;
import team_15.team_15.service.ServiceException;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public Iterable<Product> getAll() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProduct(@PathVariable("id") long id) {
        return productService.findById(id);
    }

    @PostMapping("/")
    public Iterable<Product> addBus(@Valid @RequestBody Product product) {
        try {
            productService.add(product);
        }
        catch (ServiceException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name", exc);
        }
        return productService.getAll();
    }
}
