package alliness.apartmentparser;

import alliness.apartmentparser.bot.TelgeramBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Main {

    public static void main(String[] args){
        Config.get();
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(TelgeramBot.getInstance());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        try {
            new ApartmentParser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
