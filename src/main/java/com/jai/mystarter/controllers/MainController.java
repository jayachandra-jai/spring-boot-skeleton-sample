package com.jai.mystarter.controllers;

import com.jai.mystarter.models.dto.utils.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class MainController {
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<GenericResponse> test(){
        log.info("Simple API Call");
        GenericResponse response=new GenericResponse();
        return new ResponseEntity<GenericResponse>(response, HttpStatus.OK);
    }

}
