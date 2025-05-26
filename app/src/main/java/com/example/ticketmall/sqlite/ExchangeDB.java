package com.example.ticketmall.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ticketmall.entity.Exchange;
import com.example.ticketmall.utils.SqliteUtils;
import com.gzone.university.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExchangeDB {

    /**
     * 获取兑换记录列表
     */
    @SuppressLint("Range")
    public static BusinessResult<List<Exchange>> getExchangeList(int userId) {
        SQLiteDatabase db = SqliteUtils.getInstance().getReadableDatabase();
        // 按照兑换时间降序排列
        Cursor cursor = db.query("_exchange", null, "user_id=?",
                new String[]{String.valueOf(userId)}, null, null, "create_time desc");
        List<Exchange> exchangeList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Exchange exchange = new Exchange();
            exchange.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            exchange.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
            exchange.setPoints(cursor.getInt(cursor.getColumnIndex("points")));
            exchange.setStuffName(cursor.getString(cursor.getColumnIndex("stuff_name")));
            exchange.setCreateTime(cursor.getString(cursor.getColumnIndex("create_time")));
            exchangeList.add(exchange);
        }
        cursor.close();
        BusinessResult<List<Exchange>> result = new BusinessResult<>();
        result.setSuccess(true);
        result.setData(exchangeList);
        return result;
    }

    /**
     * 添加兑换记录
     */
    public static BusinessResult<Void> addExchange(Exchange exchange) {
        BusinessResult<Void> result = new BusinessResult<>();
        if (exchange == null) {
            result.setSuccess(false);
            result.setMessage("兑换信息不能为空");
            return result;
        }

        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", exchange.getUserId());
        values.put("points", exchange.getPoints());
        values.put("stuff_name", exchange.getStuffName());
        values.put("create_time", DateUtils.format(new Date()));

        long i = db.insert("_exchange", null, values);
        if (i > 0) {
            // 更新用户积分
            Cursor cursor = db.query("_user", new String[]{"points"}, "_id=?",
                    new String[]{String.valueOf(exchange.getUserId())}, null, null, null);
            int currentPoints = 0;
            if (cursor.moveToFirst()) {
                @SuppressLint("Range")
                int points = cursor.getInt(cursor.getColumnIndex("points"));
                currentPoints = points;
            }
            cursor.close();

            // 检查积分是否足够
            if (currentPoints < exchange.getPoints()) {
                result.setSuccess(false);
                result.setMessage("积分不足");
                // 删除刚才添加的兑换记录
                db.delete("_exchange", "_id=?", new String[]{String.valueOf(i)});
                return result;
            }

            // 扣除积分
            int newPoints = currentPoints - exchange.getPoints();
            BusinessResult<Void> updateResult = UserDB.updatePoints(exchange.getUserId(), newPoints);
            if (!updateResult.isSuccess()) {
                result.setSuccess(false);
                result.setMessage("兑换失败，更新积分异常");
                // 删除刚才添加的兑换记录
                db.delete("_exchange", "_id=?", new String[]{String.valueOf(i)});
                return result;
            }

            result.setSuccess(true);
            result.setMessage("兑换成功");
        } else {
            result.setSuccess(false);
            result.setMessage("兑换失败");
        }
        return result;
    }

    /**
     * 删除兑换记录
     */
    public static BusinessResult<Void> deleteExchange(int exchangeId) {
        BusinessResult<Void> result = new BusinessResult<>();
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        db.delete("_exchange", "_id=?", new String[]{String.valueOf(exchangeId)});
        result.setSuccess(true);
        result.setMessage("删除成功");
        return result;
    }
} 