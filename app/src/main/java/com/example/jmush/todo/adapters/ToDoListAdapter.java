package com.example.jmush.todo.adapters;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jmush.todo.Presenter.CommonMethods;
import com.example.jmush.todo.R;
import com.example.jmush.todo.activity.MainActivity;
import com.example.jmush.todo.modal.ToDoData;
import com.example.jmush.todo.sqlite.SqliteHelper;
import com.example.jmush.todo.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jmush on 9/15/17.
 */
public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder> {
    List<ToDoData> ToDoDataArrayList = new ArrayList<ToDoData>();
    Context context;


    public ToDoListAdapter(ArrayList<ToDoData> toDoDataArrayList, Context context) {
        this.ToDoDataArrayList = toDoDataArrayList;
        this.context = context;
    }

    @Override
    public ToDoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cardlayout, parent, false);
        ToDoListViewHolder toDoListViewHolder = new ToDoListViewHolder(view, context);
        return toDoListViewHolder;
    }

    @Override
    public void onBindViewHolder(ToDoListViewHolder holder, final int position) {

        final ToDoData td = ToDoDataArrayList.get(position);
        holder.todoDetails.setText(td.getToDoTaskDetails());
        holder.todoNotes.setText(td.getToDoNotes());
        holder.todoDate.setText(td.getToDoDate());
        String tdStatus = td.getToDoTaskStatus();
        if (tdStatus.matches(Constants.COMPLETE)) {
            holder.todoDetails.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            holder.todoDetails.setText(td.getToDoTaskDetails());
        }
        String type = td.getToDoTaskPriority();
        int color = 0;
        if (type.matches(Constants.NORMAL)) {
            color = Color.parseColor("#009EE3");
        } else if (type.matches(Constants.LOW)) {
            color = Color.parseColor("#33AA77");
        } else {
            color = Color.parseColor("#FF7799");
        }
        ((GradientDrawable) holder.proprityColor.getBackground()).setColor(color);

        /**
         *  Deleted card on click of delete Button
         */

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = td.getToDoID();
                SqliteHelper mysqlite = new SqliteHelper(view.getContext());
                Cursor b = mysqlite.deleteTask(id);
                if (b.getCount() == 0) {
                    Toast.makeText(view.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            // Code here will run in UI thread
                            /* ToDoDataArrayList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,ToDoDataArrayList.size()); */
                            //notifyDataSetChanged();
                        }
                    });
                } else {
                    Toast.makeText(view.getContext(), "Deleted else", Toast.LENGTH_SHORT).show();
                }


            }
        });

        /**
         *  Editing card on click of edit Button
         */
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.custom_dailog);
                dialog.show();
                EditText todoText = (EditText) dialog.findViewById(R.id.input_task_desc);
                EditText todoNote = (EditText) dialog.findViewById(R.id.input_task_notes);
                final EditText tododate = (EditText) dialog.findViewById(R.id.date);
                CheckBox cb = (CheckBox) dialog.findViewById(R.id.checkbox);
                RadioButton rbHigh = (RadioButton) dialog.findViewById(R.id.high);
                RadioButton rbNormal = (RadioButton) dialog.findViewById(R.id.normal);
                RadioButton rbLow = (RadioButton) dialog.findViewById(R.id.low);

                if (td.getToDoTaskPriority().matches(Constants.NORMAL)) {
                    rbNormal.setChecked(true);
                } else if (td.getToDoTaskPriority().matches(Constants.LOW)) {
                    rbLow.setChecked(true);
                } else {
                    rbHigh.setChecked(true);
                }
                if (td.getToDoTaskStatus().matches(Constants.COMPLETE)) {
                    cb.setChecked(true);
                }

                todoText.setText(td.getToDoTaskDetails());
                todoNote.setText(td.getToDoNotes());
                tododate.setText(td.getToDoDate());
                Button save = (Button) dialog.findViewById(R.id.btn_save);
                Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);

                Button datePicker= (Button) dialog.findViewById(R.id.btnDate);

                datePicker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         *  Creating instance of CommonMethods class and getting the
                         *  DatePickerDialog
                         */

                        CommonMethods commonMethods=new CommonMethods();
                        commonMethods.datePicker(tododate,view.getContext());
                    }
                });

                /**
                 *  Cancel dialog on click of cancel dialog
                 */

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                /**
                 *  Save all fields of dialog on click of save dialog
                 */
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText todoText = (EditText) dialog.findViewById(R.id.input_task_desc);
                        EditText todoNote = (EditText) dialog.findViewById(R.id.input_task_notes);
                        EditText tododate = (EditText) dialog.findViewById(R.id.date);
                        CheckBox cb = (CheckBox) dialog.findViewById(R.id.checkbox);
                        if (todoText.getText().length() >= 2) {
                            RadioGroup proritySelection = (RadioGroup) dialog.findViewById(R.id.toDoRG);
                            String RadioSelection = new String();
                            if (proritySelection.getCheckedRadioButtonId() != -1) {
                                int id = proritySelection.getCheckedRadioButtonId();
                                View radiobutton = proritySelection.findViewById(id);
                                int radioId = proritySelection.indexOfChild(radiobutton);
                                RadioButton btnRadio = (RadioButton) proritySelection.getChildAt(radioId);
                                RadioSelection = (String) btnRadio.getText();
                            }
                            ToDoData updateTd = new ToDoData();
                            updateTd.setToDoID(td.getToDoID());
                            updateTd.setToDoTaskDetails(todoText.getText().toString());
                            updateTd.setToDoTaskPriority(RadioSelection);
                            updateTd.setToDoNotes(todoNote.getText().toString());
                            updateTd.setToDoDate(tododate.getText().toString());
                            if (cb.isChecked()) {
                                updateTd.setToDoTaskStatus(Constants.COMPLETE);
                            } else {
                                updateTd.setToDoTaskStatus(Constants.INCOMPLETE);
                            }
                            SqliteHelper mysqlite = new SqliteHelper(view.getContext());

                            /**
                             *  Updating values to database
                             */
                            Cursor cursor = mysqlite.updateTask(updateTd);
                            ToDoDataArrayList.set(position, updateTd);
                            if (cursor.getCount() == 0) {
                                //Toast.makeText(view.getContext(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Code here will run in UI thread
                                        notifyDataSetChanged();
                                    }
                                });
                                dialog.hide();
                            } else {


                                dialog.hide();

                            }

                        } else {
                            Toast.makeText(view.getContext(), "Please enter To Do Task", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

    // Returns size of todoList
    @Override
    public int getItemCount() {
        return ToDoDataArrayList.size();
    }

    // Holder class which holds the contents of RecyclerView

    public class ToDoListViewHolder extends RecyclerView.ViewHolder {
        TextView todoDetails, todoNotes,todoDate;
        ImageButton proprityColor;
        ImageView edit, deleteButton;
        ToDoData toDoData;

        public ToDoListViewHolder(View view, final Context context) {
            super(view);
            todoDetails = (TextView) view.findViewById(R.id.toDoTextDetails);
            todoNotes = (TextView) view.findViewById(R.id.toDoTextNotes);
            todoDate = (TextView) view.findViewById(R.id.todoDate);
            proprityColor = (ImageButton) view.findViewById(R.id.typeCircle);
            edit = (ImageView) view.findViewById(R.id.edit);
            deleteButton = (ImageView) view.findViewById(R.id.delete);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
    }
}
