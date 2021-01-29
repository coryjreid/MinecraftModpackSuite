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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.base.Preconditions;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Loads {@link Config}s.
 */
public final class ConfigLoader {
    private static final String CONFIG_FILE_ARGUMENT_KEY = "configFile";
    private static final String DEFAULT_CONFIG_FILE_NAME = "application.conf";

    private static final JSAP sArgumentParser = new JSAP();

    static {
        try {
            sArgumentParser.registerParameter(
                new UnflaggedOption(CONFIG_FILE_ARGUMENT_KEY)
                    .setStringParser(JSAP.STRING_PARSER)
                    .setRequired(true)
                    .setHelp("The absolute path to the configuration file. e.g. /path/to/config/file.cfg"));
        } catch (final JSAPException exception) {
            System.err.println("Failed to setup the argument parser");
            System.exit(1);
        }
    }

    /**
     * Prevent instantiation.
     */
    private ConfigLoader() {
        // Nothing to do.
    }

    /**
     * Attempts to load either an {@link #DEFAULT_CONFIG_FILE_NAME} next to a JAR or a config file parameter passed in
     * via command line arguments.
     *
     * @param programArgs the string array of program arguments to parse (never {@code null})
     * @return the loaded {@link Config}
     */
    public static Config loadConfig(final String[] programArgs) {
        Preconditions.checkNotNull(programArgs, "programArgs cannot be null");

        final Path configFile = Paths.get(programArgs.length == 0
            ? DEFAULT_CONFIG_FILE_NAME
            : parseArguments(programArgs).getString(CONFIG_FILE_ARGUMENT_KEY));

        if (Files.exists(configFile)) {
            return ConfigFactory.parseFile(configFile.toFile());
        } else {
            System.err.println("ERROR: A configuration file cannot be found!");
            System.err.println("Create '" + configFile.toAbsolutePath() + "' or specify a path to a config file");
            System.exit(1);
            return null;
        }
    }

    private static JSAPResult parseArguments(final String[] args) {
        final JSAPResult parseResult = sArgumentParser.parse(args);
        if (parseResult.success()) {
            return parseResult;
        } else {
            System.err.println("Failed to parse command line arguments!");
            System.err.println("Usage:\njava -jar " + getExecutableName() + " " + sArgumentParser.getUsage() + "\n");
            System.err.println("Help:\n" + sArgumentParser.getHelp());
            System.exit(1);
            return null;
        }
    }

    private static String getExecutableName() {
        return new File(ConfigLoader.class.getProtectionDomain()
            .getCodeSource()
            .getLocation()
            .getPath()).getName();
    }
}
