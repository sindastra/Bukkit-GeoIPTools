/*
 * Copyright 2011 Sebastian Köhler <sebkoehler@whoami.org.uk>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.sindastra.BukkitGeoIPTools;

import com.maxmind.geoip.Country;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

import io.github.sindastra.BukkitGeoIPTools.util.ConsoleLogger;
import io.github.sindastra.BukkitGeoIPTools.util.Settings;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

/**
 * @author Sebastian Köhler <sebkoehler@whoami.org.uk>
 */
// Cleanup by Fishrock123 <Fishrock123@rocketmail.com>
public class GeoIPLookup {

    private LookupService geocities = null;
    private LookupService geocountries = null;
    private LookupService geov6 = null;
    private Settings settings;

    protected GeoIPLookup(Settings settings) throws IOException {
        this.settings = settings;
        this.geocountries = new LookupService(settings.getCountryDatabasePath(),LookupService.GEOIP_MEMORY_CACHE);
        this.geov6 = new LookupService(settings.getIPv6DatabasePath(),LookupService.GEOIP_MEMORY_CACHE);
        this.geocities = new LookupService(settings.getCityDatabasePath(),LookupService.GEOIP_MEMORY_CACHE);
    }

    /**
     * Look up a Country in the database.
     *
     * @param inet Can be Inet4Address or Inet6Address
     * @return The country
     */
    public synchronized Country getCountry(InetAddress inet) {
        if (inet instanceof Inet4Address) {
            if (geocountries != null) {
                return geocountries.getCountry(inet);
            } else {
                ConsoleLogger.info("Uninitialised LookupService");
                return new Country("--", "N/A");
            }
        }
        if (inet instanceof Inet6Address) {
            if (geov6 != null) {
                return geov6.getCountryV6(inet);
            } else {
                ConsoleLogger.info("Uninitialised IPv6 LookupService");
                return new Country("--", "N/A");
            }
        }
        //Will never be reached
        ConsoleLogger.info("I see you are using IPv5");
        return new Country("--", "N/A");
    }

    /**
     * Look up a Location in the database. The Object needs to be created with
     * the CITYDATABASE bitmask for this method wo work.
     *
     * @param inet A Inet4Address
     * @return Location or null if the city database was not initialised
     */
    public synchronized Location getLocation(InetAddress inet) {
        if (inet instanceof Inet4Address) {
            if (geocities != null) {
                return geocities.getLocation(inet);
            } else {
                ConsoleLogger.info("Uninitialised LookupService");
            }
        } else if (inet instanceof Inet6Address) {
            ConsoleLogger.info("IPv6 is not supported for getLocation");
        }
        return null;
    }

    synchronized void reload() throws IOException {
        if (geocities != null) {
            geocities.close();
            geocities = new LookupService(settings.getCityDatabasePath(),LookupService.GEOIP_MEMORY_CACHE);
        }
        if (geov6 != null) {
            geov6.close();
            geov6 = new LookupService(settings.getIPv6DatabasePath(),LookupService.GEOIP_MEMORY_CACHE);
        }
        if(geocountries != null) {
            geocountries.close();
            geocountries = new LookupService(settings.getCountryDatabasePath(),LookupService.GEOIP_MEMORY_CACHE);
        }
    }

    synchronized void close() {
        if (geocities != null) {
            geocities.close();
            geocities = null;
        }
        if (geov6 != null) {
            geov6.close();
            geov6 = null;
        }
        if (geocountries != null) {
            geocountries.close();
            geocountries = null;
        }
    }
}
