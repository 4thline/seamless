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

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Christian Bauer
 */
public class RandomToken {

    final protected Random random;

    public RandomToken() {
        try {
            random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        // Force secure seeding
        random.nextBytes(new byte[1]);
    }

    public String generate() {
        String token = null;
        while (token == null || token.length() == 0) {
            long r0 = random.nextLong();
            if (r0 < 0)
                r0 = -r0;
            long r1 = random.nextLong();
            if (r1 < 0)
                r1 = -r1;
            token = Long.toString(r0, 36) + Long.toString(r1, 36);
        }
        return token;
    }

}
