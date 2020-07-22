package com.example.retrofit2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public
class NewsCategoryAdapter extends RecyclerView.Adapter<NewsCategoryAdapter.NewsCategoryHolder> {


    private Context context;
    private String[] newsCategories;
    private NewsCategoryClickListener listener;
    private int selectedPosition = -1;

    public void setListener(NewsCategoryClickListener listener){
        this.listener = listener;
    }

    public NewsCategoryAdapter(Context context){
        this.context = context;
        this.newsCategories = context.getResources().getStringArray(R.array.news_categories);
    }

    @NonNull
    @Override
    public
    NewsCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsCategoryHolder(LayoutInflater.from(context).inflate(R.layout.cell_news_category,parent,false));
    }

    @Override
    public
    void onBindViewHolder(@NonNull NewsCategoryHolder holder, final int position) {
        final String newsCat = newsCategories[position];
        holder.tvNewsCategory.setText(newsCat);

        holder.rlNewsCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
              if (listener !=null){
                  listener.onNewsCategoryClicked(newsCat);
                    selectedPosition =position;
                    notifyDataSetChanged();
                }
            }
        });
        if(position == selectedPosition){
            holder.rlNewsCategory.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.bg_selected,null));
            holder.tvNewsCategory.setTextColor(ResourcesCompat.getColor(context.getResources(),R.color.colorWhite,null));
        }else{
            holder.rlNewsCategory.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.bg_unselected,null));
            holder.tvNewsCategory.setTextColor(ResourcesCompat.getColor(context.getResources(),R.color.colorBlack,null));
        }

    }

    @Override
    public
    int getItemCount() {
        return newsCategories != null ? newsCategories.length : 0;
    }

    class NewsCategoryHolder extends RecyclerView.ViewHolder{

        private RelativeLayout rlNewsCategory;
        private TextView tvNewsCategory;

        public
        NewsCategoryHolder(@NonNull View itemView) {
            super(itemView);

            rlNewsCategory = itemView.findViewById(R.id.rl_layout);
            tvNewsCategory = itemView.findViewById(R.id.txt_news_category);
        }
    }
    public interface  NewsCategoryClickListener{
        void onNewsCategoryClicked(String category);
    }
}
