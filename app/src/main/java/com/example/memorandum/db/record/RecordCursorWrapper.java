package com.example.memorandum.db.record;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;
import android.widget.Toast;

import com.example.memorandum.model.Record;
import com.example.memorandum.tools.DateUtils;

import java.util.Date;

//辅助查询语句的工具类
public class RecordCursorWrapper extends CursorWrapper {
    //创建游标包装器
    public RecordCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    private static final String TAG = "RecordCursorWrapper";
    //将查询到的数据获取并封装到一个Record对象，将此封装好的对象返回
    public Record getRecord(){
        //拿到各项数据
        int id = getInt(getColumnIndex(RecordSchema.Cols.ID));
        String title = getString(getColumnIndex(RecordSchema.Cols.TITLE));
        String content = getString(getColumnIndex(RecordSchema.Cols.CONTENT));
        String picture = getString(getColumnIndex(RecordSchema.Cols.PICTURE));
        int completed = getInt(getColumnIndex(RecordSchema.Cols.COMPLETED));
        long finishDate = getLong(getColumnIndex(RecordSchema.Cols.FINISH_DATE));
        int importance = getInt(getColumnIndex(RecordSchema.Cols.IMPORTANCE));
        //将数据封装到Record对象
        Record record = new Record();
        record.setId(id);
        record.setTitle(title);
        record.setContent(content);
        record.setPicture(picture);
        record.setDeadLine(new Date(finishDate));
        record.setImportance(importance);
        record.setCompleted(completed != 0);
        return record;
    }
}
