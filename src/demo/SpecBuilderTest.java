package demo;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

public class SpecBuilderTest {

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
		
		
		RequestSpecification req = new RequestSpecBuilder()
			.setBaseUri("https://rahulshettyacademy.com")
			.addQueryParam("key", "qaclick123")
			.setContentType(ContentType.JSON).build();
		
		
		ResponseSpecification resspec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		
		RequestSpecification postResponse = given()
			.spec(req)
			.body(ap);//serialized to JSON and passed in body.
		
		Response response = postResponse.when().log().all()
			.post("/maps/api/place/add/json")
		.then().log().all()
			.spec(resspec).extract().response();
		
		String postResponseString = response.asString();
		System.out.println(postResponseString);

	}

}
