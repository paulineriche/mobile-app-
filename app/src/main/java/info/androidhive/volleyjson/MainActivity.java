package info.androidhive.volleyjson;

import info.androidhive.volleyjson.R;
import info.androidhive.volleyjson.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;





public class MainActivity extends Activity {

    private static String TAG = MainActivity.class.getSimpleName();

    // Progress dialog
    private ProgressDialog pDialog;

    private TextView txtResponse;

    // temporary string to show the parsed response
    private String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnMakeObjectRequest = (Button) findViewById(R.id.btnObjRequest);
        Button btnMakeArrayRequest = (Button) findViewById(R.id.btnArrayRequest);
        txtResponse = (TextView) findViewById(R.id.txtResponse);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        btnMakeObjectRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // making json object request
                makeJsonObjectRequest();
            }
        });

        btnMakeArrayRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // making json array request
                makeJsonArrayRequest();
            }
        });

    }

    /**
     * Method to make json object request where json response starts wtih {
     * */
    private void makeJsonObjectRequest() {

        showpDialog();

        // json object response url
        String urlJsonObj = "https://coronavirus-19-api.herokuapp.com/all";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String cases = response.getString("cases");
                    String deaths = response.getString("deaths");
                    String recovered = response.getString("recovered");

                    jsonResponse = "";
                    jsonResponse += "cases: " + cases + "\n\n";
                    jsonResponse += "death: " + deaths + "\n\n";
                    jsonResponse += "recovered: " + recovered + "\n\n";

                    txtResponse.setText(jsonResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    /**
     * Method to make json array request where response starts with [
     * */
    private void makeJsonArrayRequest() {

        showpDialog();

        // json array response url
        String urlJsonArry = "https://coronavirus-19-api.herokuapp.com/countries";
        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject country = (JSONObject) response
                                        .get(i);

                                String countryName = country.getString("country");
                                String cases = country.getString("cases");
                                String todayCases = country.getString("todayCases");
                                String deaths = country.getString("deaths");
                                String todayDeaths = country.getString("todayDeaths");
                                String recovered = country.getString("recovered");
                                String active = country.getString("active");
                                String critical= country.getString("critical");
                                String casesPerOneMillion = country.getString("casesPerOneMillion");
                                String deathsPerOneMillion= country.getString("deathsPerOneMillion");
                                String totalTests = country.getString("totalTests");
                                String testsPerOneMillion= country.getString("testsPerOneMillion");


                                jsonResponse += "Country: " + countryName + "\n\n";
                                jsonResponse += "cases: " + cases + "\n\n";
                                jsonResponse += "todayCases: " + todayCases + "\n\n";
                                jsonResponse += "deaths: " + deaths + "\n\n";
                                jsonResponse += "todayDeaths: " + todayDeaths + "\n\n";
                                jsonResponse += "recovered: " + recovered + "\n\n";
                                jsonResponse += "active: " + active + "\n\n";
                                jsonResponse += "critical: " + critical + "\n\n";
                                jsonResponse += "cases per one million: " + casesPerOneMillion + "\n\n";
                                jsonResponse += "deaths per one million: " + deathsPerOneMillion + "\n\n";
                                jsonResponse += "total tests: " + totalTests + "\n\n";
                                jsonResponse += "test per one million: " + testsPerOneMillion + "\n\n\n";

                            }

                            txtResponse.setText(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
