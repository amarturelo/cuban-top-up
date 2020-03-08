package com.wirelesskings.wkreload.model;

/**
 * Created by alberto on 22/01/18.
 */

public class FilterItemModel {
    private String id;
    private String text;

    public FilterItemModel() {
    }

    public String getId() {
        return id;
    }

    public FilterItemModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return text;
    }

    public FilterItemModel setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public String toString() {
        return text
                ;
    }
}
