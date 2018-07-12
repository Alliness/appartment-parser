package alliness.apartmentparser.bot;

import alliness.apartmentparser.Config;
import alliness.apartmentparser.dto.Offer;
import alliness.core.helpers.FReader;
import alliness.core.helpers.FWriter;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;

public class OlxBot extends TelegramLongPollingBot {

    private static final Logger log = Logger.getLogger(OlxBot.class);
    private static       OlxBot instance;

    public static OlxBot getInstance() {
        if (instance == null) {
            instance = new OlxBot();
        }
        return instance;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("has updates");
        addChatId(String.valueOf(update.getMessage().getChatId()));

        switch (update.getMessage().getText()) {
            case "/stop":
                System.exit(0);
                break;
            case "/info":
//                BotMessageHandler.sendInfo(update.getMessage().getChatId(), this);
                break;
            case "/help":
                BotMessageHandler.sendHelp(update.getMessage().getChatId(), this);
                break;
            case "/subscribe": //add chatId to subscribers
                break;
            case "/pull": //execute queries
                break;
            default:
        }

        System.out.println(update.getMessage().getText());

    }

    public void sendMessage(Offer offer) {
        for (Object o : getChats()) {
            String chatId = String.valueOf(o);

            log.info(String.format("Send message %s to chat %s", offer.getOfferId(), chatId));

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setParseMode("HTML");
            message.setText(offer.getParsedMessage());

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public JSONArray getChats() {
        File f = new File("data/recipients.json");
        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FWriter.writeToFile(new JSONArray().toString(), f, false);
        }
        return FReader.readJSONArray("data/recipients.json");
    }

    private void addChatId(String chatId) {

        File f = new File("data/recipients.json");
        if (!f.exists()) {
            try {
                f.createNewFile();
                FWriter.writeToFile(new JSONArray().toString(), f, false);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        JSONArray list = FReader.readJSONArray(f);

        if (list.length() == 0 || !list.toList().contains(chatId)) {
            list.put(String.valueOf(chatId));
        }

        FWriter.writeToFile(list.toString(), f, false);
    }

    @Override
    public String getBotUsername() {
        return "olxParserBot";
    }

    @Override
    public String getBotToken() {
        return Config.get().getBots().getTelegram();
    }

}
