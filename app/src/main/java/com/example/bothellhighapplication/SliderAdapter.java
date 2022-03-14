package com.example.bothellhighapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    public SliderAdapter(Context context){
        this.context = context;

    }

    public int[] slider_images = {
            R.drawable.slider3, R.drawable.slider2, R.drawable.slider3
    };

    public String[] slider_heading = {
            "Capture", "Upload", "Results"
    };

    public String[] slider_description = {
            "Take a picture of your skin to check what disease you have.",
            "Upload image for the app to analyze",
            "Get a thorough analysis of your condition, along with possible treatment"
    };

    @Override
    public int getCount() {
        return slider_heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position){
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        TextView textView = (TextView) view.findViewById(R.id.heading);
        TextView descriptionView = (TextView) view.findViewById(R.id.description);
        imageView.setImageResource(slider_images[position]);
        textView.setText(slider_heading[position]);
        descriptionView.setText(slider_description[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object){
        container.removeView((RelativeLayout)object);
    }
}
