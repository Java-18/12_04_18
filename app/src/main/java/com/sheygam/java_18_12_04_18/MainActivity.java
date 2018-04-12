package com.sheygam.java_18_12_04_18;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MyAdapter.MyAdapterClickListener {

    private RecyclerView myList;
    private MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this,2);
        RecyclerView.ItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        adapter = new MyAdapter(this);
        myList = findViewById(R.id.my_list);
        myList.setLayoutManager(manager);
        myList.setAdapter(adapter);
        myList.addItemDecoration(divider);

        ItemTouchHelper helper = new ItemTouchHelper(new MyTouchCallback(new MyTouchCallbackListener() {
            @Override
            public void onSwiped(final int position) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete item?")
                        .setMessage("Are you sure that you want delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.remove(position);
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.notifyItemChanged(position);
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }

            @Override
            public void onMoved(int from, int to) {
                adapter.move(from,to);
            }
        }));

        helper.attachToRecyclerView(myList);


    }

    @Override
    public void onClick(User user, int position) {
        Toast.makeText(this, user.getName(), Toast.LENGTH_SHORT).show();
    }



    class MyTouchCallback extends ItemTouchHelper.Callback{
        private MyTouchCallbackListener listener;

        public MyTouchCallback(MyTouchCallbackListener listener) {
            this.listener = listener;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.END|ItemTouchHelper.START);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            if(listener!=null){
                listener.onMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                return true;
            }
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if (listener!=null){
                listener.onSwiped(viewHolder.getAdapterPosition());

            }
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }
    }

    public interface MyTouchCallbackListener{
        void onSwiped(int position);
        void onMoved(int from, int to);
    }
}
