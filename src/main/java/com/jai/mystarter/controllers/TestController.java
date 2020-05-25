package com.jai.mystarter.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<?> test(){
        log.info("Simple API Call");
        return new ResponseEntity<String>("Test API", HttpStatus.OK);
    }
}
