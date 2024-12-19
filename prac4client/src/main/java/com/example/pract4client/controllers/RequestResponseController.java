package com.example.pract4client.controllers;

import com.example.pract4client.models.CatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/cats")
public class RequestResponseController {
    private final RSocketRequester rSocketRequester;
    @Autowired
    public RequestResponseController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }
    @GetMapping("/{id}")
    public Mono<CatModel> getCat(@PathVariable Long id) {
        return rSocketRequester
                .route("getCat")
                .data(id)
                .retrieveMono(CatModel.class);
    }
    @PostMapping
    public Mono<CatModel> addCat(@RequestBody CatModel cat) {
        return rSocketRequester
                .route("addCat")
                .data(cat)
                .retrieveMono(CatModel.class);
    }
}
