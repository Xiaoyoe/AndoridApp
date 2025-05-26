package com.example.ticketmall.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ticketmall.entity.Cart;
import com.example.ticketmall.entity.Ticket;
import com.example.ticketmall.utils.SqliteUtils;

import java.util.ArrayList;
import java.util.List;

public class CartDB {

    /**
     * 获取购物车列表
     */
    @SuppressLint("Range")
    public static BusinessResult<List<Cart>> getCartList(int userId, Integer type) {
        SQLiteDatabase db = SqliteUtils.getInstance().getReadableDatabase();
        Cursor cursor;
        if (type == null) {
            cursor = db.query("_cart", null, "user_id=?", new String[]{String.valueOf(userId)}, null, null, null);
        } else {
            cursor = db.query("_cart", null, "user_id=? and type=?", new String[]{String.valueOf(userId), String.valueOf(type)}, null, null, null);
        }
        List<Cart> cartList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Cart cart = new Cart();
            cart.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            cart.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
            cart.setType(cursor.getInt(cursor.getColumnIndex("type")));
            cart.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            cart.setImageResId(cursor.getInt(cursor.getColumnIndex("image_res_id")));
            cart.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
            cart.setCount(cursor.getInt(cursor.getColumnIndex("count")));
            cartList.add(cart);
        }
        cursor.close();
        BusinessResult<List<Cart>> result = new BusinessResult<>();
        result.setSuccess(true);
        result.setData(cartList);
        return result;
    }

    /**
     * 购物车是否存在该商品
     */
    public static BusinessResult<Boolean> isExistByTitle(int userId, String title) {
        BusinessResult<Boolean> result = new BusinessResult<>();
        SQLiteDatabase db = SqliteUtils.getInstance().getReadableDatabase();
        Cursor cursor = db.query("_cart", null, "user_id=? and title=?", new String[]{String.valueOf(userId), title}, null, null, null);
        if (cursor.moveToNext()) {
            result.setSuccess(true);
            result.setData(true);
            result.setMessage("该商品已存在购物车");
        } else {
            result.setSuccess(true);
            result.setData(false);
            result.setMessage("该商品不存在购物车");
        }
        cursor.close();
        return result;
    }


    /**
     * 添加商品到购物车
     */
    @SuppressLint("Range")
    public static BusinessResult<Void> addCart(int userId, Ticket ticket) {
        BusinessResult<Void> result = new BusinessResult<>();
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        //如果购物车中已有该商品，则更新数量
        int count = 1;
        if (isExistByTitle(userId, ticket.getTitle()).getData()) {
            Cursor cursor = db.query("_cart", null, "user_id=? and title=?", new String[]{String.valueOf(userId), ticket.getTitle()}, null, null, null);
            if (cursor.moveToNext()) {
                count = cursor.getInt(cursor.getColumnIndex("count")) + 1;
            }
            cursor.close();
        }
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("type", ticket.getType());
        values.put("title", ticket.getTitle());
        values.put("image_res_id", ticket.getImageResId());
        values.put("price", ticket.getPrice());
        values.put("count", count);
        int i = 0;
        if (count > 1) {
            //更新
            i = db.update("_cart", values, "user_id=? and title=?", new String[]{String.valueOf(userId), ticket.getTitle()});
        } else {
            i = (int) db.insert("_cart", null, values);
        }
        if (i > 0) {
            result.setSuccess(true);
            result.setMessage("添加成功");
        } else {
            result.setSuccess(false);
            result.setMessage("添加失败");
        }
        return result;
    }

    /**
     * 修改购物车商品数量
     */
    public static BusinessResult<Void> updateCart(int cartId, int count) {
        if (count < 1) {
            //删除商品
            return deleteCart(cartId);
        }
        BusinessResult<Void> result = new BusinessResult<>();
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("count", count);
        int i = db.update("_cart", values, "_id=?", new String[]{String.valueOf(cartId)});
        if (i > 0) {
            result.setSuccess(true);
            result.setMessage("修改成功");
        } else {
            result.setSuccess(false);
            result.setMessage("修改失败");
        }
        return result;
    }

    /**
     * 删除购物车商品
     */
    public static BusinessResult<Void> deleteCart(int cartId) {
        BusinessResult<Void> result = new BusinessResult<>();
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        db.delete("_cart", "_id=?", new String[]{String.valueOf(cartId)});
        result.setSuccess(true);
        result.setMessage("删除成功");
        return result;
    }

}
