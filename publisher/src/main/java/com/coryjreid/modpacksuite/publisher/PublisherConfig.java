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
package com.coryjreid.modpacksuite.publisher;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.coryjreid.modpacksuite.config.ConfigPath;
import com.typesafe.config.Config;

public class PublisherConfig {
    private final Config mConfig;

    public PublisherConfig(final Config config) {
        mConfig = config;
    }

    public Path getMinecraftInstanceRootPath() {
        return Paths.get(mConfig.getString(ConfigPath.minecraftInstanceRoot()));
    }

    public List<Integer> getServerOnlyModIds() {
        return mConfig.getIntList(ConfigPath.serverOnlyModIds());
    }

    public List<Integer> getClientOnlyModIds() {
        return mConfig.getIntList(ConfigPath.clientOnlyModIds());
    }

    public List<Integer> getDevOnlyModIds() {
        return mConfig.getIntList(ConfigPath.devOnlyModIds());
    }
}
