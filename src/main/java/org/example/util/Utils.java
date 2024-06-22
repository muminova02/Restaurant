package org.example.util;

import java.util.Map;

public interface Utils {



    String SHOW_MENU = "🍴SHow menu";
    String MY_ORDERS = "📩My Orders";
    String SAVAT = "🛒SAVAT";
    String ALOQA = "☎Aloqa";
    String XABAR_YUBORISH = "✍Xabar yuborish";
    String SOZLAMALAR = "⚙ Sozlamalar";
    String BIZ_HAQIMIZDA = "📑Biz haqimizda";
    String[][] mainMenuUser = {
            {SHOW_MENU},
            {MY_ORDERS},
            {SAVAT,ALOQA},
            {XABAR_YUBORISH,SOZLAMALAR},
            {BIZ_HAQIMIZDA}
    };

    String TILNI_OZGARTRISH="🌏Tilni o'zgartrish";
    String MALUMOTLARNI_TOZALASH="🗑Ma'lumotlari tozalash";
    String ORQAGA ="orqaga";

String [][] sozlamalarMenu={
        {TILNI_OZGARTRISH},
        {MALUMOTLARNI_TOZALASH},
        {ORQAGA}
};


}
