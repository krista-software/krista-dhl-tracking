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

import com.krista.extensions.logistics.trackinganddelivery.dhl.catalog.entity.TrackingInfo;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class TrackingServiceImplementationTest {

    private static final Logger log = LoggerFactory.getLogger(TrackingServiceImplementationTest.class);
    private final TrackingServiceImplementation track = new TrackingServiceImplementation("WNVFKVnpfZNGJFxwjB7Q4AItMPcJJOG7","https://api-eu.dhl.com");
    private static final String trackingNumber = "2042611047314463";

    @Test
    void trackShipment() {
        log.info("Tracking package: "+trackingNumber);
        try {
            TrackingInfo trackingInfo = track.trackShipment(trackingNumber);
            log.info("Last Event: " +trackingInfo.lastEvent);
            assertNotNull(trackingInfo.lastEvent);
        }catch (Exception ex){
            log.info("Tracking Info client could not complete transaction with service");
        }
    }
}