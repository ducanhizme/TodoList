package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends BaseAdapter {
    private ArrayList<Todo> list;
    private int layout;
    private Context mContext;

    public TodoAdapter(ArrayList<Todo> list, int layout, Context mContext) {
        this.list = list;
        this.layout = layout;
        this.mContext = mContext;
    }

    public void updateList(ArrayList<Todo> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public ArrayList<Todo> getList() {
        return list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DBHelper db = new DBHelper(mContext);
       LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout,null);
        TextView txtNameTodo = convertView.findViewById(R.id.nameTodo);
        TextView txtTimes = convertView.findViewById(R.id.timeS);
        TextView txtTimeE = convertView.findViewById(R.id.timeE);
        Todo todo = (Todo) getItem(position);
        txtNameTodo.setText(todo.getName());
        txtTimes.setText(todo.getStartTime());
        txtTimeE.setText(todo.getEndTime());
        ImageButton menuBtn = convertView.findViewById(R.id.menuBtn_i);
        if(menuBtn!=null) {
            menuBtn.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete_btn:
                                db.deleteTodo(todo);
                                list.remove(todo);
                                notifyDataSetChanged();
                                break;
                            case R.id.update_btn:
                                Toast.makeText(mContext, "update", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.done_btn:
                                Toast.makeText(mContext, "done", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });

            });
        }
        return convertView;
    }
}
