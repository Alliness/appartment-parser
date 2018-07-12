package alliness.apartmentparser;

import alliness.apartmentparser.dto.AppConfig;
import alliness.apartmentparser.enums.DistributorsEnum;
import alliness.apartmentparser.interfaces.DistributorInterface;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DistributorBuilder {


    private DistributorBuilder() {
    }

    public static DistributorInterface fromConfig(AppConfig.Distributors distributor) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> clazz = DistributorsEnum.valueOf(distributor.getName()).implementation.getDeclaredConstructor(AppConfig.Distributors.class);
        clazz.setAccessible(true);
        return (DistributorInterface) clazz.newInstance(distributor);
    }
}
