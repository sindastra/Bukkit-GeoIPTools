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
package uk.org.whoami.geoip;

import uk.org.whoami.geoip.util.ConsoleLogger;
import uk.org.whoami.geoip.util.MetricsLite;
import uk.org.whoami.geoip.util.Settings;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This bukkit plugin provides an API for Maxmind GeoIP database lookups.
 *
 * @author Sebastian Köhler <whoami@whoami.org.uk>
 */
//Updating & cleanup by Fishrock123 <Fishrock123@rocketmail.com>
public class GeoIPTools extends JavaPlugin {
	
    private Settings settings;
    private GeoIPLookup geo = null;

    @Override
    public void onLoad() {
        settings = new Settings(this);
        ConsoleLogger.info("Starting database updates");
        getServer().getScheduler().scheduleAsyncDelayedTask(this, new UpdateTask(this, Bukkit.getConsoleSender()), 0);
    }

    @Override
    public void onEnable() {
        try {
            new MetricsLite(this).start();
            this.geo = new GeoIPLookup(settings);
        } catch (IOException e) {
            ConsoleLogger.info(e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        if (geo != null) {
            geo.close();
            geo = null;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("geoupdate")) {
            if (sender.hasPermission("GeoIPTools.geoupdate")) {
                getServer().getScheduler().scheduleAsyncDelayedTask(this, new UpdateTask(this, sender), 0);
            }
            return true;
        }
        return false;
    }

    Settings getSettings() {
        return settings;
    }
    
    /**
     * Get a GeoIPLookup.
     * @return A GeoIPLookup class
     */
    public GeoIPLookup getGeoIPLookup() {
        return geo;
    }
}
