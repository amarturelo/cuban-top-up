package com.wirelesskings.wkreload.mailmiddleware;

import java.util.Map;
import java.util.function.BiConsumer;

public class Structure {


    private Map<String, Object> node;

    public Structure() {
    }

    public Structure setNode(Map<String, Object> node) {
        this.node = node;
        return this;
    }

    public String getConcat() {
        return fetch(node);
    }

    public Map<String, Object> getNode() {
        return node;
    }

    public static String fetch(Map<String, Object> node) {
        final String[] result = {""};
        node.forEach((s, o) -> {
            if (o instanceof Map)
                result[0] += fetch((Map<String, Object>) o);
            else
                result[0] += o.toString();
        });
        return result[0];
    }
}