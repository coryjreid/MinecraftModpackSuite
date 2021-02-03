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

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import com.github.fracpete.processoutput4j.core.StreamingProcessOutputType;
import com.github.fracpete.processoutput4j.core.StreamingProcessOwner;
import com.github.fracpete.processoutput4j.output.StreamingProcessOutput;
import com.github.fracpete.rsync4j.RSync;

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
    private static final Map<String, String> sTransferMap = new HashMap<>();

    /**
     * The {@link TextArea} for the program's output.
     */
    private static final TextArea sConsole = new TextArea();

    /**
     * The {@link Thread} to do the actual synchronization.
     */
    private static Thread sSyncThread;

    static {
        // Mods
        sTransferMap.put("mods/**", "mods");
        sTransferMap.put("clientmods/**", "mods");

        // Config
        sTransferMap.put("config/**", "config");
        sTransferMap.put("defaultconfigs/**", "defaultconfigs");

        // KubeJS
        sTransferMap.put("kubejs/**", "kubejs");

        // Resourcepacks
        sTransferMap.put("resourcepacks/**", "resourcepacks");
    }

    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("Minecraft Server Sync Tool");
        sConsole.setEditable(false);

        final StackPane root = new StackPane();
        root.getChildren().add(sConsole);
        primaryStage.setScene(new Scene(root, 600, 200));
        primaryStage.show();

        sSyncThread.start();
    }

    public static void main(final String[] args) {
        final ClientConfig config = new ClientConfig(args);
        sSyncThread = new Thread(() -> {
            sTransferMap.forEach((key, value) -> {
                final RSync rSync =
                    new RSync()
                        .checksum(true)
                        .verbose(true)
                        .recursive(true)
                        .source(config.getRsyncAddress() + key)
                        .destination(config.getMinecraftPath() + value);

                try {
                    new StreamingProcessOutput(new TextAreaOutput()).monitor(rSync.builder());
                } catch (final Exception exception) {
                    sLogger.error("Writing the process output failed", exception);
                    Platform.exit();
                }
            });
            Platform.exit();
        });
        sSyncThread.setDaemon(true);

        launch(args);
    }

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
