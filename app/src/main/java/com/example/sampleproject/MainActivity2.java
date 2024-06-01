package com.example.sampleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


    //list handler for application.
public class MainActivity2 extends AppCompatActivity {






    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> setCollection;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_database);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.bottom_database)
                return true;
            else if (item.getItemId() == R.id.bottom_weather) {
                startActivity(new Intent(getApplicationContext(), WeatherActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_database) {
                startActivity(new Intent(getApplicationContext(), MainActivity2.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_calories) {
                startActivity(new Intent(getApplicationContext(), CaloriesActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });


        createGroupList();
        createCollection();
        expandableListView = findViewById(R.id.elvSets);
        expandableListAdapter = new MyExpandableListAdapter(this, groupList, setCollection);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if(lastExpandedPosition != -1 && groupPosition != lastExpandedPosition)
                {
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String selected = expandableListAdapter.getChild(groupPosition, childPosition).toString();
                Toast.makeText(getApplicationContext(),"Selected : " + selected, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void createCollection() {
        String[] backList = {"Roman chair"};
        String[] chestList = {"Incline barbell lifting"};
        String[] armsList = {"Hammer hands"};
        String[] shoulderList = {"Cup raisers"};
        String[] legsList = {"Squats"};

        setCollection = new HashMap<String, List<String>>();

        for (String group: groupList) {
            if (group.equals("Back")){
                loadChild(backList);
            } else if (group.equals("Chest")) {
                loadChild(chestList);
            } else if (group.equals("Arms")) {
                loadChild(armsList);
            } else if (group.equals("Shoulders")) {
                loadChild(shoulderList);
            } else if (group.equals("Legs")) {
                loadChild(legsList);
            }
            setCollection.put(group, childList);
        }
    }

    private void loadChild(String[] trainingList) {
        childList = new ArrayList<>();
        for (String set : trainingList)
        {
            childList.add(set);
        }
    }

    private void createGroupList() {
        groupList = new ArrayList<>();
        groupList.add("Back");
        groupList.add("Chest");
        groupList.add("Arms");
        groupList.add("Shoulders");
        groupList.add("Legs");
    }



}