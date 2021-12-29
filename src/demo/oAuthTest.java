package demo;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.api;
import pojo.getCourse;
import pojo.webAutomation;

public class oAuthTest {

	public static void main(String[] args) {
		
		//Google does not allow user logins via automated framework, thus commenting out. 
		/*
		 * System.setProperty("webdriver.chrome.driver","C://chromedriver.exe");
		 * WebDriver driver = new ChromeDriver(); driver.get(
		 * "https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php"
		 * ); driver.findElement(By.cssSelector("input[type='email']")).sendKeys(
		 * "srinath123");
		 * driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER
		 * ); Thread.sleep(3000);
		 * driver.findElement(By.cssSelector("input[type='password']")).sendKeys(
		 * password);
		 * driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Keys.
		 * ENTER); Thread.sleep(4000);
		 */
			//String url = driver.getCurrentUrl();
		
			String[] webTitles = {"Selenium Webdriver Java", "Cypress", "Protractor"};
		
			String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AX4XfWgOTpQL-rO4-DF7WctXVbDVi0OtaPca6SsiQdyaX07w_IQRnxEZF1BxDTMuHPy3ng&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
			String partialcode = url.split("code=")[1];
			String code = partialcode.split("&scope")[0];
			System.out.println(code);
			
		
		//OAuth 2.0 
		String accessTokenResponse = given()
			.urlEncodingEnabled(false)
			.queryParams("code",code)
			.queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
			.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
			.queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
			.queryParams("grant_type", "authorization_code")
		.when().log().all()
			.post("https://www.googleapis.com/oauth2/v4/token").asString();
		
		JsonPath js = new JsonPath(accessTokenResponse);
		String accessToken= js.getString("access_token");
		
		
			
		/*
		 * String response = given() .queryParam("access_token", accessToken)
		 * .when().log().all()
		 * .get("https://rahulshettyacademy.com/getCourse.php").asString();
		 * 
		 * System.out.println(response);
		 */
		
		getCourse gc = given()
			.queryParam("access_token", accessToken)
			.expect().defaultParser(Parser.JSON)
		.when()
			.get("https://rahulshettyacademy.com/getCourse.php").as(getCourse.class);
		
		System.out.println(gc.getInstructor());
		System.out.println(gc.getLinkedIn());
		
		
		System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());
		
		//iterate through all the courses to get desired book's price
		List<api> apiCourses =gc.getCourses().getApi();
		for(int i=0; i<apiCourses.size(); i++) {
			
			if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
				System.out.println(apiCourses.get(i).getPrice());
				 				 
			}
		}

		ArrayList<String> a = new ArrayList<String>();
		
		
		List<webAutomation> webAutoCourses =gc.getCourses().getWebAutomation();
		for(int i=0; i<webAutoCourses.size(); i++) {
			a.add(webAutoCourses.get(i).getCourseTitle());
		}
		
		List<String> expectedList = Arrays.asList(webTitles);
		
		Assert.assertTrue(a.equals(expectedList));
		
		
	}

}
