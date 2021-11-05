package com.example.memorandum.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.memorandum.R;
import com.example.memorandum.model.Record;
import com.example.memorandum.tools.DateUtils;
import com.example.memorandum.tools.RecordLab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;


//该Fragment负责管理记录详情页的展示
public class RecordFragment extends Fragment {
    private static final String ARG_RECORD_ID = "record_id";
    private View view;
    private boolean isCreate;       //是否是创建新记录
    private EditText mTitle;        //标题
    private EditText mContent;      //内容
    private ImageButton mCutoffDate;//设置截止日期
    private TextView mCutoffDateText;//显示截止日期
    private Spinner mImportance;    //重要程度
    private CheckBox mCompleted;    //执行情况
    private Record mRecord;         //存储点击列表中的某条记录的对象
    private ImageButton mCameraCtrl;  //拍照控制按钮
    private ImageButton mAlbumCtrl;   //相册控制按钮
    private ImageButton mDeleteCtrl;  //记录删除按钮
    private ImageView mPictureContentView;  //内容中的图片显示
    private Uri mImageUri;  //相机拍照后的存储路径
    //定义行为的状态码
    private static final int CODE_TAKE_PHOTO = 1; //使用相机拍照
    private static final int CODE_TAKE_ALBUM = 2; //选取相册图片
    private boolean mContextIsChanged = false;

    /**
     * 由托管的activity来调用，代替new RecordFragment(),因为要传参数
     */
    public static RecordFragment newInstance(int recordId) {
        RecordFragment recordFragment = new RecordFragment();
        //创建fragment保存键值对的类
        Bundle args = new Bundle();
        //存键值对
        args.putInt(ARG_RECORD_ID, recordId);
        //把键值对的Bundle对象放到fragment
        recordFragment.setArguments(args);
        return recordFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        isCreate = args == null;
        if (isCreate) {//说明跳转过来的是新增操作
            mRecord = new Record();
        } else {//说明跳转过来的是更新操作
            int recordId = args.getInt(ARG_RECORD_ID);
            mRecord = RecordLab.getLab(getActivity()).selectRecordById(recordId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //加载布局
        view = inflater.inflate(R.layout.fragment_record, container, false);
        loadTitleET(view);
        loadContentET(view);
        loadCompletedCheckbox(view);
        loadCutoffDateBtn(view);
        loadImportanceSpinner(view);
        loadPictureContentView(view);
        loadAlbumCtrl(view);
        loadCameraCtrl(view);
        loadDeleteCtrl(view);
        return view;
    }

    //加载布局中的标题控件
    public void loadTitleET(View view) {
        mTitle = view.findViewById(R.id.record_title);
        mTitle.setText(mRecord.getTitle());
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mContextIsChanged = true;
                //如果标题被改变更新标题到mRecord
                mRecord.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //加载布局中的内容控件
    public void loadContentET(View view) {
        mContent = view.findViewById(R.id.record_content);
        mContent.setText(mRecord.getContent());
        mContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mContextIsChanged = true;
                //如果内容被改变更新内容到mRecord
                mRecord.setContent(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    //加载布局中截止日期控件
    public void loadCutoffDateBtn(View view) {
        mCutoffDate = view.findViewById(R.id.record_cutoff_date);
        mCutoffDateText = view.findViewById(R.id.record_cutoff_date_text);
        if (mRecord.getDeadLine()!=null){
            StringBuilder sb = new StringBuilder("截止日期:").append(DateUtils.DateToString(mRecord.getDeadLine()));
            mCutoffDateText.setText(sb);
        }

        mCutoffDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                //获取日历的一个实例，里面包含了当前的年月日
                Calendar calendar = Calendar.getInstance();
                //构建一个日期对话框，该对话框已经集成了日期选择器
                //DatePickerDialog的第二个构造参数指定了日期监听器
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //拼接字符
                        sb.append(year).append("年").append(month + 1).append("月").append(dayOfMonth).append("日").append(" ");
                        //选择具体的世界
                        TimePickerDialog dialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //拼接字符
                                sb.append(hourOfDay).append(":");
                                if (minute <= 9)
                                    sb.append("0").append(minute).append(":00");
                                else
                                    sb.append(minute).append(":00");
                                //保存到record对象
                                mRecord.setDeadLine(DateUtils.StringToDate(sb.toString()));
                                sb.insert(0,"截止日期:");
                                Toast.makeText(getContext(),sb,Toast.LENGTH_SHORT).show();
                                //显示截止日期
                                mCutoffDateText.setText(sb);
                            }
                        },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true);//true表示使用二十四小时制
                        //把时间对话框显示在界面上
                        dialog.show();
                    }
                },      calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                //把日期对话框显示在界面上
                dialog.show();

            }
        });
    }

    //加载布局中重要程度列表控件
    public void loadImportanceSpinner(View view) {
        mImportance = view.findViewById(R.id.importance);
        mImportance.setSelection(mRecord.getImportance());
        mImportance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isCreate)
                    mContextIsChanged = true;
                mRecord.setImportance(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //加载布局中执行情况的控件
    public void loadCompletedCheckbox(View view) {
        mCompleted = view.findViewById(R.id.is_completed);
        mCompleted.setChecked(mRecord.isCompleted());
        if (mCompleted.isChecked())
            mCompleted.setText(R.string.record_completed_text);
        else
            mCompleted.setText(R.string.record_uncompleted_text);
        mCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isCreate)
                    mContextIsChanged = true;
                //如果复选框被改变更新mRecord
                mRecord.setCompleted(isChecked);
                //改变现实的文本
                if (isChecked)
                    mCompleted.setText(R.string.record_completed_text);
                else
                    mCompleted.setText(R.string.record_uncompleted_text);
            }
        });
    }

    //加载拍照按钮
    public void loadCameraCtrl(View v) {
        mCameraCtrl = v.findViewById(R.id.take_photos);
        mCameraCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建图片的存储路径
                String imageName = String.valueOf(System.currentTimeMillis());
                //获取应用缓存目录
                File storePath = getActivity().getExternalCacheDir();
                /**
                 * 通过Context.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
                 * 通过Context.getExternalCacheDir()方法可以获取到 SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
                 */
                try {
                    File file = File.createTempFile(imageName, ".jpg", storePath);
                    mImageUri = FileProvider.getUriForFile(getActivity(), "com.example.memorandum.fragment.fileprovide", file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(intent, CODE_TAKE_PHOTO);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_TAKE_PHOTO:
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(mImageUri));
                    mPictureContentView.setImageBitmap(bitmap);
                    mPictureContentView.setVisibility(View.VISIBLE);
                    mRecord.setPicture(mImageUri.toString());
                    mContextIsChanged = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case CODE_TAKE_ALBUM:
                if (data == null)
                    return;
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                    mPictureContentView.setImageBitmap(bitmap);
                    mPictureContentView.setVisibility(View.VISIBLE);
                    mRecord.setPicture(uri.toString());
                    mContextIsChanged = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    //加载打开相册按钮
    public void loadAlbumCtrl(View v) {
        mAlbumCtrl = v.findViewById(R.id.add_image);
        mAlbumCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, CODE_TAKE_ALBUM);
            }
        });
    }


    //加载删除按钮
    public void loadDeleteCtrl(View v) {
        mDeleteCtrl = v.findViewById(R.id.record_delete);
        mDeleteCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(getContext());
                normalDialog.setIcon(R.mipmap.ico_notices);
                normalDialog.setTitle("提示");
                normalDialog.setMessage(R.string.record_delete_dialog_is_confirm);
                normalDialog.setPositiveButton(R.string.record_delete_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int i = RecordLab.getLab(getActivity()).deleteRecord(mRecord.getId());
                                if (i > 0) {
                                    Toast.makeText(getContext(), R.string.record_delete_success, Toast.LENGTH_SHORT).show();
                                    //删除后回到首页
                                    getActivity().onBackPressed();

                                } else {
                                    Toast.makeText(getContext(), R.string.record_delete_failed, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                normalDialog.setNegativeButton(R.string.record_delete_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                normalDialog.show();  //显示对话框


            }
        });
    }

    //加载图片内容显示按钮
    public void loadPictureContentView(View v) {
        mPictureContentView = v.findViewById(R.id.content_image_view);
        //如果记录中有图片就加载图片
        if (mRecord.getPicture() != null) {
            try {
                Uri uri = Uri.parse(mRecord.getPicture());
                InputStream is = getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                mPictureContentView.setImageBitmap(bitmap);
                mPictureContentView.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }


    //当fragment销毁时保存数据，即当按下back按钮时
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isCreate && mContextIsChanged) {
            RecordLab.getLab(getActivity()).addRecord(mRecord);
        } else {
            RecordLab.getLab(getActivity()).updateRecord(mRecord);
        }
    }

}
