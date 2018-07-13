package alliness.apartmentparser.bot;

import alliness.apartmentparser.Config;
import alliness.apartmentparser.DataReader;
import alliness.apartmentparser.dto.AppConfig;
import alliness.apartmentparser.enums.DistrictsEnum;
import alliness.apartmentparser.interfaces.DistributorInterface;
import alliness.core.helpers.FReader;
import org.json.JSONArray;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.util.Map;

public class BotMessageHandler {


    public static void sendInfo(Long chatId, TelegramBot telegramBot) throws TelegramApiException {
        StringBuilder builder = new StringBuilder();

        builder.append("<b>Application info</b>\n\n");
        builder.append("<b>Distributors</b>\n");
        for (AppConfig.Distributors distributors : Config.get().getDistributors()) {
            builder.append(String.format("<b>%s</b>\n", distributors.getName()));
            builder.append(String.format("\tPrices: %s-%s\n",
                    distributors.getMinPrice(),
                    distributors.getMaxPrice()
            ));

            builder.append(String.format("\tRooms: %s-%s\n",
                    distributors.getMinRooms(),
                    distributors.getMaxRooms()
            ));
            builder.append("<b>\tdistricts:</b>\n");
            for (String name : distributors.getDistricts()) {
                builder.append(String.format("\t\t<i>-%s</i>\n", DistrictsEnum.valueOf(name).ruName));
            }
        }

        builder.append("<b>Total Offers:</b>\n");

        for (Map.Entry<String, File> entry : DataReader.getInstance().getOffersFiles().entrySet()) {
            JSONArray array = FReader.readJSONArray(entry.getValue());
            builder.append(String.format("\t<b>%s</b><i>%s</i>\n", entry.getKey(), array.length()));
        }

        SendMessage message = new SendMessage();
        message.setParseMode("HTML");
        message.setText(builder.toString());
        message.setChatId(chatId);
        telegramBot.execute(message);
    }

    public static void sendHelp(Long chatId, TelegramBot telegramBot) throws TelegramApiException {

        StringBuilder builder = new StringBuilder();

        String text = builder.append("<b>Available commands</b>\n")
                             .append("<b>/stop</b><i> - Stop application</i>\n")
                             .append("<b>/info</b><i> - Show application info</i>\n")
                             .append("<b>/subscribe</b><i> - Subscribe to channel</i>\n")
                             .append("<b>/unsubscribe</b><i> - Unsubscribe from channel</i>\n")
                             .append("<b>/pull</b><i> - ping distributors for content</i>\n")
                             .toString();


        SendMessage message = new SendMessage();
        message.setParseMode("HTML");
        message.setText(text);
        message.setChatId(chatId);
        telegramBot.execute(message);
    }

    public static void sendDistributorsInfo(DistributorInterface distributorInterface, Long chatId, TelegramBot telegramBot) throws TelegramApiException {

        SendMessage message = new SendMessage();
        message.setParseMode("HTML");
        message.setText(String.format("<b>%s</b> : %s new offers",
                distributorInterface.getConfig().getName(),
                distributorInterface.getLastOffers().size())
        );
        message.setChatId(chatId);
        telegramBot.execute(message);
    }

    public static void sendSubscribeNotice(Long chatId, TelegramBot telegramBot) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setParseMode("HTML");
        message.setText("<b>current chat added to subscribers</b>");
        message.setChatId(chatId);
        telegramBot.execute(message);
    }

    public static void sendUnSubscribeNotice(Long chatId, TelegramBot telegramBot) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setParseMode("HTML");
        message.setText("<b>current chat removed from subscribers</b>");
        message.setChatId(chatId);
        telegramBot.execute(message);
    }
}
