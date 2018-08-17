package invenz.roy.ngtcadmin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import invenz.roy.ngtcadmin.utils.Constents;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ROY";
    private ImageView ivItemImage;
    private EditText etItemName;
    private Button btSelectImage, btAddInfo, btShowInfo, btSendNotification, btShowOrders;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivItemImage = findViewById(R.id.idItemImage_mainAct);
        etItemName = findViewById(R.id.idItemName);
        btSelectImage = findViewById(R.id.idSelectImage);
        btAddInfo = findViewById(R.id.idAddInformation);
        btShowInfo = findViewById(R.id.idShowInfo);
        btSendNotification = findViewById(R.id.idSenNotificationInfo);
        btShowOrders = findViewById(R.id.idShowOrders);


        /*####              Choosing image              #####*/
        btSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });



        /*#######           Submitting information       #####*/
        btAddInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etItemName.getText().toString().isEmpty() ){
                    uploadImage();

                }else {
                    Toast.makeText(MainActivity.this, "Please enter image name ", Toast.LENGTH_SHORT).show();

                }

            }
        });


        /*###               showing all information              ###*/
        btShowInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AllItemsActivity.class));
            }
        });


        /*##                                         ###*/
        btSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SendNotificationActivity.class));
            }
        });


        /*####                                         ####*/
        btShowOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShowOrdersActivity.class));
            }
        });


    }






    /*#######           Method for selecting image           #########*/
    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }


    /*###### onActivityResult #######*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1  &&  resultCode == RESULT_OK && data!=null){

            Uri uri = data.getData();
            Log.d(TAG, "onActivityResult: "+uri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                ivItemImage.setImageBitmap(bitmap);
                ivItemImage.setVisibility(View.VISIBLE);
                etItemName.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }




    /*####                 Converting Image Bitmap into image String               #####*/
    private String imageToString(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return imageString;

    }




    /*#####                 Uploading image into server storage                      ########*/
    private void uploadImage(){

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Constents.ADD_INFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();

                            ivItemImage.setImageResource(0);
                            ivItemImage.setVisibility(View.GONE);

                            etItemName.setText("");
                            etItemName.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse1(MainActivity) : "+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Date date = new Date();
                String str = date.getTime()+""+date.getSeconds();

                String sImageName = etItemName.getText().toString().trim();

                Map<String, String> infoMap = new HashMap<>();
                infoMap.put("image_title", sImageName+""+str);
                infoMap.put("name", sImageName);
                infoMap.put("image", imageToString(bitmap));

                return infoMap;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }




}
