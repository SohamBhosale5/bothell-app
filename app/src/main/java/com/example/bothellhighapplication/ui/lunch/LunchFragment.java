package com.example.bothellhighapplication.ui.lunch;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bothellhighapplication.R;
import com.example.bothellhighapplication.ui.slideshow.Extracurricular;
import com.example.bothellhighapplication.ui.slideshow.RecyclerAdapter;

import java.util.ArrayList;
//Displays lunch menu
public class LunchFragment extends Fragment {

    private LunchViewModel mViewModel;
    View root;
    ArrayList<Menu> menuArrayList;
    RecyclerView recyclerView;
    public static LunchFragment newInstance() {
        return new LunchFragment();
    }

    //Creating view that will be shown
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        menuArrayList = new ArrayList<Menu>();
        setMenuArrayList();
        root = inflater.inflate(R.layout.fragment_lunch, container, false);
        recyclerView = root.findViewById(R.id.lunch_recycler);
        LunchAdapter adapter = new LunchAdapter(getContext(), menuArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

    //Entering data
    private void setMenuArrayList() {
        menuArrayList.add(new Menu("April 18", "Chicken Burger", R.drawable.burger, "Pizza", R.drawable.pizza_stone, "Fruits", R.drawable.seasonal_fruit, "Milk", R.drawable.milk));
        menuArrayList.add(new Menu("April 17", "Chicken", R.drawable.chicken, "Spaghetti", R.drawable.spaghetti, "Fries", R.drawable.french_fries, "Juice", R.drawable.juice));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = new ViewModelProvider(this).get(LunchViewModel.class);
        // TODO: Use the ViewModel
    }

}