package org.example.util;

public interface Utils {



    String SHOW_MENU = "SHow menu";
    String MY_ORDERS = "My Orders";
    String SAVAT = "SAVAT";
    String ALOQA = "Aloqa";
    String XABAR_YUBORISH = "Xabar yuborish";
    String SOZLAMALAR = "Sozlamalar";
    String BIZ_HAQIMIZDA = "Biz haqimizda";
    String[][] mainMenuUser = {
            {SHOW_MENU},
            {MY_ORDERS},
            {SAVAT,ALOQA},
            {XABAR_YUBORISH,SOZLAMALAR},
            {BIZ_HAQIMIZDA}
    };
    String BUYURTMA = "Buyurtma berish";
    String[][] savatMenu = {
            {BUYURTMA,"orqaga"},
    };
    String[][] order_menu = {
            {ALOQA,"orqaga"},
    };




}
