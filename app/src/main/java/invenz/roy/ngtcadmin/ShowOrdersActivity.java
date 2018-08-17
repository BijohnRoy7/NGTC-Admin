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
import invenz.roy.ngtcadmin.utils.OrderCustomAdapter;

public class ShowOrdersActivity extends AppCompatActivity {

    private static final String TAG = "ROY" ;
    private RecyclerView orderRecyclerView;
    private OrderCustomAdapter customAdapter;
    private List<Order> orderList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_orders);

        orderRecyclerView = findViewById(R.id.idOrderRecView_orderAct);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderRecyclerView.setHasFixedSize(true);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(JsonArrayRequest.Method.POST, Constents.GET_ALL_ORDERS_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        orderList = new ArrayList<>();

                        for (int i=0; i<response.length() ; i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);


                                Order order = new Order();
                                String orderId = jsonObject.getString("order_id");

                                order.setOrderId(Integer.parseInt(orderId));
                                order.setItemId(jsonObject.getString("item_id"));
                                order.setItemName(jsonObject.getString("image_name"));
                                order.setBuyerName(jsonObject.getString("buyer_name"));
                                order.setQuantity(jsonObject.getString("quantity"));
                                order.setContactNo(jsonObject.getString("contact_no"));
                                order.setCity(jsonObject.getString("city"));
                                //Toast.makeText(ShowOrdersActivity.this, ""+orderId+", "+order.getBuyerName(), Toast.LENGTH_SHORT).show();

                                orderList.add(order);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        customAdapter = new OrderCustomAdapter(ShowOrdersActivity.this, orderList);
                        customAdapter.notifyDataSetChanged();
                        orderRecyclerView.setAdapter(customAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse(ShowOrdersActivity): "+error);
                error.getStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }
}
