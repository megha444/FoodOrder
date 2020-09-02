package com.meghaagarwal.foodorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class OpenOrders extends AppCompatActivity {
private RecyclerView mFoodList;
private DatabaseReference mDB;
private FirebaseRecyclerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_orders);

        mFoodList = (RecyclerView) findViewById(R.id.rvOrderLayout);
        mFoodList.setHasFixedSize(true);
        mFoodList.setLayoutManager(new LinearLayoutManager(this));
        mDB = FirebaseDatabase.getInstance().getReference().child("orders");

        fetch();
    }

    public void fetch()
    {
        Query query = FirebaseDatabase.getInstance().getReference().child("orders");

        FirebaseRecyclerOptions<Orders> options = new FirebaseRecyclerOptions.Builder<Orders>().setQuery(query, new SnapshotParser<Orders>() {
            @NonNull
            @Override
            public Orders parseSnapshot(@NonNull DataSnapshot snapshot) {
                return new Orders(snapshot.child("itemname").getValue().toString(),
                        snapshot.child("username").getValue().toString());
            }
        }).build();

        mAdapter = new FirebaseRecyclerAdapter<Orders, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull Orders orders) {
                orderViewHolder.setUserName(orders.getUsername());
                orderViewHolder.setItemName(orders.getItemname());
            }


            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleorderlayout, parent, false);
                return new OrderViewHolder(view);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    public static class OrderViewHolder extends RecyclerView.ViewHolder
    {

        View orderView;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderView = itemView;
        }
        public void setUserName(String username)
        {
            TextView tvUserName = (TextView) orderView.findViewById(R.id.orderUsername);
            tvUserName.setText(username);
        }
        public void setItemName(String itemName)
        {
            TextView tvItemName = (TextView) orderView.findViewById(R.id.orderItemname);
            tvItemName.setText(itemName);
        }
    }
}