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
package com.coryjreid.modpacksuite.sync.client.gui;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Window;

/**
 * A dialog for capturing values to generate a default configuration.
 */
public class ConfigurationGenerationDialog extends Dialog<Map<String, String>> {
    private final TextField mMinecraftInstanceRootField = new TextField();
    private final TextField mServerHostNameField = new TextField();
    private final TextField mServerPortField = new TextField();
    private final TextField mServerPathField = new TextField();

    private BooleanBinding mMinecraftInstanceRootFieldValid;
    private BooleanBinding mServerHostNameFieldValid;
    private BooleanBinding mServerPortFieldValid;
    private BooleanBinding mServerPathFieldValid;

    public ConfigurationGenerationDialog() {
        setTitle("Generate Configuration File");
        setHeaderText("A configuration file was not found.\nFill in the values to generate a configuration file.");

        getDialogPane().getButtonTypes().addAll(ButtonType.OK);

        final ColumnConstraints columnConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnConstraints.setHgrow(Priority.ALWAYS);
        final GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.getColumnConstraints().add(columnConstraints);

        initializeComponents();

        grid.add(new Label("Minecraft Instance Folder:"), 0, 0);
        grid.add(mMinecraftInstanceRootField, 0, 1);

        grid.add(new Label("Rsync Server Hostname:"), 0, 2);
        grid.add(mServerHostNameField, 0, 3);

        grid.add(new Label("Rsync Server Port:"), 0, 4);
        grid.add(mServerPortField, 0, 5);

        grid.add(new Label("Rsync Server Path:"), 0, 6);
        grid.add(mServerPathField, 0, 7);

// Enable/Disable login button depending on whether a minecraftInstanceRoot was entered.
        final Node okButton = getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty()
            .bind(mMinecraftInstanceRootFieldValid.not()
                .or(mServerHostNameFieldValid.not())
                .or(mServerPortFieldValid.not())
                .or(mServerPathFieldValid.not()));

        getDialogPane().setContent(grid);

        Platform.runLater(mMinecraftInstanceRootField::requestFocus);

        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                final Map<String, String> configValueMap = new HashMap<>();
                configValueMap.put("minecraftInstanceRoot", mMinecraftInstanceRootField.getText());
                configValueMap.put("serverHostname", mServerHostNameField.getText());
                configValueMap.put("serverPort", mServerPortField.getText());
                configValueMap.put("serverPath", mServerPathField.getText());
                return configValueMap;
            }
            return null;
        });

        final Window window = getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> Platform.exit());
    }

    private void initializeComponents() {
        mMinecraftInstanceRootField.setPromptText("C:\\path\\to\\folder");
        mMinecraftInstanceRootField.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%)");
        mMinecraftInstanceRootFieldValid = Bindings.createBooleanBinding(
            () -> !mMinecraftInstanceRootField.getText().isEmpty()
                && Files.exists(Paths.get(mMinecraftInstanceRootField.getText())),
            mMinecraftInstanceRootField.textProperty());

        mServerHostNameField.setPromptText("example.com");
        mServerHostNameField.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%)");
        mServerHostNameFieldValid = Bindings.createBooleanBinding(
            () -> !mServerHostNameField.getText().isEmpty(),
            mServerHostNameField.textProperty());

        mServerPortField.setPromptText("25000");
        mServerPortField.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%)");
        mServerPortFieldValid = Bindings.createBooleanBinding(
            () -> !mServerPortField.getText().isEmpty() && isInteger(mServerPortField.getText()),
            mServerPortField.textProperty());

        mServerPathField.setPromptText("path");
        mServerPathField.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%)");
        mServerPathFieldValid = Bindings.createBooleanBinding(
            () -> !mServerPathField.getText().isEmpty(),
            mServerPathField.textProperty());
    }

    private boolean isInteger(final String string) {
        try {
            Integer.parseInt(string);
        } catch (final Exception ignoredException) {
            return false;
        }

        return true;
    }
}
