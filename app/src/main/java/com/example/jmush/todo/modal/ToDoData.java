package com.example.jmush.todo.modal;


public class ToDoData {
    int ToDoID;
    String ToDoTaskDetails;

    public String getToDoTaskPriority() {
        return ToDoTaskPriority;
    }

    public void setToDoTaskPriority(String toDoTaskPriority) {
        ToDoTaskPriority = toDoTaskPriority;
    }

    String ToDoTaskPriority;
    String ToDoTaskStatus;
    String ToDoNotes;
    String ToDoDate;

    public String getToDoDate() {
        return ToDoDate;
    }

    public void setToDoDate(String toDoDate) {
        ToDoDate = toDoDate;
    }



    public int getToDoID() {
        return ToDoID;
    }

    public void setToDoID(int toDoID) {
        ToDoID = toDoID;
    }

    public String getToDoTaskDetails() {
        return ToDoTaskDetails;
    }

    public void setToDoTaskDetails(String toDoTaskDetails) {
        ToDoTaskDetails = toDoTaskDetails;
    }

    public String getToDoTaskStatus() {
        return ToDoTaskStatus;
    }

    public void setToDoTaskStatus(String toDoTaskStatus) {
        ToDoTaskStatus = toDoTaskStatus;
    }

    public String getToDoNotes() {
        return ToDoNotes;
    }

    public void setToDoNotes(String toDoNotes) {
        ToDoNotes = toDoNotes;
    }

    @Override
    public String toString() {
        return "ToDoData {id-" + ToDoID + ", " +
                "taskDetails-" + ToDoTaskDetails + ", " +
                "propity-" + ToDoTaskPriority + ", " +
                "status-" + ToDoTaskStatus + ", " +
                "notes-" + ToDoNotes + ", " +
                "date-" + ToDoDate +"}";
    }

}
