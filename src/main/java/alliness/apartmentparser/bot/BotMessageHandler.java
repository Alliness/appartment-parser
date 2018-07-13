package alliness.apartmentparser.bot;

import org.json.JSONArray;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class BotMessageHandler {


//    public static void sendInfo(Long chatId, TelgeramBot olxBot) {
//        SendMessage message = new SendMessage();
//        message.setChatId(chatId);
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("<b>Районы поиска:</b>\n");
//        for (String s : Config.getInstance().getConfig().getOlx().getDistricts()) {
//            sb.append(String.format("<i>-%s</i>\n", DistrictsEnum.valueOf(s).ruName));
//        }
//
//        sb.append("<b>Конфиг:</b>\n");
//        sb.append(String.format("<i>Цены: %s-%s</i>\n",
//                Config.getInstance().getConfig().getOlx().getMinPrice(),
//                Config.getInstance().getConfig().getOlx().getMaxPrice())
//        );
//        sb.append(String.format("<i>Комнаты: %s-%s</i>\n",
//                Config.getInstance().getConfig().getOlx().getMinRooms(),
//                Config.getInstance().getConfig().getOlx().getMaxRooms())
//        );
//        sb.append(String.format("<i>Интервал поиска каждые: %s секунд</i>\n", Config.getInstance().getConfig().getApp().getPingInterval()));
//        sb.append(String.format("<i>Всего собрано обявлений: %s</i>\n", OlxClient.getInstance().getOffers().size()));
//        sb.append(String.format("<i>Подписано на рассылку: %s </i>\n", TelgeramBot.getInstance().getChats().length()));
//        message.setText(sb.toString());
//        message.setParseMode("HTML");
//        try {
//            olxBot.execute(message);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }

    public static void sendMessage(JSONArray chats, String text, TelgeramBot olxBot) {
        for (Object chat : chats) {
            String      chatId  = String.valueOf(chat);
            SendMessage message = new SendMessage(chatId, text.replace("/send", ""));
            try {
                olxBot.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }
    }

    public static void sendHelp(Long chatId, TelgeramBot olxBot) {

    }
}
