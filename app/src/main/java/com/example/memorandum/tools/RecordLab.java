package com.example.memorandum.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.memorandum.db.record.RecordCursorWrapper;
import com.example.memorandum.db.record.RecordHelper;
import com.example.memorandum.db.record.RecordSchema;
import com.example.memorandum.model.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 模拟一个存放Record记录的地方，它基本具备增删改查的操作
 */
public class RecordLab {
    private static RecordLab sRecordLab;
    private static SQLiteDatabase sDatabase; //执行数据库操作的对象

    public static RecordLab getLab(Context context) {
        if (sRecordLab == null) {
            sRecordLab = new RecordLab(context);
        }
        return sRecordLab;
    }

    private RecordLab(Context context) {
        //创建数据库
        RecordHelper recordHelper = new RecordHelper(context);
        //打开一个可写入的数据库
        sDatabase = recordHelper.getWritableDatabase();
    }

    //从数据库中查询所有记录，返回记录列表
    public List<Record> getRecords() {
        List<Record> records = new ArrayList<>();//存储查询到的所有记录
        //执行sql查询语句得到包含查到数据的游标cursor
        Cursor cursor = sDatabase.query(RecordSchema.TABLE_NAME, null, null, null, null, null, null, null);
        //使用查询语句工具类得到数据列表
        RecordCursorWrapper cursorWrapper = new RecordCursorWrapper(cursor);
        cursorWrapper.moveToFirst();
        while (true) {
            if (cursorWrapper.isAfterLast())
                break;
            //将游标中的所有数据取出添加到records列表中
            Record record = cursorWrapper.getRecord();
            records.add(record);
            cursorWrapper.moveToNext();                   //移向下一条记录
        }
        cursor.close();
        //返回数据列表
        return records;
    }


    //添加新的记录
    public void addRecord(Record record) {
        ContentValues contentValues = getContentValues(record);
        //执行插入语句
        sDatabase.insert(RecordSchema.TABLE_NAME, null, contentValues);
    }

    //根据记录id查询记录
    public Record selectRecordById(int recordId) {
        Cursor cursor = sDatabase.query(RecordSchema.TABLE_NAME, null, "record_id=?"
                , new String[]{String.valueOf(recordId)}, null, null, null, null);
        RecordCursorWrapper cursorWrapper = new RecordCursorWrapper(cursor);
        cursorWrapper.moveToFirst();
        return !cursorWrapper.isAfterLast() ? cursorWrapper.getRecord() : new Record();
    }

    //更新记录
    public void updateRecord(Record record) {
        ContentValues contentValues = getContentValues(record);
        sDatabase.update(RecordSchema.TABLE_NAME, contentValues, "record_id=?"
                , new String[]{String.valueOf(record.getId())});
    }

    //删除记录
    public int deleteRecord(int recordId){
        return sDatabase.delete(RecordSchema.TABLE_NAME,RecordSchema.Cols.ID + "=?",new String[]{String.valueOf(recordId)});
    }

    //定义要插入或要更新的参数
    public ContentValues getContentValues(Record record) {
        ContentValues contentValues = new ContentValues();
        //由于record_id字段是自增，也不可改，因此在插入、更新操作时，不需要关注id的变化
        contentValues.put(RecordSchema.Cols.TITLE, record.getTitle());
        contentValues.put(RecordSchema.Cols.CONTENT, record.getContent());
        contentValues.put(RecordSchema.Cols.PICTURE, record.getPicture());
        if (record.getDeadLine() !=null)
            contentValues.put(RecordSchema.Cols.FINISH_DATE, record.getDeadLine().getTime());
        contentValues.put(RecordSchema.Cols.IMPORTANCE, record.getImportance());
        contentValues.put(RecordSchema.Cols.COMPLETED, record.isCompleted());
        return contentValues;
    }
}
