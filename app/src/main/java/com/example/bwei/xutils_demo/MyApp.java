package com.example.bwei.xutils_demo;

import android.app.Application;
import android.widget.Toast;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

/**
 * Created by YU on 2017/7/6.
 */
public class MyApp extends Application{

    private DbManager.DaoConfig daoConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);//初始化数据
        x.Ext.setDebug(false);//输出Debug日志，开启会影响性能
        daoConfig = new DbManager.DaoConfig()
                .setDbName("x.db")
                .setDbVersion(1)
                .setAllowTransaction(true)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging();

                    }
                })
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {
                        Toast.makeText(MyApp.this, "---" + table.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }
}
