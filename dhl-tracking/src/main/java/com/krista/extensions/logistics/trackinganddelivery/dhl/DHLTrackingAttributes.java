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

import app.krista.extension.executor.Invoker;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.inject.Named;

@Service
public class DHLTrackingAttributes {
    public static final String DHL_API_KEY = "dhl.api.key"; //
    private static final Logger log = LoggerFactory.getLogger(DHLTrackingAttributes.class);
    public static final String URL = "dhl.url";
    private String url;
    private String apiKey;

    @Inject
    @Named("self")
    private Invoker self;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        if (this.self != null && this.self.getAttributes().containsKey(URL) && this.self.getAttributes().get(URL) != null) {
            return this.self.getAttributes().get(URL).toString();
        } else {
            log.warn("Could not find url in invoker attributes. Using privately managed:{} ", this.url);
            return url;
        }
    }

    public String getApiKey() {
        if (this.self != null && this.self.getAttributes().containsKey(DHL_API_KEY) && this.self.getAttributes().get(DHL_API_KEY) != null) {
            return this.self.getAttributes().get(DHL_API_KEY).toString();
        } else {
            log.warn("Could not find API Key in invoker attributes. Using privately managed:{} ", this.apiKey);
            return apiKey;
        }
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public void loadFromInvoker() {
        this.url = self.getAttributes().get(URL).toString();
        this.apiKey = self.getAttributes().get(DHL_API_KEY).toString();
    }

}

