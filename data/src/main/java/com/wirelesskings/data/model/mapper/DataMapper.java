package com.wirelesskings.data.model.mapper;

import java.util.List;

/**
 * Created by Alberto on 18/10/2017.
 */

public interface DataMapper<T, K> {

    List<K> transform(List<T> ts);

    K transform(T t);

}
