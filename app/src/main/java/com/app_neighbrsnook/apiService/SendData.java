package com.app_neighbrsnook.apiService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.app_neighbrsnook.utils.OnTaskCompleted;

import org.json.JSONObject;

public class SendData extends AsyncTask<Object, Void, Object> {
    public ProgressDialog dialog;
    public OnTaskCompleted<String> listener;
    Context context;
    String url = "";
    JSONObject jsonObject;
    boolean isDialog;
    public String response = "";
    int status_code = 0;



    public SendData(Context context, JSONObject jsonObject, String url, OnTaskCompleted onTaskCompleted, boolean isDialog) {
        this.url = url;
        this.jsonObject = jsonObject;
        this.context = context;
        this.isDialog = isDialog;
        this.listener = onTaskCompleted;

    }

/*

    @Override
    protected Object doInBackground(Object... objects) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000); //Timeout Limit
            HttpResponse httpResponse;
            Log.d("POST_URL", url);
            HttpPost httpPost = new HttpPost(url);
            StringEntity se = null;

            se = new StringEntity(jsonObject.toString());

            Log.d("JSONOBJECT", jsonObject.toString());
            httpPost.setEntity(se);
            httpPost.setHeader(new Bas icHeader("Content-type", "application/json"));
            httpPost.setHeader(new BasicHeader("Authorization", "Bearer " + StringUtils.TOKEN));
            Log.d("Token", StringUtils.TOKEN);
            HttpParams httpParams = new SyncBasicHttpParams();
            int timeoutConnection = 5000;
            HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
            int timeoutSocket = 5000;
            HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
            HttpContext localContext = new BasicHttpContext();

            //httpResponse = httpClient.execute(httpPost);
            //  Log.d("Response", response.toString());
            httpResponse = httpClient.execute(httpPost, localContext);
            status_code = httpResponse.getStatusLine().getStatusCode();
            */
/*Checking response *//*

            if (httpResponse != null) {
                InputStream in = httpResponse.getEntity().getContent(); //Get the data in the entity
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                for (String line; (line = r.readLine()) != null; ) {
                    total.append(line).append('\n');
                }

                Log.d("Total", total.toString());

              // Constants.SEND_STATUS = status_code;
                response = total.toString();


            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SendException", e.toString());
        }

        return null;
    }

*/

    @Override
    protected Object doInBackground(Object... objects) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        if (isDialog) {
            dialog.setTitle("Please wait");
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    @Override
    protected void onPostExecute(Object o) {
  try {
      dialog.dismiss();
      /*  if (status_code==200) {
            listener.onTaskCompleted(response);
            dialog.dismiss();
        }else {*/
      Log.d("Status", status_code + "");
    /*  if (status_code==401){
          UtilityFunction.refereshToken((Activity)context);
      }else {
          listener.onTaskCompleted(response);
      }*/
      listener.onTaskCompleted(response, status_code);

  }catch (Exception e)
  {
      e.printStackTrace();
  }
        // Toast.makeText(context, "Oops! something went wrong. Please try again after some time", Toast.LENGTH_SHORT).show();

        // }
    }

}
