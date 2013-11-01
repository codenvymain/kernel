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
package org.exoplatform.services.rpc.impl;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.xml.InitParams;
import org.jgroups.Address;
import org.jgroups.Channel;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.View;
import org.jgroups.blocks.GroupRequest;
import org.jgroups.util.RspList;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * This class is the implementation of the {@link AbstractRPCService} for JGroups 2.
 * 
 * @author <a href="mailto:nicolas.filotto@exoplatform.com">Nicolas Filotto</a>
 * @version $Id$
 */
public class RPCServiceImpl extends AbstractRPCService
{

   /**
    * {@inheritDoc}
    */
   public RPCServiceImpl(ExoContainerContext ctx, InitParams params, ConfigurationManager configManager)
   {
      super(ctx, params, configManager);
   }

   /**
    * {@inheritDoc}
    */
   protected Address getLocalAddress()
   {
      return channel.getLocalAddress();
   }
   
   /**
    * {@inheritDoc}
    */
   protected RspList castMessage(List<Address> dests, Message msg, boolean synchronous, long timeout)
   {
      return dispatcher.castMessage(dests instanceof Vector ? (Vector<Address>)dests : new Vector<Address>(dests), msg,
         synchronous ? GroupRequest.GET_ALL : GroupRequest.GET_NONE, timeout);
   }
   
   /**
    * {@inheritDoc}
    */
   protected Channel createChannel() throws Exception
   {
      Channel channel = new JChannel(configurator);
      channel.setOpt(Channel.AUTO_RECONNECT, true);
      return channel;
   }

   /**
    * {@inheritDoc}
    */
   protected List<Address> getMembers(View view)
   {
      return view.getMembers();
   }

   /**
    * {@inheritDoc}
    */
   protected void setObject(Message m, Object o)
   {
      m.setObject((Serializable)o);
   }
}
