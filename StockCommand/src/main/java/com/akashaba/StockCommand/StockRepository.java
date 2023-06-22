package com.akashaba.StockCommand;

import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockRepository extends MongoRepository<Stock, Integer> {
}
