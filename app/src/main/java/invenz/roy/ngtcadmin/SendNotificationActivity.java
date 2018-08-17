package invenz.roy.ngtcadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import invenz.roy.ngtcadmin.utils.Constents;

public class SendNotificationActivity extends AppCompatActivity {

    private static final String TAG =  "ROY" ;
    private EditText etTitle, etMessage;
    private Button btSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        etTitle = findViewById(R.id.idTitle_notification);
        etMessage = findViewById(R.id.idMessage_notification);
        btSend = findViewById(R.id.idSend_notification);


        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String sTitle = etTitle.getText().toString().trim();
                final String sMsg = etMessage.getText().toString().trim();

                if (sTitle.isEmpty() || sMsg.isEmpty()){
                    Toast.makeText(SendNotificationActivity.this, "Please give all informations", Toast.LENGTH_SHORT).show();
                }else {

                    StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Constents.SEND_NOTIFICATION_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Toast.makeText(SendNotificationActivity.this, "Ok", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse(SendNotificationActivity): "+error);
                            error.getStackTrace();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> notificationMap = new HashMap<>();
                            notificationMap.put("title", sTitle);
                            notificationMap.put("message", sMsg);

                            return notificationMap;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(SendNotificationActivity.this);
                    requestQueue.add(stringRequest);

                }


            }
        });


    }
}
