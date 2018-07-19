package alliness.apartmentparser.enums;

import alliness.apartmentparser.implementation.LunDistributor;
import alliness.apartmentparser.implementation.OlxDistributor;
import alliness.apartmentparser.implementation.RiaDistributor;
import alliness.apartmentparser.interfaces.DistributorInterface;

public enum DistributorsEnum {

    ria(RiaDistributor.class),
    olx(OlxDistributor.class),
    lun(LunDistributor.class);

    public Class<?> implementation;

    <T extends DistributorInterface> DistributorsEnum(Class<T> implementationclass) {
        implementation = implementationclass;
    }


}
