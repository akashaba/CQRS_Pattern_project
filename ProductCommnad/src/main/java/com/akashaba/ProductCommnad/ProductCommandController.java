package com.akashaba.ProductCommnad;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductCommandController {

    private ProductRepository productRepository;
    private KafkaTemplate<String, ProductEvent> kafkaTemplate;

    public ProductCommandController(ProductRepository productRepository, KafkaTemplate kafkaTemplate) {
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product){
        Product product1 = productRepository.save(product);
        ProductEvent productEvent = new ProductEvent("ProductCreated", product1);
        kafkaTemplate.send("products", productEvent);
        return new ResponseEntity<>(product1, HttpStatus.OK);
    }

    @PutMapping("/{productNo}")
    public ResponseEntity<?> updateProduct(@PathVariable("productNo") int productNo, @RequestBody Product product){
        Product product1 = productRepository.findById(productNo).orElse(null);
        if (product1 != null){
            product1=productRepository.save(product);
            ProductEvent productEvent = new ProductEvent("UpdateProduct", product1);
            kafkaTemplate.send("products", productEvent);
            return new ResponseEntity<>(product1, HttpStatus.OK);
        }else {
            return null;
        }
    }

    @DeleteMapping("/{productNo}")
    public void deleteProduct(@PathVariable("productNo") int productNo){
        Product product = productRepository.findById(productNo).orElse(null);
        if (product!=null){
            productRepository.delete(product);
            ProductEvent productEvent = new ProductEvent("DeletedProduct", product);
            kafkaTemplate.send("products", productEvent);
        }
    }
}
