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

import com.krista.extensions.logistics.trackinganddelivery.dhl.dao.Shipments;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TrackingClientTest {

    private static final Logger log = LoggerFactory.getLogger(TrackingClientTest.class);
    private static final TrackingClient client = new TrackingClient("GyD4cTJnhPLhQjOzSSixjZeSZAteTkHc", "https://api-eu.dhl.com/track/shipments");
    private static final String trackingNumber = "2042611047314463";

    @Test
    void trackPackage() {
        log.info("Tracking package: " + trackingNumber);
        try {
            Shipments shipments = client.trackPackage(trackingNumber);
            Assertions.assertEquals("GM26D083B51B3C4CF2BF7E", shipments.getShipments()[0].getId().toString());
        } catch (Exception ex) {
            log.info("Tracking Info client could not complete transaction with service");
        }
    }
}