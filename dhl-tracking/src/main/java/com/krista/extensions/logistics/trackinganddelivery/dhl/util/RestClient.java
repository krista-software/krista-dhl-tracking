/*
 * Dhl Tracking Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>. 
 */

package com.krista.extensions.logistics.trackinganddelivery.dhl.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import java.util.Map;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class RestClient {

    private static final Logger log = LoggerFactory.getLogger(RestClient.class);
    private static final String DHL_API_KEY_HEADER = "DHL-API-Key";
    private final WebTarget webTarget;
    private final String DHL_API_ID;

    public RestClient(String apiKey,String url) {
        this.DHL_API_ID = apiKey;
        webTarget = ClientBuilder.newClient().target(url);
    }

    /**
     * Executes HTTP GET request to DHL API with query parameters.
     * Builds request with API key authentication header and processes response.
     *
     * @param url Relative path to append to base URL (can be empty string)
     * @param queryParams Map of query parameters to include in request
     * @return Response body as JSON string
     * @throws RuntimeException if authentication fails (401)
     * @throws ClientErrorException if request fails with client error (4xx)
     */
    public String get(String url, Map<String, Object> queryParams) {

        WebTarget resourceTarget = webTarget.path(url);
        for (Map.Entry<String, Object> entrySet : queryParams.entrySet()) {
            resourceTarget = resourceTarget.queryParam(entrySet.getKey(), entrySet.getValue());
        }
        Invocation.Builder request = resourceTarget.request(APPLICATION_JSON);
        request.header(DHL_API_KEY_HEADER, DHL_API_ID);

        log.info("Requesting to : "+webTarget.getUri());
        log.info("Params: "+queryParams);
        return handleResponse(request.get());
    }

    /**
     * Processes HTTP response from DHL API.
     * Handles success (2xx), unauthorized (401), and error responses.
     *
     * @param response HTTP response from DHL API
     * @return Response body as string if successful
     * @throws RuntimeException if response is 401 Unauthorized
     * @throws ClientErrorException if response is any other error status
     */
    private static String handleResponse(Response response) {
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
            return response.readEntity(String.class);
        else if (response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
            log.info("Request was: {} - {}", response.getStatus(), response.getStatusInfo().getReasonPhrase());
            throw new RuntimeException(response.getStatusInfo().getReasonPhrase());
        }else{
            log.info("CODE: {}; Response Body: {}", response.getStatus(), response.readEntity(String.class));
            throw new ClientErrorException(response);
        }
    }
}
