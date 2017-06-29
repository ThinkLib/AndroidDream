package com.madreain.androiddream.core.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.madreain.androiddream.core.Model.MSecondType;
import com.madreain.androiddream.core.Model.MTKnowledge;
import com.madreain.androiddream.core.Model.MType;
import com.madreain.androiddream.core.Constants;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author madreain
 * @desc
 * @time 2017/4/5
 */

public class MHelper extends OrmLiteSqliteOpenHelper {

    // 数据库version
    private static final int DATABASE_VERSION = 1;
    //    private static Context context;
    private static MHelper helper;
    private Map<String, Dao> ormDaoMap = new HashMap<String, Dao>();


    private MHelper(Context context) {
        super(context, Constants.DATABASE_NAME + ".db", null, 4);
    }

//    public MHelper(Context context) {
//        super(context, Constants.DATABASE_NAME + ".db", null, DATABASE_VERSION);
//        this.context = context;
////        Log.e("数据库的名字", "数据库的名字" + GlobalApplication.DATABASE_NAME);
//    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, MType.class);
            TableUtils.createTable(connectionSource, MSecondType.class);
            TableUtils.createTable(connectionSource, MTKnowledge.class);
        } catch (java.sql.SQLException e) {

            e.printStackTrace();
        }
    }

    //18868234226
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
//        Log.e("更新User表", "更新User表" + GlobalApplication.DATABASE_NAME);
    }


    public static synchronized MHelper getHelper(Context context) {
        if (helper == null) {
            synchronized (MHelper.class) {
                if (helper == null)
                    helper = new MHelper(context);
            }
        }
        return helper;
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (ormDaoMap.containsKey(className)) {
            dao = ormDaoMap.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            ormDaoMap.put(className, dao);
        }
        return dao;
    }

    @Override
    public void close() {
        super.close();
        helper = null;
        for (String key : ormDaoMap.keySet()) {
            Dao ormDao = ormDaoMap.get(key);
            ormDao = null;
        }
        ormDaoMap.clear();
    }

}
