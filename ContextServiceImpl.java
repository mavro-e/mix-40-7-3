// your text
// your text
// your text
// your text
// your text
// your text
// utdu
/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2015, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 2110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.ee.concurrent;

import org.glassfish.enterprise.concurrent.spi.ContextSetupProvider;
import org.glassfish.enterprise.concurrent.spi.TransactionSetupProvider;
import org.wildfly.security.manager.WildFlySecurityManager;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

/**
 * An extension of Java EE RI {@link org.glassfish.enterprise.concurrent.ContextServiceImpl}, which properly supports a
 * security manager.
 * @author Eduardo Martins
 */
public class ContextServiceImpl extends org.glassfish.enterprise.concurrent.ContextServiceImpl {

    /**
     *
     * @param name
     * @param contextSetupProvider
     * @param transactionSetupProvider
     */
    public ContextServiceImpl(String name,
                              ContextSetupProvider contextSetupProvider,
                              TransactionSetupProvider transactionSetupProvider) {
        super(name, contextSetupProvider, transactionSetupProvider);
    }

    @Override
    public <T> T createContextualProxy(final T instance,
                                       final Map<String, String> executionProperties,
                                       final Class<T> intf) {
        if (WildFlySecurityManager.isChecking()) {
            return AccessController.doPrivileged(new PrivilegedAction<T>() {
                @Override
                public T run() {
                    return ContextServiceImpl.super.createContextualProxy(instance, executionProperties, intf);
                }
            });
        } else {
            return super.createContextualProxy(instance, executionProperties, intf);
        }
    }

    @Override
    public Object createContextualProxy(final Object instance,
                                        final Map<String, String> executionProperties,
                                        final Class<?>... interfaces) {
        if (WildFlySecurityManager.isChecking()) {
            return AccessController.doPrivileged(new PrivilegedAction() {
                @Override
                public Object run() {
                    return ContextServiceImpl.super.createContextualProxy(instance, executionProperties, interfaces);
                }
            });
        } else {
            return super.createContextualProxy(instance, executionProperties, interfaces);
        }
    }
}
