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
package org.exoplatform.services.cache;

import java.io.Serializable;

/**
 * Created by The eXo Platform SAS Author : Thuannd nhudinhthuan@yahoo.com Apr
 * 4, 2006
 */
public class ExpireKeyStartWithSelector<K extends Serializable, V> implements CachedObjectSelector<K, V>
{

   private String keyStartWith_;

   public ExpireKeyStartWithSelector(String keyStartWith)
   {
      keyStartWith_ = keyStartWith;
   }

   public boolean select(K key, ObjectCacheInfo<? extends V> ocinfo)
   {
      String skey = (String)key;
      if (skey.startsWith(keyStartWith_))
         return true;
      return false;
   }

   public void onSelect(ExoCache<? extends K, ? extends V> cache, K key, ObjectCacheInfo<? extends V> ocinfo) throws Exception
   {
      cache.remove(key);
   }
}
