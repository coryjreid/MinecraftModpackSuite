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

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * A custom Jackson deserializer for deserializing Addons in a minecraftinstance.json file.
 */
public class AddonDeserializer extends StdDeserializer<Addon> {
    public AddonDeserializer() {
        this(null);
    }

    public AddonDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public Addon deserialize(
        final JsonParser jsonParser, final DeserializationContext context) throws IOException {

        final JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        final JsonNode installedFile = root.get("installedFile");

        return new Addon(
            root.get("addonID").asInt(),
            installedFile.get("id").asInt(),
            installedFile.get("FileNameOnDisk").asText(),
            installedFile.get("downloadUrl").asText());
    }
}
