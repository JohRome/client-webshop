package com.jrome.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private long id;
    private String name;
    private double price;
    private String description;

    @Override
    public String toString() {
        return  "\nProduct id: " + id +
                "\nProduct name: " + name +
                "\nProduct price: " + price +
                "\nProduct description: " + description;
    }
}
