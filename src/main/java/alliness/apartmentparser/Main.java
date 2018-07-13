package alliness.apartmentparser;

import alliness.apartmentparser.bot.TelegramBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Main {

    public static void main(String[] args) {
        Config.get();
        DataReader.getInstance();
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(TelegramBot.getInstance());
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
