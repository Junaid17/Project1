package com.example.jmush.todo.activity;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jmush.todo.Presenter.CommonMethods;
import com.example.jmush.todo.R;
import com.example.jmush.todo.adapters.ToDoListAdapter;
import com.example.jmush.todo.modal.ToDoData;
import com.example.jmush.todo.sqlite.SqliteHelper;
import com.example.jmush.todo.utils.Constants;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private FloatingActionButton addTask;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private  ArrayList<ToDoData> tdd = new ArrayList<>();
    private SqliteHelper mysqlite;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_s);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        addTask = (FloatingActionButton) findViewById(R.id.imageButton);

        //  Refereshing page on on swiping top to bottom
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        adapter = new ToDoListAdapter(tdd, getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Pupulating data to recyclerView
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.accent),
                getResources().getColor(R.color.divider));
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                updateCardView();
            }
        });

        /**
         * onClick of action Menu
         */
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Open CustomDailog and initialising all fields
                 */
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.custom_dailog);
                dialog.show();
                Button save = (Button) dialog.findViewById(R.id.btn_save);
                Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);

                CheckBox cb = (CheckBox) dialog.findViewById(R.id.checkbox);
                TextView tvstatus = (TextView) dialog.findViewById(R.id.status);
                cb.setVisibility(View.GONE);
                tvstatus.setVisibility(View.GONE);


                final EditText tododate = (EditText) dialog.findViewById(R.id.date);
                Button datePicker= (Button) dialog.findViewById(R.id.btnDate);

//                OnClick of datepicker get datePicker from CommonMethods Class

                datePicker.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                      CommonMethods commonMethods=new CommonMethods();
                        commonMethods.datePicker(tododate,MainActivity.this);
                    }
                });

/**
 *  Cancel dialog on click of dialog
 */

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                /**
                 *  Save Details on click of save
                 */
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText todoText = (EditText) dialog.findViewById(R.id.input_task_desc);
                        EditText todoNotes = (EditText) dialog.findViewById(R.id.input_task_notes);
                        EditText tododate = (EditText) dialog.findViewById(R.id.date);


                        if (todoText.getText().length() >= 2) {
                            RadioGroup proritySelection = (RadioGroup) dialog.findViewById(R.id.toDoRG);
                            String RadioSelection = new String();
                            if (proritySelection.getCheckedRadioButtonId() != -1) {
                                int id = proritySelection.getCheckedRadioButtonId();
                                View radiobutton = proritySelection.findViewById(id);
                                int radioId = proritySelection.indexOfChild(radiobutton);
                                RadioButton btn = (RadioButton) proritySelection.getChildAt(radioId);
                                RadioSelection = (String) btn.getText();
                            }

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("ToDoTaskDetails", todoText.getText().toString());
                            contentValues.put("ToDoTaskPriority", RadioSelection);
                            contentValues.put("ToDoTaskStatus", Constants.INCOMPLETE);
                            contentValues.put("ToDoNotes", todoNotes.getText().toString());
                            contentValues.put("ToDoDate", tododate.getText().toString());
                            // Creating instance of SqliteHelper
                            mysqlite = new SqliteHelper(getApplicationContext());

                            //Inserting all content vales to database
                            Boolean b = mysqlite.insertInto(contentValues);
                            if (b) {
                                dialog.hide();
                                updateCardView();
                            } else {
                                Toast.makeText(getApplicationContext(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter To Do Task", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

            }
        });
    }

    /**
     *  Update cardview values on Refresh
     */

    public void updateCardView() {
        swipeRefreshLayout.setRefreshing(true);
        mysqlite = new SqliteHelper(getApplicationContext());
        Cursor result = mysqlite.selectAllData();
        if (result.getCount() == 0) {
            tdd.clear();
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "No Tasks", Toast.LENGTH_SHORT).show();
        } else {
            tdd.clear();
            adapter.notifyDataSetChanged();
            while (result.moveToNext()) {
                ToDoData tddObj = new ToDoData();
                tddObj.setToDoID(result.getInt(0));
                tddObj.setToDoTaskDetails(result.getString(1));
                tddObj.setToDoTaskPriority(result.getString(2));
                tddObj.setToDoTaskStatus(result.getString(3));
                tddObj.setToDoNotes(result.getString(4));
                tddObj.setToDoDate(result.getString(5));

                tdd.add(tddObj);
            }
            adapter.notifyDataSetChanged();
        }

//        Refereshing page on on swiping top to bottom
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        updateCardView();
    }
}
