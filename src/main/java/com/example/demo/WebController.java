package com.example.demo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {
    @Autowired
    private ProductRepository productRepository;
    
    @ModelAttribute("authentication")
    public Authentication authentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/cart")
    public String cart() {
        return "cart";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    }
    
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    @GetMapping("/products")
    public String showProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "products";
    }
    @GetMapping("/product/{id}")
    @ResponseBody
    public Product showProductDetails(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        return product;
    }
    @GetMapping("/navbar")
    public String navbar(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("authentication", authentication);
        return "navbar";
    }
    @PostMapping("/admin/products")
    public String createProduct(@RequestParam String name,
                                @RequestParam String description,
                                @RequestParam BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        productRepository.save(product);
        return "redirect:/admin";
    }
    @PostMapping("/admin/products/update")
    public String updateProduct(@RequestParam Long id,
                                @RequestParam String name,
                                @RequestParam String description,
                                @RequestParam BigDecimal price) {
        // Find the product in the database
        Product product = productRepository.findById(id).orElseThrow();

        // Update the product with the new data
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);

        // Save the updated product to the database
        productRepository.save(product);

        return "redirect:/admin/editproducts";
    }

    @PostMapping("/admin/products/delete")
    public String deleteProduct(@RequestBody Map<String, Long> body) {
        Long id = body.get("id");
        // Delete the product from the database
        productRepository.deleteById(id);

        return "redirect:/admin/editproducts";
    }

    @GetMapping("/admin/editproducts")
    public String showEditProducts(Model model, Authentication authentication) {
        System.out.println(authentication);
        // Query the database for the data for all products
        List<Product> products = productRepository.findAll();

        // Log the value of the products variable to the console for debugging
        System.out.println(products);

        // Add the data for all products to the model
        model.addAttribute("products", products);

        return "editProducts";
    }

}
