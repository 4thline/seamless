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
