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

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests that the deserialization of a minecraftinstance.json file works correctly.
 */
public class MinecraftInstanceTest {
    private static final ObjectMapper sMapper = new ObjectMapper();
    private static MinecraftInstance sMinecraftInstance;

    @Test
    void testGetForgeVersion() {
        assertEquals("35.1.36", sMinecraftInstance.getForgeVersion());
    }

    @Test
    void testGetMinecraftVersion() {
        assertEquals("1.16.4", sMinecraftInstance.getMinecraftVersion());
    }

    @Test
    void testGetAddons() {
        final List<Addon> addons = sMinecraftInstance.getAddons();

        assertFalse(addons.isEmpty());
        assertEquals(93, addons.size());
        assertTrue(addons.stream().anyMatch(addon -> addon.getAddonId() == 228525));
        assertEquals(1, addons.stream().filter(addon -> addon.getFileId() == 3167277).count());
        assertFalse(addons.get(0).getFileName().isEmpty());
        assertFalse(addons.get(0).getDownloadUrl().isEmpty());
    }

    @BeforeAll
    static void before() throws IOException {
        sMinecraftInstance = sMapper.readValue(
            MinecraftInstanceTest.class.getClassLoader().getResourceAsStream("minecraftinstance.json"),
            MinecraftInstance.class);
    }
}
