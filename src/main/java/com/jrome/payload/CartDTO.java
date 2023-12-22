package com.jrome.payload;

public class CartDTO {

    int cart_id;


    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    @Override
    public String toString() {
        return "CartDTO{" +
                "cart_id=" + cart_id +
                '}';
    }


}
