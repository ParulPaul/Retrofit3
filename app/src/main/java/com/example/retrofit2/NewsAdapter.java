package com.example.retrofit2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public
class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> implements Filterable {

    private Context context;
    private ArrayList<Article> articles;
    private ArrayList<Article> displayArticles;
    private ArrayList<Article> suggestions;
    private NewsFilter filter;


    public NewsAdapter(Context context, ArrayList<Article> articles){
        this.articles = articles;
        this.context = context;
        this.displayArticles = (ArrayList<Article>) articles.clone();
        this.suggestions = new ArrayList<>();
        filter = new NewsFilter();
    }

    @NonNull
    @Override
    public
    NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsHolder(LayoutInflater.from(context).inflate(R.layout.cells_news, parent,false));
    }

    @Override
    public
    void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        Article currentArticle = displayArticles.get(position);
        if (currentArticle != null) {
            holder.TvTitle.setText(currentArticle.title);
            holder.TvDescription.setText(currentArticle.description);

            Glide.with(context).load(currentArticle.urlToImage).placeholder(R.drawable.ic_launcher_foreground).into(holder.IvNewsImage);

        }

    }

    @Override
    public
    int getItemCount() {
        return  displayArticles != null ? displayArticles.size() : 0;
    }

    @Override
    public
    Filter getFilter() {
        return filter;
    }

    class NewsHolder extends RecyclerView.ViewHolder{

        private TextView TvTitle;
        private TextView TvDescription;
        private ImageView IvNewsImage;

        public
        NewsHolder(@NonNull View itemView) {
            super(itemView);

            TvTitle = itemView.findViewById(R.id.tv_news_title);
            TvDescription = itemView.findViewById(R.id.tv_news_title);
            IvNewsImage = itemView.findViewById(R.id.iv_news_image);
        }
    }

    public class NewsFilter extends Filter{

        @Override
        protected
        FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();

            suggestions.clear();
            if (charSequence != null) {
                for (Article article : articles) {
                    if (article.title.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        suggestions.add(article);
                    } else if (article.description.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        suggestions.add(article);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();


            return results;
        }

        @Override
        protected
        void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (filterResults != null) {
                if (filterResults.count > 0) {
                    displayArticles = (ArrayList<Article>) filterResults.values;
                    notifyDataSetChanged();
                }
            }
        }
    }

    public void clearSearch() {
        displayArticles.clear();
        displayArticles = (ArrayList<Article>) articles.clone();
        notifyDataSetChanged();



    }
}
