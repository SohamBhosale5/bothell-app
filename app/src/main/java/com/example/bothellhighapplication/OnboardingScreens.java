package com.example.bothellhighapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnboardingScreens extends AppCompatActivity {
    private ViewPager mSlideViewPager;
    SharedPreferences sharedPreference;
    private LinearLayout mDotsLayout;
    private SliderAdapter sliderAdapter;
    Button bt_next;
    Button bt_back;
    private TextView[] mDots;
    private int mCurrentPage;
    Window window;
    boolean signupstatus=false;
    @Override
    //For creating onBoarding Screens View
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //For hiding title bar
        sharedPreference = getSharedPreferences("MyPref",MODE_PRIVATE);
        signupstatus = sharedPreference.getBoolean("IsSignInSuccessful",false);

        getSupportActionBar().hide();
        if(Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.bothell_blue));
        }
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_onboarding_screens);
        mSlideViewPager = (ViewPager) findViewById(R.id.ViewPage);
        mDotsLayout = (LinearLayout) findViewById(R.id.dots_layout);
        bt_next = (Button) findViewById(R.id.ButtonNext);
        bt_back = (Button) findViewById(R.id.ButtonBack);
        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn_text = bt_next.getText().toString();
                Intent intent;
                if(btn_text.equals("Finish")){
                    if(signupstatus) {
                        intent = new Intent(OnboardingScreens.this, SignUp.class);}

                    else{
                        intent = new Intent(OnboardingScreens.this, SignUp.class);
                    }
                    startActivity(intent);
                } else {
                    mSlideViewPager.setCurrentItem(mCurrentPage+1);
                }
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(mCurrentPage-1);
            }
        });
    }

    //Adding dots indicator on the bottom of the view
    public void addDotsIndicator(int position){
        mDots = new TextView[3];
        mDotsLayout.removeAllViews();
        for(int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.black));
            mDotsLayout.addView(mDots[i]);
        }
        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.bothell_blue));

        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            mCurrentPage = position;
            if(position == 0) {
                bt_next.setEnabled(true);
                bt_back.setEnabled(false);
                bt_back.setVisibility(View.INVISIBLE);
                bt_next.setText("Next");
            } else if(position== mDots.length-1) {
                bt_next.setEnabled(true);
                bt_back.setEnabled(true);
                bt_back.setVisibility(View.VISIBLE);
                bt_next.setText("Finish");
                bt_back.setText("Back");
            } else {
                bt_next.setEnabled(true);
                bt_back.setEnabled(true);
                bt_back.setVisibility(View.VISIBLE);
                bt_next.setText("Next");
                bt_back.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}