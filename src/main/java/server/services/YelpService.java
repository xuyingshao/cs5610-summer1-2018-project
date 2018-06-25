package server.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
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
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@CrossOrigin(origins = "https://cs5610-project-client.herokuapp.com", allowCredentials = "true")
public class YelpService {

  ObjectMapper mapper = new ObjectMapper();
  OkHttpClient client = new OkHttpClient();
  String YELP_API_KEY = "BFdreHHnLQOBS1qmDGgBWX0uhhs0EJbYyooSrCJJEOJomKpCn68uwJz" +
          "jistEXLHS1DIea0ec5GadKS1x1i568S0BhjKQzUs4uG-NcZ-8T343orYQJguWMEfajj0fW3Yx";

  @ResponseBody
  @GetMapping("/api/yelp/restaurant/phone/{phone}")
  List<Restaurant> findRestaurantByMatching(@PathVariable("phone") String phone,
                                            HttpServletResponse responsebody)
          throws IOException {
    String url = "https://api.yelp.com/v3/businesses/search/phone";
    url = url + "?phone=" + phone;

    Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("authorization", "Bearer " + YELP_API_KEY)
            .addHeader("cache-control", "no-cache")
            .addHeader("postman-token", "d2f54ea5-ca7e-db6a-9b24-1228699d1030")
            .build();

    Response response = client.newCall(request).execute();

    JSONObject jsonObject = new JSONObject(response.body().string().trim());
    JSONArray myResponse = (JSONArray) jsonObject.get("businesses");

    if (myResponse.length() != 1) {
      responsebody.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    return jsonArrayToObjectList(myResponse);
  }

  @GetMapping("/api/yelp/restaurant")
  List<Restaurant> findAllRestaurants() throws IOException {
    Request request = new Request.Builder()
            .url("https://api.yelp.com/v3/businesses/search?location=boston&limit=50")
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


  @GetMapping("/api/yelp/restaurant/term/{term}/location/{location}")
  List<Restaurant> findRestaurantsByTermAndLocation(@PathVariable("term") String term,
                                             @PathVariable("location") String location)
          throws IOException {
    String url = "https://api.yelp.com/v3/businesses/search?limit=50";
    location = location.replace(" ", "%20");
    term = term.replace(" ", "%20");
    url = url + "&term=" + term + "&location=" + location;

    Request request = new Request.Builder()
            .url(url)
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


  @GetMapping("/api/yelp/restaurant/location/{location}")
  List<Restaurant> findRestaurantsByLocation(@PathVariable("location") String location)
          throws IOException {
    String url = "https://api.yelp.com/v3/businesses/search?limit=50";
    location = location.replace(" ", "%20");
    url = url + "&location=" + location;

    Request request = new Request.Builder()
            .url(url)
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

  @GetMapping("/api/yelp/restaurant/{yelpId}")
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
      Restaurant restaurant = jsonToObject(temp);
      restaurants.add(restaurant);
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

    StringBuffer address = new StringBuffer();
    for (int j = 0; j < displayAddress.length(); j++) {
      address.append(displayAddress.get(j)).append(", ");
    }
    restaurant.setAddress(address.substring(0, address.length() - 2).toString());
    restaurant.setPhone(object.getString("phone"));
    JSONArray categories = object.getJSONArray("categories");
    StringBuffer category = new StringBuffer();
    for (int j = 0; j < categories.length(); j++) {
      category.append(categories.getJSONObject(j).getString("title") + ", ");
    }
    restaurant.setCategory(category.substring(0, category.length() - 2).toString());
    restaurant.setPhone(object.getString("phone"));
    restaurant.setRating(object.getInt("rating"));

    restaurant.setPhone(object.getString("phone"));
    restaurant.setRating(object.getInt("rating"));

    return restaurant;
  }
}
