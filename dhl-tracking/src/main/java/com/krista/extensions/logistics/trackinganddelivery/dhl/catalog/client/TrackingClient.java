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

package com.krista.extensions.logistics.trackinganddelivery.dhl.catalog.client;

import com.google.gson.Gson;
import com.krista.extensions.logistics.trackinganddelivery.dhl.dao.Shipments;
import com.krista.extensions.logistics.trackinganddelivery.dhl.util.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TrackingClient {

    private static final Logger log = LoggerFactory.getLogger(TrackingClient.class);
    private final RestClient restClient;
    private final Gson gson = new Gson();

    public TrackingClient(String clientID,String url){
        restClient = new RestClient(clientID,url);
    }

    /**
     * Tracks a DHL package using its tracking number.
     * Makes REST API call to DHL tracking service and deserializes JSON response.
     *
     * @param trackingNumber DHL tracking number to query
     * @return Shipments object containing all shipment data from DHL API
     * @throws RuntimeException if API call fails or tracking number is invalid
     */
    public Shipments trackPackage(String trackingNumber){
        String response = restClient.get("",paramParser("trackingNumber",trackingNumber));
        Shipments shipment = gson.fromJson(response, Shipments.class);
        log.info("Response" + shipment);
        return shipment;
    }

    /**
     * Converts a single parameter name-value pair into a Map for query parameters.
     * Helper method to create query parameter map for REST client.
     *
     * @param paramName Name of the query parameter
     * @param paramValue Value of the query parameter
     * @return Map containing the single parameter entry
     */
    private Map<String,Object> paramParser(String paramName,String paramValue){
        Map<String,Object> params = new HashMap<>();
        params.put(paramName,paramValue);
        return params;
    }

}
