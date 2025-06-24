package com.desouza.dscommerce.dto.order;

import com.desouza.dscommerce.entities.User;

public class OrderClientDTO {

    private Long id;
    private String name;

    public OrderClientDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public OrderClientDTO(User enttiy) {
        id = enttiy.getId();
        name = enttiy.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
