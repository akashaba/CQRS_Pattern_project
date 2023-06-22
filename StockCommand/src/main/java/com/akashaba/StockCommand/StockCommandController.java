package com.akashaba.StockCommand;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
public class StockCommandController {

    private StockRepository stockRepository;
    private KafkaTemplate<String, StockEvent> kafkaTemplate;

    public StockCommandController(StockRepository stockRepository, KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate =kafkaTemplate;
        this.stockRepository = stockRepository;
    }
    @PostMapping
    public ResponseEntity<?>createStock(@RequestBody Stock stock){
        Stock stock1 = stockRepository.save(stock);
        StockEvent stockEvent = new StockEvent("StockCreated", stock1);
        kafkaTemplate.send("stock", stockEvent);
        return new ResponseEntity<>(stock1, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStock(@PathVariable("id") int id, @RequestBody Stock stock){
        Stock stock1 = stockRepository.findById(id).orElse(null);
        if (stock1 != null){
            stock1=stockRepository.save(stock);
            StockEvent stockEvent = new StockEvent("UpdateStock", stock1);
            kafkaTemplate.send("stock", stockEvent);
            return new ResponseEntity<>(stock1, HttpStatus.OK);
        }else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteStock(@PathVariable("productNo") int id){
        Stock stock = stockRepository.findById(id).orElse(null);
        if (stock!=null){
            stockRepository.delete(stock);
            StockEvent stockEvent = new StockEvent("DeletedStock", stock);
            kafkaTemplate.send("stock", stockEvent);
        }
    }
}
