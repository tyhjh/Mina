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
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        linkMens.clear();
        List<LinkMan> linkMen=User.getFriends(this);
        if(linkMen!=null)
            for(int i=0;i<linkMen.size();i++){
                linkMens.add(linkMen.get(i));
            }
        updateView(-1);
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
        User.upDateView(this,linkMens);
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
                    LinkMan linkMan=linkMens.get(i);
                    linkMens.remove(i);
                    linkMan.getMessges().add(messge);
                    linkMens.add(0,linkMan);
                    updateView(i);
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

    @Override
    protected void onDestroy() {
        unregisterReceiver(msgBoradCastReceiver);
        ActivityCollector.removeActivity(this);
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getFriends();
    }
}
