package com.example.memorandum.db.record;

//定义数据库表结构描述
public class RecordSchema {
    public static final String TABLE_NAME="record";

    //定义行字段
    public static final class Cols{
        public static final String ID="record_id";
        public static final String TITLE="record_title";
        public static final String CONTENT="record_content";
        public static final String CREATE_DATE="record_create_date";
        public static final String FINISH_DATE="record_cutoff_date";
        public static final String COMPLETED="record_completed";
        public static final String PICTURE="record_picture";
        public static final String IMPORTANCE="record_importance";
    }
}
