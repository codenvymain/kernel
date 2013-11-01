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

import junit.framework.TestCase;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.impl.jboss.lru.LRUExoCacheCreator.LRUExoCache;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * 21 juil. 2009  
 */
public class TestLRUCache extends TestCase
{

   CacheService service_;

   public TestLRUCache(String name)
   {
      super(name);
   }

   public void setUp() throws Exception
   {
      service_ = (CacheService)PortalContainer.getInstance().getComponentInstanceOfType(CacheService.class);
   }

   public void testPolicy() throws Exception
   {
      testPolicy("test-lru");
      testPolicy("test-lru-with-old-config");
   }

   private void testPolicy(String cacheName) throws Exception
   {
      LRUExoCache cache = (LRUExoCache)service_.getCacheInstance(cacheName);
      cache.put("a", "a");
      cache.put("b", "a");
      cache.put("c", "a");
      cache.put("d", "a");
      assertEquals(4, cache.getCacheSize());
      cache.put("e", "a");
      assertEquals(5, cache.getCacheSize());
      cache.put("f", "a");
      assertEquals(6, cache.getCacheSize());
      Thread.sleep(1000);
      assertFalse(cache.get("b") == null);
      assertFalse(cache.get("c") == null);
      assertFalse(cache.get("d") == null);
      Thread.sleep(600);
      assertEquals(3, cache.getCacheSize());
      Thread.sleep(500);
      assertEquals(0, cache.getCacheSize());
      cache.setMaxSize(3);
      cache.setTimeToLive(500);
      cache.setMaxAge(1000);
      cache.put("a", "a");
      cache.put("b", "a");
      cache.put("c", "a");
      cache.put("d", "a");
      assertEquals(4, cache.getCacheSize());
      cache.put("e", "a");
      assertEquals(5, cache.getCacheSize());
      cache.put("f", "a");
      Thread.sleep(500);
      cache.get("a");
      cache.get("b");
      assertEquals(3, cache.getCacheSize());
      Thread.sleep(600);
      assertEquals(0, cache.getCacheSize());
   }
}
