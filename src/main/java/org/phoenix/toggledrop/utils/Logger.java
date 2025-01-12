/**
 * LICENSE
 * PlayerProfile for RPG System
 * Copyright (C) 2022 Phoenix
 * ----------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 * END
 */

package org.phoenix.toggledrop.utils;

import org.bukkit.Bukkit;

public class Logger {

    public static void log(LogLevel level, String message) {
        if (message == null) return;

        switch (level) {
            case INFO ->
                    Bukkit.getConsoleSender().sendMessage(ColorCodeTranslate.chat("&8[&b&lINFO&8] &f" + message));
            case SUCCESS ->
                    Bukkit.getConsoleSender().sendMessage(ColorCodeTranslate.chat("&8[&a&lSUCCESS&8] &a" + message));
            case WARNING ->
                    Bukkit.getConsoleSender().sendMessage(ColorCodeTranslate.chat("&8[&e&lWARNING&8] &e" + message));
            case ERROR ->
                    Bukkit.getConsoleSender().sendMessage(ColorCodeTranslate.chat("&8[&c&lERROR&8] &c" + message));
            case OUTLINE ->
                    Bukkit.getConsoleSender().sendMessage(ColorCodeTranslate.chat("&7" + message));
        }
    }

    public enum LogLevel {
        INFO,
        SUCCESS,
        WARNING,
        ERROR,
        OUTLINE
    }
}
