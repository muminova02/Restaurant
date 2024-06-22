package org.example.service;

import org.example.payload.InlineString;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.ArrayList;
import java.util.List;

public class InlineMarkupService {
    public  InlineKeyboardMarkup inlineMarkup(InlineString[][] buttons) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(markup);
        for (InlineString[] button : buttons) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (InlineString s : button) {
                InlineKeyboardButton inlineButton = new InlineKeyboardButton();
                inlineButton.setText(s.getMessage());
                inlineButton.setCallbackData(s.getInlineData().toString());
                row.add(inlineButton);
            }
            markup.add(row);
        }
        return inlineKeyboardMarkup;
    }
}
