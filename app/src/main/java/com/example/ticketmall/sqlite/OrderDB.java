package com.example.ticketmall.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ticketmall.entity.Cart;
import com.example.ticketmall.entity.Order;
import com.example.ticketmall.utils.SqliteUtils;
import com.gzone.university.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDB {

    /**
     * 查询所有订单
     */
    @SuppressLint("Range")
    public static BusinessResult<List<Order>> getOrderList(int userId) {
        SQLiteDatabase db = SqliteUtils.getInstance().getReadableDatabase();
        // 查询用户的所有订单,并按照订单号降序排列
        Cursor cursor = db.query("_order", null, "user_id=?", new String[]{String.valueOf(userId)}, null, null, "_id desc");
        List<Order> orderList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Order order = new Order();
            order.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            order.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
            order.setTotal(cursor.getDouble(cursor.getColumnIndex("total")));
            order.setDetail(cursor.getString(cursor.getColumnIndex("detail")));
            order.setCreateTime(cursor.getString(cursor.getColumnIndex("create_time")));
            orderList.add(order);
        }
        cursor.close();
        BusinessResult<List<Order>> result = new BusinessResult<>();
        result.setSuccess(true);
        result.setData(orderList);
        return result;
    }

    /**
     * 添加订单
     */
    public static BusinessResult<Void> addOrder(int userId, List<Cart> cartList) {
        double total = 0;
        StringBuilder detail = new StringBuilder();
        for (Cart cart : cartList) {
            total += cart.getPrice() * cart.getCount();
            detail.append(cart.getTitle()).append(" x ").append(cart.getCount()).append(",");
            BusinessResult<Void> deleteCartResult = CartDB.deleteCart(cart.getId());
            if (!deleteCartResult.isSuccess()) {
                BusinessResult<Void> result = new BusinessResult<>();
                result.setSuccess(false);
                result.setMessage("创建订单异常，删除购物车记录被中断");
                return result;
            }
        }
        BusinessResult<Void> result = new BusinessResult<>();
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("total", total);
        values.put("detail", detail.toString().substring(0, detail.length() - 1));
        values.put("create_time", DateUtils.format(new Date()));
        long i = db.insert("_order", null, values);
        if (i > 0) {
            // 更新用户积分
            Cursor cursor = db.query("_user", new String[]{"points"}, "_id=?", new String[]{String.valueOf(userId)}, null, null, null);
            int currentPoints = 0;
            if (cursor.moveToFirst()) {
                currentPoints = cursor.getInt(0);
            }
            cursor.close();

            // 积分 = 当前积分 + 订单金额（向下取整）
            int newPoints = currentPoints + (int) total;
            BusinessResult<Void> updateResult = UserDB.updatePoints(userId, newPoints);
            if (!updateResult.isSuccess()) {
                result.setSuccess(false);
                result.setMessage("创建订单成功，但更新积分失败");
                return result;
            }
            
            result.setSuccess(true);
            result.setMessage("创建订单成功,结算成功,积分已更新");
        } else {
            result.setSuccess(false);
            result.setMessage("创建订单失败");
        }
        return result;
    }

    /**
     * 删除订单
     */
    public static BusinessResult<Void> deleteOrder(int orderId) {
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        db.delete("_order", "_id=?", new String[]{String.valueOf(orderId)});
        BusinessResult<Void> result = new BusinessResult<>();
        result.setSuccess(true);
        result.setMessage("删除订单成功");
        return result;
    }

}
