/*
 * Copyright (C) 2010 eXo Platform SAS.
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
package org.exoplatform.services.rpc;

import java.io.Serializable;

/**
 *  This class represents the command that can be executed on a remote server.
 *  A RemoteCommand needs to be ThreadSafe since it can be re-used by several
 *  threads in parallel.
 * @author <a href="mailto:nicolas.filotto@exoplatform.com">Nicolas Filotto</a>
 * @version $Id$
 */
public interface RemoteCommand
{
   /**
    * This method will execute the command on the local machine.
    * @param container The container from which the services needed for the command
    * will be extracted
    * @param args The parameters needed to execute the command
    * @return arbitrary return value generated by performing this command
    * @throws Throwable in the event of problems.
    */
   Serializable execute(Serializable[] args) throws Throwable;

   /**
    * Gives the id of the command
    * @return the unique ID of the command
    */
   String getId();
}
