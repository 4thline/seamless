/*
 * Copyright (C) 2011 4th Line GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.seamless.gwt.server;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPCRequest;
import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.hibernate.exception.ConstraintViolationException;
import org.seamless.gwt.validation.shared.ValidationException;
import org.seamless.util.jpa.HibernateUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.gwt.user.server.rpc.RPC.encodeResponseForFailure;

/**
 * @author Christian Bauer
 */
public class HibernateRemoteServiceServlet extends ExtendableRemoteServiceServlet {

    final private static Logger log = Logger.getLogger(HibernateRemoteServiceServlet.class.getName());

    protected Session getCurrentSession() {
        return HibernateUtil.getSessionFactory().getCurrentSession();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log.fine("Initializing HibernateUtil");
        HibernateUtil.getSessionFactory();
    }

    @Override
    protected void onAfterRequestDeserialized(RPCRequest rpcRequest) {
        super.onAfterRequestDeserialized(rpcRequest);
        log.fine("Starting a database transaction");
        getCurrentSession().beginTransaction();
    }

    @Override
    protected String onBeforeResponseSerialized(Object result) {
        if (!getCurrentSession().getTransaction().isActive()) return null;

        try {
            // Commit and cleanup
            log.fine("Committing the database transaction");
            getCurrentSession().getTransaction().commit();

        } catch (RuntimeException ex) {
            // Rollback only
            log.fine("Runtime exception occurred, considering transaction rollback: " + ex);
            try {
                if (getCurrentSession().getTransaction().isActive()) {
                    log.fine("Trying to rollback database transaction after exception");
                    getCurrentSession().getTransaction().rollback();
                    log.fine("Transaction rolled back");
                }
            } catch (Throwable rbEx) {
                log.log(Level.SEVERE, "Could not rollback transaction after exception!", rbEx);
            }

            // Note that the exception you marshall to the client has to be
            // serializable/cross-compiled (in your 'shared'  or 'client' package)
            // and it has to implement com.google.gwt.user.client.rpc.IsSerializable
            // to pass the serialization policy security.

            if (ex instanceof StaleObjectStateException) {
                StaleObjectStateException sosEx = (StaleObjectStateException) ex;
                log.fine("Stale object state detected, serializing message to client for: " + sosEx);

                StringBuilder sb = new StringBuilder();
                sb.append("Concurrent modification error, ");
                sb.append("simultaneous modification of '").append(sosEx.getEntityName()).append("'");
                sb.append(" with identifier: ").append(sosEx.getIdentifier());

                ValidationException serializableException = new ValidationException(sb.toString());
                try {
                    return encodeResponseForFailure(null, serializableException);
                } catch (SerializationException e) {
                    log.fine("Can't serialize concurrent modification error message: " + e);
                    throw ex;
                }

            }

            if (ex instanceof ConstraintViolationException) {
                ConstraintViolationException cvEx = (ConstraintViolationException) ex;
                log.fine("Integrity constraint violation detected, serializing message to client for: " + cvEx);

                StringBuilder sb = new StringBuilder();
                sb.append("Violation of database integrity, ");
                sb.append("error code: ").append(cvEx.getErrorCode());
                ValidationException serializableException = new ValidationException(sb.toString());
                try {
                    return encodeResponseForFailure(null, serializableException);
                } catch (SerializationException e) {
                    log.fine("Can't serialize integrity rule violation message: " + e);
                    throw ex;
                }
            } else {
                throw ex;
            }
        }
        return null;
    }

    @Override
    protected void onBeforeExceptionSerialized(Throwable t) {
        if (!getCurrentSession().getTransaction().isActive()) return;
        log.fine("Exception trapped during service request processing, rolling back transaction: " + t);
        try {
            if (getCurrentSession().getTransaction().isActive()) {
                log.fine("Trying to rollback database transaction after exception");
                getCurrentSession().getTransaction().rollback();
                log.fine("Transaction rolled back");
            }
        } catch (Throwable rbEx) {
            log.log(Level.SEVERE, "Could not rollback transaction after exception!", rbEx);
        }
    }
}
