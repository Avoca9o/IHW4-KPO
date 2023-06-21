package com.example.handler.services;

import com.example.handler.api.models.Dish;
import com.example.handler.data.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class DishService {
    private final DishRepository dishRepository;
    private final RestTemplateBuilder restTemplateBuilder;
    private final RestTemplate restTemplate;


    @Autowired
    public DishService(DishRepository dishRepository, RestTemplateBuilder restTemplateBuilder) {
        this.dishRepository = dishRepository;
        this.restTemplateBuilder = restTemplateBuilder;
        restTemplate = restTemplateBuilder.build();
    }

    public List<Dish> getMenu() {
        return dishRepository.findAll().stream().filter(d -> d.getQuantity() > 0).toList();
    }

    public Dish getDishInfo(String token, Long idx) throws Exception {
        checkIfAdmin(token);
        checkIndex(idx);
        return dishRepository.findById(idx).get();
    }

    public void createDish(String token, Dish d) throws Exception {
        checkIfAdmin(token);
        checkDish(d);
        dishRepository.save(d);
    }

    public void updateDish(String token, Long idx, Dish d) throws Exception {
        checkIfAdmin(token);
        checkDish(d);
        checkIndex(idx);
        Dish newDish = dishRepository.findById(idx).get();
        newDish.setDescription(d.getDescription());
        newDish.setName(d.getName());
        newDish.setPrice(d.getPrice());
        newDish.setQuantity(d.getQuantity());
        dishRepository.save(newDish);
    }

    public void deleteDish(String token, Long idx) throws Exception {
        checkIfAdmin(token);
        checkIndex(idx);
        dishRepository.delete(dishRepository.findById(idx).get());
    }

    private void checkDish(Dish d) {
        if (d.getName().isEmpty()) {
            throw new IllegalArgumentException("Name shouldn't be empty");
        }
        if (d.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description shouldn't be empty");
        }
        if (d.getPrice() <= 0) {
            throw new IllegalArgumentException("Price should be positive");
        }
        if (d.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity shouldn't be negative");
        }
    }

    private void checkIndex(Long idx) {
        if (idx < 0) {
            throw new IllegalArgumentException("index shouldn't be negative");
        }
        if (dishRepository.findById(idx).isEmpty()) {
            throw new IllegalArgumentException("no such dish");
        }
    }

    void checkIfAdmin(String token) throws Exception {
//        String url = "https://localhost:8080/api/users/info/{token}";
//        ResponseEntity<OrderrService.UserInfo> response = restTemplate.getForEntity(url, OrderrService.UserInfo.class, token);
//        if (response.getStatusCode() != HttpStatus.OK) {
//            throw new Exception("something went wrong with authorizing");
//        }
//        OrderrService.UserInfo user = response.getBody();
//        List<String> roles = Arrays.stream(user.getRoles().split(",")).toList();
//        if (!roles.contains("admin")) {
//            throw new IllegalAccessException();
//        }
    }
}
