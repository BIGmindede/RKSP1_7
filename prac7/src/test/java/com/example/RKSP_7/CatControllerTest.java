package com.example.RKSP_7;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class CatControllerTest {
    @Test
    public void testCreateCat() {
        Cat cat = new Cat();
        cat.setId(1L);
        cat.setName("Asteroid destroyer");
        CatRepository catRepository = Mockito.mock(CatRepository.class);
        when(catRepository.save(cat)).thenReturn(Mono.just(cat));
        CatController catController = new CatController(catRepository);
        Mono<Cat> response = catController.createCat(cat);
        assertEquals(cat, response.block());
    }
    @Test
    public void testGetAllCats() {
        Cat cat1 = new Cat();
        cat1.setId(1L);
        cat1.setName("Asteroid destroyer");
        cat1.setAge(3);
        Cat cat2 = new Cat();
        cat2.setId(2L);
        cat2.setName("Batman`s granny");
        cat2.setAge(4);
        CatRepository catRepository = Mockito.mock(CatRepository.class);
        when(catRepository.findAll()).thenReturn(Flux.just(cat1, cat2));
        CatController catController = new CatController(catRepository);
        Flux<Cat> response = catController.getAllCats(null);
        assertEquals(2, response.collectList().block().size());
    }
    @Test
    public void testDeleteCat() {
        Cat cat = new Cat();
        cat.setId(1L);
        cat.setName("Asteroid destroyer");
        CatRepository catRepository = Mockito.mock(CatRepository.class);
        when(catRepository.findById(1L)).thenReturn(Mono.just(cat));
        when(catRepository.delete(cat)).thenReturn(Mono.empty());
        CatController catController = new CatController(catRepository);
        ResponseEntity<Void> response = catController.deleteCat(1L).block();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}