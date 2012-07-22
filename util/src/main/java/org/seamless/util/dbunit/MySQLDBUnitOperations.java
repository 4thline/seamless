/*
 * Copyright (C) 2012 4th Line GmbH, Switzerland
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

package org.seamless.util.dbunit;

import org.dbunit.database.IDatabaseConnection;

/**
 * @author Christian Bauer
 */
public abstract class MySQLDBUnitOperations extends DBUnitOperations {

    @Override
    protected void disableReferentialIntegrity(IDatabaseConnection con) {
        try {
            con.getConnection().prepareStatement("set foreign_key_checks=0").execute(); // MySQL > 4.1.1
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void enableReferentialIntegrity(IDatabaseConnection con) {
        try {
            con.getConnection().prepareStatement("set foreign_key_checks=1").execute(); // MySQL > 4.1.1
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
