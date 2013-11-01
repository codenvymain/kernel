/*
 * Copyright (C) 2011 eXo Platform SAS.
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
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.container;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.Disposable;
import org.picocontainer.Parameter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.Startable;
import org.picocontainer.defaults.AbstractPicoVisitor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:nicolas.filotto@exoplatform.com">Nicolas Filotto</a>
 * @version $Id$
 */
public class LifecycleVisitor extends AbstractPicoVisitor
{

   private static final Log LOG = ExoLogger.getLogger("exo.kernel.container.LifecycleVisitor");

   private static final Method START;

   private static final Method STOP;

   private static final Method DISPOSE;
   static
   {
      try
      {
         START = Startable.class.getMethod("start", (Class<?>[])null);
         STOP = Startable.class.getMethod("stop", (Class<?>[])null);
         DISPOSE = Disposable.class.getMethod("dispose", (Class<?>[])null);
      }
      catch (NoSuchMethodException e)
      {
         throw new InternalError(e.getMessage());
      }
   }

   private final Method method;

   private final Class<?> type;

   private final boolean visitInInstantiationOrder;

   private final List componentInstances;

   private final boolean ignoreError;

   public LifecycleVisitor(Method method, Class<?> ofType, boolean visitInInstantiationOrder, boolean ignoreError)
   {
      this.method = method;
      this.type = ofType;
      this.visitInInstantiationOrder = visitInInstantiationOrder;
      this.componentInstances = new ArrayList();
      this.ignoreError = ignoreError;
   }

   public Object traverse(Object node)
   {
      componentInstances.clear();
      try
      {
         super.traverse(node);
         if (!visitInInstantiationOrder)
         {
            Collections.reverse(componentInstances);
         }
         for (Iterator iterator = componentInstances.iterator(); iterator.hasNext();)
         {
            Object o = iterator.next();
            try
            {
               method.invoke(o, (Object[])null);
            }
            catch (IllegalArgumentException e)
            {
               if (ignoreError)
               {
                  if (LOG.isDebugEnabled())
                  {
                     LOG.debug("Can't call " + method.getName() + " on " + o, e);
                  }
                  continue;
               }
               throw new PicoIntrospectionException("Can't call " + method.getName() + " on " + o, e);
            }
            catch (IllegalAccessException e)
            {
               if (ignoreError)
               {
                  if (LOG.isDebugEnabled())
                  {
                     LOG.debug("Can't call " + method.getName() + " on " + o, e);
                  }
                  continue;
               }
               throw new PicoIntrospectionException("Can't call " + method.getName() + " on " + o, e);
            }
            catch (InvocationTargetException e)
            {
               if (ignoreError)
               {
                  if (LOG.isDebugEnabled())
                  {
                     LOG.debug("Failed when calling " + method.getName() + " on " + o, e.getTargetException());
                  }
                  continue;
               }
               throw new PicoIntrospectionException("Failed when calling " + method.getName() + " on " + o,
                  e.getTargetException());
            }
         }
      }
      finally
      {
         componentInstances.clear();
      }
      return Void.TYPE;
   }

   public void visitContainer(PicoContainer pico)
   {
      checkTraversal();
      componentInstances.addAll(pico.getComponentInstancesOfType(type));
   }

   public void visitComponentAdapter(ComponentAdapter componentAdapter)
   {
      checkTraversal();
   }

   public void visitParameter(Parameter parameter)
   {
      checkTraversal();
   }

   /**
    * Invoke the standard PicoContainer lifecycle for {@link Startable#start()}.
    * @param node The node to start the traversal.
    */
   public static void start(Object node)
   {
      new LifecycleVisitor(START, Startable.class, true, false).traverse(node);;
   }

   /**
    * Invoke the standard PicoContainer lifecycle for {@link Startable#stop()}.
    * @param node The node to start the traversal.
    */
   public static void stop(Object node)
   {
      new LifecycleVisitor(STOP, Startable.class, false, true).traverse(node);;
   }

   /**
    * Invoke the standard PicoContainer lifecycle for {@link Disposable#dispose()}.
    * @param node The node to start the traversal.
    */
   public static void dispose(Object node)
   {
      new LifecycleVisitor(DISPOSE, Disposable.class, false, true).traverse(node);;
   }

}
