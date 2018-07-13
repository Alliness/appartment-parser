package alliness.apartmentparser.interfaces;

import alliness.apartmentparser.dto.AppConfig;
import alliness.apartmentparser.dto.Offer;
import alliness.apartmentparser.enums.DistrictsEnum;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

public interface DistributorInterface {

    HashMap<DistrictsEnum, URI> getQueries();

    HashMap<String, String> getQueryMapping();

    AppConfig.Distributors getConfig();

    void executeQueries();

    List<Offer> parse(Document document);

    List<String> getOffersIds();

    boolean addOfferId(String offers);
}
