package com.controller;

import com.wordnik.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value="/api")
public class SwaggerController {
    /*
     *  http://localhost:8080/swagger/index.html
     */

    @ApiOperation(value="Get with id",notes="requires the id of transaction")
    @RequestMapping(value="/{txId}",method=RequestMethod.GET)
    public String getTransaction(@PathVariable String txId){
        return null;
    }
}
