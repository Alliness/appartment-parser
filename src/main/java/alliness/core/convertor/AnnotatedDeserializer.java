package alliness.core.convertor;

import alliness.core.annotations.RequiredField;
import com.google.gson.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class AnnotatedDeserializer<T> implements JsonDeserializer<T> {
    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        T       pojo   = new Gson().fromJson(jsonElement, type);
        Field[] fields = pojo.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(RequiredField.class) != null) {
                field.setAccessible(true);
                try {
                    if (field.get(pojo) == null) {
                        throw new JsonParseException(String.format("Missing mandatory json key '%s'", field.getName()));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return pojo;
    }
}
