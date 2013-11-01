/*
 * Copyright (C) 2009 eXo Platform SAS.
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

import junit.framework.AssertionFailedError;

import org.exoplatform.commons.utils.PropertyManager;
import org.exoplatform.commons.utils.Tools;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * <p>An helper for building a root container and a portal container. I have done several attempt to make easily
 * and safe root/portal container boot for unit test. This one is my best attempt so far.</p>
 *
 * <p>Note that the portal container are booted in the order they are declared first.</p>
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ContainerBuilder
{

   /** A hack used during the boot of a portal container. */
   private final ThreadLocal<String> bootedPortalName = new ThreadLocal<String>();

   /** . */
   private ClassLoader loader;

   /** . */
   private List<URL> configURLs;

   /** . */
   private LinkedHashMap<String, List<URL>> portalConfigURLs;

   /** . */
   private Set<String> profiles;

   public ContainerBuilder()
   {
      this.loader = Thread.currentThread().getContextClassLoader();
      this.configURLs = new ArrayList<URL>();
      this.portalConfigURLs = new LinkedHashMap<String, List<URL>>();
   }

   public ContainerBuilder withRoot(String configPath)
   {
      configURLs.addAll(urls(configPath));
      return this;
   }

   public ContainerBuilder withRoot(URL configURL)
   {
      configURLs.add(configURL);
      return this;
   }

   public ContainerBuilder withPortal(String configPath)
   {
      return withPortal("portal", configPath);
   }

   public ContainerBuilder withPortal(String portalName, String configPath)
   {
      for (URL configURL : urls(configPath))
      {
         withPortal(portalName, configURL);
      }
      return this;
   }

   public ContainerBuilder withPortal(URL configURL)
   {
      return withPortal("portal", configURL);
   }

   public ContainerBuilder withPortal(String portalName, URL configURL)
   {
      List<URL> urls = portalConfigURLs.get(portalName);
      if (urls == null)
      {
         urls = new ArrayList<URL>();
         portalConfigURLs.put(portalName, urls);
      }
      urls.add(configURL);
      return this;
   }

   public ContainerBuilder withLoader(ClassLoader loader)
   {
      this.loader = loader;
      return this;
   }

   public ContainerBuilder profiledBy(String... profiles)
   {
      this.profiles = Tools.set(profiles);
      return this;
   }

   private List<URL> urls(String path)
   {
      try
      {
         return Collections.list(loader.getResources(path));
      }
      catch (IOException e)
      {
         AssertionFailedError err = new AssertionFailedError();
         err.initCause(e);
         throw err;
      }
   }

   public RootContainer build()
   {
      try
      {
         return _build();
      }
      catch (Exception e)
      {
         AssertionFailedError err = new AssertionFailedError();
         err.initCause(e);
         throw err;
      }
   }

   private RootContainer _build() throws Exception
   {
      //
      if (configURLs.size() == 0)
      {
         throw new IllegalStateException("Must provide at least one URL for building the root container");
      }

      // Must clear the top container first otherwise it's not going to work well
      // it's a big ugly but I don't want to change anything in the ExoContainerContext class for now
      // and this is for unit testing
      Field topContainerField = ExoContainerContext.class.getDeclaredField("topContainer");
      topContainerField.setAccessible(true);
      topContainerField.set(null, null);

      ExoContainerContext.setCurrentContainer(null);
      // Same remark than above
      Field singletonField = RootContainer.class.getDeclaredField("singleton_");
      singletonField.setAccessible(true);
      singletonField.set(null, null);

      // Setup profiles
      if (profiles == null)
      {
         PropertyManager.setProperty(PropertyManager.RUNTIME_PROFILES, "");
      }
      else
      {
         StringBuilder builder = new StringBuilder();
         for (Iterator<String> i = profiles.iterator();i.hasNext();)
         {
            builder.append(i.next());
            if (i.hasNext())
            {
               builder.append(',');
            }
         }
         PropertyManager.setProperty(PropertyManager.RUNTIME_PROFILES, builder.toString());
      }

      //
      ClassLoader rootCL = new ClassLoader(loader)
      {
         @Override
         public Enumeration<URL> getResources(String name) throws IOException
         {
            if ("conf/configuration.xml".equals(name))
            {
               return Collections.enumeration(configURLs);
            }
            else if ("conf/portal/configuration.xml".equals(name))
            {
               String portalName = bootedPortalName.get();
               return Collections.enumeration(portalConfigURLs.get(portalName));
            }
            else if ("conf/portal/test-configuration.xml".equals(name))
            {
               return Collections.enumeration(Collections.<URL>emptyList());
            }
            else
            {
               return super.getResources(name);
            }
         }
      };

      //
      ClassLoader oldCL = Thread.currentThread().getContextClassLoader();

      // Boot root container
      RootContainer root;
      try
      {
         Thread.currentThread().setContextClassLoader(rootCL);

         //
         root = RootContainer.getInstance();

         PortalContainer.reloadConfig();
         
         //
         for (String portalName : portalConfigURLs.keySet())
         {
            try
            {
               bootedPortalName.set(portalName);
               root.getPortalContainer(portalName);
            }
            finally
            {
               bootedPortalName.set(null);
            }
         }
      }
      finally
      {
         Thread.currentThread().setContextClassLoader(oldCL);
      }

      //
      return root;
   }

   public static RootContainer bootstrap(URL configurationURL, String... profiles)
   {
      ContainerBuilder builder = new ContainerBuilder();
      builder.withRoot(configurationURL);
      builder.profiledBy(profiles);
      return builder.build();
   }
}
