package com.wirelesskings.wkreload.mailmiddleware;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;

import java.util.Map;

public class Util {

    public Util() {
    }

    public static String fetch(Map<String, Object> node) {
        final String[] result = {""};
        Stream.of(node).forEach(new Consumer<Map.Entry<String, Object>>() {
            @Override
            public void accept(Map.Entry<String, Object> o) {
                if (o.getValue() instanceof Map)
                    result[0] += fetch((Map<String, Object>) o.getValue());
                else
                    result[0] += o.getValue().toString();
            }
        });
        return result[0];
    }
}