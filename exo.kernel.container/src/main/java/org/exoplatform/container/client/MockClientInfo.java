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
package org.exoplatform.container.client;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 7, 2005
 * @version $Id: MockClientInfo.java 5799 2006-05-28 17:55:42Z geaz $
 */
public class MockClientInfo implements ClientInfo
{
   public MockClientInfo()
   {
   }

   public String getClientType()
   {
      return "N/A";
   }

   public String getRemoteUser()
   {
      return "exo";
   }

   public String getIpAddress()
   {
      return "127.0.0.1";
   }

   public String getClientName()
   {
      return "Mock client";
   }

}
