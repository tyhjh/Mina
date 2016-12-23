package com.example.tyhj.mina_android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import adpter.LinkManAdpter;
import fragement.MyMenuFragment;
import myviews.waveNavigation.FlowingView;
import myviews.waveNavigation.LeftDrawerLayout;
import object.LinkMan;
import object.Messge;
import object.User;
import tools.ActivityCollector;
import tools.SavaDate;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    List<LinkMan> linkMens=new ArrayList<LinkMan>();
    private LeftDrawerLayout mLeftDrawerLayout;

    String TAG;

    LinkManAdpter manAdpter;

    MsgBoradCastReceiver msgBoradCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG=getLocalClassName();
        ActivityCollector.addActivity(this, getClass());
    }

    @ViewById
    RecyclerView rcly_qun;

    @Click(R.id.iv_showMenu)
    void showMenu(){
        mLeftDrawerLayout.openDrawer();
    }

    @AfterViews
    void afterView(){
        initdrawerLayout();
        manAdpter=new LinkManAdpter(linkMens,this);
        rcly_qun.setAdapter(manAdpter);
        rcly_qun.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcly_qun.setItemAnimator(new DefaultItemAnimator());
        signBroadCast();
        getFriends();
    }

    @Background
    void getFriends(){
        linkMens.clear();
        linkMens.addAll(User.getFriends(this));
        updateView(-1);
        User.getNewMsg();
        geUpdateView();
    }

    private void initdrawerLayout() {
        mLeftDrawerLayout = (LeftDrawerLayout) findViewById(R.id.id_drawerlayout);
        FragmentManager fm = getSupportFragmentManager();
        MyMenuFragment mMenuFragment = (MyMenuFragment) fm.findFragmentById(R.id.id_container_menu);
        FlowingView mFlowingView = (FlowingView) findViewById(R.id.sv);
        if (mMenuFragment == null) {
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment = new MyMenuFragment()).commit();
        }
        mLeftDrawerLayout.setFluidView(mFlowingView);
        mLeftDrawerLayout.setMenuFragment(mMenuFragment);
    }

    @UiThread()
    void updateView(int from){
        if(from==-1||linkMens.size()<2) {
            manAdpter.notifyDataSetChanged();
        }else{
            manAdpter.notifyItemMoved(from,0);
        }
        //Log.e("MainActivity","执行了2："+System.currentTimeMillis());
    }

    //广播
    class MsgBoradCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Messge messge= (Messge) intent.getSerializableExtra("msg_single");
            messge.setRead(false);
            Log.e(TAG,"收到广播了");
            for(int i=0;i<linkMens.size();i++){
                if(linkMens.get(i).getId().equals(messge.getFrom())){
                    if(linkMens.get(i).getMessges()==null){
                        List<Messge> messges=new ArrayList<Messge>();
                        messges.add(messge);
                        linkMens.get(i).setMessges(messges);
                    }else
                        linkMens.get(i).getMessges().add(messge);
                    manAdpter.notifyItemChanged(i);
                    break;
                }

            }
        }
    }

    //注册广播
    private void signBroadCast() {
        msgBoradCastReceiver=new MainActivity.MsgBoradCastReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("boradcast.action.GETMESSAGE");
        intentFilter.setPriority(900);
        registerReceiver(msgBoradCastReceiver,intentFilter);
    }

    //获取更新后的列表
    @UiThread
    void geUpdateView(){
        linkMens.clear();
        linkMens.addAll(User.getUpdate());
        manAdpter.notifyDataSetChanged();
        User.upDateView(this,linkMens);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(msgBoradCastReceiver);
        ActivityCollector.removeActivity(this);
        User.upDateView(this,linkMens);
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        geUpdateView();
        super.onRestart();
    }
}
