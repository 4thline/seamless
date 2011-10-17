package org.seamless.gwt.server;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;

import java.lang.Class;
import java.lang.IllegalAccessException;
import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.Override;
import java.lang.SecurityException;
import java.lang.String;
import java.lang.StringBuffer;
import java.lang.Throwable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static com.google.gwt.user.server.rpc.RPC.decodeRequest;
import static com.google.gwt.user.server.rpc.RPC.encodeResponseForFailure;
import static com.google.gwt.user.server.rpc.RPC.getDefaultSerializationPolicy;

/**
 * Hurray for Google genius coders.
 *
 * http://code.google.com/p/google-web-toolkit/issues/detail?id=1291
 */
public class ExtendableRemoteServiceServlet extends RemoteServiceServlet {

    /**
     * This is based on Google's RPC.java source. The improvement here is that the
     * invokeAndEncodeResponse() is broken up into separate invoke() and
     * encodeResponse() methods.
     */
    public class RPCUtils {

        /**
         * Invoke the specified method and return the result
         *
         * @param target
         * @param serviceMethod
         * @param args
         * @throws InvocationTargetException
         */
        public Object invokeMethod(Object target, Method serviceMethod, Object[] args) throws InvocationTargetException {
            if (serviceMethod == null) {
                throw new NullPointerException("serviceMethod");
            }
            try {
                return serviceMethod.invoke(target, args);
            } catch (IllegalAccessException e) {
                SecurityException securityException = new SecurityException(formatIllegalAccessErrorMessage(target, serviceMethod));
                securityException.initCause(e);
                throw securityException;
            } catch (IllegalArgumentException e) {
                SecurityException securityException = new SecurityException(formatIllegalArgumentErrorMessage(target, serviceMethod, args));
                securityException.initCause(e);
                throw securityException;
            }
        }

        /**
         * Serialize the result object
         *
         * @param result
         * @param target
         * @param serviceMethod
         * @param args
         * @param serializationPolicy
         * @throws SerializationException
         */
        public String encodeResponse(Object result, Object target, Method serviceMethod, Object[] args, SerializationPolicy serializationPolicy) throws SerializationException {
            if (serviceMethod == null) {
                throw new NullPointerException("serviceMethod");
            }

            if (serializationPolicy == null) {
                throw new NullPointerException("serializationPolicy");
            }
            return RPC.encodeResponseForSuccess(serviceMethod, result, serializationPolicy);
        }

        /**
         * Copied from Google's RPC.java
         */
        protected String getSourceRepresentation(Method method) {
            return method.toString().replace('$', '.');
        }

        /**
         * Copied from Google's RPC.java
         */
        protected String formatIllegalAccessErrorMessage(Object target, Method serviceMethod) {
            StringBuffer sb = new StringBuffer();
            sb.append("Blocked attempt to access inaccessible method '");
            sb.append(getSourceRepresentation(serviceMethod));
            sb.append("'");
            if (target != null) {
                sb.append(" on target '");
                sb.append(printTypeName(target.getClass()));
                sb.append("'");
            }
            sb.append("; this is either misconfiguration or a hack attempt");
            return sb.toString();
        }

        /**
         * Copied from Google's RPC.java
         */
        protected String formatIllegalArgumentErrorMessage(Object target, Method serviceMethod, Object[] args) {
            StringBuffer sb = new StringBuffer();
            sb.append("Blocked attempt to invoke method '");
            sb.append(getSourceRepresentation(serviceMethod));
            sb.append("'");

            if (target != null) {
                sb.append(" on target '");
                sb.append(printTypeName(target.getClass()));
                sb.append("'");
            }

            sb.append(" with invalid arguments");

            if (args != null && args.length > 0) {
                sb.append(Arrays.asList(args));
            }
            return sb.toString();
        }

        /**
         * Copied from Google's RPC.java
         */
        protected String printTypeName(Class<?> type) {
            // Primitives
            if (type.equals(Integer.TYPE)) {
                return "int";
            } else if (type.equals(Long.TYPE)) {
                return "long";
            } else if (type.equals(Short.TYPE)) {
                return "short";
            } else if (type.equals(Byte.TYPE)) {
                return "byte";
            } else if (type.equals(Character.TYPE)) {
                return "char";
            } else if (type.equals(Boolean.TYPE)) {
                return "boolean";
            } else if (type.equals(java.lang.Float.TYPE)) {
                return "float";
            } else if (type.equals(Double.TYPE)) {
                return "double";
            }

            // Arrays
            if (type.isArray()) {
                Class<?> componentType = type.getComponentType();
                return printTypeName(componentType) + "[]";
            }

            // Everything else
            return type.getName().replace('$', '.');
        }

    }

    RPCUtils utils = new RPCUtils();

    @Override
    public String processCall(String payload) throws SerializationException {

        RPCRequest rpcRequest = decodeRequest(payload, this.getClass(), this);
        try {

            onAfterRequestDeserialized(rpcRequest);

            Object result = utils.invokeMethod(this, rpcRequest.getMethod(), rpcRequest.getParameters());

            String override = onBeforeResponseSerialized(result);
            if (override != null)
                return override;

            return utils.encodeResponse(
                result,
                this,
                rpcRequest.getMethod(),
                rpcRequest.getParameters(),
                rpcRequest.getSerializationPolicy()
            );

        } catch (IncompatibleRemoteServiceException e) {
            log("An IncompatibleRemoteServiceException was thrown while processing this call.", e);

            onBeforeExceptionSerialized(e);

            return encodeResponseForFailure(null, e);

        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();

            onBeforeExceptionSerialized(cause);

            return encodeResponseForFailure(
                rpcRequest.getMethod(),
                cause,
                getDefaultSerializationPolicy(),
                rpcRequest.getFlags()
            );
        }
    }

    /**
     * Called after RPC method invocation but before the response is serialized.
     *
     * @param result The result of the method invocation or null if the invocation failed.
     * @return A non-null value to skip serialization and return the given string to the client.
     */
    protected String onBeforeResponseSerialized(Object result) {
        return null;
    }

    /**
     * @param t Any exception thrown during processing before it is eligible for response encoding.
     */
    protected void onBeforeExceptionSerialized(Throwable t) {
    }
}