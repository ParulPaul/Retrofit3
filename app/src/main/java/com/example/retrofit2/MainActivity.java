package com.example.retrofit2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public
class MainActivity extends AppCompatActivity implements NewsCategoryAdapter.NewsCategoryClickListener {

    private RecyclerView rcNews;
    private ProgressDialog progressDialog;
    private EditText EtSearchNews;
    private NewsAdapter adapter;

    private String selectedCountry ="us";
    private String selectedCategory;



    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //String[] newsCat = getResources().getStringArray(R.array.news_categories);
        //ArrayList<String>  categories = new ArrayList<String>(Arrays.asList(newsCat));

        String[] countries = getResources().getStringArray(R.array.country_code);

        final ArrayList<String> countriesList = new ArrayList<>(Arrays.asList(countries));


        rcNews = findViewById(R.id.rc_news);
        rcNews.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Getting News");


        EtSearchNews = findViewById(R.id.et_search_news);
        final ImageView IvClearSearch = findViewById(R.id.iv_clear_search);

        RecyclerView rcNewsCategories = findViewById(R.id.rc_news_category);
        rcNewsCategories.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.HORIZONTAL,false));

        NewsCategoryAdapter newsCategoryAdapter = new NewsCategoryAdapter(MainActivity.this);
        newsCategoryAdapter.setListener(this);
        rcNewsCategories.setAdapter(newsCategoryAdapter);

        Spinner mSpnCountries = findViewById(R.id.spn_countries);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(MainActivity.this,R .layout.cell_spinner, R.id.txt_country_holder,countriesList);
        mSpnCountries.setAdapter(countryAdapter);

        mSpnCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public
            void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedCountry = countriesList.get(position);

                getNewsByCategory(selectedCategory);

            }

            @Override
            public
            void onNothingSelected(AdapterView<?> parent) {

            }
        });
        EtSearchNews.addTextChangedListener(new TextWatcher() {
            @Override
            public
            void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public
            void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public
            void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    IvClearSearch.setVisibility(View.VISIBLE);
                    adapter.getFilter().filter(editable);
                } else {
                    IvClearSearch.setVisibility(View.GONE);
                }
            }
        });

        IvClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                EtSearchNews.setText("");
                adapter.clearSearch();
                IvClearSearch.setVisibility(View.GONE);
            }
        });

        selectedCategory="";
        getNewsByCategory("");
    }


    public
    void onGetNewsClicked(View view) {

        //getNews();
    }

    //Result
    private void getNews() {
      progressDialog.show();
        APInterface apInterface = APIClient.getClient().create(APInterface.class);

        Call<Result> getNews = apInterface.getNews("google-news", "ace4c36314e2435f9b65691f7d47ed5e");

        getNews.enqueue(new Callback<Result>() {
            @Override
            public
            void onResponse(Call<Result> call, Response<Result> response) {
                Result responseValue = response.body();

                ArrayList<Article> newsArticles = responseValue.articleList;
               progressDialog.hide();

                adapter = new NewsAdapter(MainActivity.this,newsArticles);
                rcNews.setAdapter(adapter);
            }

            @Override
            public
            void onFailure(Call<Result> call, Throwable t) {

                //progressDialog.hide();
            }


        });
    }

    private void getNewsByCategory(String category){
        progressDialog.show();
        APInterface apInterface = APIClient.getClient().create(APInterface.class);

        Map<String, Object> params = new HashMap<>();

        params.put("category", category);
        params.put("country", selectedCountry);
        params.put("apiKey", "ace4c36314e2435f9b65691f7d47ed5e");

        Call<Result> getAllNews = apInterface.getNews(params);
         getAllNews.enqueue(new Callback<Result>() {
             @Override
             public
             void onResponse(Call<Result> call, Response<Result> response) {
                 Result responseValue = response.body();

                 ArrayList<Article> newsArticles = responseValue.articleList;
               // progressDialog.hide();

                 adapter = new NewsAdapter(MainActivity.this,newsArticles);
                 rcNews.setAdapter(adapter);
                 progressDialog.hide();
             }

             @Override
             public
             void onFailure(Call<Result> call, Throwable t) {
                progressDialog.hide();
             }
         });

    }

    @Override
    public
    void onNewsCategoryClicked(String category) {
        selectedCategory = category;
        getNewsByCategory(category);
    }
}


