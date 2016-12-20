package adpter;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.tyhj.mina_android.R;
import com.example.tyhj.mina_android.SendMessage_;
import com.squareup.picasso.Picasso;

import java.util.List;

import object.LinkMan;
import tools.Defined;


/**
 * Created by Tyhj on 2016/10/14.
 */

public class LinkManAdpter extends RecyclerView.Adapter<LinkManAdpter.MyViewHolder> {
    List<LinkMan> linkMans;
    private LayoutInflater mInflater;
    private Context context;


    public LinkManAdpter(List<LinkMan> linkMans, Context context) {
        this.linkMans = linkMans;
        this.mInflater =LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.item_linkman,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final LinkMan linkMan=linkMans.get(position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.iv_headImage.setClipToOutline(true);
            holder.iv_headImage.setOutlineProvider(Defined.getOutline(true,10,0));
        }

        if(linkMan.getHeadImage()==null||linkMan.getHeadImage().equals("null"))
            Picasso.with(context).load(R.mipmap.defult).into(holder.iv_headImage);
        else
            Picasso.with(context).load(linkMan.getHeadImage()).into(holder.iv_headImage);

        holder.tv_group_name.setText(linkMan.getName());
        if(linkMan.getUnRead()==0)
            holder.tv_msgCount.setVisibility(View.GONE);
        else {
            holder.tv_msgCount.setVisibility(View.VISIBLE);
            holder.tv_msgCount.setText(linkMan.getUnRead() + "");
        }

        holder.tv_send_time.setText(linkMan.getTime());
        holder.tv_who_send.setText(linkMan.getWho());
                int type = linkMan.getType();
                switch (type) {
                    case 0:
                        holder.iv_type.setVisibility(View.GONE);
                        break;
                    case 1:
                        holder.iv_type.setImageResource(R.drawable.ic_camera_24dp);
                        holder.tv_text.setText("图片");
                        break;
                    case 2:
                        holder.iv_type.setImageResource(R.drawable.ic_mic_24dp);
                        holder.tv_text.setText("语音");
                        break;
                    case 3:
                        holder.iv_type.setImageResource(R.drawable.ic_file_24dp);
                        holder.tv_text.setText("文件");
                        break;
                }

        holder.ll_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, SendMessage_.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("man",linkMan);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return linkMans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_headImage, iv_type;
        ImageView ib_status;
        LinearLayout ll_group;
        TextView tv_group_name, tv_send_time, tv_who_send, tv_text, tv_msgCount;
        public MyViewHolder(View view) {
            super(view);
            ll_group = (LinearLayout) view.findViewById(R.id.ll_group);
            iv_headImage = (ImageView) view.findViewById(R.id.iv_headImage);
            iv_type = (ImageView) view.findViewById(R.id.iv_type);
            ib_status = (ImageView) view.findViewById(R.id.iv_status);
            tv_group_name = (TextView) view.findViewById(R.id.tv_group_name);
            tv_send_time = (TextView) view.findViewById(R.id.tv_send_time);
            tv_who_send = (TextView) view.findViewById(R.id.tv_who_send);
            tv_text = (TextView) view.findViewById(R.id.tv_text);
            tv_msgCount = (TextView) view.findViewById(R.id.tv_msgCount);
        }
    }

}
