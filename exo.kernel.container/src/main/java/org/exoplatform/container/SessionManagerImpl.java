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

import org.exoplatform.container.security.ContainerPermissions;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by The eXo Platform SAS Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Nov 4, 2005
 */
public class SessionManagerImpl extends Hashtable<String, SessionContainer> implements SessionManager
{

   public List<SessionContainer> getLiveSessions()
   {
      List<SessionContainer> list = new ArrayList<SessionContainer>(size() + 1);
      list.addAll(values());
      return list;
   }

   final public SessionContainer getSessionContainer(String id)
   {
      return get(id);
   }

   final public void removeSessionContainer(String id)
   {
      SecurityManager security = System.getSecurityManager();
      if (security != null)
         security.checkPermission(ContainerPermissions.MANAGE_CONTAINER_PERMISSION);     
      
      remove(id);
   }

   final public void addSessionContainer(SessionContainer scontainer)
   {
      SecurityManager security = System.getSecurityManager();
      if (security != null)
         security.checkPermission(ContainerPermissions.MANAGE_CONTAINER_PERMISSION);     
      
      put(scontainer.getSessionId(), scontainer);
   }
}
