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
package org.exoplatform.services.cache.impl.jboss;

import org.exoplatform.services.cache.CacheListener;
import org.exoplatform.services.cache.CachedObjectSelector;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.cache.ExoCacheConfig;
import org.exoplatform.services.cache.ExoCacheInitException;
import org.jboss.cache.Cache;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * 21 juil. 2009  
 */
public class TestExoCacheCreator implements ExoCacheCreator
{

   public ExoCache create(ExoCacheConfig config, Cache<Serializable, Object> cache) throws ExoCacheInitException
   {
      return new TestExoCache();
   }

   public Class<? extends ExoCacheConfig> getExpectedConfigType()
   {
      return TestExoCacheConfig.class;
   }

   public String getExpectedImplementation()
   {
      return "TEST";
   }

   public static class TestExoCache implements ExoCache
   {

      public void addCacheListener(CacheListener listener)
      {
         // TODO Auto-generated method stub

      }


      public int getCacheHit()
      {
         // TODO Auto-generated method stub
         return 0;
      }

      public int getCacheMiss()
      {
         // TODO Auto-generated method stub
         return 0;
      }

      public int getCacheSize()
      {
         // TODO Auto-generated method stub
         return 0;
      }

      public List getCachedObjects()
      {
         // TODO Auto-generated method stub
         return null;
      }

      public String getLabel()
      {
         // TODO Auto-generated method stub
         return null;
      }

      public long getLiveTime()
      {
         // TODO Auto-generated method stub
         return 0;
      }

      public int getMaxSize()
      {
         // TODO Auto-generated method stub
         return 0;
      }

      public String getName()
      {
         return "name";
      }

      public boolean isDistributed()
      {
         // TODO Auto-generated method stub
         return false;
      }

      public boolean isLogEnabled()
      {
         // TODO Auto-generated method stub
         return false;
      }

      public boolean isReplicated()
      {
         // TODO Auto-generated method stub
         return false;
      }


      public void select(CachedObjectSelector selector) throws Exception
      {
         // TODO Auto-generated method stub

      }

      public void setDistributed(boolean b)
      {
         // TODO Auto-generated method stub

      }

      public void setLabel(String s)
      {
         // TODO Auto-generated method stub

      }

      public void setLiveTime(long period)
      {
         // TODO Auto-generated method stub

      }

      public void setLogEnabled(boolean b)
      {
         // TODO Auto-generated method stub

      }

      public void setMaxSize(int max)
      {
         // TODO Auto-generated method stub

      }

      public void setName(String name)
      {
         // TODO Auto-generated method stub

      }

      public void setReplicated(boolean b)
      {
         // TODO Auto-generated method stub

      }

      public void clearCache()
      {
         // TODO Auto-generated method stub
         
      }

      public Object get(Serializable key)
      {
         // TODO Auto-generated method stub
         return null;
      }

      public void put(Serializable key, Object value) throws NullPointerException
      {
         // TODO Auto-generated method stub
         
      }

      public void putMap(Map objs) throws NullPointerException, IllegalArgumentException
      {
         // TODO Auto-generated method stub
         
      }

      public Object remove(Serializable key) throws NullPointerException
      {
         // TODO Auto-generated method stub
         return null;
      }

      public List removeCachedObjects()
      {
         // TODO Auto-generated method stub
         return null;
      }

   }
}
