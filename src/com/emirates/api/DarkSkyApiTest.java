package com.emirates.api;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DarkSkyApiTest {

	public static void main(String [] args)
	{
		//inline will store the JSON data streamed in string format
		String inline = "";
		String inputUrl = "https://api.darksky.net/forecast/7ec1083f80e0e4622c8a886149bc7676/37.8267,-122.4233";
	
		try
		{
			URL url = new URL(inputUrl);
			
			//Parse URL into HttpURLConnection in order to open the connection in order to get the JSON data
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			//Set the request to GET or POST as per the requirements
			conn.setRequestMethod("GET");
			//Use the connect method to create the connection bridge
			conn.connect();
			//Get the response status of the Rest API
			int responseCode = conn.getResponseCode();
			System.out.println("Response code is: " +responseCode);
			
			//Iterating condition to if response code is not 200 then throw a runtime exception
			//else continue the actual process of getting the JSON data
			if(responseCode != 200)
				throw new RuntimeException("HttpResponseCode: " +responseCode);
			else
			{
				//Scanner functionality will read the JSON data from the stream
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
					inline+=sc.nextLine();
				}
				System.out.println("\nJSON Response in String format"); 
				System.out.println(inline);
				//Close the stream when reading the data has been finished
				sc.close();
			}
			
			//JSONParser reads the data from string object and break each data into key value pairs
			JSONParser parse = new JSONParser();
			//Type caste the parsed json data in json object
			JSONObject jobj = (JSONObject)parse.parse(inline);
			//Store the JSON object in JSON array as objects (For level 1 array element i.e Results)
			
			// Not used as of now - due to time bound.
     		// DarkSkyPojo darkSkyObj = (DarkSkyPojo) jobj;
			
			Double latitude = (Double) jobj.get("latitude");
			Double longitude = (Double) jobj.get("longitude");
			String timezone = (String) jobj.get("timezone");
			
			System.out.println("Latitude value --> "+latitude);
			System.out.println("Longitude value --> "+longitude);
			System.out.println("Timezone value --> "+timezone);
			
			JSONObject minutey= (JSONObject) jobj.get("minutely");
			
			System.out.println("Elements under Minutely object response.");
			System.out.println("\nSUMMARY : " +minutey.get("summary"));
			System.out.println("\nICON : " +minutey.get("icon"));
			
			// handling minutely data - array of elements.
			JSONArray minutelyData = (JSONArray) minutey.get("data");
			
			for(int j=0;j<minutelyData.size();j++)
			  {
			     //Same just store the JSON objects in an array
			     //Get the index of the JSON objects and print the values as per the index
			     JSONObject jsonobj_2 = (JSONObject) minutelyData.get(j);
			     //Store the data as String objects
			     long minutelyDatatime = (long) jsonobj_2.get("time");
			     long minutelyDataPrecipIntensity = (long) jsonobj_2.get("precipIntensity");
			     long minutelyDataPrecipProbability = (long) jsonobj_2.get("precipProbability");
			     
			     System.out.println(minutelyDatatime+" -- "+minutelyDataPrecipIntensity+" -- "+minutelyDataPrecipProbability);
			     
			     System.out.println();
			  }


			//Disconnect the HttpURLConnection stream
			conn.disconnect();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}