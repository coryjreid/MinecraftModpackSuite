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

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import com.coryjreid.modpacksuite.config.ConfigLoader;
import com.coryjreid.modpacksuite.sync.client.gui.ConfigurationGenerationDialog;
import com.github.fracpete.processoutput4j.core.StreamingProcessOutputType;
import com.github.fracpete.processoutput4j.core.StreamingProcessOwner;
import com.github.fracpete.processoutput4j.output.StreamingProcessOutput;
import com.github.fracpete.rsync4j.RSync;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * An application which connects to an server hosted by Rsync Daemon for synchronizing a Minecraft directory.
 */
public class SyncClient extends Application {
    private static final Logger sLogger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * A map containing a "source pattern" to "destination pattern" mapping. Please see rsync documentation for more
     * information.
     */
    private static final Map<String, String> sTransferMap = new LinkedHashMap<>();

    /**
     * The {@link TextArea} for the program's output.
     */
    private static final TextArea sConsole = new TextArea();

    static {
        // Mods
        // Important: The order is important as is the inclusion of ** on clientmods/. The ** wildcard ensures that
        // the "force delete" isn't triggered when syncing client mods so that we don't accidentally delete all the
        // required server mods
        sTransferMap.put("mods/", "mods");
        sTransferMap.put("clientmods/**", "mods");

        // Config
        sTransferMap.put("config/", "config");
        sTransferMap.put("defaultconfigs/", "defaultconfigs");

        // KubeJS
        sTransferMap.put("kubejs/", "kubejs");

        // Resourcepacks
        sTransferMap.put("resourcepacks/", "resourcepacks");
    }

    @Override
    public void start(final Stage primaryStage) throws IOException {
        final ClientConfig clientConfig = new ClientConfig(loadOrCreateConfig());
        final Thread syncThread = new Thread(() -> {
            sTransferMap.forEach((key, value) -> {
                final RSync rSync =
                    new RSync()
                        .delete(true)
                        .force(true)
                        .checksum(true)
                        .verbose(true)
                        .recursive(true)
                        .source(clientConfig.getRsyncAddress() + key)
                        .destination(clientConfig.getMinecraftPath() + value);
                try {
                    new StreamingProcessOutput(new TextAreaOutput()).monitor(rSync.builder());
                } catch (final Exception exception) {
                    sLogger.error("Writing the process output failed", exception);
                    Platform.exit();
                }
            });
            Platform.exit();
        });
        syncThread.setDaemon(true);

        primaryStage.setTitle("Minecraft Server Sync Tool");
        sConsole.setEditable(false);

        final StackPane root = new StackPane();
        root.getChildren().add(sConsole);
        primaryStage.setScene(new Scene(root, 600, 200));
        primaryStage.show();

        syncThread.start();
    }

    public static void main(final String[] args) {
        launch(args);
    }

    /**
     * Creates a {@link Config} by doing one of the following:
     * <ul>
     *     <li>Finding the {@link ConfigLoader#DEFAULT_CONFIG_FILE_NAME} file in the default location</li>
     *     <li>Finding the {@link ConfigLoader#DEFAULT_CONFIG_FILE_NAME} from the command line arguments</li>
     *     <li>Prompting the user for the values and creating a configuration in the default location</li>
     * </ul>
     * <br><br>
     * The default location for the config file is right next to the JAR file.
     *
     * @return the {@link Config} instance
     * @throws IOException if saving a new configuration file in the default location occurs
     */
    private Config loadOrCreateConfig() throws IOException {
        final String[] args = getParameters().getRaw().toArray(new String[] {});
        Config config;

        try {
            config = ConfigLoader.loadConfig(args, false);
        } catch (final IllegalStateException ignoredException) {
            sLogger.error("Configuration file not found! Prompting user for values...");
            final ConfigurationGenerationDialog configurationGenerationDialog = new ConfigurationGenerationDialog();
            final Optional<Map<String, String>> result = configurationGenerationDialog.showAndWait();

            if (!result.isPresent()) {
                System.exit(1);
            }

            config = ConfigFactory.parseMap(result.get());

            Files.write(
                Paths.get(ConfigLoader.DEFAULT_CONFIG_FILE_NAME),
                Collections.singleton(config.root().render(ConfigRenderOptions
                    .defaults()
                    .setOriginComments(false)
                    .setComments(false)
                    .setFormatted(true))));
        }

        return config;
    }

    /**
     * A {@link StreamingProcessOwner} implementation which writes to a {@link TextArea}.
     */
    private static class TextAreaOutput implements StreamingProcessOwner {

        @Override
        public StreamingProcessOutputType getOutputType() {
            return StreamingProcessOutputType.BOTH;
        }

        @Override
        public void processOutput(final String line, final boolean stdout) {
            Platform.runLater(() -> {
                sConsole.appendText(line + "\n");
            });
        }
    }
}
