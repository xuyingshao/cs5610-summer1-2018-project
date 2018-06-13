package server.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import server.models.Restaurant;

@RestController
@CrossOrigin(origins = "*")
public class YelpService {

  ObjectMapper mapper = new ObjectMapper();
  OkHttpClient client = new OkHttpClient();
  String YELP_API_KEY = "BFdreHHnLQOBS1qmDGgBWX0uhhs0EJbYyooSrCJJEOJomKpCn68uwJz" +
          "jistEXLHS1DIea0ec5GadKS1x1i568S0BhjKQzUs4uG-NcZ-8T343orYQJguWMEfajj0fW3Yx";

  @GetMapping("/api/yelp/restaurant")
  List<Restaurant> findAllRestaurants() throws IOException {
    Request request = new Request.Builder()
            .url("https://api.yelp.com/v3/businesses/search?location=boston&radius=40000")
            .get()
            .addHeader("authorization", "Bearer " + YELP_API_KEY)
            .addHeader("cache-control", "no-cache")
            .addHeader("postman-token", "d2f54ea5-ca7e-db6a-9b24-1228699d1030")
            .build();

    Response response = client.newCall(request).execute();

    JSONObject jsonObject = new JSONObject(response.body().string().trim());
    JSONArray myResponse = (JSONArray) jsonObject.get("businesses");

    return jsonArrayToObjectList(myResponse);
  }


  @GetMapping("/api/yelp/restautant/{yelpId}")
  Restaurant findRestaurantByYelpId(@PathVariable("yelpId") String yelpId) throws IOException {
    Request request = new Request.Builder()
            .url("https://api.yelp.com/v3/businesses/" + yelpId)
            .get()
            .addHeader("authorization", "Bearer " + YELP_API_KEY)
            .addHeader("cache-control", "no-cache")
            .addHeader("postman-token", "d2f54ea5-ca7e-db6a-9b24-1228699d1030")
            .build();

    Response response = client.newCall(request).execute();

    JSONObject jsonObject = new JSONObject(response.body().string().trim());

    return jsonToObject(jsonObject);
  }

  List<Restaurant> jsonArrayToObjectList(JSONArray response) {
    List<Restaurant> restaurants = new ArrayList<Restaurant>();
    for (int i = 0; i < response.length(); i++) {
      JSONObject temp = response.getJSONObject(i);

      Restaurant r = jsonToObject(temp);

//      r.setYelpId(temp.getString("id"));
//      r.setName(temp.getString("name"));
//      r.setImage_url(temp.getString("image_url"));
//      r.setDisplay_phone(temp.getString("display_phone"));
//      JSONArray displayAddress = temp.getJSONObject("location").getJSONArray("display_address");
//      r.setAddress(displayAddress.get(0).toString() + ", " + displayAddress.get(1).toString());
//      r.setPhone(temp.getString("phone"));
//      JSONArray categories = temp.getJSONArray("categories");
//      StringBuffer category = new StringBuffer();
//      for (int j = 0; j < categories.length(); j++) {
//        category.append(categories.getJSONObject(j).getString("title") + " ");
//      }
//      r.setCategory(category.toString());
//      r.setPhone(temp.getString("phone"));
//      r.setRating(temp.getInt("rating"));

      restaurants.add(r);
    }
    return restaurants;
  }

  Restaurant jsonToObject(JSONObject object) {

    Restaurant restaurant = new Restaurant();

    restaurant.setYelpId(object.getString("id"));
    restaurant.setName(object.getString("name"));
    restaurant.setImage_url(object.getString("image_url"));
    restaurant.setDisplay_phone(object.getString("display_phone"));
    JSONArray displayAddress = object.getJSONObject("location").getJSONArray("display_address");
    restaurant.setAddress(displayAddress.get(0).toString() + ", " + displayAddress.get(1).toString());
    restaurant.setPhone(object.getString("phone"));
    JSONArray categories = object.getJSONArray("categories");
    StringBuffer category = new StringBuffer();
    for (int j = 0; j < categories.length(); j++) {
      category.append(categories.getJSONObject(j).getString("title") + " ");
    }
    restaurant.setCategory(category.toString());
    restaurant.setPhone(object.getString("phone"));
    restaurant.setRating(object.getInt("rating"));

    return restaurant;
  }
}
