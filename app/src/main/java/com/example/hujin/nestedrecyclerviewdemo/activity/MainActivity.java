package com.example.hujin.nestedrecyclerviewdemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.hujin.nestedrecyclerviewdemo.R;
import com.example.hujin.nestedrecyclerviewdemo.view.NestedChildRecyclerView;
import com.example.hujin.nestedrecyclerviewdemo.view.NestedRecyclerView;

public class MainActivity extends AppCompatActivity {
    private NestedRecyclerView mRvContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mRvContainer = findViewById(R.id.nrv_container);
        mRvContainer.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRvContainer.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        MyAdapter adapter = new MyAdapter(getApplicationContext());
        mRvContainer.setAdapter(adapter);
    }

    public class MyAdapter extends RecyclerView.Adapter {

        private Context mContext;

        public MyAdapter(Context context) {
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == 1){
                return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_child_item, parent,
                        false));
            }
            return new MyHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_child_item2, parent,
                    false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyHolder myHolder;
            MyHolder2 myHolder2;
            if(position == 8){
                myHolder = (MyHolder) holder;
                myHolder.mChild.setLayoutManager(new LinearLayoutManager(mContext));
                myHolder.mChild.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
                OtherAdapter otherAdapter = new OtherAdapter();
                myHolder.mChild.setAdapter(otherAdapter);
            }else {
                myHolder2 = (MyHolder2) holder;
            }


        }

        @Override
        public int getItemViewType(int position) {
            if (position == 8) {
                return 1;
            }
            return 0;
        }

        @Override
        public int getItemCount() {
            return 20;
        }

        class MyHolder extends RecyclerView.ViewHolder {
            NestedChildRecyclerView mChild;

            public MyHolder(View itemView) {
                super(itemView);
                mChild = itemView.findViewById(R.id.rv_child);
                mChild.setHanldeEvent(mRvContainer);
            }
        }

        class MyHolder2 extends RecyclerView.ViewHolder {

            public MyHolder2(View itemView) {
                super(itemView);
            }
        }
    }

    public static class OtherAdapter extends RecyclerView.Adapter{


        public OtherAdapter() {
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OtherHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_child_item2, parent,
                    false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            OtherHolder otherHolder = (OtherHolder) holder;
            otherHolder.mTvContent.setText("child" + position);

        }


        @Override
        public int getItemCount() {
            return 20;
        }

        class OtherHolder extends RecyclerView.ViewHolder {
           TextView mTvContent;
            public OtherHolder(View itemView) {
                super(itemView);
                itemView.setBackgroundColor(R.color.colorPrimary);
                mTvContent = itemView.findViewById(R.id.tv_content);
            }
        }

    }


}
