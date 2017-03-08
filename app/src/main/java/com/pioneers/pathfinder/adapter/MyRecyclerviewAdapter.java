package com.pioneers.pathfinder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pioneers.pathfinder.R;
import com.pioneers.pathfinder.model.DataModel;

import java.util.List;


public class MyRecyclerviewAdapter extends RecyclerView.Adapter<MyRecyclerviewAdapter.DataObjectHolder> {

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private static MyClickListener myClickListener;
    private List<DataModel> mDataset;

    public MyRecyclerviewAdapter(List<DataModel> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        DataModel m = mDataset.get(position);
        holder.tvtitle.setText(m.getName());
        holder.tvroute1.setText(m.getRoute1());
        holder.tvroute2.setText(m.getRoute2());

    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvtitle, tvroute1, tvroute2;


        public DataObjectHolder(View itemView) {
            super(itemView);
            tvtitle = (TextView) itemView.findViewById(R.id.grid_name);
            tvroute1 = (TextView) itemView.findViewById(R.id.txtRoute1);
            tvroute2 = (TextView) itemView.findViewById(R.id.txtRoute2);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);

        }
    }
}
