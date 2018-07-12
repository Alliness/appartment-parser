package alliness.apartmentparser.implementation;

import alliness.apartmentparser.Config;
import alliness.apartmentparser.QueryExecutor;
import alliness.apartmentparser.dto.AppConfig;
import alliness.apartmentparser.interfaces.DistributorInterface;
import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class BaseDistributor implements DistributorInterface {

    private final ScheduledExecutorService executor;
    private       AppConfig.Distributors   config;
    private       Logger                   log = Logger.getLogger(BaseDistributor.class);

    public BaseDistributor(AppConfig.Distributors config) {
        this.config = config;
        executor = Executors.newScheduledThreadPool(config.getDistricts().size());
        log.info(String.format(
                "[%s] Create %s queries with %s seconds interval",
                getConfig().getName(),
                getQueries().size(),
                Config.get().getInterval()
        ));

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

}
