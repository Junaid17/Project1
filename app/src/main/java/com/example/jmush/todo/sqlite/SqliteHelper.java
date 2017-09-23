package com.example.jmush.todo.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jmush.todo.modal.ToDoData;
import com.example.jmush.todo.utils.Constants;


public class SqliteHelper extends SQLiteOpenHelper {


    // Create DB to store To Do Tasks
    public SqliteHelper(Context context) {
        super(context, Constants.DatabaseName, null, 1);

    }


    // Create Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "create table if not exists " + Constants.TableName + " ("
                 +Constants.ID+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Constants.TASK +  " TEXT,"
                + Constants.TodoPRIORITY + " TEXT,"
                + Constants.STATUS + " TEXT,"
                + Constants.NOTES + " TEXT,"
                + Constants.TDDATE + " TEXT )";
        db.execSQL(createTableQuery);
    }

    // Drop Table for new table creation
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TableName);
        onCreate(db);
    }

    // Insert into Table
    public boolean insertInto(ContentValues cv) {
        SQLiteDatabase db = this.getWritableDatabase();
        long results = db.insert(Constants.TableName, null, cv);
        if (results == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Select * from Table i.e get all data
    public Cursor selectAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + Constants.TableName +  " ORDER BY " +Constants.TDDATE + " ASC";
        Cursor result = db.rawQuery(query, null);
        return result;
    }

    // Update specific Task
    public Cursor updateTask(ToDoData td) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE "
                + Constants.TableName
                + " SET "
                + Constants.TASK + "='" + td.getToDoTaskDetails()
                + "', "
                + Constants.TodoPRIORITY + "='" + td.getToDoTaskPriority()
                + "', "
                + Constants.STATUS + "='" + td.getToDoTaskStatus()
                + "', "
                + Constants.NOTES + "='" + td.getToDoNotes()
                + "', "
                + Constants.TDDATE + "='" + td.getToDoDate()

                + "' WHERE " + Constants.ID + "='" + td.getToDoID() + "'";
        Cursor results = db.rawQuery(query, null);
        return results;
    }

    // Delete specific Task
    public Cursor deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + Constants.TableName
                + " WHERE "
                + Constants.ID + "='"
                + id + "'";
        Cursor result = db.rawQuery(query, null);
        return result;
    }
}
