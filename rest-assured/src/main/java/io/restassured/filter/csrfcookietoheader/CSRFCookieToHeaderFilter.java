package io.restassured.filter.csrfcookietoheader;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class CSRFCookieToHeaderFilter implements Filter {

    private final AtomicReference<String> token = new AtomicReference<>();

    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        String cookieName = requestSpec.getConfig().getCSRFCookieToHeaderConfig().cookieName();
        String headerName = requestSpec.getConfig().getCSRFCookieToHeaderConfig().headerName();
        String cookieValue = requestSpec.getCookies().getValue(cookieName);

        //If a CSRF token has been captured, then set the CSRF cookie in the request if it is not there yet
        if (hasToken() && cookieValue == null) {
            cookieValue = token.get();
            requestSpec.cookie(cookieName, cookieValue);
        }

        //If a CSRF token has been captured, then set the CSRF header in the request if it is not there yet
        if (cookieValue != null && requestSpec.getHeaders().get(headerName) == null) {
            requestSpec.header(headerName, cookieValue);
        }

        //If the response contains a CSRF cookie, capture the CSRF token from it (overriding the existing value if there is one)
        final Response response = ctx.next(requestSpec, responseSpec);
        final String tokenInResponse = response.cookie(cookieName);
        if (isNotBlank(tokenInResponse)) {
            token.set(tokenInResponse);
        }

        return response;
    }

    /**
     * @return The last CSRF token captured by the filter.
     */
    public String getToken() {
        return token.get();
    }

    /**
     * @return <code>true</code> if a CSRF token has been returned from the server and captured by the filter, <code>false</code> otherwise.
     */
    public boolean hasToken() {
        return isNotBlank(token.get());
    }
}
