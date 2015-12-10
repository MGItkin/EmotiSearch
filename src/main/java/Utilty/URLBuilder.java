package Utilty;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

/**
 * Created by TUSHAR_SK on 11/13/15.
 */
public class URLBuilder {

    public static String getResponse(String url) throws JSONException {
        StringBuilder response = null;
        StringBuilder urlBuilder =  new StringBuilder();
        BufferedReader in = null;
        HttpsURLConnection con = null;
        ArrayList<String> responseList =  new ArrayList<String>();
        int responseCode = -1;
        try
        {
            response = new StringBuilder();

            urlBuilder =  new StringBuilder();
            urlBuilder.append(url);
            urlBuilder.append("client_id=").append(Constants.CLIENT_ID);
            urlBuilder.append("&client_secret=").append(Constants.CLIENT_SECRET);
            URL urlObj = new URL(urlBuilder.toString());

            con = (HttpsURLConnection)urlObj.openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(false);
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            responseCode = con.getResponseCode();
            String inputLine = "";
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

        } catch (Exception e)
        {
            in = new BufferedReader(
                    new InputStreamReader(con.getErrorStream()));
            String inputLine = "";

            try
            {
                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
            }
            catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            if(responseCode == -1)
            {

            }
            else
            {
                System.out.println(response.toString());
            }
        }

        finally
        {
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        responseList.add(response.toString());
        responseList.add(String.valueOf(responseCode));
        return response.toString();

    }

    public static String postRequest(String url, String reqBody)
    {
        StringBuilder response = null;
        StringBuilder urlBuilder =  null;
        BufferedReader in = null;
        HttpsURLConnection con = null;
        int responseCode = -1;
        DataOutputStream wr = null;

        try
        {
            response = new StringBuilder();

            urlBuilder =  new StringBuilder();
            urlBuilder.append(url);
            URL urlObj = new URL(urlBuilder.toString());

            con = (HttpsURLConnection)urlObj.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);

            con.setRequestProperty("Ocp-Apim-Subscription-Key",Constants.SUBSCRIPTION_KEY);
            con.setRequestProperty("Content-Type", "application/json");

            RequestBody requestBody = new RequestBody();
            requestBody.setUrl(reqBody);
            Gson gson= new Gson();
            StringEntity postingString =new StringEntity(gson.toJson(requestBody));

            JSONObject json = new JSONObject();
            json.put("url",reqBody);

            // Send post request
            wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(json.toString());

            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            responseCode = con.getResponseCode();
            String inputLine = "";
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            responseCode = con.getResponseCode();

        } catch (Exception e)
        {
            if(responseCode == -1)
            {
                System.out.println("");
            }
            else
            {
                in = new BufferedReader(
                        new InputStreamReader(con.getErrorStream()));
                String inputLine = "";

                try
                {
                    while ((inputLine = in.readLine()) != null)
                    {
                        response.append(inputLine);
                    }
                }
                catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                finally
                {
                    try {
                        in.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }

                System.out.println(response.toString());
            }

        }

        finally
        {
            try {
                wr.flush();
                wr.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return response.toString();



    }


}
