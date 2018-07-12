package alliness.apartmentparser;

import alliness.apartmentparser.dto.Offer;
import alliness.apartmentparser.enums.DistrictsEnum;
import alliness.apartmentparser.interfaces.DistributorInterface;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;

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
//        try {
            log.info(String.format("[%s] execute request for district [%s]\n%s", distributor.getConfig().getName(), district.enName, uri.toString()));
//            Document document = Jsoup.connect(String.valueOf(uri))
//                                     .userAgent(Config.get().getAgent())
//                                     .get();

//            Offer offer = distributor.parse(document);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
