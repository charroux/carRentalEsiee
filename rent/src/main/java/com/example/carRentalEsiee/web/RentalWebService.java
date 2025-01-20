package com.example.carRentalEsiee.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class RentalWebService {

    Logger logger = LoggerFactory.getLogger(RentalWebService.class);

    @GetMapping("/bonjour")
    public String sayHello(){
        return "Bonjour Madame !";
    }

    @PutMapping(value = "/cars/{plateNumber}") public void rent(
            @PathVariable("plateNumber") String plateNumber,
            @RequestParam(value="rent", required = true)boolean rent,
            @RequestBody Dates dates){

        logger.info("Plate number:" + plateNumber);
        logger.info("Rent: " + rent);
        logger.info("Dates: " + dates);
    }

}
