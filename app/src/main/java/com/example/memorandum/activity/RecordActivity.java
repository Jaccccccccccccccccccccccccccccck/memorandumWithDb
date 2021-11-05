package com.example.memorandum.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.memorandum.R;
import com.example.memorandum.fragment.RecordFragment;
import com.example.memorandum.fragment.RecordListFragment;
import com.example.memorandum.model.Record;

//主活动
public class RecordActivity extends AppCompatActivity implements RecordListFragment.Callbacks {

    private View mDetailsTitleView; //详情页标题布局视图
    private View mHomeTitleView;    //主页标题布局视图
    //定义页面跳转行为
    private final Integer GO_TO_DETAILS = 0; //主页->记录详情
    private final Integer ADD_NEW = 1;       //主页->添加记录
    private final Integer BACK_HOME = 2;     //详情/添加->主页

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ActionBar actionBar = getSupportActionBar();
        //获取标题栏布局视图
        mDetailsTitleView = findViewById(R.id.details_title);
        mHomeTitleView = findViewById(R.id.home_title);
        //为了使用自定义的标题栏，隐藏自带的标题栏
        if (actionBar != null)
            actionBar.hide();
        //获取fragment管理器来管理fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        //获取fragment
        Fragment fragment = new RecordListFragment();
        //添加fragment到activity中
        fragmentManager.beginTransaction().add(R.id.fragment_container,fragment).commit();
    }

    //新建记录时跳转到新建记录页面
    @Override
    public void onNewRecord() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container,new RecordFragment()).addToBackStack(null).commit();
        changeTitleBar(ADD_NEW);
    }

    //点击某记录时打开详情页面
    @Override
    public void onRecordSelect(int recordId) {
        FragmentManager fm = getSupportFragmentManager();
        RecordFragment recordFragment = RecordFragment.newInstance(recordId);
        fm.beginTransaction()
                .replace(R.id.fragment_container,recordFragment)
                .addToBackStack(null)
                .commit();
        changeTitleBar(GO_TO_DETAILS);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        changeTitleBar(BACK_HOME);
    }

    //改变标题栏
    public void changeTitleBar(int category){
        //从主页跳到详情页
        if (category == GO_TO_DETAILS){
            //切换为详情页标题栏
            mDetailsTitleView.setVisibility(View.VISIBLE);
            mHomeTitleView.setVisibility(View.INVISIBLE);
            //更换标题栏文本
            TextView titleText = mDetailsTitleView.findViewById(R.id.details_title_text);
            titleText.setText(R.string.details_title_text);
            loadReturnBtn();
        }

        //从主页新增记录
        if (category == ADD_NEW){
            //切换为新增页标题栏
            mDetailsTitleView.setVisibility(View.VISIBLE);
            mHomeTitleView.setVisibility(View.INVISIBLE);
            //更换标题栏文本
            TextView titleText = mDetailsTitleView.findViewById(R.id.details_title_text);
            titleText.setText(R.string.add_new_title_text);
            loadReturnBtn();
        }

        //返回主页
        if (category == BACK_HOME){
            //切换为主页标题栏
            mDetailsTitleView.setVisibility(View.INVISIBLE);
            mHomeTitleView.setVisibility(View.VISIBLE);
        }
    }

    //加载返回按钮
    @SuppressLint("ClickableViewAccessibility")
    public void loadReturnBtn(){
        ImageButton backBtn =  mDetailsTitleView.findViewById(R.id.back);
        backBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //更改为按下时的背景图片
                    backBtn.setImageDrawable(getResources().getDrawable(R.mipmap.ico_back_pressed,null));
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    //改为抬起时的图片
                    backBtn.setImageDrawable(getResources().getDrawable(R.mipmap.ico_back,null));
                    onBackPressed();
                }
                return false;
            }
        });
    }

}