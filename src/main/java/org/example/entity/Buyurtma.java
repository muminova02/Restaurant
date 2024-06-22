package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.BuyurtmaState;

import java.util.Random;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Buyurtma {
    private String productId;
    private String  name;
    private String phone;
    private String menuType;
    private String mealName;
    private int count;
    private double price;
    private BuyurtmaState state;


    public void clear(){
        count=0;
        price=0d;
        state=BuyurtmaState.CHALA;
    }
}
