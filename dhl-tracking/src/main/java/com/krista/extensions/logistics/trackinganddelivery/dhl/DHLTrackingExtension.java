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

package com.krista.extensions.logistics.trackinganddelivery.dhl;

import app.krista.extension.impl.anno.*;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

@Java(version = Java.Version.JAVA_21)
@Field(name = DHLTrackingAttributes.URL, type = "Text", required = true)
@Field.Text(name = DHLTrackingAttributes.DHL_API_KEY, isSecured = true, required = true)
@StaticResource(path = "docs", file = "documentation")
@Extension(version = "2.0.1", name="Delivery and Tracking")
public class DHLTrackingExtension {
    private static final Logger logger = LoggerFactory.getLogger(DHLTrackingExtension.class);

    private final DHLTrackingAttributes DHLTrackingAttributes;

    @Inject
    public DHLTrackingExtension(DHLTrackingAttributes attributes) {
        this.DHLTrackingAttributes = attributes;
    }

    /**
     * Invoked when the extension is loaded into the Krista platform.
     * Initializes extension attributes from the invoker configuration.
     */
    @InvokerRequest(InvokerRequest.Type.INVOKER_LOADED)
    public void loadedInvoker() {
        logger.info("Invoker Loaded");
        DHLTrackingAttributes.loadFromInvoker();
    }

    /**
     * Provides custom tabs for the extension UI.
     *
     * @return Map containing tab name and path to documentation
     */
    @InvokerRequest(InvokerRequest.Type.CUSTOM_TABS)
    public Map<String, String> customTabs() {
        return Map.of("Documentation", "static/docs");
    }


    /**
     * Validates extension configuration attributes before saving.
     * Verifies URL format and tests DHL API connectivity with provided credentials.
     *
     * @param attributes Map containing extension configuration (dhl.url and dhl.api.key)
     * @throws RuntimeException if URL is invalid or credentials cannot be authenticated
     */
    @InvokerRequest(InvokerRequest.Type.VALIDATE_ATTRIBUTES)
    public void validateAttributes(Map<String, Object> attributes) {

        logger.info("Verifying: {}", attributes.get("dhl.url").toString());
        if (UrlValidator.getInstance().isValid(attributes.get("dhl.url").toString())) {
            try {
                getValidAccountStatus(attributes.get("dhl.api.key").toString(), attributes.get("dhl.url").toString());
            } catch (Exception cause) {
                logger.info(" Invalid Login Credentials " + cause);
                throw new RuntimeException(" Invalid Login Credentials ");
            }

            DHLTrackingAttributes.setApiKey(attributes.get("dhl.api.key").toString());
            DHLTrackingAttributes.setUrl(attributes.get("dhl.url").toString());
            logger.info("Valid Data!");
        } else {
            logger.info("Invalid URL, please verify your input");
            throw new RuntimeException("Invalid URL, please verify your input");
        }
    }

    /**
     * Tests the connection to DHL API using configured credentials.
     * Validates URL format and authenticates with DHL tracking service.
     *
     * @throws RuntimeException if URL is invalid or connection test fails
     */
    @InvokerRequest(InvokerRequest.Type.TEST_CONNECTION)
    public void testConnection() {

        logger.info("Verifying: {}", DHLTrackingAttributes.getUrl());
        if (UrlValidator.getInstance().isValid(DHLTrackingAttributes.getUrl())) {
            try {
                getValidAccountStatus(DHLTrackingAttributes.getApiKey(), DHLTrackingAttributes.getUrl());
            } catch (Exception cause) {
                logger.info(" Invalid Login Credentials " + cause);
                throw new RuntimeException(" Invalid Login Credentials ");
            }

            logger.info("Valid Data!");
        } else {
            logger.info("Invalid URL, please verify your input");
            throw new RuntimeException("Invalid URL, please verify your input");
        }
    }

    /**
     * Validates DHL API credentials by performing a test tracking request.
     * Uses a known tracking number to verify API key and URL are valid.
     *
     * @param API_KEY DHL API key for authentication
     * @param URL Base URL for DHL tracking API
     * @throws RuntimeException if credentials are invalid or API request fails
     */
    private void getValidAccountStatus(String API_KEY, String URL) {
        String trackingNumber = "2042611047314400";
        try {
            String lastEvent = trackShipment(trackingNumber, API_KEY, URL);
            logger.info(" Last Event: " + lastEvent);
        } catch (Exception cause) {
            logger.info(" Invalid Login Credentials " + cause);
            throw new RuntimeException(" Invalid Login Credentials ");
        }
    }

    /**
     * Performs a test shipment tracking request to DHL API.
     * Makes HTTP GET request to DHL tracking endpoint with provided credentials.
     *
     * @param trackingNumber DHL tracking number to query
     * @param API_KEY DHL API key for authentication
     * @param URL Base URL for DHL tracking API
     * @return JSON response body from DHL API if successful
     * @throws RuntimeException if authentication fails or request returns non-200 status
     */
    private static String trackShipment(String trackingNumber, String API_KEY, String URL) {
        String url = URL + "/track/shipments";

        WebTarget webTarget = ClientBuilder.newClient().target(url);
        webTarget = webTarget.queryParam("trackingNumber", trackingNumber);

        Invocation.Builder request = webTarget.request(MediaType.APPLICATION_JSON);
        request.header("DHL-API-Key", API_KEY);

        System.out.println("Requesting to: " + webTarget.getUri());
        System.out.println("Tracking Number: " + trackingNumber);

        Response response = request.get();
        try {
            logger.info(" Response Status " + response.getStatus());
            if (response.getStatus() == 200) {
                String responseBody = response.readEntity(String.class);
                return responseBody;
            } else if (response.getStatus() == 404) {
                logger.info(" No Data Found For that ShipmentId " + response.readEntity(String.class));
            } else if (response.getStatus() == 401) {
                logger.info(" Not an authorised User " + response.readEntity(String.class));
                throw new RuntimeException(" Invalid Login Credentials ");
            } else {
                throw new RuntimeException(" Invalid Login Credentials ");
            }
        } catch (Exception cause) {
            logger.info(" Invalid Login Credentials " + cause);
            throw new RuntimeException(" Invalid Login Credentials ");
        }
        return null;
    }

}
