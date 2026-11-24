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

package com.krista.extensions.logistics.trackinganddelivery.dhl.catalog.entity;

import app.krista.extension.impl.anno.*;

@Domain(id = "catEntryDomain_b0ee93da-7064-481c-912b-7cecbca32540",
        name = "Tracking and Delivery",
        ecosystemId = "catEntryEcosystem_753b460d-53fc-4530-b6b1-61de354f3379",
        ecosystemName = "Logistics",
        ecosystemVersion ="07331c7e-890c-461e-afa8-3676bf437fa0")
@Entity(name = "Tracking Info", id = "localDomainEntity_e30cb990-36b7-4661-948c-b2b6053625d6", primaryKey = "Tracking Number", supportStore = false, options = {})
public class TrackingInfo {

    @Field.Text(name = "Tracking Number", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String trackingNumber;

    @Field.Text(name = "Provider", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String provider;

    @Field.Date(name = "Last Event Date and Time", required = true, includeTimeOfDay = true, showHowManyDaysInViewer = 60, allowPast = true, allowToday = true, allowFuture = true, defaultTimeSpan = 1, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public Long lastEventDateAndTime;

    @Field.Text(name = "Last Event", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String lastEvent;

    @Field.Text(name = "Last Status", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String lastStatus;

    @Field.Text(name = "Last Location", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String lastLocation;

    @Field(name = "Summary", type = "RichText", required = true, attributes = {@Attribute(name = "visualWidth", value = "L")}, options = {})
    public String summary;

    public TrackingInfo(String trackingNumber, String provider, Long lastEventDateAndTime, String lastEvent, String lastStatus, String lastLocation, String summary) {
        this.trackingNumber = trackingNumber;
        this.provider = provider;
        this.lastEventDateAndTime = lastEventDateAndTime;
        this.lastEvent = lastEvent;
        this.lastStatus = lastStatus;
        this.lastLocation = lastLocation;
        this.summary = summary;
    }
}