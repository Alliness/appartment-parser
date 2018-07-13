package alliness.apartmentparser.implementation;

import alliness.apartmentparser.Config;
import alliness.apartmentparser.DataReader;
import alliness.apartmentparser.QueryExecutor;
import alliness.apartmentparser.dto.AppConfig;
import alliness.apartmentparser.dto.Offer;
import alliness.apartmentparser.interfaces.DistributorInterface;
import alliness.core.helpers.FReader;
import alliness.core.helpers.FWriter;
import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class BaseDistributor implements DistributorInterface {

    private final ScheduledExecutorService executor;
    private       AppConfig.Distributors   config;
    Logger log = Logger.getLogger(BaseDistributor.class);
    private List<String> offers = Collections.synchronizedList(new ArrayList<>());
    private File         file;
    private List<Offer>  lastOffers;

    public BaseDistributor(AppConfig.Distributors config) {
        this.config = config;
        lastOffers = new ArrayList<>();
        executor = Executors.newScheduledThreadPool(config.getDistricts().size());

        log.info(String.format(
                "[%s] Create %s queries with %s seconds interval",
                getConfig().getName(),
                getQueries().size(),
                Config.get().getInterval()
        ));

        file = DataReader.getInstance().getFileForDistributor(config.getName());

        for (Object o : FReader.readJSONArray(file).toList()) {
            offers.add((String) o);
        }
    }

    public void executeQueries() {
        getQueries().forEach((districtsEnum, uri) ->
                executor.scheduleWithFixedDelay(
                        new QueryExecutor(districtsEnum, uri, this),
                        0,
                        Config.get().getInterval(),
                        TimeUnit.SECONDS)
        );
    }

    void setParams(URIBuilder builder, HashMap<String, String> queryMapping) {
        queryMapping.forEach(builder::setParameter);
    }

    public AppConfig.Distributors getConfig() {
        return config;
    }

    public synchronized List<String> getOffersIds() {
        return offers;
    }

    @Override
    public synchronized boolean addOfferId(String offerId) {
        try {

            if (!this.offers.contains(offerId)) {
                this.offers.add(offerId);
                FWriter.writeToFile(new JSONArray(this.offers).toString(), file, false);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void addLastOffers(List<Offer> offers) {
        lastOffers.clear();
        lastOffers.addAll(offers);
    }

    @Override
    public List<Offer> getLastOffers() {
        return lastOffers;
    }
}
