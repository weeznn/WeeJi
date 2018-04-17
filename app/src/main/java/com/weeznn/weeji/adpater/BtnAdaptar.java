package com.weeznn.weeji.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.weeznn.weeji.R;

/**
 * Created by weeznn on 2018/4/15.
 */

public class BtnAdaptar extends RecyclerView.Adapter<BtnAdaptar.BtnViewHolder> {

    private static final String TAG=BtnAdaptar.class.getSimpleName();

    private int[] imagelist=new int[]{
            R.drawable.ic_format_big,R.drawable.ic_format_small,R.drawable.ic_line_delete,
           R.drawable.ic_format_list, R.drawable.ic_line,
            R.drawable.ic_format_quote ,R.drawable.ic_code,R.drawable.ic_insert_photo,R.drawable.ic_link};

    private String[] strlist=new String[]{
            "## 标题","###### 正文","~~ 删除的文本 ~~",
            "* 列表","-------------------------------------------------\n",
            "> 引用","\n```\n\n插入代码块\n```\n"," ![]() ![图片文本](图片地址) ","  []() [链接文本](链接地址)  "};
    private LayoutInflater inflater;
    private ItemClickListener listener;


    public BtnAdaptar(Context context){
        inflater=LayoutInflater.from(context);
    }

    public void setItemClickListener(ItemClickListener l){
        this.listener=l;
    }
    @Override
    public BtnViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_btn,parent,false);
        return new BtnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BtnViewHolder holder, final int position) {
        holder.imageButton.setImageResource(imagelist[position]);
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClickListener(strlist[position]);
                Log.i(TAG,"click :"+position );

            }
        });
    }

    @Override
    public int getItemCount() {
        return imagelist.length;
    }

    public class BtnViewHolder extends RecyclerView.ViewHolder{
        public ImageButton imageButton;
        public BtnViewHolder(View itemView) {
            super(itemView);
            imageButton=itemView.findViewById(R.id.image);
        }
    }

    public interface ItemClickListener{
        void onItemClickListener(String string);
    }
}
