package com.example.foxtrotfrontend;

import android.media.Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class ReportHTTP {

    private URL url;

    public ReportHTTP(String requestURL) {
        try {
            this.url = new URL(requestURL);
        } catch (MalformedURLException e) {
            System.err.println("URL not valid");
        }
    }

    public boolean newReport(int reportId, Date date, double latitude,
                          double longitude, String name, String description,
                          String category, Image image, Date solved) {

        // POST request to the API with the input data

        boolean complete = false;

        try {

            String jsonString = new JSONObject()
                    .put("report_id", reportId)
                    .put("date", date.toString())
                    .put("latitude", latitude)
                    .put("longitude", longitude)
                    .put("name", name)
                    .put("description", description)
                    .put("category", category)
                    .put("image", image.toString())
                    .put("solved", solved.toString()).toString();

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");


            try {
                OutputStream os = con.getOutputStream();
                byte[] input = jsonString.getBytes("utf-8");
                os.write(input, 0, input.length);

                os.close();
            } catch (IOException e) {
                System.err.println("Output Stream Error");
            }

            StringBuilder response = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"));
                String responseLine;

                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                System.out.println(response.toString());


            } catch (IOException e) {
                System.err.println("Input Stream Error");
            }


            JSONObject result = new JSONObject(response.toString());
            if (result.has("complete")) { complete = true; }


        } catch (IOException e) {
            System.err.println("Unable to establish a connection to <" + url.toString() + ">");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return complete;

    }

    public void updateReport() {}



}
