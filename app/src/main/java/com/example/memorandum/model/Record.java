package com.example.memorandum.model;

import java.util.Date;

/**
 * 记录类，包含了该条记录的编号、标题、内容(可插入图片)、记录创建日期
 * 执行日期、重要性、是否已完成 字段
 */
public class Record {
    private int id;                 //编号
    private String title;           //标题
    private String content;         //内容
    private String picture;         //图片
    private Date createDate;        //生成日期
    private Date deadLine;          //截止日期
    private int importance;         //重要程度 0轻松 1一般  2重要
    private boolean completed;      //是否已经完成


    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

