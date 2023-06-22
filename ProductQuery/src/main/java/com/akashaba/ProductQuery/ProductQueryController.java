package com.akashaba.ProductQuery;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductQueryController {

    private ProductRepository productRepository;
    private StockRepository stockRepository;

    public ProductQueryController(ProductRepository productRepository, StockRepository stockRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
    }
    @KafkaListener(topics = "products", groupId = "products_group")
    public void processProductEvent(String event){
        System.out.println("Getting event"+event);
        ProductEvent productEvent =null;
        try {
            productEvent = new ObjectMapper().readValue(event, ProductEvent.class);
            System.out.println(productEvent);
            switch (productEvent.getType()){
                case "ProductCreated":
                    this.productRepository.save(productEvent.getProduct());
                    break;
                case "UpdateProduct" :
                    this.productRepository.save(productEvent.getProduct());
                    break;
                case "DeletedProduct" :
                    this.productRepository.delete(productEvent.getProduct());
                default:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @KafkaListener(topics = "stock", groupId = "stock_group")
    public void processStockEvent(String event){
        System.out.println("Getting event"+event);
        StockEvent stockEvent =null;
        try {
            stockEvent = new ObjectMapper().readValue(event, StockEvent.class);
            System.out.println(stockEvent);
            switch (stockEvent.getType()){
                case "StockCreated":
                    this.stockRepository.save(stockEvent.getStock());
                    break;
                case "UpdateStock" :
                    this.stockRepository.save(stockEvent.getStock());
                    break;
                case "DeletedStock" :
                    this.stockRepository.delete(stockEvent.getStock());
                default:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @GetMapping
    public List<Product> getProducts(){
        List<Product> products = productRepository.findAll();
        List<Stock> stocks = stockRepository.findAll();

        Map<Integer, Integer> stockQuantities = stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNo, Stock::getQuantityInSock));

        products.forEach(product -> {
            int productNumbers = product.getProductNo();
            int quantity = stockQuantities.getOrDefault(productNumbers,0);
            product.setNumberInStock(quantity);
        });
        return products;
    }
}
