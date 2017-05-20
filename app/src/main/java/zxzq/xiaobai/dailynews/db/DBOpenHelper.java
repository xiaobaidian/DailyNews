package zxzq.xiaobai.dailynews.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 蔡传飞 on 2017-05-09.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(Context context) {
        super(context, "newsdb.db", null, 1);
    }

    /**
     * 创建表
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table type(_id integer primary key autoincrement,subid integer,subgroup text)");
        db.execSQL("create table news(_id integer primary key autoincrement,type integer,nid integer,stamp text,icon text,title text,summary text,link text)");
        db.execSQL("create table lovenews(_id integer primary key autoincrement,type integer,nid integer,stamp text,icon text,title text,summary text,link text)");
    }

    /**
     * 表的升级
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
