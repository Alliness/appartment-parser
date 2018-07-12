package alliness.apartmentparser.interfaces;

import alliness.apartmentparser.dto.AppConfig;
import alliness.apartmentparser.dto.Offer;
import alliness.apartmentparser.enums.DistrictsEnum;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.util.HashMap;

public interface DistributorInterface {

    HashMap<DistrictsEnum, URI> getQueries();

    HashMap<String, String> getQueryMapping();

    AppConfig.Distributors getConfig();

    void executeQueries();

    Offer parse(Document element);

}
