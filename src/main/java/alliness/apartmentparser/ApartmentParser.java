package alliness.apartmentparser;

import alliness.apartmentparser.dto.AppConfig;
import alliness.apartmentparser.interfaces.DistributorInterface;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ApartmentParser {

    private static final Logger log = Logger.getLogger(ApartmentParser.class);

    private List<DistributorInterface> distributors;
    ScheduledExecutorService executor;

    ApartmentParser() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, URISyntaxException {

        distributors = new ArrayList<>();

        for (AppConfig.Distributors distributor : Config.get().getDistributors()) {
            log.info(String.format("Register new Distributor: %s", distributor.getName()));
            register(DistributorBuilder.fromConfig(distributor));
        }

        executor = Executors.newScheduledThreadPool(distributors.size());

        for (DistributorInterface distributor : distributors) {
            distributor.executeQueries();
        }

    }

    private void register(DistributorInterface distributor) {
        if (!distributors.contains(distributor)) {
            distributors.add(distributor);
            log.info(String.format("Distributor: %s registered successfully", distributor.getConfig().getName()));
        } else {
            log.info(String.format("Unable to register Distributor %s", distributor.getConfig().getName()));
        }
    }

}
