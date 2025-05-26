package com.example.ticketmall.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.ticketmall.entity.User;
import com.example.ticketmall.utils.MD5Utils;
import com.example.ticketmall.utils.SqliteUtils;

@SuppressLint("Range")
public class UserDB {

    /**
     * 注册用户
     */
    public static BusinessResult<User> register(User user, String confirmPassword) {
        BusinessResult<User> result = new BusinessResult<>();
        if (user == null) {
            result.setSuccess(false);
            result.setMessage("用户信息不能为空");
            return result;
        }
        if (isExistByUsername(user.getUsername()).getData()) {
            result.setSuccess(false);
            result.setMessage("用户已存在");
            return result;
        }
        if (TextUtils.isEmpty(user.getUsername()) || TextUtils.isEmpty(user.getPassword())) {
            result.setSuccess(false);
            result.setMessage("用户名或密码不能为空");
            return result;
        }
        if (!TextUtils.equals(user.getPassword(), confirmPassword)) {
            result.setSuccess(false);
            result.setMessage("两次密码不一致");
            return result;
        }
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", MD5Utils.md5(user.getPassword()));
        values.put("points", 0);
        long i = db.insert("_user", null, values);
        if (i > 0) {
            result.setSuccess(true);
            result.setMessage("注册成功");
            user.setId((int) i);
            result.setData(user);
        } else {
            result.setSuccess(false);
            result.setMessage("注册失败");
        }
        return result;
    }

    /**
     * 登录用户
     */
    public static BusinessResult<User> login(User user) {
        BusinessResult<User> result = new BusinessResult<>();
        if (user == null) {
            result.setSuccess(false);
            result.setMessage("用户信息不能为空");
            return result;
        }
        if (TextUtils.isEmpty(user.getUsername()) || TextUtils.isEmpty(user.getPassword())) {
            result.setSuccess(false);
            result.setMessage("用户名或密码不能为空");
            return result;
        }
        SQLiteDatabase db = SqliteUtils.getInstance().getReadableDatabase();
        Cursor cursor = db.query("_user", null, "username=? and password=?", new String[]{user.getUsername(), MD5Utils.md5(user.getPassword())}, null, null, null);
        if (cursor.moveToNext()) {
            result.setSuccess(true);
            result.setMessage("登录成功");
            user.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            user.setPoints(cursor.getInt(cursor.getColumnIndex("points")));
            result.setData(user);
        } else {
            result.setSuccess(false);
            result.setMessage("用户名或密码错误");
        }
        cursor.close();
        return result;
    }

    /**
     * 根据用户名查询是否存在该用户
     */
    public static BusinessResult<Boolean> isExistByUsername(String username) {
        SQLiteDatabase db = SqliteUtils.getInstance().getReadableDatabase();
        Cursor cursor = db.query("_user", null, "username=?", new String[]{username}, null, null, null);
        BusinessResult<Boolean> result = new BusinessResult<>();
        result.setSuccess(true);
        if (cursor.getCount() > 0) {
            result.setData(true);
            result.setMessage("用户已存在");
        } else {
            result.setData(false);
            result.setMessage("用户不存在");
        }
        cursor.close();
        return result;
    }

    /**
     * 更新用户积分
     */
    public static BusinessResult<Void> updatePoints(int userId, int points) {
        BusinessResult<Void> result = new BusinessResult<>();
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("points", points);
        int count = db.update("_user", values, "_id=?", new String[]{String.valueOf(userId)});
        if (count > 0) {
            result.setSuccess(true);
            result.setMessage("更新积分成功");
        } else {
            result.setSuccess(false);
            result.setMessage("更新积分失败");
        }
        return result;
    }

    public static BusinessResult<User> getByUserId(Integer userId) {
        BusinessResult<User> result = new BusinessResult<>();
        SQLiteDatabase db = SqliteUtils.getInstance().getReadableDatabase();
        Cursor cursor = db.query("_user", null, "_id=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setPoints(cursor.getInt(cursor.getColumnIndex("points")));
            result.setSuccess(true);
            result.setData(user);
        } else {
            result.setSuccess(false);
            result.setMessage("用户不存在");
        }
        cursor.close();
        return result;
    }
}
