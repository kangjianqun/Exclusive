package com.kjq.common.utils.db;

import java.util.ArrayList;

public class DataBaseUtils {
    private DataBaseUtils(){ }

    public static DataBaseUtils getInstance(){
        return MyDataBaseUtilsHolder.INSTANCE;
    }

    private static class MyDataBaseUtilsHolder{
        private static final DataBaseUtils INSTANCE = new DataBaseUtils();
    }

    /**
     * 保存实体到数据库
     * @param where 条件
     * @param model 对象
     * @param c 类
     * @param <T> 泛型
     * @return 是否成功
     */
    public <T> boolean saveModel(T where,T model,Class<T> c){
        IBaseDao<T> sIBaseDao = BaseDaoFactory.getInstance().getBaseDaoUtils(c);
        long sL_index ;
        ArrayList<T> sDModules = sIBaseDao.query(where);
        if (sDModules == null || sDModules.size() <= 0){
            sL_index = sIBaseDao.insert(model);
        }else {
            sL_index = sIBaseDao.update(model,where);
        }
        return sL_index > 0;
    }

    public <T> ArrayList<T> loadModel(T where,Class<T> c){
        IBaseDao<T> sIBaseDao = BaseDaoFactory.getInstance().getBaseDaoUtils(c);
        return sIBaseDao.query(where);
    }

    public <T> boolean checkExist(T where,Class<T> c){
        IBaseDao<T> sIBaseDao = BaseDaoFactory.getInstance().getBaseDaoUtils(c);
        ArrayList<T> sTS = sIBaseDao.query(where);
        return sTS != null && sTS.size() > 0;
    }

    public <T> boolean deleteModel(T where,Class<T> c){
        IBaseDao<T> sIBaseDao = BaseDaoFactory.getInstance().getBaseDaoUtils(c);
        return sIBaseDao.delete(where) > 0;
    }
}
