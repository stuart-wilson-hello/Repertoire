package com.stuart.repetoire_v1_0;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReplaceIngredientsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(String ingredient); //This is what will be returned when the click occurs
    }
    private OnItemClickListener listener;
    private List<List<String>> ingredients_extended;

    public ReplaceIngredientsRecyclerAdapter(List<List<String>> data, OnItemClickListener listener){
        this.ingredients_extended=data;
        this.listener=listener;
    }

    public void setData(List<List<String>> data) {
        ingredients_extended = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public class HorizontalListViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView horizontalList;
        private HorizontalAdapter horizontalAdapter;

        public HorizontalListViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(this);
            //itemView.setOnClickListener(onItemClickListener);
            Context context = itemView.getContext();
            horizontalList = (RecyclerView) itemView.findViewById(R.id.item_horizontal_list);
            horizontalList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            horizontalAdapter = new HorizontalAdapter();
            horizontalList.setAdapter(horizontalAdapter);

            LinearSnapHelper snapHelper = new LinearSnapHelper() {
                @Override
                public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                    View centerView = findSnapView(layoutManager);
                    if (centerView == null)
                        return RecyclerView.NO_POSITION;

                    int position = layoutManager.getPosition(centerView);
                    int targetPosition = -1;
                    if (layoutManager.canScrollHorizontally()) {
                        if (velocityX < 0) {
                            targetPosition = position - 1;
                        } else {
                            targetPosition = position + 1;
                        }
                    }

                    if (layoutManager.canScrollVertically()) {
                        if (velocityY < 0) {
                            targetPosition = position - 1;
                        } else {
                            targetPosition = position + 1;
                        }
                    }

                    final int firstItem = 0;
                    final int lastItem = layoutManager.getItemCount() - 1;
                    targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));
                    return targetPosition;
                }
            };
            snapHelper.attachToRecyclerView(horizontalList);
        }


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.vertical_list_item, parent, false);
        HorizontalListViewHolder holder = new HorizontalListViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position) {
        HorizontalListViewHolder holder = (HorizontalListViewHolder) rawHolder;
        holder.horizontalAdapter.setData(ingredients_extended.get(position));
        holder.horizontalList.scrollToPosition(0);
        holder.horizontalAdapter.setRowIndex(position);
        holder.horizontalAdapter.setOnItemClickListener(listener);
        holder.itemView.setTag(position);
        //holder.bind(ingredients_extended.get(position), listener);
        if(ingredients_extended.get(position).size()>1)
            holder.horizontalList.setBackgroundColor(Color.parseColor("#D6EAF8"));
        else
            holder.horizontalList.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }


    @Override
    public int getItemCount() {
        return ingredients_extended.size();
    }

    public static class HorizontalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> replacementIngredients;
        private int mRowIndex = -1;
        OnItemClickListener horizontallistener;


        public HorizontalAdapter() {
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.horizontallistener = listener;
        }

        public void setData(List<String> data) {
            if (replacementIngredients != data) {
                replacementIngredients = data;
                notifyDataSetChanged();
            }
        }

        public void setRowIndex(int index) {
            mRowIndex = index;
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            public TextView text;
            public String this_ingredient;
            private OnItemClickListener listener;

            public ItemViewHolder(View itemView) {
                super(itemView);
                text = (TextView) itemView.findViewById(R.id.item_text);
            }
            public void bind(final String item, final OnItemClickListener listener) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemClick(this_ingredient);
                    }
                });
            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View itemView = LayoutInflater.from(context).inflate(R.layout.horizontal_list_item, parent, false);
            ItemViewHolder holder = new ItemViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position) {
            ItemViewHolder holder = (ItemViewHolder) rawHolder;
            holder.text.setText(replacementIngredients.get(position));
            holder.this_ingredient=replacementIngredients.get(position);
            holder.itemView.setTag(position);
            holder.bind(replacementIngredients.get(position), horizontallistener);
        }

        @Override
        public int getItemCount() {
            return replacementIngredients.size();
        }

        private View.OnClickListener mItemClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int columnIndex = (int) v.getTag();
                int rowIndex = mRowIndex;

                String text = String.format("rowIndex:%d ,columnIndex:%d", rowIndex, columnIndex);
                Log.d("test", text);
            }
        };

    }


}
