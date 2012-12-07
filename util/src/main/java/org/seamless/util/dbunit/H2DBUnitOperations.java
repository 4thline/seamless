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

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;

import java.sql.Types;

/**
 * @author Christian Bauer
 */
public abstract class H2DBUnitOperations extends DBUnitOperations {

    @Override
    protected void disableReferentialIntegrity(IDatabaseConnection con) {
        try {
            con.getConnection().prepareStatement("set referential_integrity FALSE").execute(); // HSQL/H2 DB
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void enableReferentialIntegrity(IDatabaseConnection con) {
        try {
            con.getConnection().prepareStatement("set referential_integrity TRUE").execute();  // HSQL/H2 DB
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void editConfig(DatabaseConfig config) {
        super.editConfig(config);

        // TODO: DBUnit/HSQL bugfix
        // http://www.carbonfive.com/community/archives/2005/07/dbunit_hsql_and.html
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new DefaultDataTypeFactory() {
            @Override
            public DataType createDataType(int sqlType, String sqlTypeName)
               throws DataTypeException {
                if (sqlType == Types.BOOLEAN) {
                    return DataType.BOOLEAN;
                }
                return super.createDataType(sqlType, sqlTypeName);
            }
        });
    }
}
