package com.jai.mystarter.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private Logger logger = LoggerFactory.getLogger(TestController.class);
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<?> test(){
        logger.info("Simple API Call");
        return new ResponseEntity<String>("Test API", HttpStatus.OK);
    }
}
