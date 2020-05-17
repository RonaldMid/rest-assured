/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.restassured.config;

import io.restassured.filter.csrfcookietoheader.CSRFCookieToHeaderFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;


/**
 * Configure usage of the CSRF cookie to header pattern for REST Assured. Can be used in conjunction with a {@link CSRFCookieToHeaderFilter}.
 * Here you can define the default name of the cookie that delivers the CSRF token to the browser (by default it's {@value CSRFCookieToHeaderConfig#DEFAULT_COOKIE_NAME}).
 * You can also define the default name of the header in which the CSRF token is delivered back to the server.
 */
public class CSRFCookieToHeaderConfig implements Config {

    public static final String DEFAULT_COOKIE_NAME = "XSRF-TOKEN";
    public static final String DEFAULT_HEADER_NAME = "X-" + DEFAULT_COOKIE_NAME;
    private final String cookieName;
    private final String headerName;
    private final boolean isUserDefined;

    /**
     * Create a new configuration for the CSRF cookie to header pattern with cookie name {@value #DEFAULT_COOKIE_NAME} and no csrf token value.
     */
    public CSRFCookieToHeaderConfig() {
        this(DEFAULT_COOKIE_NAME, DEFAULT_HEADER_NAME, false);
    }

    /**
     * Create a new configuration for the CSRF cookie to header pattern with explicit names for the cookie and header.
     *
     * @param cookieName The name of the cookie that contains the CSRF token. By default it's {@value #DEFAULT_COOKIE_NAME}.
     * @param headerName the name of the header that contains the CSRF token. By default it's {@value #DEFAULT_HEADER_NAME}. Once a CSRF token is captured from a cookie, this header will be added for each request that uses this configuration instance (unless it's overwritten by the DSL).
     */
    public CSRFCookieToHeaderConfig(String cookieName, String headerName) {this(cookieName, headerName, true);}

    private CSRFCookieToHeaderConfig(String cookieName, String headerName, boolean isUserDefined) {
        Validate.notEmpty(cookieName, "Cookie name cannot be empty.");
        Validate.notEmpty(headerName, "Header name cannot be empty");
        this.cookieName = cookieName;
        this.headerName = headerName;
        this.isUserDefined = isUserDefined;
    }

    /**
     * Set the cookie name. This is the name of the cookie that contains the CSRF token. By default it's {@value #DEFAULT_COOKIE_NAME}.
     *
     * @param cookieName The name of the cookie
     * @return A new SessionConfig instance
     */
    public CSRFCookieToHeaderConfig cookieName(String cookieName) {
        return new CSRFCookieToHeaderConfig(cookieName, headerName, true);
    }

    /**
     * Set the header name. This is the name of the header that contains the CSRF token. By default it's {@value #DEFAULT_HEADER_NAME}.
     *
     * @param headerName The name of the header
     * @return A new SessionConfig instance
     */
    public CSRFCookieToHeaderConfig headerName(String headerName) {
        return new CSRFCookieToHeaderConfig(cookieName, headerName, true);
    }

    /**
     * @return The name of the cookie containing the CSRF token
     */
    public String cookieName() {
        return cookieName;
    }

    public String headerName() {
        return headerName;
    }

    /**
     * @return A static way to create a new CSRFCookieToHeaderConfig instance without calling "new" explicitly. Mainly for syntactic sugar.
     */
    public static CSRFCookieToHeaderConfig csrfCookieToHeaderConfig() {
        return new CSRFCookieToHeaderConfig();
    }

    /**
     * Syntactic sugar.
     *
     * @return The same CSRFCookieToHeaderConfig instance.
     */
    public CSRFCookieToHeaderConfig and() {
        return this;
    }

    @Override
    public boolean isUserConfigured() {
        return isUserDefined;
    }
}