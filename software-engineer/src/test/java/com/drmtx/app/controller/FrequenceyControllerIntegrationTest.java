package com.drmtx.app.controller;


import com.drmtx.app.Application;
import com.jayway.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class FrequenceyControllerIntegrationTest {

    @Value("${local.server.port}")
    int port;

    @Before
    public void setup(){
        RestAssured.port = port;
        System.setProperty("http.agent", "something");
    }


    @Test
    @Ignore
    /* This test actually hits reddit and thus can be brittle because reddit very agggresively rate limits api usage.
    *  Ignored for now, rely on more reliable services in the real world
    * */
    public void analyzeRedditAndGetFrequency(){

        //create a frequency analysis run
        String runid = given()
                .param("url", "https://reddit.com/r/java/comments/32pj67/java_reference_in_gta_v_beautiful/.json")
                .when()
                .post("/frequency/new")
                .then().statusCode(HttpStatus.SC_OK).extract().path("runid");


        //lookup the frequency analysis for that run and get the top two
        given()
                .pathParam("runid", runid)
                .param("count", 2)
                .expect()
                .body("size()", is(2))
                .when()
                .get("/frequency/{runid}")
                .then().statusCode(HttpStatus.SC_OK);

    }


}
