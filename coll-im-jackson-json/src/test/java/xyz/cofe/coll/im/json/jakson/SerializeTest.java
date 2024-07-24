package xyz.cofe.coll.im.json.jakson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import xyz.cofe.coll.im.ImList;

public class SerializeTest {
    @Test
    public void test(){
        ObjectMapper om = new ObjectMapper();
        om.findAndRegisterModules();

        try {
            var json = om.writeValueAsString(ImList.of("12","abc"));
            System.out.println(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
