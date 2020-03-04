package com.example.foxtrotfrontend;

import android.graphics.Bitmap;
import android.media.Image;

import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.ws.http.HTTPException;
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
                             String category, Bitmap image, String severity) {

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
                    .put("solved","")
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
                e.printStackTrace();
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
                e.printStackTrace();
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

    public boolean updateReport(int reportId, Date date, String description,
                                Image image, int severity) {

        // POST request to the API with update data

        boolean complete = false;

        try {

            URL updateReportURL = new URL(url.toString() + "/api/update");

            String jsonString = new JSONObject()
                    .put("report_id", reportId)
                    .put("date", date.toString())
                    .put("image", image.toString())
                    .put("severity", severity).toString();

            HttpURLConnection con = (HttpURLConnection) updateReportURL.openConnection();
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

    }


   /* public JSONArray getPestInfo(String name, double latitude, double longitude) {

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
            con.setRequestProperty();
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

    }*/

    public JSONArray getPestInfo(String name, double latitude, double longitude) {

        // GET request to the API with the input data

        JSONArray result = null;
        try {
            URL getInfoURL = new URL(url.toString() + "/map/pest");
            result = new JSONArray(HttpRequest.get(
                    getInfoURL.toString(), true, "name", name, "latitude", latitude,"longitude", longitude
            ).body().toString());

        } catch (HTTPException |MalformedURLException e) {
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


/*    public JSONArray getDiseaseInfo(String name, double latitude, double longitude) {

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
    */

    public JSONArray getDiseaseInfo(String name, double latitude, double longitude) {

        // GET request to the API with the input data

        JSONArray result = null;
        try {
            URL getInfoURL = new URL(url.toString() + "/map/disease");
            result = new JSONArray(HttpRequest.get(
                    getInfoURL.toString(), true, "name", name, "latitude", latitude,"longitude", longitude
            ).body().toString());

        } catch (HTTPException |MalformedURLException e) {
            System.err.println("Malformed URL");
        } catch (IOException e) {
            System.err.println("Unable to establish a connection to <" + url.toString() + ">");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;


    }

    public ArrayList<NearbyReport> getDiseaseRadar(String[] pests, double latitude, double longitude) {

        // Takes in all pests and queries for them in a particular location

        ArrayList<NearbyReport> result = null;

        for (String pest : pests) {

            JSONArray get = getDiseaseInfo(pest, latitude, longitude);

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

    public ArrayList<NearbyReport> getInitialRadar(double latitude, double longitude) {

        // GET request to the API with the input data

        JSONArray temp = null;

        try {
                URL getInfoURL = new URL(url.toString() + "/map/local");
                temp = new JSONArray(HttpRequest.get(
                        getInfoURL.toString(), true, "latitude", latitude, "longitude", longitude
                ).body().toString());

        } catch (MalformedURLException e) {
            System.err.println("Malformed URL");
        } catch (IOException e) {
            System.err.println("Unable to establish a connection to <" + url.toString() + ">");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<NearbyReport> result = null;

        if (temp != null) {

            for (int i = 0; i < temp.length(); i++) {

                try {
                    // Parse JSONObjects to NearbyReports
                    JSONObject current = temp.getJSONObject(i);

                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    result.add(new NearbyReport(
                            current.getString("name"),
                            new Date(current.getString("date")), // CHECK THIS
                            current.getDouble("latitude"),
                            current.getDouble("longitude"),
                            current.getInt("severity")));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;

    }


}
