package com.example.ticketmall.utils;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gzone.university.utils.AppUtils;

public class SqliteUtils extends SQLiteOpenHelper {

    public SqliteUtils() {
        super(AppUtils.getApplication(), "ticket_mall.db", null, 1);
    }

    /**
     * 创建并获取单例
     */
    public static SqliteUtils getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
        用户表(_user)：
        _id       integer  用户id
        username  varchar  用户名
        password  varchar  密码
        points    integer  积分
         */
        db.execSQL("CREATE TABLE _user(_id INTEGER PRIMARY KEY AUTOINCREMENT,username VARCHAR(20) ,password VARCHAR(20),points INTEGER DEFAULT 0)");
        /*
        购物车表(_cart):
        _id           integer  购物车id
        user_id       integer  用户id
        type          integer  类型(1：电影，2：演唱会，3：音乐节，4：脱口秀)
        title         varchar  名称
        image_res_id  integer  图片资源id
        price         double   价格
        count         integer  数量
         */
        db.execSQL("CREATE TABLE _cart(_id INTEGER PRIMARY KEY AUTOINCREMENT,user_id INTEGER,type INTEGER,title VARCHAR(20),image_res_id INTEGER,price DOUBLE,count INTEGER)");
        /*
        订单记录表(_order)：
        _id          integer   订单id
        user_id      integer   用户id
        total        double    总价
        detail       varchar   订单详情
        create_time  varchar   创建时间
         */
        db.execSQL("CREATE TABLE _order(_id INTEGER PRIMARY KEY AUTOINCREMENT,user_id INTEGER,total DOUBLE,detail VARCHAR(200),create_time VARCHAR(20))");
        /*
        兑换记录表(_exchange)：
        _id                 integer   兑换记录id
        user_id             integer   用户id
        points              integer   积分
        stuff_name          varchar   商品名称
        create_time         varchar   创建时间
         */
        db.execSQL("CREATE TABLE _exchange(_id INTEGER PRIMARY KEY AUTOINCREMENT,user_id INTEGER,points INTEGER,stuff_name VARCHAR(100),create_time VARCHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private static final class InstanceHolder {
        /**
         * 单例
         */
        static final SqliteUtils instance = new SqliteUtils();
    }
}
