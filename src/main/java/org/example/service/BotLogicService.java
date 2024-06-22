package org.example.service;

import org.example.db.Db;
import org.example.entity.Buyurtma;
import org.example.entity.Meal;
import org.example.entity.MenuType;
import org.example.entity.User;
import org.example.enums.AdminState;
import org.example.enums.BuyurtmaState;
import org.example.enums.UserState;
import org.example.payload.InlineString;
import org.example.util.Utils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BotLogicService {
    private final UserService userServise = UserService.getInstance();
    public final Buyurtma buyurtma = new Buyurtma();
    public final MenuType menuType=new MenuType();
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
                }else if (chatId == 6436944940L ){
                    userServise.updateState(chatId, AdminState.ADMIN_START);
//                    userStateHandler(update);
                    adminStateHandler(chatId,update);
                }
            }
            case Utils.SHOW_MENU-> {
                sendMessage.setText("choose menu type:");
                sendMessage.setReplyMarkup(replyService.keyboardMaker(db.getAllMeals().keySet()));
                botService.executeMessages(sendMessage);
                userServise.updateState(chatId,UserState.CHOOSE_MENU);
            }
            case Utils.MY_ORDERS -> {

            }
            case Utils.SAVAT -> {
                for (Buyurtma buyurtma1 : db.getMySavat().get(chatId)) {
                    sendMessage.setText(buyurtma1.toString());
                    sendMessage.setReplyMarkup(CommandHandler.buyurtmaInline(buyurtma1));
                    botService.executeMessages(sendMessage);
                    userServise.updateState(chatId,UserState.SEARCH_SAVAT);
                }
            }
            case Utils.ALOQA -> {

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
            case Utils.CREATE_MENU -> {
                sendMessageToAdmin.setText("Menuga mahsulot qo'shishni xoxlaysizmi");
                sendMessageToAdmin.setReplyMarkup(CommandHandler.adminInline());
                botService.executeMessages(sendMessageToAdmin);
               // userServise.updateState(chatId,AdminState.SEND_PRODUCT_PHOTO);
            }
            default -> {
                userStateHandler(update);
            }
        }
    }

    private void adminStateHandler(Long chatId, Update update) {
        String text = update.getMessage().getText();
        sendMessageToAdmin.setChatId(chatId);
        AdminState adminState=userServise.getAdminState(chatId);
        switch (adminState){
            case ADMIN_START -> {
                sendMessageToAdmin.setText("Siz admin qilib tayinlandingiz ");
                sendMessageToAdmin.setReplyMarkup(replyService.keyboardMaker(Utils.mainMenuAdmin));
                userServise.updateState(chatId,AdminState.ADMIN_MENU);
                botService.executeMessages(sendMessageToAdmin);
            }
            case SEND_PRODUCT_PHOTO -> {
                menuType.setPhoto(text);
                sendMessageToAdmin.setText("Product nomini kiriting");
                botService.executeMessages(sendMessageToAdmin);
                userServise.updateState(chatId,AdminState.SEND_PRODUCT_NAME);
            }
            case SEND_PRODUCT_NAME -> {
                menuType.setTitle(text);
                sendMessageToAdmin.setText("Menuga mahsulot qo'shishni xoxlaysizmi");
                sendMessageToAdmin.setReplyMarkup(CommandHandler.adminInline());
                botService.executeMessages(sendMessageToAdmin);
            }

        }
    }

    public void userStateHandler(Update update){
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        System.out.println(chatId);
//        User currentUser1 = db.getUsers().get(chatId);
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
