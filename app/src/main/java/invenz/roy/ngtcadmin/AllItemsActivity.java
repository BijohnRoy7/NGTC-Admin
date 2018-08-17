package invenz.roy.ngtcadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import invenz.roy.ngtcadmin.utils.Constents;

public class AllItemsActivity extends AppCompatActivity {

    private static final String TAG = "ROY";
    private RecyclerView itemsRecyclerView;
    private List<Item> itemList = new ArrayList<>();
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);

        itemsRecyclerView = findViewById(R.id.idInfoRecView_AllItemsActivity);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(AllItemsActivity.this));
        itemsRecyclerView.setHasFixedSize(true);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(JsonArrayRequest.Method.POST, Constents.GET_INFO_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        itemList.clear();
                        for (int i =0; i<response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String name = jsonObject.getString("image_name");
                                //String imagePath = jsonObject.getString("actual_path");
                                String imageTitle = jsonObject.getString("image_title");

                                Item item = new Item(id, name, imageTitle);
                                Toast.makeText(AllItemsActivity.this, ""+imageTitle, Toast.LENGTH_SHORT).show();
                                itemList.add(item);

                                customAdapter = new CustomAdapter(AllItemsActivity.this, itemList);
                                customAdapter.notifyDataSetChanged();
                                itemsRecyclerView.setAdapter(customAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse1(AllItemsActivity): "+error);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(AllItemsActivity.this);
        requestQueue.add(jsonArrayRequest);

    }
}
