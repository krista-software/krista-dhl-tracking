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

package com.krista.extensions.logistics.trackinganddelivery.dhl.service.implementation;

import com.krista.extensions.logistics.trackinganddelivery.dhl.catalog.client.TrackingClient;
import com.krista.extensions.logistics.trackinganddelivery.dhl.catalog.entity.TrackingInfo;
import com.krista.extensions.logistics.trackinganddelivery.dhl.service.TrackingService;
import com.krista.extensions.logistics.trackinganddelivery.dhl.dao.Shipment;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TrackingServiceImplementation implements TrackingService {

    private final TrackingClient client;
    private static final Logger logger = LoggerFactory.getLogger(TrackingServiceImplementation.class);

    public TrackingServiceImplementation(String apiKey, String url) {
        client = new TrackingClient(apiKey, url + "/track/shipments");
    }

    /***
     * Track shipment with a tracking number
     * @param trackingNumber Tracking number of the DHL Package
     * @return TrackingInfo Object of the Package.
     */
    @Override
    public TrackingInfo trackShipment(String trackingNumber) {
        if (StringUtils.isNotEmpty(trackingNumber)) {
            Shipment shipment = client.trackPackage(trackingNumber).getShipments()[0];
            return trackingInfoParser(shipment, trackingNumber);
        } else {
            throw new NullPointerException("Empty Tracking Number");
        }
    }

    /**
     * Transforms DHL Shipment data into Krista TrackingInfo entity.
     * Extracts key information including latest event, status, location, and summary.
     *
     * @param shipment The shipment data from DHL API
     * @param trackingNumber Original tracking number used for the query
     * @return TrackingInfo entity populated with shipment details
     */
    private TrackingInfo trackingInfoParser(Shipment shipment, String trackingNumber) {

        return new TrackingInfo(
                trackingNumber,
                "DHL",
                timestampStringToLong(shipment.getEvents().get(0).getTimestamp()),
                shipment.getEvents().get(0).getStatus(),
                shipment.getStatus().getStatus(),
                shipment.getEvents().get(0).getLocation().getAddress().getAddressLocality(),
                parseSummary(shipment)
        );
    }

    /**
     * Generates a human-readable summary of shipment information.
     * Includes package reference, shipping method, weight, and current status.
     *
     * @param shipment The shipment data from DHL API
     * @return Formatted multi-line summary string
     */
    private String parseSummary(Shipment shipment) {
        return new StringBuilder("The package " + shipment.getDetails().getReferences().get(0).getNumber())
                .append("\nWas sent by " + shipment.getDetails().getProduct().getProductName())
                .append("\nWeight " + shipment.getDetails().getWeight().getValue() + " " + shipment.getDetails().getWeight().getUnitText())
                .append("\nCurrent Status: " + shipment.getEvents().get(0).getStatus() + " at " + shipment.getEvents().get(0).getTimestamp())
                .toString();
    }

    /**
     * Converts ISO 8601 timestamp string to Unix epoch milliseconds.
     * Parses DHL API timestamp format (yyyy-MM-dd'T'HH:mm:ss) to long value.
     *
     * @param timestampString Timestamp string in ISO 8601 format
     * @return Unix epoch time in milliseconds, or current time if parsing fails
     */
    private long timestampStringToLong(String timestampString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date;
        try {
            date = dateFormat.parse(timestampString);
        } catch (ParseException cause) {
            date = new Date();
            logger.warn("Error while converting timestamp string {} to long: {}", timestampString, cause.getMessage());
        }
        return date.getTime();
    }


}
