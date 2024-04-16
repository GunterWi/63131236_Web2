package com.nqt.tutorial.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nqt.tutorial.models.Product;
import com.nqt.tutorial.repositories.ProductRepository;



@Configuration
public class Database {
    private static final Logger Logger = LoggerFactory.getLogger(Database.class);
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository){
        return new CommandLineRunner(){
            @Override
            public void run(String... args) throws Exception{
                Product productA = new Product("Macbook", 2022, 1000.0, "");
                Product productB = new Product("Dell", 2022, 1000.0, "");
                Logger.info("Insert data: "+productRepository.save(productA));
                Logger.info("Insert data: "+productRepository.save(productB));
            }
        };
    }
}
