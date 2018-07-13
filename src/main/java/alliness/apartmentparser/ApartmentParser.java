package alliness.apartmentparser;

import alliness.apartmentparser.dto.AppConfig;
import alliness.apartmentparser.interfaces.DistributorInterface;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ApartmentParser {

    private static final Logger log = Logger.getLogger(ApartmentParser.class);

    private List<DistributorInterface> distributors;
    private ScheduledExecutorService   executor;

    private static ApartmentParser instance;

    public static ApartmentParser getInstance() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (instance == null) {
            instance = new ApartmentParser();
        }
        return instance;
    }


    ApartmentParser() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        createExecutorProcesses();
    }

    public void createExecutorProcesses() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        distributors = new ArrayList<>();


        for (AppConfig.Distributors distributor : Config.get().getDistributors()) {
            register(DistributorBuilder.fromConfig(distributor));
        }

        if (executor != null) {
            executor.shutdown();
            log.info("Executor service is shutdown");
        }

        executor = Executors.newScheduledThreadPool(distributors.size());

        log.info("Executor service is started");
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

    public List<DistributorInterface> getDistributors() {
        return distributors;
    }
}
