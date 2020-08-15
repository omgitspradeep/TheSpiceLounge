package com.sujan.info.thespicelounge.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sujan.info.thespicelounge.ChefMenuModifyActivity;
import com.sujan.info.thespicelounge.R;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.models.FoodDetails;

import java.util.ArrayList;

/**
 * Created by pradeep on 10/7/20.
 */

public class FoodMenuAdapterChef extends RecyclerView.Adapter<FoodMenuAdapterChef.ViewHolder>{

    LayoutInflater mInflator;
    ChefMenuModifyActivity mContext;
    ArrayList<FoodDetails> foodDetailsArrayList;
    public Drawable drawable;

    public interface onEditListener{
        public void editClickListener(FoodDetails foodDetails);
        public void deleteButtonClicked(FoodDetails foodDetails);
    }

    public FoodMenuAdapterChef(ChefMenuModifyActivity mContext) {
        this.mContext = mContext;
        this.mInflator=mContext.getLayoutInflater();
        this.foodDetailsArrayList = ResourceManager.getGlobalFoodMenu();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=mInflator.inflate(R.layout.food_menu_adapter_chef,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FoodDetails foodDetails = foodDetailsArrayList.get(position);
        holder.foodImageChef.setImageResource(foodDetails.getImage());
        holder.foodNameChef.setText(foodDetails.getItemName()+" "+foodDetails.getItemCategory()+"( "+foodDetails.getServeType()+" )");
        holder.foodPriceChef.setText(foodDetails.getPrice()+"");
        holder.foodTimeChef.setText("Time : "+foodDetails.getPrepareTimeinMins()+" Mins");
        holder.foodRatingChef.setNumStars(foodDetails.getRatingStar());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          //      Utils.displayShortToastMessage(mContext,"This item will be edited");
                mContext.editClickListener(foodDetails);

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.deleteButtonClicked(foodDetails);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodDetailsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView foodImageChef;
        TextView foodNameChef,foodTimeChef,foodPriceChef;
        RatingBar foodRatingChef;
        ImageButton edit,delete;

        public ViewHolder(View itemView) {
            super(itemView);
            foodImageChef = itemView.findViewById(R.id.food_image_chef);
            foodNameChef = itemView.findViewById(R.id.food_name_chef);
            foodPriceChef = itemView.findViewById(R.id.food_price_chef);
            foodTimeChef=itemView.findViewById(R.id.food_prepare_time_chef);
            foodRatingChef = itemView.findViewById(R.id.food_rating_chef);
            drawable = foodRatingChef.getProgressDrawable();
            drawable.setColorFilter(Color.parseColor("#F2BA45"), PorterDuff.Mode.SRC_ATOP);
            edit=itemView.findViewById(R.id.edit_food_item_chef);
            delete=itemView.findViewById(R.id.delete_food_item_chef);
        }
    }
}
