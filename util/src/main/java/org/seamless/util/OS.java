/*
 * Copyright (C) 2011 4th Line GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.seamless.util;

/**
 * @author Christian Bauer
 */
public class OS {

    public static boolean checkForLinux() {
        return checkForPresence("os.name", "linux");
    }

    public static boolean checkForHp() {
        return checkForPresence("os.name", "hp");
    }

    public static boolean checkForSolaris() {
        return checkForPresence("os.name", "sun");
    }

    public static boolean checkForWindows() {
        return checkForPresence("os.name", "win");
    }

    public static boolean checkForMac() {
        return checkForPresence("os.name", "mac");
    }

    private static boolean checkForPresence(String key, String value) {
        try {
            String tmp = System.getProperty(key);
            return tmp != null && tmp.trim().toLowerCase().startsWith(value);
        }
        catch (Throwable t) {
            return false;
        }
    }

}
