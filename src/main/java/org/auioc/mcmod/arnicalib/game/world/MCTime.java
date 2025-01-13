/*
 * Copyright (C) 2022-2025 AUIOC.ORG
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

package org.auioc.mcmod.arnicalib.game.world;

import net.minecraft.world.level.Level;

public class MCTime {

    public static final int SUNRISE = 23000;
    public static final int DAY_BEGIN = 0;
    public static final int DAY = 1000;
    public static final int NOON = 6000;
    public static final int SUNSET = 12000;
    public static final int NIGHT = 13000;
    public static final int MIDNIGHT = 18000;
    public static final int DAY_END = 24000;

    public static final int TICKS_PER_DAY = 24000;
    public static final int TICKS_PER_HOUR = 1000;
    public static final double TICKS_PER_MINUTE = 1000D / 60D;
    public static final double TICKS_PER_SECOND = TICKS_PER_MINUTE / 60D;
    public static final double TICKS_PER_MILLISECOND = TICKS_PER_SECOND / 1000D;

    public static long[] getTime(Level level) {
        return new long[] { level.getDayTime(), level.getGameTime(), System.currentTimeMillis() };
    }

    public static int getDayTime(Level level) {
        return (int) (level.getDayTime() % 24000L);
    }

    private static int mod(long dayTime) {
        return (int) (dayTime % 2147483647L);
    }

    public static int[] formatDayTime(long dayTime) {
        int dayTimeI = mod(dayTime);

        int day = dayTimeI / TICKS_PER_DAY;

        int ticks = dayTimeI - day * TICKS_PER_DAY;
        int hour = (int) (ticks / TICKS_PER_HOUR + 6) % 24;
        int min = (int) (ticks / TICKS_PER_MINUTE) % 60;
        int sec = (int) (ticks / TICKS_PER_SECOND) % 60;
        int msec = (int) (ticks / TICKS_PER_MILLISECOND) % 1000;

        return new int[] { day, hour, min, sec, msec };
    }

    public static int getDay(long dayTime) {
        return mod(dayTime) / TICKS_PER_DAY;
    }

    public static int getHour(long dayTime) {
        return (mod(dayTime) / TICKS_PER_HOUR + 6) % 24;
    }

    public static int getMinute(long dayTime) {
        return (mod(dayTime) % TICKS_PER_HOUR) * 60 / TICKS_PER_HOUR;
    }

    // ====================================================================== //

    public enum Unit {
        TICK, SECOND, REAL
    }

}
