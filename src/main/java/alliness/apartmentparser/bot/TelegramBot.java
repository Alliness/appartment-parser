package alliness.apartmentparser.bot;

import alliness.apartmentparser.ApartmentParser;
import alliness.apartmentparser.Config;
import alliness.apartmentparser.DataReader;
import alliness.apartmentparser.dto.Offer;
import alliness.apartmentparser.interfaces.DistributorInterface;
import alliness.core.helpers.FReader;
import alliness.core.helpers.FWriter;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class TelegramBot extends TelegramLongPollingBot {

    private static final Logger      log = Logger.getLogger(TelegramBot.class);
    private static       TelegramBot instance;

    public static TelegramBot getInstance() {
        if (instance == null) {
            instance = new TelegramBot();
        }
        return instance;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            switch (update.getMessage().getText()) {
                case "/stop":
                    System.exit(0);
                    break;
                case "/info":
                    BotMessageHandler.sendInfo(update.getMessage().getChatId(), this);
                    break;
                case "/help":
                    BotMessageHandler.sendHelp(update.getMessage().getChatId(), this);
                    break;
                case "/subscribe":
                    addChatId(String.valueOf(update.getMessage().getChatId()));
                    BotMessageHandler.sendSubscribeNotice(update.getMessage().getChatId(), this);
                    break;
                case "/unsubscribe":
                    removeChatId(String.valueOf(update.getMessage().getChatId()));
                    BotMessageHandler.sendUnSubscribeNotice(update.getMessage().getChatId(), this);
                    break;
                case "/pull":
                    try {
                        ApartmentParser.getInstance().createExecutorProcesses();
                        for (DistributorInterface distributorInterface : ApartmentParser.getInstance().getDistributors()) {
                            BotMessageHandler.sendDistributorsInfo(distributorInterface, update.getMessage().getChatId(), this);
                        }
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        log.info(String.format("[Message][%s] %s", update.getMessage().getChatId(), update.getMessage().getText()));

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
        return FReader.readJSONArray(DataReader.getInstance().getRecipientsFile());
    }

    private void removeChatId(String chatId) {
        File      file = DataReader.getInstance().getRecipientsFile();
        JSONArray list = FReader.readJSONArray(file);

        for (int i = 0; i < list.length(); i++) {
            if(list.getString(i).equals(chatId)){
                list.remove(i);
            }
        }
        FWriter.writeToFile(list.toString(), file, false);

    }

    private void addChatId(String chatId) {

        File      file = DataReader.getInstance().getRecipientsFile();
        JSONArray list = FReader.readJSONArray(file);

        if (list.length() == 0 || !list.toList().contains(chatId)) {
            list.put(String.valueOf(chatId));
            log.info(String.format("chatId %s is added to subscribers", chatId));
        }

        FWriter.writeToFile(list.toString(), file, false);
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
