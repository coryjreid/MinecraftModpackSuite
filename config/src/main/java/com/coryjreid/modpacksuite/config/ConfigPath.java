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
package com.coryjreid.modpacksuite.config;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Creates HOCON configuration path expressions to find settings in a {@link com.typesafe.config.Config}. See <a
 * href="https://github.com/lightbend/config/blob/master/HOCON.md#path-expressions">Path Expressions</a> for more
 * information.
 */
public final class ConfigPath {
    /**
     * Prevent instantiation.
     */
    private ConfigPath() {
        // Nothing to do.
    }

    public static String minecraftInstanceRoot() {
        return createPathExpression(PathComponent.MINECRAFT_INSTANCE_ROOT);
    }

    public static String serverOnlyModIds() {
        return createPathExpression(
            PathComponent.MOD_EXCEPTIONS,
            PathComponent.SERVER_ONLY_MOD_IDS);
    }

    public static String clientOnlyModIds() {
        return createPathExpression(
            PathComponent.MOD_EXCEPTIONS,
            PathComponent.CLIENT_ONLY_MOD_IDS);
    }

    public static String devOnlyModIds() {
        return createPathExpression(
            PathComponent.MOD_EXCEPTIONS,
            PathComponent.DEV_ONLY_MOD_IDS);
    }

    /**
     * Creates a configuration path expression for finding settings in {@link com.typesafe.config.Config}s.
     *
     * @param expressions the {@link PathComponent}s to build this path from
     * @return the String representing the path expression
     */
    private static String createPathExpression(final PathComponent... expressions) {
        return Arrays.stream(expressions).map(PathComponent::toString).collect(Collectors.joining("."));
    }

    /**
     * Valid path expression components for this program.
     */
    private enum PathComponent {
        MINECRAFT_INSTANCE_ROOT("minecraftInstanceRoot"),
        MOD_EXCEPTIONS("modExceptions"),
        CLIENT_ONLY_MOD_IDS("clientOnly"),
        SERVER_ONLY_MOD_IDS("serverOnly"),
        DEV_ONLY_MOD_IDS("devOnly");

        private final String mValue;

        PathComponent(final String value) {
            mValue = value;
        }

        @Override
        public String toString() {
            return mValue;
        }
    }
}
