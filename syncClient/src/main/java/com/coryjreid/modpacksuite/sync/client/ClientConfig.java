/*
 * Copyright (C) 2021  Cory J. Reid
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
package com.coryjreid.modpacksuite.sync.client;

import com.typesafe.config.Config;

/**
 * Represents an {@code application.conf} file.
 */
public class ClientConfig {
    private final Config mConfig;

    public ClientConfig(final Config config) {
        mConfig = config;
    }

    /**
     * @return the rsync daemon address in {@code rsync://hostname:port/path/} format (note the trailing slash)
     */
    public String getRsyncAddress() {
        return "rsync://"
            + mConfig.getString("serverHostname")
            + ":"
            + mConfig.getInt("serverPort")
            + "/"
            + mConfig.getString("serverPath")
            + "/";
    }

    /**
     * @return the Minecraft instance root directory in {@code /path/to/dir/} format (note the trailing slash)
     */
    public String getMinecraftPath() {
        final String path = mConfig.getString("minecraftInstanceRoot").replace('\\', '/');
        return path.endsWith("/") ? path : path + "/";
    }
}
