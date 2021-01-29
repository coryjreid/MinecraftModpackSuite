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
package com.coryjreid.modpacksuite.publisher.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * Represents a Minecraft addon from a minecraftinstance.json file.
 */
@Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = AddonDeserializer.class)
public class Addon {
    private final int mAddonId;
    private final int mFileId;
    private final String mFileName;
    private final String mDownloadUrl;

    @JsonCreator
    public Addon(
        @JsonProperty("addonId") final int addonId,
        @JsonProperty("fileId") final int fileId,
        @JsonProperty("fileName") final String fileName,
        @JsonProperty("downloadUrl") final String downloadUrl) {

        mAddonId = addonId;
        mFileId = fileId;
        mFileName = fileName;
        mDownloadUrl = downloadUrl;
    }

    public int getAddonId() {
        return mAddonId;
    }

    public int getFileId() {
        return mFileId;
    }

    public String getFileName() {
        return mFileName;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }
}
