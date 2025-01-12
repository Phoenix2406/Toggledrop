/**
 * LICENSE
 * Toggledrop for prevent items being dropped
 * Copyright (C) 2025 Phoenix
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

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorCodeTranslate {

    public static String chat(String message) {
        Pattern colorPattern = Pattern.compile("\\\\u\\ + [a-fA-F0-9]{4}");
        Matcher match = colorPattern.matcher(message);
        while (match.find()) {
            String code = message.substring(match.start(), match.end());
            message = message.replace(code, Character.toString((char) Integer.parseInt(code.replace("\\u+",""),16)));
            match = colorPattern.matcher(message);
        }

        Pattern hexPattern = Pattern.compile("&#[a-fA-F0-9]{6}");
        match = hexPattern.matcher(message);
        while (match.find()) {
            String color = message.substring(match.start(), match.end());
            message = message.replace(color, String.valueOf(ChatColor.of(color.replace("&", ""))));
            match = hexPattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
