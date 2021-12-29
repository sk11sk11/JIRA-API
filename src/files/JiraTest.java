package files;
import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
//https://docs.atlassian.com/software/jira/docs/api/REST/7.0-SNAPSHOT/#api/2/issue-addComment
public class JiraTest {

	public static void main(String[] args) {
		// Login scenario....take away is the sessionFilter and pathParam
	
		SessionFilter session = new SessionFilter();
		
			
		String authResponse = 
		given().log().all()
			.header("Content-Type","application/json")
			.body("{ \"username\": \"shashank.ken\", \"password\": \"ATG7foot\" }")
			.filter(session)
		.when().log().all()
			.post("/rest/auth/1/session")
		.then().log().all()
			.extract().response().asString();
		
			
String expectedMessage = "Hi, How are you?";
		//ADD Comment
		RestAssured.baseURI = "http://localhost:8080";
		String addCommentResponse = given().log().all()
			.pathParam("id", "10005")
			.header("Content-Type","application/json")
			.body("{\r\n"
				+ "    \"body\": \""+expectedMessage+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}")
			.filter(session)
		.when().log().all()
			.post("/rest/api/2/issue/{id}/comment")
		.then().log().all()
			.assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js = new JsonPath(addCommentResponse);
		String commentID = js.getString("id");
		
		/*
		 * //ADD Attachment... //sending files as Attachments using RestAssured using
		 * multiPart method... 
		 * //curl -D- -u admin:admin -X POST -H
		 * "X-Atlassian-Token: nocheck" -F "file=@myfile.txt"
		 * http://myhost/rest/api/2/issue/TEST-123/attachments
		 */		
		/*
		 * given().log().all() .header("X-Atlassian-Token","nocheck")
		 * .header("Content-Type","multipart/form-data") .filter(session)
		 * .pathParam("id", "10005") .multiPart("file", new File("jira.txt"))
		 * .when().log().all() .post("/rest/api/2/issue/{id}/attachments")
		 * .then().log().all() .assertThat().statusCode(200);
		 */
		
		
		//GET issue
		//limit the output by adding queryParam
		String issueDetails = given().log().all()
			.filter(session)
			.pathParam("id", "10005")
			.queryParam("fields", "comment")//to get only the comment field
		.when()
			.get("/rest/api/2/issue/{id}")
		.then().log().all()
			.extract().response().asString();
		System.out.println(issueDetails);
		
		JsonPath js1 = new JsonPath(issueDetails);
		int commentsCount = js1.getInt("fields.comment.comments.size()");
		
		for(int i=0; i<commentsCount; i++) {
			
			String commentIDIssue = js1.get("fields.comment.comments["+i+"].id");
			
			if(commentIDIssue.equalsIgnoreCase(commentID)) {
				String message = js1.get("fields.comment.comments["+i+"].body").toString();
				System.out.println(message);
				Assert.assertEquals(message, expectedMessage);
			}
		}
		
		
		
	}
	
	

}
