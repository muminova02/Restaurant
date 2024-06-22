package org.example.db;

import lombok.*;
import org.example.entity.Buyurtma;
import org.example.entity.Meal;
import org.example.entity.MenuType;
import org.example.entity.User;
import org.example.enums.AdminState;
import org.example.enums.UserState;

import java.util.*;

@Data
public class Db {
    private final HashMap<MenuType, ArrayList<Meal>> allMeals = new HashMap<>();

    private final HashMap<Long,User> users = new HashMap<>();

    private final HashMap<Long,ArrayList<Buyurtma>> mySavat = new HashMap<>();
    private final Set<MenuType> menuTypeSet=new HashSet<>();

    private final HashMap<Long,ArrayList<Buyurtma>> buyurtma = new HashMap<>();



//    public void addUser(User main) {
//        users.add(main);
//    }

//    public Optional<User> getUserById(String chatId) {
//        for (User user : users) {
//            if(user.getChatId().equals(Long.valueOf(chatId))){
//                return Optional.of(user);
//            }
//        }
//        return Optional.empty();
//    }

    private static Db db;

    public static Db getInstance() {
        if (db == null) {
            db=new Db();
//            User user = new User(1121l,"Muqad","+889984");
//            db.users.add(user)
            db.getAllMeals().put(new MenuType("Ichimlik","photo"),new ArrayList<>());
            db.getAllMeals().put(new MenuType("Salatlar","photo"),new ArrayList<>());
            db.getAllMeals().put(new MenuType("Ovqatlar","photo"),new ArrayList<>());
            db.getAllMeals().put(new MenuType("Muzqaymoqlar","photo"),new ArrayList<>());
            db.getAllMeals().put(new MenuType("Burgers","photo"),new ArrayList<>());
            db.getAllMeals().forEach((menuType, meals) -> {
                if (menuType.getTitle().equals("Ichimlik")){
                    meals.add(new Meal("Kola","PhotoMeal","This is description",123d));
                    meals.add(new Meal("Pepsi","PhotoMeal","This is description",123d));
                    meals.add(new Meal("dinay","PhotoMeal","This is description",123d));
                    meals.add(new Meal("asdf","PhotoMeal","This is description",123d));
                    meals.add(new Meal("asdfdd","PhotoMeal","This is description",123d));
                }
            });
            User user = new User(6436944940L,"Muqaddas","+99909992999", AdminState.DEFAULT);
            db.getUsers().put(6436944940L,user);

        }
        return db;
    }
    public void createMenu(MenuType menu){
        menuTypeSet.add(menu);
    }


}
