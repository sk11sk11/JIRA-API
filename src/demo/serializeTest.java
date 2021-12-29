package demo;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import pojo.AddPlace;
import pojo.Location;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

public class serializeTest {

	public static void main(String[] args) {
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		//create a java object with supported pojo classes
		AddPlace ap = new AddPlace();
		ap.setAccuracy(50);
		ap.setAddress("1778 Plano Pkwy");
		ap.setLanguage("Mandarin");
		ap.setPhone_number("345-456-2342");
		ap.setWebsite("https://www.rahulshetty.com");
		ap.setName("Interstell");
		List<String> myList = new ArrayList<String>();
		myList.add("Yellowstone National Park");
		myList.add("Yosemite National Park");
		ap.setTypes(myList);
		Location sierra = new Location();
		sierra.setLat(-40.4534);
		sierra.setLng(54.3453);
		ap.setLocation(sierra);
		
		Response postResponse = given().log().all()
			.queryParam("key", "qaclick123")
			.body(ap)//serialized to JSON and passed in body.
		.when().log().all()
			.post("/maps/api/place/add/json")
		.then().log().all()
			.assertThat().statusCode(200).extract().response();
		
		String postResponseString = postResponse.asString();
		System.out.println(postResponseString);

	}

}
