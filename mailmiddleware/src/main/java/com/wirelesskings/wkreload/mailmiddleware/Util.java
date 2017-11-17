package com.wirelesskings.wkreload.mailmiddleware;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.DoubleBinaryOperator;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Set;

public class Util {

    public Util() {
    }

/*    public static String fetch(Map<String, Object> node) {
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
    }*/

    public static String scraper(JsonElement node) {
        String result = "";

        if (node.isJsonArray())
            for (JsonElement element :
                    node.getAsJsonArray())
                result += scraper(element);
        else if (node.isJsonObject()) {
            Set<Map.Entry<String, JsonElement>> members = node.getAsJsonObject().entrySet();
            for (Map.Entry<String, JsonElement> e : members) {
                result += scraper(e.getValue());
            }
        } else if (node.isJsonPrimitive()) {
            if(node.getAsJsonPrimitive().isNumber())
                return removeLastChars(node.getAsJsonPrimitive().getAsDouble(),".0");
            if(node.getAsJsonPrimitive().isBoolean())
                return node.getAsJsonPrimitive().getAsBoolean()?"1":"0";
            return node.getAsJsonPrimitive().getAsString();
        }
        return result;
    }

    private static String removeLastChars(double eval, String text) {

        String res = String.valueOf(eval);
        int length = text.length();

        if (res.length() > length) {
            res = res.substring((res.length() - length), res.length()).equals(text)
                    ? res.substring(0, (res.length() - length)) : res;
        }

        return res;
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private static String decimal(double answer) {
        DecimalFormat df = new DecimalFormat("###.#");
        return df.format(answer);
    }
}