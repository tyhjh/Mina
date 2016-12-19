package com.example.tyhj.mina_android;

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
import object.User;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    List<LinkMan> linkMens=new ArrayList<LinkMan>();
    private LeftDrawerLayout mLeftDrawerLayout;

    LinkManAdpter manAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        getFriends();
    }

    @Background
    void getFriends(){
        List<LinkMan> linkMen=User.getFriends(this);
        if(linkMen!=null)
            for(int i=0;i<linkMen.size();i++){
                linkMens.add(linkMen.get(i));
            }
        updateView();
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
    void updateView(){
        manAdpter.notifyDataSetChanged();
    }
}
