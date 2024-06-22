package org.example.service;

import org.example.entity.Buyurtma;
import org.example.enums.BuyurtmaState;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class CommandHandler {

    public static InlineKeyboardMarkup productMurkup(int quantity, long prductId){
        InlineKeyboardMarkup build = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder().text("-").callbackData("-;" + quantity +";"+ prductId).build()
                        , InlineKeyboardButton.builder().text(String.valueOf(quantity)).callbackData("num").build()
                        , InlineKeyboardButton.builder().text("+").callbackData("+;" + quantity +";"+ prductId).build()))
                .keyboardRow(List.of(InlineKeyboardButton.builder().text("Add to basket ✅").callbackData("basket;" +prductId+";"+quantity).build()))
                .build();
        return build;
    }

    public static InlineKeyboardMarkup buyurtmaInline(Buyurtma buyurtma){
        InlineKeyboardMarkup build = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("      -      ")
                                .callbackData(buyurtma.getProductId()).build())).build();
        return build;
    }
    public static InlineKeyboardMarkup orderInline(String s){
        InlineKeyboardMarkup build = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text(s)
                                .callbackData(s).build())).build();
        return build;
    }
}
