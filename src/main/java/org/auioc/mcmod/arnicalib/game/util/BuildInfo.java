/*
 * Copyright (C) 2024-2025 AUIOC.ORG
 *
 * This file is part of ArnicaLib, a mod made for Minecraft.
 *
 * ArnicaLib is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.auioc.mcmod.arnicalib.game.util;

import org.auioc.mcmod.arnicalib.ArnicaLib;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public record BuildInfo(
    String version,
    String reversion,
    int buildNumber,
    boolean isCIBuild,
    boolean isRelease,
    boolean isDirty,
    String minecraftVersion
) {

    public String shortReversion() {
        return reversion.length() == 40 ? reversion.substring(0, 7) : reversion;
    }

    @Override
    public String toString() {
        return String.format(
            "%s-%s-rev.%s-build.%d%s%s",
            minecraftVersion, version, shortReversion(), buildNumber,
            isRelease ? "" : "-dev", isDirty ? "-dirty" : ""
        );
    }

    public void log(Logger logger, Marker marker) {
        logger.info(marker, "Version: " + this.version + " (" + this + ")");
        if (!this.isRelease) { logger.warn(marker, "Mod is a development version"); }
        if (this.isDirty) { logger.warn(marker, "Mod is a dirty build"); }
    }

    // ============================================================================================================== //

    public static final String DEFAULT_CLASS_NAME = "BuildInfo";
    private static final Marker MARKER = MarkerFactory.getMarker("BuildInfo");

    public static BuildInfo fromClass(Class<?> clazz) {
        try {
            var b = new BuildInfo(
                clazz.getField("VERSION").get(null).toString(),
                clazz.getField("REVERSION").get(null).toString(),
                clazz.getField("BUILD_NUMBER").getInt(null),
                clazz.getField("IS_CI_BUILD").getBoolean(null),
                clazz.getField("IS_RELEASE").getBoolean(null),
                clazz.getField("IS_DIRTY").getBoolean(null),
                clazz.getField("MINECRAFT_VERSION").get(null).toString()
            );
            return b;
        } catch (Exception e) {
            ArnicaLib.LOGGER.error(MARKER, "Failed to parse build info", e);
            return null;
        }
    }

    public static BuildInfo fromClass(String className) {
        try {
            return fromClass(Class.forName(className));
        } catch (Exception e) {
            ArnicaLib.LOGGER.error(MARKER, "Failed to load build info", e);
            return null;
        }
    }

    public static BuildInfo fromPackage(String packageName) {
        return fromClass(packageName + "." + DEFAULT_CLASS_NAME);
    }

    public static BuildInfo fromPackage(Class<?> clazz) {
        return fromPackage(clazz.getPackageName());
    }

}
