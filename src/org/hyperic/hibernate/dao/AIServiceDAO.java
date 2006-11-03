package org.hyperic.hibernate.dao;

import org.hibernate.Session;
import org.hyperic.hq.autoinventory.AIService;
import org.hyperic.hq.appdef.shared.AIServiceValue;

import java.util.Collection;

/*
* NOTE: This copyright does *not* cover user programs that use HQ
* program services by normal system calls through the application
* program interfaces provided as part of the Hyperic Plug-in Development
* Kit or the Hyperic Client Development Kit - this is merely considered
* normal use of the program, and does *not* fall under the heading of
* "derived work".
* 
* Copyright (C) [2004, 2005, 2006], Hyperic, Inc.
* This file is part of HQ.
* 
* HQ is free software; you can redistribute it and/or modify
* it under the terms version 2 of the GNU General Public License as
* published by the Free Software Foundation. This program is distributed
* in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
* even the implied warranty of MERCHANTABILITY or FITNESS FOR A
* PARTICULAR PURPOSE. See the GNU General Public License for more
* details.
* 
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
* USA.
*/
/**
 * CRUD, finders, etc for AIService
 */
public class AIServiceDAO extends HibernateDAO
{
    public AIServiceDAO(Session session)
    {
        super(AIService.class, session);
    }

    public AIService findById(Integer id)
    {
        return (AIService)super.findById(id);
    }

    public void evict(AIService entity)
    {
        super.evict(entity);
    }

    public AIService merge(AIService entity)
    {
        return (AIService)super.merge(entity);
    }

    public void save(AIService entity)
    {
        super.save(entity);
    }

    public void remove(AIService entity)
    {
        super.remove(entity);
    }

    public AIService create(AIServiceValue sv)
    {
        AIService s = new AIService(sv);
        save(s);
        return s;
    }

    public Collection findByType(String stName)
    {
        String sql="from AIService where serviceTypeName=?";
        return getSession().createQuery(sql)
            .setString(0, stName)
            .list();
    }

    public AIService findByName(String name)
    {
        String sql="from AIService where lower(name)=?";
        return (AIService)getSession().createQuery(sql)
            .setString(0, name.toLowerCase())
            .uniqueResult();
    }

}
