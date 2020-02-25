package com.example.foxtrotfrontend;

import android.media.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
                          String category, Image image, Date solved, String severity) {

        // POST request to the API with the input data

        boolean complete = false;

        try {

            URL newReportURL = new URL(url.toString() + "/api/new");

            String jsonString = new JSONObject()
                    .put("report_id", reportId)
                    .put("date", date.toString())
                    .put("latitude", latitude)
                    .put("longitude", longitude)
                    .put("name", name)
                    .put("description", description)
                    .put("category", category)
                    .put("image", image.toString())
                    .put("solved", solved.toString())
                    .put("severity", severity).toString();

            HttpURLConnection con = (HttpURLConnection) newReportURL.openConnection();
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
                br.close();

            } catch (IOException e) {
                System.err.println("Input Stream Error");
            }

            JSONObject result = new JSONObject(response.toString());
            if (result.has("complete")) {
                complete = true;
            }

        } catch (MalformedURLException e) {
            System.err.println("Malformed URL");
        } catch (IOException e) {
            System.err.println("Unable to establish a connection to <" + url.toString() + ">");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return complete;

        // Complete is true if API POST worked, else false

    }


    public JSONArray getPestInfo(String name, double latitude, double longitude) {

        // GET request to the API with the input data

        JSONArray result = null;

        try {

            URL getInfoURL = new URL(url.toString() + "/map/pest?"); // NEED TO FILL THIS IN


            Map<String, String> parameters = new HashMap<>();
            parameters.put("name", name);
            parameters.put("latitude", latitude + "");
            parameters.put("longitude", longitude + "");

            HttpURLConnection con = (HttpURLConnection) getInfoURL.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            DataOutputStream dos = new DataOutputStream(con.getOutputStream());
            dos.writeBytes(ParameterStringBuilder.getParamsString(parameters));
            dos.flush();
            dos.close();

            con.setRequestProperty("Content-Type", "application/json");

            StringBuilder response = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"));
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                br.close();

            } catch (IOException e) {
                System.err.println("Input Stream Error");
            }

            result = new JSONArray(response.toString());


        } catch (MalformedURLException e) {
            System.err.println("Malformed URL");
        } catch (IOException e) {
            System.err.println("Unable to establish a connection to <" + url.toString() + ">");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;

        // Returns JSONArray if API GET worked, else null

    }

    public ArrayList<NearbyReport> getPestRadar(String[] pests, double latitude, double longitude) {

        // Takes in all pests and queries for them in a particular location

        ArrayList<NearbyReport> result = null;

        for (String pest : pests) {

            JSONArray get = getPestInfo(pest, latitude, longitude);

            if (get != null) {

                for (int i = 0; i < get.length(); i++) {

                    try {
                        // Parse JSONObjects to NearbyReports
                        JSONObject current = get.getJSONObject(i);

                        if (result == null) {
                            result = new ArrayList<>();
                        }
                        result.add(new NearbyReport(
                                pest,
                                new Date(current.getString("date")), // CHECK THIS
                                current.getDouble("latitude"),
                                current.getDouble("longitude"),
                                current.getInt("severity")));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }


    public JSONArray getDiseaseInfo(String name, double latitude, double longitude) {

        // GET request to the API with the input data

        JSONArray result = null;

        try {

            URL getInfoURL = new URL(url.toString() + "/map/disease?"); // NEED TO FILL THIS IN


            Map<String, String> parameters = new HashMap<>();
            parameters.put("name", name);
            parameters.put("latitude", latitude + "");
            parameters.put("longitude", longitude + "");

            HttpURLConnection con = (HttpURLConnection) getInfoURL.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            DataOutputStream dos = new DataOutputStream(con.getOutputStream());
            dos.writeBytes(ParameterStringBuilder.getParamsString(parameters));
            dos.flush();
            dos.close();

            con.setRequestProperty("Content-Type", "application/json");


            StringBuilder response = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"));
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                br.close();

            } catch (IOException e) {
                System.err.println("Input Stream Error");
            }

            result = new JSONArray(response.toString());


        } catch (MalformedURLException e) {
            System.err.println("Malformed URL");
        } catch (IOException e) {
            System.err.println("Unable to establish a connection to <" + url.toString() + ">");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;

        // Returns JSONArray if API GET worked, else null

    }

    public ArrayList<NearbyReport> getDiseaseRadar(String[] pests, double latitude, double longitude) {

        // Takes in all pests and queries for them in a particular location

        ArrayList<NearbyReport> result = null;

        for (String pest : pests) {

            JSONArray get = getPestInfo(pest, latitude, longitude);

            if (get != null) {

                for (int i = 0; i < get.length(); i++) {

                    try {
                        // Parse JSONObjects to NearbyReports
                        JSONObject current = get.getJSONObject(i);

                        if (result == null) {
                            result = new ArrayList<>();
                        }
                        result.add(new NearbyReport(
                                pest,
                                new Date(current.getString("date")), // CHECK THIS
                                current.getDouble("latitude"),
                                current.getDouble("longitude"),
                                current.getInt("severity")));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }


}
