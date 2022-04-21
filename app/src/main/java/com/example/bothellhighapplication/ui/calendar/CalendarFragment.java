package com.example.bothellhighapplication.ui.calendar;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.example.bothellhighapplication.R;
import static com.example.bothellhighapplication.ui.calendar.CalendarUtils.daysInMonthArray;
import static com.example.bothellhighapplication.ui.calendar.CalendarUtils.monthYearFromDate;
import org.w3c.dom.Text;

//Calendar Fragment displays monthly calendar
public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener{

    //Declaring the necessary variables
    private CalendarViewModel mViewModel;
    View root;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private TextView back;
    private TextView next;
    private Button weekly;

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    //Sets up the Calendar view
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.calendar_fragment, container, false);
        initWidgets(root);
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();
        return root;
    }

    //Initializes all the onClickListener for the widgets
    private void initWidgets(View root)
    {
        calendarRecyclerView = root.findViewById(R.id.calendarRecyclerView);
        monthYearText = root.findViewById(R.id.monthYearTV);
        back = root.findViewById(R.id.back_btn);
        next = root.findViewById(R.id.next_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                previousMonthAction(root);
            }
        });
        weekly = root.findViewById(R.id.weekly);
        weekly.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WeekViewActivity.class);
                startActivity(intent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                nextMonthAction(root);
            }
        });
    }

    //Sets up the View to show the month
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    //Updates view for previous month
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    //Updates view for previous month
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    //Action for when Clicked
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date)
    {
        if(date != null)
        {
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
        // TODO: Use the ViewModel
    }

}
