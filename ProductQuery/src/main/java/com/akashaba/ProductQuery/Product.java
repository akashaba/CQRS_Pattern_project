package com.akashaba.ProductQuery;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Product {

    @Id
    private int productNo;
    private String name;
    private String price;
    private int numberInStock;
}
