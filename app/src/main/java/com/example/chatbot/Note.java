package com.example.chatbot;

public class Note {

    int _id;
    String todo;
    int checktodo;

    public Note(int _id, String todo, int checktodo){
        this._id = _id; //정렬용
        this.todo = todo; //저장용
        this.checktodo = checktodo;
    }

    public int get_id(){
        return _id;
    }

    public void set_id(int _id){
        this._id = _id;
    }

    public String getTodo(){
        return todo;
    }

    public void setTodo(String Todo){
        this.todo = todo;
    }

    public void setchecktodo(){this.checktodo = checktodo;}

    public int getChecktodo(){return checktodo;}

}