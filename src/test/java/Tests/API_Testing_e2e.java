package Tests;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class API_Testing_e2e {

    @Test
    public void e2eApiTest(){
       // store the login creditentials to the Hashmap
       HashMap<Object,Object> loginpayload = new HashMap<>();
       loginpayload.put("email", "sharathkraj5@gmail.com");
       loginpayload.put("password", "Sharath@1710");


        Playwright playwright = Playwright.create();
        APIRequestContext apiRequest = playwright.request().newContext();
        APIResponse apiResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login",
                RequestOptions.create().setData(loginpayload));
        Assert.assertTrue(apiResponse.ok());
        System.out.println(apiResponse.text());

        // extract payload form the response
        String token = JsonPath.read(apiResponse.text(), "$.token");
        System.out.println(token);

        // create a event
        String eventTitle = "Playwright API Testing";
        HashMap<Object,Object> eventpayload = new HashMap<>();
        eventpayload.put("title", "API QA Event meetup");
        eventpayload.put("description", "api");
        eventpayload.put("category", "Conference");
        eventpayload.put("venue", "Madurai");
        eventpayload.put("city", "Bangalore");
        eventpayload.put("eventDate", "2026-07-16T05:41.000Z");
        eventpayload.put("price", 100);
        eventpayload.put("totalSeats", 500);

        APIResponse eventResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/events",
                 RequestOptions.create().setHeader("Authorization","Bearer" +token)
                         .setData(eventpayload));

        // Assert.assertTrue(eventResponse.ok(), "API Response successful");

        Integer eventid = JsonPath.read(eventResponse.text(), "$.data.id");
        System.out.println("Event id is " + eventid);

        // Get Event

        APIResponse retriveEvents = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setQueryParam("page","1").setQueryParam("limit","1")
                        .setHeader("Authorization","Bearer" +token));

        Assert.assertTrue(retriveEvents.ok(), "API Response successful");

        List<Integer> alleventsIDs = JsonPath.read(retriveEvents.text(), "$.data[*].id");
        Assert.assertTrue(alleventsIDs.contains(eventid),"id is present");


        // delete

        APIResponse deleteResponce = apiRequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events/"+ eventid,
                RequestOptions.create().setHeader("Authorization","Bearer" +token));

        Assert.assertTrue(deleteResponce.ok());

     // verify the deletion is sucess
        APIResponse verifydelete = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create()
                        .setHeader("Authorization","Bearer" + token)
                        .setQueryParam("page","1").setQueryParam("limit","12"));

        Assert.assertTrue(verifydelete.ok(), "API Response successful");

        List<Integer> verifyids = JsonPath.read(retriveEvents.text(), "$.data[*].title");
        Assert.assertTrue(verifyids.contains(eventid),"id is present");

    }

}
