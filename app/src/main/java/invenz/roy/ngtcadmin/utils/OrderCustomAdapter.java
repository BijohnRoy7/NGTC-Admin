package invenz.roy.ngtcadmin.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import invenz.roy.ngtcadmin.Order;
import invenz.roy.ngtcadmin.R;

public class OrderCustomAdapter extends RecyclerView.Adapter<OrderCustomAdapter.MyViewHolder>
{
    private static final String TAG = "ROY";
    private Context context;
    private List<Order> orderList;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;

    public OrderCustomAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_order_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderCustomAdapter.MyViewHolder holder, final int position) {

        final Order order = orderList.get(position);

        holder.tvOrderNo.setText((position+1)+"");
        holder.tvItemName.setText(order.getItemName());
        holder.tvBuyerName.setText(order.getBuyerName());
        holder.tvQuanity.setText(order.getQuantity());
        holder.tvCity.setText(order.getCity());
        holder.tvContactNo.setText(order.getContactNo());


        /*####                    completed button                   ###*/
        holder.btComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Completed", Toast.LENGTH_SHORT).show();



                /*####                          Alert Dialog                         ###*/
                alertDialog = new AlertDialog.Builder(context)
                        .setTitle("Completed?")
                        .setMessage("Is this ordered completed successfuly?")
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                /*#####           yes button is clicked          #####*/
                                progressDialog = new ProgressDialog(context);
                                progressDialog.setTitle("Updating Database.");
                                progressDialog.setMessage("Please wait...");
                                progressDialog.show();

                                /*#####                     server work                       ####*/
                                StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Constents.COMPLETE_ORDER_URL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    String serverResponse = jsonObject.getString("response");
                                                    Toast.makeText(context, ""+serverResponse, Toast.LENGTH_SHORT).show();

                                                    orderList.remove(position);
                                                    notifyItemRemoved(position);

                                                    progressDialog.dismiss();

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "onErrorResponse(OrderCustomAdapter): "+error);
                                        error.getStackTrace();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> orderMap = new HashMap<>();
                                        orderMap.put("item_id", order.getItemId());
                                        orderMap.put("order_id", String.valueOf(order.getOrderId()));
                                        return orderMap;
                                    }
                                };

                                RequestQueue requestQueue = Volley.newRequestQueue(context);
                                requestQueue.add(stringRequest);
                                /////////////////////////////////////////////////////////////

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });




        /*####                    delete button                   ###*/
        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();


                /*####                          Alert Dialog                         ###*/
                alertDialog = new AlertDialog.Builder(context)
                        .setTitle("Delete?")
                        .setMessage("Do you really want to delete this order?")
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                /*#####               Yes Button is clicked              #####*/
                                progressDialog = new ProgressDialog(context);
                                progressDialog.setTitle("Updating Database.");
                                progressDialog.setMessage("Please wait...");
                                progressDialog.show();

                                /*####                    Server Work                 ###*/
                                StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Constents.DELETE_ORDER_URL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    String serverResponse = jsonObject.getString("response");
                                                    Toast.makeText(context, ""+serverResponse, Toast.LENGTH_SHORT).show();

                                                    orderList.remove(position);
                                                    notifyItemRemoved(position);

                                                    progressDialog.dismiss();

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "onErrorResponse(OrderCustomAdapter): "+error);
                                        error.getStackTrace();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> orderMap = new HashMap<>();
                                        orderMap.put("order_id", String.valueOf(order.getOrderId()));
                                        return orderMap;
                                    }
                                };

                                RequestQueue requestQueue = Volley.newRequestQueue(context);
                                requestQueue.add(stringRequest);
                                /////////////////////

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();




            }
        });



        /*####                         To copy Contact number                     #####*/
        holder.copyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*####                 Animation when cliked image                 ####*/
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_animation));

                String contactNo = holder.tvContactNo.getText().toString();

                /*###                    Copying to clipboard                       ####*/
                ClipboardManager cm = (ClipboardManager)context.getSystemService(context.CLIPBOARD_SERVICE);
                cm.setText(contactNo);
                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrderNo,tvItemName, tvBuyerName, tvCity, tvQuanity, tvContactNo;
        Button btComplete, btDelete;
        ImageView copyImageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvOrderNo = itemView.findViewById(R.id.idOrderNo_singleOrder);
            tvItemName = itemView.findViewById(R.id.idItemName_singleOrder);
            tvBuyerName = itemView.findViewById(R.id.idBuyerName_singleOrder);
            tvCity = itemView.findViewById(R.id.idCity_singleOrder);
            tvQuanity = itemView.findViewById(R.id.idQuantity_singleOrder);
            tvContactNo = itemView.findViewById(R.id.idContactNo_singleOrder);
            btComplete = itemView.findViewById(R.id.idCompleteOrder_singleOrder);
            btDelete = itemView.findViewById(R.id.idDeleteOrder_singleOrder);
            copyImageView = itemView.findViewById(R.id.idCopyContact_singleOrder);

        }
    }
}
