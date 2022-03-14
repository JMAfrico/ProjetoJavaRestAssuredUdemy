package br.com.JMRest.core;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;


public class BaseTest implements Constantes{

	@BeforeClass
	public static void setup() {
		System.out.println(">>>>>>>Teste Iniciado<<<<<<<");
		RestAssured.given().log().all();
		RestAssured.baseURI = APP_BASE_URL;
		RestAssured.port = APP_PORT;
		RestAssured.basePath = APP_BASE_PATH;	
		
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.setContentType(APP_CONTENT_TYPE);	
		reqBuilder.log(APP_LOG_DETAIL);
		RestAssured.requestSpecification = reqBuilder.build();
		
		ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
		resBuilder.expectResponseTime(Matchers.lessThan(MAX_TIMEOUT));
		RestAssured.responseSpecification = resBuilder.build();

	}
}
