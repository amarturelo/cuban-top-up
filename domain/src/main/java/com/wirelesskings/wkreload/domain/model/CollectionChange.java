package com.wirelesskings.wkreload.domain.model;

import java.util.List;

/**
 * Created by Alberto on 28/10/2017.
 */

public class CollectionChange<T> {
    private List<T> inserted;
    private List<T> deleted;
    private List<T> changed;

    public CollectionChange() {
    }

    public List<T> getInserted() {
        return inserted;
    }

    public CollectionChange setInserted(List<T> inserted) {
        this.inserted = inserted;
        return this;
    }

    public List<T> getDeleted() {
        return deleted;
    }

    public CollectionChange setDeleted(List<T> deleted) {
        this.deleted = deleted;
        return this;
    }

    public List<T> getChanged() {
        return changed;
    }

    public CollectionChange setChanged(List<T> changed) {
        this.changed = changed;
        return this;
    }
}
