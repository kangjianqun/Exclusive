package com.kjq.common.utils.db;

import java.util.ArrayList;

/**
 * 数据库操控
 * Created by Administrator on 2018/3/9 0009.
 */

public interface IBaseDao<T> {
    long insert(T model);
    int update(T model, T where);
    int delete(T model);

    ArrayList<T> query(T where);
    ArrayList<T> query(T where, String orderBy);
    ArrayList<T> query(T where, String orderBy, Integer startIndex, Integer limit);
}
