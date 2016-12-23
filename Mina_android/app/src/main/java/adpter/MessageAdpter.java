package adpter;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tyhj.mina_android.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import myinterface.ExpendImage;
import object.Messge;
import tools.Defined;

/**
 * Created by Tyhj on 2016/10/14.
 */

public class MessageAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Messge> messges;
    private LayoutInflater mInflater;
    String headImage;
    private Context context;
    private ExpendImage expendImage;
    ImageLoader imageLoader;

    public MessageAdpter(List<Messge> messges, Context context,String headImage) {
        this.messges = messges;
        this.context = context;
        this.headImage=headImage;
        mInflater=LayoutInflater.from(context);
        imageLoader=ImageLoader.getInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        switch (viewType){
            case 0:
                view=mInflater.inflate(R.layout.item_chatlist_you,parent,false);
                viewHolder=new ViewHolder_you(view);
                return viewHolder;
            case 1:
                view=mInflater.inflate(R.layout.item_chatlist_me,parent,false);
                viewHolder=new ViewHolder_me(view);
                return viewHolder;
            case 2:
                view=mInflater.inflate(R.layout.item_chatlist_time,parent,false);
                viewHolder=new ViewHolder_time(view);
                return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Messge messge=messges.get(position);
        switch (messge.getType()){
            //对方
            case 0:
                ViewHolder_you holder_you= (ViewHolder_you) holder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder_you.headImage.setClipToOutline(true);
                    holder_you.headImage.setOutlineProvider(Defined.getOutline(true,10,0));
                }
                Picasso.with(context).load(headImage).error(R.mipmap.defult).into(holder_you.headImage);
                switch (messge.getContentType()){
                    case 0:
                        holder_you.text.setVisibility(View.VISIBLE);
                        holder_you.text.setText(messge.getContent());
                        holder_you.image.setVisibility(View.GONE);
                        holder_you.iv_voice.setVisibility(View.GONE);
                        break;
                    case 1:
                        holder_you.image.setVisibility(View.VISIBLE);
                        holder_you.text.setVisibility(View.GONE);
                        imageLoader.displayImage(messge.getContent(), holder_you.image, Defined.getOption());
                        holder_you.iv_voice.setVisibility(View.GONE);
                        holder_you.image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                expendImage.callBack(messge);
                            }
                        });
                        break;
                    case 2:
                        //语音
                        final boolean[] first = {true};
                        holder_you.iv_voice.setVisibility(View.VISIBLE);
                        holder_you.text.setVisibility(View.VISIBLE);
                        holder_you.image.setVisibility(View.GONE);
                        holder_you.text.setText(messge.getVoiceLength());
                        final MediaPlayer player = new MediaPlayer();
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        try {
                            player.setDataSource(messge.getContent());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                            }
                        });
                        holder_you.ll_voice.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!Defined.isIntenet(context))
                                    return;
                                if (player.isPlaying())
                                    return;
                                else if (first[0]) {
                                    player.prepareAsync();
                                    first[0] = false;
                                } else if (!first[0]) {
                                    player.start();
                                }
                            }
                        });
                        break;
                    case 3:
                        holder_you.text.setVisibility(View.GONE);
                        holder_you.image.setVisibility(View.GONE);
                        holder_you.iv_voice.setVisibility(View.GONE);
                        break;
                }
                break;
            //自己
            case 1:
                ViewHolder_me holder_me= (ViewHolder_me) holder;
                switch (messge.getContentType()){
                    case 0:
                        holder_me.text.setVisibility(View.VISIBLE);
                        holder_me.image.setVisibility(View.GONE);
                        holder_me.iv_voice.setVisibility(View.GONE);
                        holder_me.text.setText(messge.getContent());
                        Picasso.with(context).load(R.drawable.ic_sent).into(holder_me.status);
                        break;
                    case 1:
                        holder_me.text.setVisibility(View.GONE);
                        holder_me.image.setVisibility(View.VISIBLE);
                        holder_me.iv_voice.setVisibility(View.GONE);
                        Picasso.with(context).load(R.drawable.ic_sent).into(holder_me.status);
                        imageLoader.displayImage(messge.getContent(), holder_me.image, Defined.getOption());
                        holder_me.image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                expendImage.callBack(messge);
                            }
                        });
                        break;
                    case 2:
                        final boolean[] first = {true};
                        holder_me.text.setVisibility(View.VISIBLE);
                        holder_me.image.setVisibility(View.GONE);
                        holder_me.iv_voice.setVisibility(View.VISIBLE);
                        Picasso.with(context).load(R.drawable.ic_sent).into(holder_me.status);
                        holder_me.text.setText(messge.getVoiceLength());
                        final MediaPlayer player = new MediaPlayer();
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        try {
                            player.setDataSource(messge.getContent());
                        } catch (IOException e) {
                            messge.setSoundPath(null);
                            try {
                                player.setDataSource(messge.getContent());
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                            }
                        });
                        holder_me.ll_voice.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!Defined.isIntenet(context))
                                    return;
                                if (player.isPlaying())
                                    return;
                                else if (first[0]) {
                                    try {
                                        player.prepareAsync();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    first[0] = false;
                                } else if (!first[0]) {
                                    player.start();
                                }
                            }
                        });
                        break;
                    case 3:

                        break;
                }
                break;
            //时间
            case 2:
                ViewHolder_time viewHolder_time= (ViewHolder_time) holder;
                viewHolder_time.time.setText(messge.getTime());
                break;
        }
            updateTime();
    }

    private void updateTime() {
        for (int i = messges.size() - 1; i >= 0; i--) {
            if (messges.get(i).getType()== 0) {
                if (messges.get(i).getTime()==null||messges.get(i).getTime().contains("月"))
                    break;
                messges.get(i).setTime(Defined.getTime2(messges.get(i).getIntTime()));
            }
        }
    }


    @Override
    public int getItemCount() {
        return messges.size();
    }

    @Override
    public int getItemViewType(int position) {
        return messges.get(position).getType();
    }

    class ViewHolder_you extends RecyclerView.ViewHolder {
        TextView text;
        ImageView headImage, image, iv_voice;
        LinearLayout ll_voice;
        public ViewHolder_you(View itemView) {
            super(itemView);
            text= (TextView) itemView.findViewById(R.id.tv_yourtext);
            headImage= (ImageView) itemView.findViewById(R.id.iv_yourheadImage);
            image= (ImageView) itemView.findViewById(R.id.iv_yourimage);
            iv_voice= (ImageView) itemView.findViewById(R.id.iv_voice);
            ll_voice= (LinearLayout) itemView.findViewById(R.id.ll_voice);
        }
    }

    class ViewHolder_me extends RecyclerView.ViewHolder {
        TextView text;
        ImageView image, status, iv_voice;
        LinearLayout ll_voice;
        public ViewHolder_me(View itemView) {
            super(itemView);
            text= (TextView) itemView.findViewById(R.id.tv_mytext);
            image= (ImageView) itemView.findViewById(R.id.iv_myimage);
            iv_voice= (ImageView) itemView.findViewById(R.id.iv_voice);
            status= (ImageView) itemView.findViewById(R.id.iv_status);
            ll_voice= (LinearLayout) itemView.findViewById(R.id.ll_voice);
        }
    }

    class ViewHolder_time extends RecyclerView.ViewHolder{
        TextView time;
        public ViewHolder_time(View itemView) {
            super(itemView);
            time= (TextView) itemView.findViewById(R.id.tv_chatTiem);
        }
    }

    public List<Messge> getMessges() {
        return messges;
    }

    public void setExpendImage(ExpendImage expendImage){
        this.expendImage=expendImage;
    }
}
