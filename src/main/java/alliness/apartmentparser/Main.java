package alliness.apartmentparser;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws URISyntaxException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        Config.get();

        //init bot
        new ApartmentParser();
    }

}
