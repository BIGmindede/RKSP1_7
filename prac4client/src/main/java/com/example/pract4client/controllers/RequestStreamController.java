package com.example.pract4client.controllers;

import com.example.pract4client.models.CatModel;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Flow;

@RestController
@RequestMapping("/api/cats")
public class RequestStreamController {
    private final RSocketRequester rSocketRequester;
    @Autowired
    public RequestStreamController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }
    @GetMapping
    public Publisher<CatModel> getCats() {
        return rSocketRequester
            .route("getCats")
            .data(new CatModel())
            .retrieveFlux(CatModel.class);
    }
}

