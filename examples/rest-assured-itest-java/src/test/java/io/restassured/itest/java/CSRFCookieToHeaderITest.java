package io.restassured.itest.java;

import io.restassured.filter.csrfcookietoheader.CSRFCookieToHeaderFilter;
import io.restassured.itest.java.support.WithJetty;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CSRFCookieToHeaderITest extends WithJetty {

    @Test
    public void csrfCaptureAndDefaultUse() {
        CSRFCookieToHeaderFilter csrfFilter = new CSRFCookieToHeaderFilter();
        given().
            filter(csrfFilter).
        when().
            get("/csrfCookie").
        then().
            statusCode(200);

        given().
            filter(csrfFilter).
        when().
            post("/j_spring_security_check_with_csrf_cookie_and_header").
        then().
            statusCode(200).
            body(equalTo("OK"));
    }


    @Test
    public void csrfCookieFromRequestLeading() {
        CSRFCookieToHeaderFilter csrfFilter = new CSRFCookieToHeaderFilter();
        given().
            filter(csrfFilter).
        when().
            get("/csrfCookie").
        then().
            statusCode(200);

        given().
            cookie("XSRF-TOKEN", "12345").
            filter(csrfFilter).
        when().
            post("/j_spring_security_check_with_csrf_cookie_and_header").
        then().
            statusCode(401).
            body(equalTo("CSRF header X-XSRF-TOKEN has incorrect value"));
    }

    @Test
    public void csrfHeaderFromRequestLeading() {
        CSRFCookieToHeaderFilter csrfFilter = new CSRFCookieToHeaderFilter();
        given().
            filter(csrfFilter).
        when().
            get("/csrfCookie").
        then().
            statusCode(200);

        given().
            header("X-XSRF-TOKEN", "12345").
            filter(csrfFilter).
        when().
            post("/j_spring_security_check_with_csrf_cookie_and_header").
        then().
            statusCode(401).
            body(equalTo("CSRF header X-XSRF-TOKEN has incorrect value"));
    }
}
