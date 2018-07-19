package alliness.apartmentparser;

import alliness.apartmentparser.bot.TelegramBot;
import alliness.apartmentparser.dto.Offer;
import alliness.apartmentparser.enums.DistrictsEnum;
import alliness.apartmentparser.interfaces.DistributorInterface;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.util.List;

public class QueryExecutor implements Runnable {

    private static final Logger log = Logger.getLogger(QueryExecutor.class);

    private final DistrictsEnum        district;
    private final URI                  uri;
    private final DistributorInterface distributor;

    public QueryExecutor(DistrictsEnum districtsEnum, URI uri, DistributorInterface distributor) {
        this.district = districtsEnum;
        this.uri = uri;
        this.distributor = distributor;
    }

    @Override
    public void run() {
        try {

            log.info(String.format(
                    "[%s] execute request for district [%s], %s",
                    distributor.getConfig().getName(),
                    district.enName,
                    uri
            ));

            Connection request  = Jsoup.connect(String.valueOf(uri)).userAgent(Config.get().getAgent());
            Document   document = null;
            switch (distributor.getResponseType()) {
                case HTML:
                    break;
                case JSON:
                    request.ignoreContentType(true);
                    request.header("Content-Type", "application/json");
                    break;
            }

            switch (distributor.getMethodType()) {
                case GET:
                    document = request.execute().parse();
                    break;
                case POST:
                    request.method(Connection.Method.POST).requestBody(distributor.getRequestData().toString());
                    document = request.execute().parse();
                    break;
            }

            assert document != null;

            List<Offer> offers = distributor.parse(document);
            offers.removeIf(offer -> distributor.getOffersIds().contains(offer.getOfferId()));
            offers.forEach(offer -> distributor.addOfferId(offer.getOfferId()));
            log.info(String.format("[%s][%s]got %s new offers", distributor.getConfig().getName(), district.enName, offers.size()));

            for (Offer offer : offers) {
                TelegramBot.getInstance().sendMessage(offer);
            }
            distributor.addLastOffers(offers);

            offers.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
