package zxzq.xiaobai.dailynews.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import zxzq.xiaobai.dailynews.entity.News;
import zxzq.xiaobai.dailynews.entity.NewsType;

/**
 * Created by 蔡传飞 on 2017-05-09.
 */

public class NewsDBManager {
    /**
     * 1.保存新闻的分类
     * 2.获取新闻的分类
     * 3.添加数据
     * 4.查询新闻
     * 5.查询新闻id
     * 6.收藏新闻
     * 7.获取收藏新闻的列表
     * 8.从收藏列表中中删除单个新闻
     * 9.清空收藏
     */
    public DBOpenHelper dbHelper;
    public Context context;

    public NewsDBManager(Context context) {
        this.context = context;
        dbHelper = new DBOpenHelper(context);
    }

    /**
     * 保存新闻的分类
     */
    public boolean saveNewsType(List<NewsType.SubType> types) {
        for (NewsType.SubType type : types) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String sql = "select * from type where subid=" + type.getSubid();
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                cursor.close();
                return false;
            }
            ContentValues values = new ContentValues();
            values.put("subid", type.getSubid());
            values.put("subgroup", type.getSubgroup());
            db.insert("type", null, values);
            db.close();
        }
        return true;
    }

    /**
     * 获取新闻的分类
     */
    public ArrayList<NewsType.SubType> queryNewsType() {
        ArrayList<NewsType.SubType> newsList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from type order by _id desc";//根据_id降序查询
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                int subid = cursor.getInt(cursor.getColumnIndex("subid"));
                String subgroup = cursor.getString(cursor.getColumnIndex("subgroup"));
                NewsType.SubType subType = new NewsType.SubType(subid, subgroup);
                newsList.add(subType);
            }
            cursor.close();
            db.close();
        }
        return newsList;
    }

    /**
     * 添加数据
     */
    public void insertNews(News news) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type", news.getType());
        values.put("nid", news.getNid());
        values.put("stamp", news.getStamp());
        values.put("icon", news.getIcon());
        values.put("title", news.getTitle());
        values.put("summary", news.getSummary());
        values.put("link", news.getLink());
        db.insert("news", null, values);
        db.close();
    }

    /**
     * 查询新闻
     */
    public ArrayList<News> querryNews(int count, int offset) {
        ArrayList<News> newsList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from news order by _id desc limit" + count + "offset" + offset;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                int nid = cursor.getInt(cursor.getColumnIndex("nid"));
                String stamp = cursor.getString(cursor.getColumnIndex("stamp"));
                String icon = cursor.getString(cursor.getColumnIndex("icon"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String summary = cursor.getString(cursor.getColumnIndex("summary"));
                String link = cursor.getString(cursor.getColumnIndex("link"));
                News news = new News(type, nid, stamp, icon, title, summary, link);
                newsList.add(news);
            }
            db.close();
            cursor.close();
        }
        return newsList;
    }
    /**
     * 收藏新闻
     */
    public boolean saveLoveNews(News news){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        String sql="select * from lovenews where nid="+news.getNid();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            cursor.close();
            return false;
        }
        ContentValues values=new ContentValues();
        values.put("type", news.getType());
        values.put("nid", news.getNid());
        values.put("stamp", news.getStamp());
        values.put("icon", news.getIcon());
        values.put("title", news.getTitle());
        values.put("summary", news.getSummary());
        values.put("link", news.getLink());
        db.insert("lovenews", null, values);
        db.close();
        cursor.close();
        return true;
    }
    /**
     * 查看收藏新闻的列表
     */
    public ArrayList<News> querryLoveNews(){
        ArrayList<News> newsList=new ArrayList<>();
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String sql="select * from lovenews order by _id desc";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                int nid = cursor.getInt(cursor.getColumnIndex("nid"));
                String stamp = cursor.getString(cursor.getColumnIndex("stamp"));
                String icon = cursor.getString(cursor.getColumnIndex("icon"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String summary = cursor.getString(cursor.getColumnIndex("summary"));
                String link = cursor.getString(cursor.getColumnIndex("link"));
                News news = new News(type, nid, stamp, icon, title, summary, link);
                newsList.add(news);
            }
            cursor.close();
            db.close();
        }
        return newsList;
    }
    /**
     * 删除单条收藏的新闻
     */
    public boolean deletNews(News news){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        String sql="delete from lovenews where nid="+news.getNid();
        db.execSQL(sql);
        db.close();
        return true;
    }
    /**
     * 清空收藏的新闻
     */
    public boolean deleteAllNews(){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        String sql="delete from lovenews";
        db.execSQL(sql);
        db.close();
        return true;
    }
}
