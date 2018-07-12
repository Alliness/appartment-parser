package alliness.apartmentparser;

import alliness.apartmentparser.dto.Offer;
import alliness.apartmentparser.enums.DistrictsEnum;
import alliness.apartmentparser.interfaces.DistributorInterface;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
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
                    "[%s] execute request for district [%s]\n%s",
                    distributor.getConfig().getName(),
                    district.enName,
                    uri.toString()
            ));

            Connection request = Jsoup.connect(String.valueOf(uri)).userAgent(Config.get().getAgent());
            switch (distributor.getConfig().getType()) {
                case "html":
                    break;
                case "json":
                    request.ignoreContentType(true);
                    break;
            }

            List<Offer> offers = distributor.parse(request.execute().parse());

            offers.forEach(offer -> System.out.println(String.format(
                    "[%s][%s]\n[%s]", distributor.getConfig().getName(),
                    district.enName,
                    offer.serialize().toString(2)
            )));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
