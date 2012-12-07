 /*
 * Copyright (C) 2012 4th Line GmbH, Switzerland
 *
 * The contents of this file are subject to the terms of either the GNU
 * Lesser General Public License Version 2 or later ("LGPL") or the
 * Common Development and Distribution License Version 1 or later
 * ("CDDL") (collectively, the "License"). You may not use this file
 * except in compliance with the License. See LICENSE.txt for more
 * information.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
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
