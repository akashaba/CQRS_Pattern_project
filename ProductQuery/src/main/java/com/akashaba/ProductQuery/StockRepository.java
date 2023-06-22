package com.akashaba.ProductQuery;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockRepository extends MongoRepository<Stock, Integer> {
}
