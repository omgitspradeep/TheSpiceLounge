package com.sujan.info.thespicelounge.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.sujan.info.thespicelounge.R;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.models.FoodDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pradeep on 3/7/20.
 */

public class FoodMenuAdapter extends RecyclerView.Adapter<FoodMenuAdapter.ViewHolder>{



    private ArrayList<FoodDetails> mData;
    private LayoutInflater mInflater;
    Context mContext;
    public Drawable drawable;
    private ItemClickListener mClickListener;

    public FoodMenuAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext=context;
        this.mData = ResourceManager.getGlobalFoodMenu();

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.menu_item_adapter, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FoodDetails foodDetails = mData.get(position);
        holder.foodImage.setImageResource(foodDetails.getImage());
        holder.foodName.setText(ResourceManager.getFoodName(foodDetails));
        holder.foodPrice.setText(foodDetails.getPrice()+"");
        holder.foodTime.setText("Time : "+foodDetails.getPrepareTimeinMins()+" Mins");
        holder.foodRating.setNumStars(foodDetails.getRatingStar());

    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public FoodDetails getItem(int id) {
        return mData.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView foodImage;
        TextView foodName,foodPrice,foodTime;
        RatingBar foodRating;


        public ViewHolder(View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodTime = itemView.findViewById(R.id.food_prepare_time);
            foodPrice=itemView.findViewById(R.id.food_price);
            foodRating = itemView.findViewById(R.id.food_rating);
            drawable = foodRating.getProgressDrawable();
            drawable.setColorFilter(Color.parseColor("#F2BA49"), PorterDuff.Mode.SRC_ATOP);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (Utils.isConnected(mContext)){
                ResourceManager.setFoodDetailsInstance(mData.get(getAdapterPosition())); // temporary setup to inflate data
                if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
            }else{
                Utils.displayShortToastMessage(mContext,"No Internet");
            }
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }



    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
