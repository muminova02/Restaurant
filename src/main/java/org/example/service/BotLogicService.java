package org.example.service;

import org.example.db.Db;
import org.example.entity.Buyurtma;
import org.example.entity.Meal;
import org.example.entity.User;
import org.example.enums.BuyurtmaState;
import org.example.enums.UserState;
import org.example.payload.InlineString;
import org.example.util.Utils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

public class BotLogicService {
    private final UserService userServise = UserService.getInstance();
    public final Buyurtma buyurtma = new Buyurtma();
    private SendMessage sendMessage = new SendMessage();
    private SendMessage sendMessageToAdmin = new SendMessage();
    private Db db = Db.getInstance();
    private BotService botService = BotService.getInstance();
    private final User currentUser =new User();
    private final ReplyMarkupService replyService = new ReplyMarkupService();
    private final InlineMarkupService inlineService = new InlineMarkupService();

    public void messageHandler(Update update){
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        System.out.println(chatId);
        User currentUser1;
        sendMessage.setReplyMarkup(null);
        sendMessage.setChatId(chatId);
        switch (text){
            case "/start" -> {
                if (!db.getUsers().containsKey(chatId)) {
                    currentUser.setChatId(chatId);
                    currentUser.setState(UserState.START);
                    db.getUsers().put(chatId,currentUser);
                    userStateHandler(update);
//                   userServise.updateState(chatId,UserState.START);
                }
            }
            case Utils.SHOW_MENU-> {
                sendMessage.setText("choose menu type:");
                sendMessage.setReplyMarkup(replyService.keyboardMaker(db.getAllMeals().keySet()));
                botService.executeMessages(sendMessage);
                userServise.updateState(chatId,UserState.CHOOSE_MENU);
            }
            case Utils.MY_ORDERS -> {
                ArrayList<Buyurtma> buyurtmas = db.getBuyurtma().get(chatId);
                if (buyurtmas.isEmpty()) {
                    sendMessage.setText("Buyurtma Mavjud Emas");
                    sendMessage.setReplyMarkup(replyService.keyboardMaker(Utils.order_menu));
                    botService.executeMessages(sendMessage);
                    return;
                }

                StringBuilder ordersOneUser = new StringBuilder();
                User user = db.getUsers().get(chatId);
                ordersOneUser.append(user.getName()).append("\n");
                ordersOneUser.append(user.getPhoneNumber()).append("\n");
                int i=1;
                for (Buyurtma buyurtma : buyurtmas) {
                    ordersOneUser.append(i++).append("-> ");
                    ordersOneUser.append(buyurtma.getMenuType()).append("\n      ");
                    ordersOneUser.append(buyurtma.getMealName()).append("\n      ");
                    ordersOneUser.append(buyurtma.getCount()).append("\n      ");
                    ordersOneUser.append(buyurtma.getPrice()).append("\n      ");
                }
                sendMessage.setText(ordersOneUser.toString());
                sendMessage.setReplyMarkup(CommandHandler.orderInline("Kutilmoqda"));
                SendMessage sendMessage1 = new SendMessage();
                sendMessage1.setChatId(chatId);
                sendMessage1.setText("");
                sendMessage1.setReplyMarkup(replyService.keyboardMaker(Utils.order_menu));
                botService.executeMessages(sendMessage);
                botService.executeMessages(sendMessage1);
                userServise.updateState(chatId,UserState.MAIN_MENU);
            }
            case Utils.SAVAT -> {
                for (Buyurtma buyurtma1 : db.getMySavat().get(chatId)) {
                    sendMessage.setText(buyurtma1.toString());
                    sendMessage.setReplyMarkup(CommandHandler.buyurtmaInline(buyurtma1));
                    botService.executeMessages(sendMessage);
                }
                SendMessage sendMessage1 = new SendMessage();
                sendMessage1.setChatId(chatId);
                sendMessage1.setText("Buyurtmani bekor qilish uchun - tugmasini bosing, \n buyurtma berish uchun buyurtma berishni bosing\nbuyurtmalariningiz tasdiqlash uchun yubpriladi");
                sendMessage1.setReplyMarkup(replyService.keyboardMaker(Utils.savatMenu));
                botService.executeMessages(sendMessage1);
                userServise.updateState(chatId,UserState.SEARCH_SAVAT);
            }
            case Utils.BUYURTMA ->{
                ArrayList<Buyurtma> userSavatToBuyurtma = db.getMySavat().get(chatId);
                userServise.addOrder(chatId,userSavatToBuyurtma);
                db.getMySavat().get(chatId).clear();
                sendMessage.setText("Buyurtma jo'natildi");
                botService.executeMessages(sendMessage);
                userServise.updateState(chatId,UserState.MAIN_MENU);



            }

            case Utils.ALOQA -> {
                sendMessage.setText("Agar biror muammo tug'ilgan bo'lsa +998901234567 yoki" +
                        " +998912345678 shu raqamlarga murojat qilishingiz mumkin");
            }
            case Utils.XABAR_YUBORISH -> {

            }
            case Utils.SOZLAMALAR -> {

            }
            case Utils.BIZ_HAQIMIZDA -> {

            }
            case "orqaga"->{
                sendMessage.setText("main menu");
                sendMessage.setReplyMarkup(replyService.keyboardMaker(Utils.mainMenuUser));
                botService.executeMessages(sendMessage);
                userServise.updateState(chatId,UserState.MAIN_MENU);
            }
            default -> {
                userStateHandler(update);
            }
        }
    }

    public void userStateHandler(Update update){
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        System.out.println(chatId);
        User currentUser1 = db.getUsers().get(chatId);
        sendMessage.setReplyMarkup(null);
        sendMessage.setChatId(chatId);
        UserState state = userServise.getUserState(chatId);

        switch (state){
            case START -> {
                sendMessage.setText("Iltimos ismingizni kiriting.");
                userServise.updateState(chatId,UserState.FIRST_NAME);
                botService.executeMessages(sendMessage);
            }
            case FIRST_NAME -> {
                User user = db.getUsers().get(chatId);
                user.setName(text);
                userServise.updateState(chatId,UserState.PHONE_NUMBER);
                sendMessage.setText("Telfon Raqamingizni Kiriting.");
                botService.executeMessages(sendMessage);
            }
            case PHONE_NUMBER -> {
                User user = db.getUsers().get(chatId);
                user.setPhoneNumber(text);
                userServise.updateState(chatId,UserState.MAIN_MENU);

                db.getUsers().forEach((aLong, user1) ->
                        System.out.println(user1));
                sendMessage.setText("Buyurtma tanlashingiz mumkin 😊");
                sendMessage.setReplyMarkup(replyService.keyboardMaker(Utils.mainMenuUser));
                botService.executeMessages(sendMessage);
            }
            case CHOOSE_MENU -> {
                db.getAllMeals().forEach((menuType, meals) -> {
                    if (menuType.getTitle().equals(text)){
                        buyurtma.setMenuType(text);
                        buyurtma.setState(BuyurtmaState.CHALA);
                        sendMessage.setText("choose Meal : ");
                        sendMessage.setReplyMarkup(replyService.keyboardMaker(meals));
                        botService.executeMessages(sendMessage);
                        userServise.updateState(chatId,UserState.CHOOSE_MEAL);
                    }
                });
            }
            case CHOOSE_MEAL -> {
                db.getAllMeals().forEach((menuType, meals) -> {
                    if(buyurtma.getMenuType().equals(menuType.getTitle())){
                        for (Meal meal : meals) {
                            if (meal.getTitle().equals(text)){
                                buyurtma.setMealName(text);
                                User user = db.getUsers().get(chatId);
                                buyurtma.setName(user.getName());
                                buyurtma.setPhone(user.getPhoneNumber());
                                buyurtma.setPrice(meal.getPrice());
                                sendMessage.setText("Photo\n"+"name:"+meal.getTitle()+"\nDescription: "+ meal.getDescription()
                                        +"\nPrice: " + meal.getPrice());
                                sendMessage.setReplyMarkup(CommandHandler.productMurkup(1,Long.parseLong(meal.getId())));
                                botService.executeMessages(sendMessage);
                                userServise.updateState(chatId,UserState.CHOOSE_COUNT);
                            }
                        }
                    }
                });

            }
            default -> {
                sendMessage.setText("Menuni tanlang");
                sendMessage.setReplyMarkup(replyService.keyboardMaker(Utils.mainMenuUser));
                botService.executeMessages(sendMessage);
            }
        }



    }

//    public void callbackHandler(Update update) {
//
//    }

    private static BotLogicService botLogicService;

    public static BotLogicService getInstance() {
        if (botLogicService == null) {
            botLogicService = new BotLogicService();
//            botLogicService.admins.put(6436944940l,"main");

        }
        return botLogicService;
    }
}
