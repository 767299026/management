package xyz.lsl.vue;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class test {

    @Test
    void contextLoads() {
        String a = "106";
        String b = "102,107,109,154,155,145,146,148";
        List<String> list = Arrays.asList(b.split(","));
        boolean status = list.contains(a);
        System.out.println(status);
    }
}
