package com.wine.to.up.simple.parser.service;

import java.io.IOException;
import java.util.ArrayList;

import com.wine.to.up.simple.parser.service.SimpleParser.Parser;
import com.wine.to.up.simple.parser.service.SimpleParser.Wine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan("com.wine.to.up")
@EnableSwagger2
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
        try {
            Parser parser = new Parser();
            ArrayList<Wine> wines = parser.startParser();
            for (Wine w : wines)
                w.writeInfoToFile();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
