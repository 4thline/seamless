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

package org.seamless.statemachine;

import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @author Christian Bauer
 */
public class StateMachineBuilder {

    public static <T extends StateMachine> T build(Class<T> stateMachine, Class initialState) {
        return build(stateMachine, initialState, null, null);
    }

    public static <T extends StateMachine> T build(Class<T> stateMachine,
                                                   Class initialState,
                                                   Class[] constructorArgumentTypes,
                                                   Object[] constructorArguments) {
        return (T)
                Proxy.newProxyInstance(
                        stateMachine.getClassLoader(),
                        new Class[]{stateMachine},
                        new StateMachineInvocationHandler(
                                Arrays.asList(stateMachine.getAnnotation(States.class).value()),
                                initialState,
                                constructorArgumentTypes,
                                constructorArguments
                        )
                );
    }

}