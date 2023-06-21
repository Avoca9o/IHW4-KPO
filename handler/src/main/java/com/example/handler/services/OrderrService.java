package com.example.handler.services;

import com.example.handler.api.models.OrderDish;
import com.example.handler.api.models.Orderr;
import com.example.handler.data.DishRepository;
import com.example.handler.data.OrderDishRepository;
import com.example.handler.data.OrderrRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class OrderrService {
    private final OrderrRepository orderrRepository;
    private final DishRepository dishRepository;
    private final OrderDishRepository orderDishRepository;
    private final RestTemplateBuilder restTemplateBuilder;
    private final RestTemplate restTemplate;

    @Autowired
    public OrderrService(OrderrRepository orderrRepository, DishRepository dishRepository,
                         OrderDishRepository orderDishRepository, RestTemplateBuilder restTemplateBuilder) {
        this.orderrRepository = orderrRepository;
        this.dishRepository = dishRepository;
        this.orderDishRepository = orderDishRepository;
        this.restTemplateBuilder = restTemplateBuilder;
        restTemplate = restTemplateBuilder.build();
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @ToString
    public static class UserInfo {
        Long id;
        private String username;
        private String email;
        private int passwordHash;
        private String roles;
        private Timestamp createdAt;
        private Timestamp updatedAt;
    }

    public void createOrderr(String dishes, String specialRequests, String token) throws Exception {
//        String url = "https://localhost:8080/api/users/info/{token}";
//        ResponseEntity<UserInfo> response = restTemplate.getForEntity(url, UserInfo.class, token);
//        if (response.getStatusCode() != HttpStatus.OK) {
//            throw new Exception("something went wrong with authorizing");
//        }
//        UserInfo user = response.getBody();
        checkValidDishes(dishes);

        Orderr orderr = new Orderr(
//                user.getId(),
                1L,
                "enqueued",
                specialRequests,
                new Timestamp(new Date().getTime()),
                new Timestamp(new Date().getTime()),
                dishes
        );

        orderrRepository.save(orderr);

        if (orderr.getId() == 1) {
            new Thread(this::randomHandler).start();
        }

        for (var s : Arrays.stream(dishes.split(",")).toList()) {
            var l = Arrays.stream(s.split(":")).toList();
            Long dishId = Long.parseLong(l.get(0));
            Long quantity = Long.parseLong(l.get(1));
            orderDishRepository.save(new OrderDish(orderr.getId(), dishId, quantity));
        }
    }

    public Orderr getOrderInfo(Long idx) {
        if (idx < 0 || orderrRepository.findById(idx).isEmpty()) {
            throw new IllegalArgumentException("Invalid index");
        }
        return orderrRepository.findById(idx).get();
    }

    private void checkValidDishes(String dishes) {
        if (dishes.isEmpty()) {
            throw new IllegalArgumentException("Empty order");
        }
        List<String> list = Arrays.stream(dishes.split(",")).toList();
        for (var l : list) {
            List<String> orderDish = Arrays.stream(l.split(":")).toList();
            if (orderDish.size() != 2) {
                throw new IllegalArgumentException("Invalid parameters");
            }
            Long id;
            try {
                id = Long.parseLong(orderDish.get(1));
                Long.parseLong(orderDish.get(0));
            } catch (NumberFormatException n) {
                throw new IllegalArgumentException("Invalid parameters");
            }
            if (id < 0 || dishRepository.findById(id).isEmpty()) {
                throw new IllegalArgumentException("Unknown dish");
            }
            if (dishRepository.findById(id).get().getQuantity() < Long.parseLong(orderDish.get(0))) {
                throw new IllegalArgumentException("Not enough goods");
            }
        }
    }

    private void randomHandler() {
        while (true) {
            try {
                Thread.sleep(1000 * (new Random().nextInt() % 20 + 20));
            } catch (Exception e) {
                System.out.println("RandomHandler died\n");
            }
            var list = orderrRepository.findAll().stream().filter(o -> o.getStatus().equals("enqueued")).toList();
            if (list.isEmpty()) {
                continue;
            }
            var orderr = list.get(new Random().nextInt() % list.size());
            orderr.setStatus("done");
            orderrRepository.save(orderr);
        }
    }
}
