package com.example.memorandum.db.record;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//记录存储助手，使用该类将数据存入到SQLite数据库
public class RecordHelper extends SQLiteOpenHelper {

    //定义当前数据库的版本号，当数据库表变更，需要重新创建表时，则升级该版本号
    private static final int VERSION = 1;
    //数据库表名
    private static final String TABLE_NAME = "record.db";
    public RecordHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, VERSION);
    }



    /**
     * 首次创建数据库时调用。
     * 执行创建表的SQL语句
     * 也可以执行初始数据插入操作。
     * @param db 数据库
     * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table "+ RecordSchema.TABLE_NAME +
                "("
                + RecordSchema.Cols.ID+" integer primary key autoincrement,"
                + RecordSchema.Cols.TITLE+" text,"
                + RecordSchema.Cols.CONTENT+" text,"
                + RecordSchema.Cols.PICTURE+" text,"
                + RecordSchema.Cols.CREATE_DATE + " text,"
                + RecordSchema.Cols.FINISH_DATE +" datetime,"
                + RecordSchema.Cols.IMPORTANCE + " integer,"
                + RecordSchema.Cols.COMPLETED +" integer"+
                ")";
        db.execSQL(sql);
    }

    /**
     *在需要升级数据库时调用
     *应该使用此方法删除表，添加表或执行其他任何操作
     *
     * @param db 数据库。
     * @param oldVersion 旧的数据库版本。
     * @param newVersion 新的数据库版本。
     **/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
