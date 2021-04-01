package team_15.team_15.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import team_15.team_15.model.Product;
import team_15.team_15.service.ProductService;
import team_15.team_15.service.ServiceException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @GetMapping()
    public Iterable<Product> getAllProducts() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProduct(@PathVariable("id") long id) {
        return productService.findById(id);
    }

    @PostMapping()
    public String addProduct(@Valid @RequestBody Product product, @AuthenticationPrincipal Jwt accessToken) {
        try {
            String scope = accessToken.getClaims().get("scope").toString();
            boolean partnerRole = scope.contains("partner");

            if (partnerRole) {
                System.out.println("Contains sequence 'partner': " + accessToken.getClaims().get("scope").toString());
                System.out.println("Contains sequence 'partner': " + accessToken.getClaims().get("scope").toString().contains("partner"));
                
                productService.add(product);
                
                return "Product added";
            } else {
                return "Not Authorized to add product";
            }
        }
        catch (ServiceException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name", exc);
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") long id, @AuthenticationPrincipal Jwt accessToken) {
        String scope = accessToken.getClaims().get("scope").toString();
        boolean partnerRole = scope.contains("partner");

        if (partnerRole) {
            System.out.println("Contains sequence 'partner': " + accessToken.getClaims().get("scope").toString());
            System.out.println("Contains sequence 'partner': " + accessToken.getClaims().get("scope").toString().contains("partner"));
            
            productService.deleteById(id);
            
            return "Product deleted";
        } else {
            return "Not Authorized to delete product";
        }
    }

    @PutMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") long id, @Valid @RequestBody Product product, @AuthenticationPrincipal Jwt accessToken) {
        try {
            String scope = accessToken.getClaims().get("scope").toString();
            boolean partnerRole = scope.contains("partner");

            if (partnerRole) {
                System.out.println("Contains sequence 'partner': " + accessToken.getClaims().get("scope").toString());
                System.out.println("Contains sequence 'partner': " + accessToken.getClaims().get("scope").toString().contains("partner"));
                
                productService.deleteById(id);
                productService.add(product);
                
                return "Product updated";
            } else {
                return "Not Authorized to update product";
            }
        }
        catch (ServiceException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name", exc);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, ResponseStatusException.class})
    public Map<String, String> handleValidationExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        if (ex instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException)ex).getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
        }
        else {
            errors.put(((ResponseStatusException)ex).getReason(), ex.getCause().getMessage());
        }
        return errors;
    }
}
