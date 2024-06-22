package org.example.service;

//import delivery.Maps;
//import delivery.Steps;
//import model_repo.BasketRepo;
import lombok.RequiredArgsConstructor;
import org.example.db.Db;
import org.example.entity.Buyurtma;
import org.example.entity.MenuType;
import org.example.enums.AdminState;
import org.example.enums.BuyurtmaState;
import org.example.enums.UserState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
public class CallbackQueryHandler {

   private static UserService userService = UserService.getInstance();
//  private static BotLogicService botLogicService = BotLogicService.getInstance();
    private static Db db = Db.getInstance();
    public static void handle(CallbackQuery callbackQueryHandler, TelegramLongPollingBot bot) throws TelegramApiException {
        Long chatId1 = callbackQueryHandler.getMessage().getChatId();
        if (callbackQueryHandler.getData().startsWith("+")) {
            incrementQuantity(callbackQueryHandler, bot);
        } else if (callbackQueryHandler.getData().startsWith("-")) {
            decrementQuantity(callbackQueryHandler, bot);
        }
        else if (callbackQueryHandler.getData().equals("ha")){
            bot.execute(new SendMessage(chatId1.toString(),"Mahsulot photosini kiriting"));
            UserService.updateState(chatId1, AdminState.SEND_PRODUCT_PHOTO);
          /*  MenuType menuType1=new MenuType();
            MenuType menuType=BotLogicService.getInstance().menuType;
            menuType1.setPhoto(menuType.getPhoto());
            menuType1.setTitle(menuType.getTitle());*/
        }else if (callbackQueryHandler.getData().startsWith("basket")) {

            String[] split = callbackQueryHandler.getData().split(";");
            Buyurtma buyurtma1 = BotLogicService.getInstance().buyurtma;
            Buyurtma buyurtma = new Buyurtma();
            buyurtma.setProductId(split[1]);
            buyurtma.setName(buyurtma1.getName());
            buyurtma.setPhone(buyurtma1.getPhone());
            buyurtma.setPrice(buyurtma1.getPrice());
            buyurtma.setMealName(buyurtma1.getMealName());
            buyurtma.setMenuType(buyurtma1.getMenuType());
            buyurtma.setCount(Integer.parseInt(split[2]));
            Long chatId = chatId1;
            buyurtma.setState(BuyurtmaState.SAVATDA);
            userService.addBuyurtma(chatId,buyurtma);
            buyurtma1.clear();
            bot.execute(new SendMessage(chatId.toString(), "Successfully added ✅"));
            userService.updateState(chatId, UserState.CHOOSE_MEAL);
//
//            TextHandler.menuButtons(callbackQueryHandler.getMessage(), bot);
//            Maps.USER_STEPS.put(callbackQueryHandler.getMessage().getChatId(), Steps.MENU);
        }
        if (userService.getState(callbackQueryHandler.getMessage().getChatId()).equals(UserState.SEARCH_SAVAT)) {
            HashMap<Long, ArrayList<Buyurtma>> mySavat = db.getMySavat();
            ArrayList<Buyurtma> buyurtmas = mySavat.get(chatId1);
            String data = callbackQueryHandler.getData();
            buyurtmas.removeIf(buyurtma -> buyurtma.getProductId().equals(data));

//        for (Buyurtma buyurtma1 : db.getMySavat().get(chatId1)) {
//            sendMessage.setText(buyurtma1.toString());
//            sendMessage.setReplyMarkup(CommandHandler.buyurtmaInline(buyurtma1));
//            botService.executeMessages(sendMessage);
//            userServise.updateState(chatId,UserState.SEARCH_SAVAT);
//        }

            InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                    .keyboardRow(List.of(
                            InlineKeyboardButton.builder()
                                    .text(" O'chirildi ")
                                    .callbackData("what").build())).build();
            EditMessageReplyMarkup build = EditMessageReplyMarkup.builder()
                    .replyMarkup(inlineKeyboardMarkup)
                    .chatId(callbackQueryHandler.getMessage().getChatId())
                    .messageId(callbackQueryHandler.getMessage().getMessageId())
                    .build();
            bot.execute(build);
            userService.updateState(chatId1, UserState.SEARCH_SAVAT);
        } else if (callbackQueryHandler.getData().equals("Kutilmoqda")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId1);
            sendMessage.setText("Yuborilgan Buyurtmangiz Kutilmoqda");
            bot.execute(sendMessage);
        }
    }

    private static void incrementQuantity(CallbackQuery callbackQuery, TelegramLongPollingBot bot) throws TelegramApiException {
        String[] split = callbackQuery.getData().split(";");
        InlineKeyboardMarkup inlineKeyboardMarkup = CommandHandler.productMurkup(Integer.parseInt(split[1]) + 1
                , Long.parseLong(split[2]));
        EditMessageReplyMarkup build = EditMessageReplyMarkup.builder()
                .replyMarkup(inlineKeyboardMarkup)
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .build();
        bot.execute(build);


    }

    private static void decrementQuantity(CallbackQuery callbackQuery, TelegramLongPollingBot bot) throws TelegramApiException {
        String[] split = callbackQuery.getData().split(";");
        if (!split[1].equals("1")) {
            InlineKeyboardMarkup inlineKeyboardMarkup = CommandHandler.productMurkup(Integer.parseInt(split[1]) - 1
                    , Long.parseLong(split[2]));

            EditMessageReplyMarkup build = EditMessageReplyMarkup.builder()
                    .replyMarkup(inlineKeyboardMarkup)
                    .chatId(callbackQuery.getMessage().getChatId())
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .build();
            bot.execute(build);


        }
    }

}
