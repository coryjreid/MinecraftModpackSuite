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
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * A custom Jackson deserializer for deserializing a minecraftinstance.json file.
 */
public class MinecraftInstanceDeserializer extends StdDeserializer<MinecraftInstance> {
    private final ObjectMapper mMapper = new ObjectMapper();

    public MinecraftInstanceDeserializer() {
        this(null);
    }

    public MinecraftInstanceDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public MinecraftInstance deserialize(
        final JsonParser jsonParser, final DeserializationContext context) throws IOException {

        final JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        final JsonNode base = root.get("baseModLoader");

        return new MinecraftInstance(
            base.get("forgeVersion").asText(),
            base.get("minecraftVersion").asText(),
            mMapper.readerFor(TypeFactory.defaultInstance().constructCollectionType(List.class, Addon.class))
                .readValue(root.get("installedAddons")));
    }
}
