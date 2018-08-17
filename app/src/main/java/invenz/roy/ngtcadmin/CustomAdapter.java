package invenz.roy.ngtcadmin;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import invenz.roy.ngtcadmin.utils.Constents;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private static final String TAG = "ROY";
    private Context context;
    private List<Item> itemList;


    public CustomAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.single_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, final int position) {

        final Item item = itemList.get(position);
        String name = item.getName();
        String imageTitle = item.getImagePath();

        String imagePath = "http://invenz-it.com/ngtc/images/"+imageTitle+".jpg";
        holder.tvName.setText(name);
        Picasso.get().load(imagePath).into(holder.imageViewItem);


        holder.btEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Edit Info: "+item.getItemId(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Delete Info: "+item.getItemId(), Toast.LENGTH_SHORT).show();

                /*########              AlertDialog for confirmation              ########*/
                new AlertDialog.Builder(context)
                        .setTitle("Delete Information")
                        .setMessage("Do you really want to Detete this item?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                /*####               When YES button is clicked                ###*/
                                StringRequest deleteRequest = new StringRequest(StringRequest.Method.POST, Constents.DELETE_INFO_URL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    String serverERsponse = jsonObject.getString("response");
                                                    Toast.makeText(context, ""+serverERsponse, Toast.LENGTH_SHORT).show();

                                                    itemList.remove(position);
                                                    notifyDataSetChanged();

                                                } catch (JSONException e) {
                                                    e.printStackTrace();

                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "onErrorResponse1 (CustomAdapter): "+error);

                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {

                                        Map<String, String> deleteInfoMap = new HashMap<>();
                                        deleteInfoMap.put("image_id", String.valueOf(item.getItemId()));
                                        return deleteInfoMap;

                                    }
                                };

                                RequestQueue requestQueue = Volley.newRequestQueue(context);
                                requestQueue.add(deleteRequest);

                            }})
                        .setNegativeButton("No", null).show();



            }
        });



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView imageViewItem;
        Button btDelete, btEditInfo;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.idName_singleItem);
            imageViewItem = itemView.findViewById(R.id.idItemImage_singleItem);
            btDelete = itemView.findViewById(R.id.idDelete_singleItem);
            btEditInfo = itemView.findViewById(R.id.idEdit_singleItem);

        }
    }
}
