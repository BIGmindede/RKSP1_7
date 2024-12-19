package com.example.pract4client.controllers;

import com.example.pract4client.DTOs.CatListWrapper;
import com.example.pract4client.models.CatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/cats")
public class ChannelController {
    private final RSocketRequester rSocketRequester;
    @Autowired
    public ChannelController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }
    @PostMapping("/exp")
    public Flux<CatModel> addCatsMultiple(@RequestBody CatListWrapper catListWrapper){
        List<CatModel> catList = catListWrapper.getCats();
        Flux<CatModel> cats = Flux.fromIterable(catList);
        return rSocketRequester
                .route("catChannel")
                .data(cats)
                .retrieveFlux(CatModel.class);
    }
}
