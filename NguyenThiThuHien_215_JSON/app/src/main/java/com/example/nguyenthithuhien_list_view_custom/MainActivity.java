package com.example.nguyenthithuhien_list_view_custom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView); //ánh xạ ListView

        new LoadDataTask().execute("https://assets.ctfassets.net/o8rbhyyom3pw/5t5wxVKTUcVWr3xxr3McPV/8d51f86067dac0e61480fa7c1ad8d694/homiedev_blog_information.json");
    }

    private class LoadDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String blogName = jsonObject.getString("blogName");
                JSONArray categoriesArray = jsonObject.getJSONArray("categories");

                List<String> categoryList = new ArrayList<>();
                for (int i = 0; i < categoriesArray.length(); i++) {
                    JSONObject categoryObject = categoriesArray.getJSONObject(i);
                    String categoryName = categoryObject.getString("name");
                    int numberOfPosts = categoryObject.getInt("numberOfPosts");
                    String link = categoryObject.getString("link");

                    String categoryInfo = categoryName + " \n " + numberOfPosts + " \n " + link;
                    categoryList.add(categoryInfo);
                }

                // Hiển thị danh sách các danh mục
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, categoryList);
                listView.setAdapter(adapter);

                Toast.makeText(MainActivity.this, "Blog Name: " + blogName, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
            }
        }
    }
}