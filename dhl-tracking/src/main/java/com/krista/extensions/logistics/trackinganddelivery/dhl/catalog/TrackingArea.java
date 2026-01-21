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

package com.krista.extensions.logistics.trackinganddelivery.dhl.catalog;

import app.krista.extension.impl.anno.Attribute;
import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import com.krista.extensions.logistics.trackinganddelivery.dhl.DHLTrackingAttributes;
import com.krista.extensions.logistics.trackinganddelivery.dhl.catalog.entity.TrackingInfo;
import com.krista.extensions.logistics.trackinganddelivery.dhl.service.implementation.TrackingServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;

@Domain(id = "catEntryDomain_b0ee93da-7064-481c-912b-7cecbca32540",
        name = "Tracking and Delivery",
        ecosystemId = "catEntryEcosystem_753b460d-53fc-4530-b6b1-61de354f3379",
        ecosystemName = "Logistics",
        ecosystemVersion = "0b1a81b2-73ef-4d0b-ba13-eec60c80b933")
public class TrackingArea {

    private static final Logger logger = LoggerFactory.getLogger(TrackingArea.class);
    @Inject
    DHLTrackingAttributes attributes;
    TrackingServiceImplementation service;

    /**
     * Retrieves comprehensive tracking information for a DHL shipment using its tracking number.
     * Queries the DHL tracking API and returns detailed package status, location, and event history.
     *
     * @param trackingNumber The DHL tracking number to query (required, must be valid DHL format)
     * @return TrackingInfo entity containing shipment status, events, location, and summary
     * @throws IllegalArgumentException if tracking number is invalid or shipment cannot be found
     */
    @CatalogRequest(
            id = "localDomainRequest_4c33577f-5c68-49c4-a6a2-894a495e2a44",
            name = "Track by Tracking Number",
            description = "Provides customer's package tracking information based on a tracking number for various shipping services.",
            area = "Tracking",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Desc(name = "Tracking Information", type = "Entity(Tracking Info)", required = false)
    public TrackingInfo trackByTrackingNumber(
            @Field(name = "Tracking Number", type = "Identifier", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String trackingNumber) {
        try {
            setConfig();
            return service.trackShipment(trackingNumber);
        } catch (Exception cause){
            logger.info(" Unable to Track by Tracking Number : "+cause.getMessage());
            throw new IllegalArgumentException(" Unable to Track , provide correct Tracking Number ");
        }
    }

    /**
     * Initializes the tracking service with configured DHL API credentials.
     * Creates a new TrackingServiceImplementation instance with API key and base URL.
     */
    private void setConfig() {
        service = new TrackingServiceImplementation(attributes.getApiKey(), attributes.getUrl());
    }

}