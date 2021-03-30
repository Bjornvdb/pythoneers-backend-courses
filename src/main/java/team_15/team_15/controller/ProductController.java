package team_15.team_15.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import team_15.team_15.model.Product;
import team_15.team_15.service.ProductService;
import team_15.team_15.service.ServiceException;

import javax.validation.Valid;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/productsOverview")
    public String overview(Model model) {
        model.addAttribute("products", productService.getAll());
        return "product/overview";
    }

    @GetMapping("/addProduct")
    public String add(Product product) {
        return "product/add";
    }

    @PostMapping("/addProduct")
    public String add(@Valid Product product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) return "product/add";

        try {
            productService.add(product);
        } catch (ServiceException e) {
            model.addAttribute("productAlreadyInDatabase", e.getMessage());
            return "product/add";
        }

        return "redirect:/productsOverview";
    }

    @GetMapping("/updateProduct/{id}")
    public String updateProduct(@PathVariable("id") long id, Model model) {
        try {
            Product product = productService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product id"));
            model.addAttribute("product", product);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        return "product/update";
    }

    @PostMapping("/updateProduct/{id}")
    public String updateProduct(@PathVariable("id") long id, @Valid Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            product.setId(id);
            return "product/update";
        }
        try {
            productService.update(product);
        } catch (ServiceException e) {
            model.addAttribute("productAlreadyInDatabase", e.getMessage());
            product.setId(id);
            return "product/update";
        }
        return "redirect:/productsOverview";
    }

    @GetMapping("/deleteProduct/{id}")
    public String showDeleteConfirmation(@PathVariable("id") long id, Model model) {
        try {
            Product product = productService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product id"));
            model.addAttribute("product", product);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        return "product/delete";
    }

    @PostMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable("id") long id, Model model) {
        try {
            Product product = productService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product id"));
            productService.delete(product);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        return "redirect:/productsOverview";
    }
}
