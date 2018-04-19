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
package uk.org.whoami.geoip.util;

import java.util.Date;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * @author Sebastian Köhler <whoami@whoami.org.uk>
 */
//Updating & cleanup by Fishrock123 <Fishrock123@rocketmail.com>
public class Settings {

    final String CITYDATABASEPATH = "./plugins/GeoIPTools/GeoLiteCity.dat";
    final String COUNTRYDATABASEPATH = "./plugins/GeoIPTools/GeoIP.dat";
    final String IPV6DATABASEBATH = "./plugins/GeoIPTools/GeoIPv6.dat";
    private FileConfiguration conf;
    private Plugin plugin;

    public Settings(Plugin plugin) {
        this.conf = plugin.getConfig();
        this.plugin = plugin;
        write();
    }

    public final void write() {
        getIPv6DatabaseURL();
        getCityDatabaseURL();
        getCountryDatabaseURL();
        getLastUpdated();
        isUpdaterDisabled();
        getDaysUntilUpdate();
        plugin.saveConfig();
    }
    
    public boolean isUpdaterDisabled() {
        String key = "Update:disabled";
        if (conf.getString(key) == null) {
            conf.set(key, false);
        }
        return conf.getBoolean(key, false);
    }

    public void setLastUpdated(long lastUpdated) {
        String key = "Update:lastUpdated";
        conf.set(key, String.valueOf(lastUpdated));
    }

    public long getLastUpdated() {
        String key = "Update:lastUpdated";
        if (conf.getString(key) == null) {
            conf.set(key, "0");
        }
        return Long.parseLong(conf.getString(key));
    }
    
    public long getDaysUntilUpdate() {
        String key = "Update:daysBetweenUpdates";
        if (conf.getString(key) == null) {
            conf.set(key, "10");
        }
        return Long.parseLong(conf.getString(key));
    }
    
    public boolean shouldUpdate() {
    	if (new Date().getTime() - getLastUpdated() > 86400000 * getDaysUntilUpdate()) {
    		return true;
    	} else {
    		return false;
    	}
    }
    public String getIPv6DatabaseURL() {
        String key = "URL.IPv6Database";
        if (conf.getString(key) == null) {
            conf.set(key, "http://geolite.maxmind.com/download/geoip/database/GeoIPv6.dat.gz");
        }
        return conf.getString(key);
    }

    public String getCityDatabaseURL() {
        String key = "URL.CityDatabase";
        if (conf.getString(key) == null) {
            conf.set(key, "http://geolite.maxmind.com/download/geoip/database/GeoLiteCity.dat.gz");
        }
        return conf.getString(key);
    }

    public String getCountryDatabaseURL() {
        String key = "URL.CountryDatabase";
        if (conf.getString(key) == null) {
            conf.set(key, "http://geolite.maxmind.com/download/geoip/database/GeoLiteCountry/GeoIP.dat.gz");
        }
        return conf.getString(key);
    }

    public String getIPv6DatabasePath() {
        String key = "Path.IPv6Database";
        if (conf.getString(key) == null) {
            return this.IPV6DATABASEBATH;
        }
        return conf.getString(key);
    }

    public String getCityDatabasePath() {
        String key = "Path.cityDatabase";
        if (conf.getString(key) == null) {
            return this.CITYDATABASEPATH;
        }
        return conf.getString(key);
    }

    public String getCountryDatabasePath() {
        String key = "Path.countryDatabase";
        if (conf.getString(key) == null) {
            return this.COUNTRYDATABASEPATH;
        }
        return conf.getString(key);
    }
}
