package com.example.memorandum.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.memorandum.R;
import com.example.memorandum.model.Record;
import com.example.memorandum.tools.DateUtils;
import com.example.memorandum.tools.RecordLab;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

//该fragment负责管理首页的记录列表展示
public class RecordListFragment extends Fragment {

    private RecyclerView mRecordListRV; //展示记录列表
    private FloatingActionButton mAddRecordBtn;//添加新记录的悬浮按钮
    private Callbacks mCallbacks;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_record, container, false);
        //拿到布局中RecyclerView控件和FloatActionButton控件
        mRecordListRV = view.findViewById(R.id.record_list_recyclerview);
        mAddRecordBtn = view.findViewById(R.id.add_record);
        mAddRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onNewRecord();
            }
        });
        //创建布局管理器(瀑布布局)
        StaggeredGridLayoutManager fragmentManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //为RecyclerView设置布局管理器
        mRecordListRV.setLayoutManager(fragmentManager);
        //设置适配器
        mRecordListRV.setAdapter(new RecordAdapter());
        return view;
    }

    //该适配器作为RecyclerView的适配器
    class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{

        private List<Record> mRecordList; //存储记录列表

        public RecordAdapter(){
            mRecordList = RecordLab.getLab(getActivity()).getRecords();
        }

        //创建ViewHolder类
        class ViewHolder extends RecyclerView.ViewHolder{

            private CheckBox mCompleted;    //是否完成
            private TextView mTitle;        //标题
            private TextView mContent;      //内容
            private TextView mCutoffDate;   //截止日期

            public Record mCurRecord;       //当前显示的记录
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                //加载列表子项布局中的控件
                mCompleted = itemView.findViewById(R.id.record_item_completed);
                mTitle = itemView.findViewById(R.id.record_item_title);
                mContent = itemView.findViewById(R.id.record_item_content);
                mCutoffDate = itemView.findViewById(R.id.record_item_cutoff_date);
                //设置点击事件
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallbacks.onRecordSelect(mCurRecord.getId());
                    }
                });
            }
        }

        @NonNull
        @Override
        public RecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //加载记录列表子项布局
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.record_item,parent,false);
            //返回创建的ViewHolder
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecordAdapter.ViewHolder holder, int position) {
            //获取当前显示的记录对象
            holder.mCurRecord = mRecordList.get(position);
            //显示当前记录的信息
            holder.mTitle.setText(holder.mCurRecord.getTitle());
            if (holder.mCurRecord.getDeadLine()!= null)
                holder.mCutoffDate.setText(DateUtils.DateToString(holder.mCurRecord.getDeadLine()));
            holder.mContent.setText(holder.mCurRecord.getContent());
            holder.mCompleted.setChecked(holder.mCurRecord.isCompleted());
            if (holder.mCompleted.isChecked())
                holder.mCompleted.setText(R.string.record_completed_text);
            else
                holder.mCompleted.setText(R.string.record_uncompleted_text);
            //设置警示的颜色
            if (holder.mCurRecord.getImportance() == 0){
                holder.mCompleted.setBackground(getResources().getDrawable(R.drawable.item_checked_background_relaxing,null));
            }else if (holder.mCurRecord.getImportance() == 1){
                holder.mCompleted.setBackground(getResources().getDrawable(R.drawable.item_checked_background_common,null));
            }else if (holder.mCurRecord.getImportance() == 2){
                holder.mCompleted.setBackground(getResources().getDrawable(R.drawable.item_checked_background_important,null));
            }

            holder.mCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //如果复选框被改变更新mRecord
                    holder.mCurRecord.setCompleted(isChecked);
                    //改变现实的文本
                    if (isChecked)
                        holder.mCompleted.setText(R.string.record_completed_text);
                    else
                        holder.mCompleted.setText(R.string.record_uncompleted_text);
                    //保存
                    RecordLab.getLab(getActivity()).updateRecord(holder.mCurRecord);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mRecordList.size();
        }
    }

    public interface Callbacks{
        //在新建备忘录的时候，触发
        void onNewRecord();

        /**
         * 在点击列表单元项时，触发
         * @param recordId 代表列表单元的id
         * */
        void onRecordSelect(int recordId);
    }

    /**
     * 当fragment与activity建立关联时调用
     * */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    /**
     * 当fragment与activity解除关联时调用
     * */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

}
