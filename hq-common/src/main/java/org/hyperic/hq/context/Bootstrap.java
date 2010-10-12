/**
 * NOTE: This copyright does *not* cover user programs that use HQ
 * program services by normal system calls through the application
 * program interfaces provided as part of the Hyperic Plug-in Development
 * Kit or the Hyperic Client Development Kit - this is merely considered
 * normal use of the program, and does *not* fall under the heading of
 *  "derived work".
 *
 *  Copyright (C) [2009-2010], VMware, Inc.
 *  This file is part of HQ.
 *
 *  HQ is free software; you can redistribute it and/or modify
 *  it under the terms version 2 of the GNU General Public License as
 *  published by the Free Software Foundation. This program is distributed
 *  in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 *  USA.
 *
 */

package org.hyperic.hq.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

/**
 * Temporary holder for static lookup of beans through the app context
 * @author jhickey
 * 
 */
public class Bootstrap {
    static ApplicationContext appContext;
    private static Map<String, Object> testBeansByName = new HashMap<String, Object>();
    private static Map<Class<?>, Object> testBeansByType = new HashMap<Class<?>, Object>();

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> beanClass) {

        if (Bootstrap.testBeansByType.get(beanClass) != null) {
            return (T) Bootstrap.testBeansByType.get(beanClass);
        }
        Collection<T> beans = appContext.getBeansOfType(beanClass).values();
        if (beans.isEmpty() && appContext.getParent() != null) {
            beans = appContext.getParent().getBeansOfType(beanClass).values();
        }
        T bean = beans.iterator().next();
        if (bean == null) {
            throw new IllegalArgumentException("Couldn't locate bean of " + beanClass + " type");
        }
        return bean;
    }

    public static Object getBean(String name) {
        if (Bootstrap.testBeansByName.get(name) != null) {
            return Bootstrap.testBeansByName.get(name);
        }
        Object bean = appContext.getBean(name);
        if (bean == null && appContext.getParent() != null) {
            bean = appContext.getParent().getBean(name);
        }
        return bean;
    }

    public static boolean hasAppContext() {
        return Bootstrap.appContext != null;
    }

    public static Resource getResource(String location) {
        return Bootstrap.appContext.getResource(location);
    }

    /**
     * For unit testing purposes only - controls what gets returned by
     * getBean(name)
     * @param name The lookup bean name
     * @param bean The bean
     */
    public static void setBean(String name, Object bean) {
        Bootstrap.testBeansByName.put(name, bean);
    }

    /**
     * For unit testing purposes only - controls what gets returned by
     * getBean(class)
     * @param beanClass The lookup bean class
     * @param bean The bean
     */
    public static void setBean(Class<?> beanClass, Object bean) {
        Bootstrap.testBeansByType.put(beanClass, bean);
    }
}