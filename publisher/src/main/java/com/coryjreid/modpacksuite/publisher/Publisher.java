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

import java.lang.invoke.MethodHandles;

import com.coryjreid.modpacksuite.config.ConfigLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Publisher {
    private static final Logger sLogger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Prevent instantiation.
     */
    private Publisher() {
        // Nothing to do.
    }

    public static void main(final String[] args) {
        final PublisherConfig config = new PublisherConfig(ConfigLoader.loadConfig(args));


    }
}
