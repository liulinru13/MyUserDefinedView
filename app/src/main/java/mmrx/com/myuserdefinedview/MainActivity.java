package mmrx.com.myuserdefinedview;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import mmrx.com.myuserdefinedview.fragment.MyFragment;
import mmrx.com.myuserdefinedview.fragment.MyFragment02;
import mmrx.com.myuserdefinedview.fragment.MyFragment03;
import mmrx.com.myuserdefinedview.fragment.MyFragment04;


public class MainActivity extends Activity implements View.OnClickListener{
    //四个Fragment对象
    private MyFragment mFragment01;
    private MyFragment02 mFragment02;
    private MyFragment03 mFragment03;
    private MyFragment04 mFragment04;
    //底部的四个按钮
    private LinearLayout mBottomBn01;
    private LinearLayout mBottomBn02;
    private LinearLayout mBottomBn03;
    private LinearLayout mBottomBn04;
    //Fragment管理器
    private FragmentManager mFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        mFragmentManager = getFragmentManager();
        mBottomBn01 = (LinearLayout)findViewById(R.id.bottom_bar_01);
        mBottomBn02 = (LinearLayout)findViewById(R.id.bottom_bar_02);
        mBottomBn03 = (LinearLayout)findViewById(R.id.bottom_bar_03);
        mBottomBn04 = (LinearLayout)findViewById(R.id.bottom_bar_04);

        mBottomBn01.setOnClickListener(this);
        mBottomBn02.setOnClickListener(this);
        mBottomBn03.setOnClickListener(this);
        mBottomBn04.setOnClickListener(this);

        selectFragment(1);
    }


    @Override
    public void onClick(View v) {
        Log.v("--onclick--","v.getId() is " + v.getId());
        switch (v.getId()){
            case R.id.bottom_bar_01:
                selectFragment(1);
                break;
            case R.id.bottom_bar_02:
                selectFragment(2);
                break;
            case R.id.bottom_bar_03:
                selectFragment(3);
                break;
            case R.id.bottom_bar_04:
                selectFragment(4);
                break;
        }
    }
    //将bottom bar的按钮状态都清除了
    private void resetBottomBar(){
        ((Button)mBottomBn01.findViewById(R.id.bottom_bar_bn1))
                .setBackgroundColor(getResources().getColor(R.color.bottom_bar_normal));
        ((Button)mBottomBn01.findViewById(R.id.bottom_bar_bn1)).
                setTextColor(getResources().getColor(R.color.bottom_bar_text_normal));
        ((Button)mBottomBn02.findViewById(R.id.bottom_bar_bn2))
                .setBackgroundColor(getResources().getColor(R.color.bottom_bar_normal));
        ((Button)mBottomBn02.findViewById(R.id.bottom_bar_bn2)).
                setTextColor(getResources().getColor(R.color.bottom_bar_text_normal));
        ((Button)mBottomBn03.findViewById(R.id.bottom_bar_bn3))
                .setBackgroundColor(getResources().getColor(R.color.bottom_bar_normal));
        ((Button)mBottomBn03.findViewById(R.id.bottom_bar_bn3)).
                setTextColor(getResources().getColor(R.color.bottom_bar_text_normal));
        ((Button)mBottomBn04.findViewById(R.id.bottom_bar_bn4))
                .setBackgroundColor(getResources().getColor(R.color.bottom_bar_normal));
        ((Button)mBottomBn04.findViewById(R.id.bottom_bar_bn4)).
                setTextColor(getResources().getColor(R.color.bottom_bar_text_normal));
    }
    //将所有的Fragment隐藏
    private void hideAllFragment(FragmentTransaction transaction){
        if(mFragment01 != null)
            transaction.hide(mFragment01);
        if(mFragment02 != null)
            transaction.hide(mFragment02);
        if(mFragment03 != null)
            transaction.hide(mFragment03);
        if(mFragment04 != null)
            transaction.hide(mFragment04);
    }
    //选择显示哪个Fragment
    private void selectFragment(int index){
        //开启一个事务
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        //重置底部按钮的状态
        resetBottomBar();
        //隐藏所有的fragmet
        hideAllFragment(fragmentTransaction);
        switch (index){
            case 1:
                //设置bottom bar按钮的状态
                ((Button)mBottomBn01.findViewById(R.id.bottom_bar_bn1))
                    .setBackgroundColor(getResources().getColor(R.color.bottom_bar_pressed));
                ((Button)mBottomBn01.findViewById(R.id.bottom_bar_bn1))
                        .setTextColor(getResources().getColor(R.color.bottom_bar_text_pressed));
                if(mFragment01 == null) {
                    mFragment01 = new MyFragment();
                    fragmentTransaction.add(R.id.id_content,mFragment01);
                }else{
                    fragmentTransaction.show(mFragment01);
                }
                break;
            case 2:
                //设置bottom bar按钮的状态
                ((Button)mBottomBn02.findViewById(R.id.bottom_bar_bn2))
                        .setBackgroundColor(getResources().getColor(R.color.bottom_bar_pressed));
                ((Button)mBottomBn02.findViewById(R.id.bottom_bar_bn2))
                        .setTextColor(getResources().getColor(R.color.bottom_bar_text_pressed));
                if(mFragment02 == null) {
                    mFragment02 = new MyFragment02();
                    fragmentTransaction.add(R.id.id_content,mFragment02);
                }else{
                    fragmentTransaction.show(mFragment02);
                }
                break;
            case 3:
                //设置bottom bar按钮的状态
                ((Button)mBottomBn03.findViewById(R.id.bottom_bar_bn3))
                        .setBackgroundColor(getResources().getColor(R.color.bottom_bar_pressed));
                ((Button)mBottomBn03.findViewById(R.id.bottom_bar_bn3))
                        .setTextColor(getResources().getColor(R.color.bottom_bar_text_pressed));
                if(mFragment03 == null) {
                    mFragment03 = new MyFragment03();
                    fragmentTransaction.add(R.id.id_content,mFragment03);

                }else{
                    fragmentTransaction.show(mFragment03);
                }
                break;
            case 4:
                //设置bottom bar按钮的状态
                ((Button)mBottomBn04.findViewById(R.id.bottom_bar_bn4))
                        .setBackgroundColor(getResources().getColor(R.color.bottom_bar_pressed));
                ((Button)mBottomBn04.findViewById(R.id.bottom_bar_bn4))
                        .setTextColor(getResources().getColor(R.color.bottom_bar_text_pressed));
                if(mFragment04 == null) {
                    mFragment04 = new MyFragment04();
                    fragmentTransaction.add(R.id.id_content,mFragment04);
                }else{
                    fragmentTransaction.show(mFragment04);
                }
                break;
        }
        //提交事务
        fragmentTransaction.commit();
    }
}
