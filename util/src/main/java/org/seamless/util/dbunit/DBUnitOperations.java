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
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Christian Bauer
 */
public abstract class DBUnitOperations extends ArrayList<DBUnitOperations.Op> {

    final private static Logger log = Logger.getLogger(DBUnitOperations.class.getName());

    abstract public static class Op {

        ReplacementDataSet dataSet;
        DatabaseOperation operation;

        /**
         * Defaults to <tt>DatabaseOperation.CLEAN_INSERT</tt>
         */
        public Op(String dataLocation) {
            this(dataLocation, null, DatabaseOperation.CLEAN_INSERT);
        }

        /**
         * Defaults to <tt>DatabaseOperation.CLEAN_INSERT</tt>
         */
        public Op(String dataLocation, String dtdLocation) {
            this(dataLocation, dtdLocation, DatabaseOperation.CLEAN_INSERT);
        }

        public Op(String dataLocation, String dtdLocation, DatabaseOperation operation) {

            try {
                this.dataSet = dtdLocation != null
                   ? new ReplacementDataSet(new FlatXmlDataSet(openStream(dataLocation), openStream(dtdLocation)))
                   : new ReplacementDataSet(new FlatXmlDataSet(openStream(dataLocation)));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            this.dataSet.addReplacementObject("[NULL]", null);
            this.operation = operation;
        }

        public IDataSet getDataSet() {
            return dataSet;
        }

        public DatabaseOperation getOperation() {
            return operation;
        }

        public void execute(IDatabaseConnection connection) {
            try {
                this.operation.execute(connection, dataSet);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        abstract protected InputStream openStream(String location);
    }

    static public class ClasspathOp extends Op {

        public ClasspathOp(String dataLocation) {
            super(dataLocation);
        }

        public ClasspathOp(String dataLocation, String dtdLocation) {
            super(dataLocation, dtdLocation);
        }

        public ClasspathOp(String dataLocation, String dtdLocation, DatabaseOperation operation) {
            super(dataLocation, dtdLocation, operation);
        }

        @Override
        protected InputStream openStream(String location) {
            return Thread.currentThread().getContextClassLoader().getResourceAsStream(location);
        }
    }

    public class FileOp extends Op {

        public FileOp(String dataLocation) {
            super(dataLocation);
        }

        public FileOp(String dataLocation, String dtdLocation) {
            super(dataLocation, dtdLocation);
        }

        public FileOp(String dataLocation, String dtdLocation, DatabaseOperation operation) {
            super(dataLocation, dtdLocation, operation);
        }

        @Override
        protected InputStream openStream(String location) {
            try {
                return new FileInputStream(location);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public DBUnitOperations() {
    }

    abstract public DataSource getDataSource();

    public void execute() {
        log.info("Executing DBUnit operations: " + size());
        IDatabaseConnection con = null;
        try {
            con = getConnection();
            disableReferentialIntegrity(con);
            for (Op op : this) {
                op.execute(con);
            }
            enableReferentialIntegrity(con);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ex) {
                    log.log(Level.WARNING, "Failed to close connection after DBUnit operation: " + ex, ex);
                }
            }
        }
    }

    // Subclasses can/have to override the following methods

    /**
     * Override this method if you want to provide your own DBUnit <tt>IDatabaseConnection</tt> instance.
     * <p/>
     *
     * @return a DBUnit database connection (wrapped)
     */
    protected IDatabaseConnection getConnection() {
        try {
            DataSource datasource = getDataSource();
            Connection con = datasource.getConnection();
            IDatabaseConnection dbUnitCon = new DatabaseConnection(con);
            editConfig(dbUnitCon.getConfig());
            return dbUnitCon;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Execute whatever statement is necessary to either defer or disable foreign
     * key constraint checking on the given database connection, which is used by
     * DBUnit to import datasets.
     *
     * @param con A DBUnit connection wrapper, which is used afterwards for dataset operations
     */
    abstract protected void disableReferentialIntegrity(IDatabaseConnection con);

    /**
     * Execute whatever statement is necessary to enable integrity constraint checks after
     * dataset operations.
     *
     * @param con A DBUnit connection wrapper, before it is used by the application again
     */
    abstract protected void enableReferentialIntegrity(IDatabaseConnection con);

    /**
     * Override this method if you require DBUnit configuration features or additional properties.
     * <p/>
     * Called after a connection has been obtaind and before the connection is used. Can be a
     * NOOP method if no additional settings are necessary for your DBUnit/DBMS setup.
     *
     * @param config A DBUnit <tt>DatabaseConfig</tt> object for setting properties and features
     */
    protected void editConfig(DatabaseConfig config) {
    }
}