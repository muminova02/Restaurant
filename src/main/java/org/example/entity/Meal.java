package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meal {
    private String id = String.valueOf(new Random().nextInt(1,9999));
    private String title;
    private String photo;
    private String Description;
    private Double price;

    public Meal(String title, String photoMeal, String thisIsDescription, double price) {
        this.id = String.valueOf(new Random().nextInt(1,9999));
        this.title = title;
        this.photo = photoMeal;
        this.Description = thisIsDescription;
        this.price = price;
    }
}
