package com.jrome.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseHistoryDTO {

    private Long id;

    private String history;

    private double price;

    @Override
    public String toString() {
        return "History ID: " + id + "\nHistory: " + history + "\nPrice: " + price;
    }
}
