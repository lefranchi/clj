/*
 *	ServiceProviderTableModel.java
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package br.com.ablebit.clj.audio.jsinfo;

import  java.lang.reflect.Method;

import  java.util.Iterator;
import  java.util.List;
import  java.util.ArrayList;

import  javax.swing.table.AbstractTableModel;



public class ServiceProviderTableModel
extends AbstractTableModel
{
	public static final Object[]	sm_configurationSources = new Object[]
	{
		// for Sun jdk1.3.0
		new String[]{"sun.misc.Service", "providers"},
		// for Tritonus with jdk1.2.X
		new String[]{"org.tritonus.core.Service", "providers"},
	};

	private static final String[] sm_astrColumnNames =
	{
		"Name"
	};



	private String		m_strName;
	private Class		m_class;
	private List		m_serviceProviderCache;
	private Method		m_providersMethod = null;



	public ServiceProviderTableModel()
	{
		if (JSInfoDebug.getTraceServiceProviderTableModel())
		{
			JSInfoDebug.out("ServiceProviderTableModel.<init>(): begin");
		}
		m_serviceProviderCache = new ArrayList();

		for (int i = 0; i < sm_configurationSources.length; i++)
		{
			String	strServiceClassName = ((String[]) sm_configurationSources[i])[0];
			String	strProvidersMethodName = ((String[]) sm_configurationSources[i])[1];
			try
			{
				if (JSInfoDebug.getTraceServiceProviderTableModel())
				{
					JSInfoDebug.out("ServiceProviderTableModel.<init>(): Testing class: " + strServiceClassName);
				}
				Class	serviceClass = Class.forName(strServiceClassName);
				if (JSInfoDebug.getTraceServiceProviderTableModel())
				{
					JSInfoDebug.out("ServiceProviderTableModel.<init>(): class loaded");
				}
				if (JSInfoDebug.getTraceServiceProviderTableModel())
				{
					JSInfoDebug.out("ServiceProviderTableModel.<init>(): Testing method: " + strProvidersMethodName);
				}
				m_providersMethod = serviceClass.getMethod(strProvidersMethodName, new Class[]{Class.class});
				if (JSInfoDebug.getTraceServiceProviderTableModel())
				{
					JSInfoDebug.out("ServiceProviderTableModel.<init>(): method found");
				}
				break;
			}
			catch (Exception e)
			{
				if (JSInfoDebug.getTraceServiceProviderTableModel()
				    || JSInfoDebug.getTraceAllExceptions())
				{
					JSInfoDebug.out(e);
				}
			}
		}
		if (JSInfoDebug.getTraceServiceProviderTableModel())
		{
			JSInfoDebug.out("ServiceProviderTableModel.<init>(): end");
		}
	}



	public void setName(String strName)
	{
		if (JSInfoDebug.getTraceServiceProviderTableModel())
		{
			JSInfoDebug.out("ServiceProviderTableModel.setName(): begin");
		}
		m_strName = strName;
		if (JSInfoDebug.getTraceServiceProviderTableModel())
		{
			JSInfoDebug.out("ServiceProviderTableModel.setName(): trying to load class: " + strName);
		}
		try
		{
			m_class = Class.forName(strName);
			if (JSInfoDebug.getTraceServiceProviderTableModel())
			{
				JSInfoDebug.out("ServiceProviderTableModel.setName(): class loaded");
			}
		}
		catch (ClassNotFoundException e)
		{
			if (JSInfoDebug.getTraceServiceProviderTableModel()
			    || JSInfoDebug.getTraceAllExceptions())
			{
				JSInfoDebug.out(e);
			}
		}
		cache();
		fireTableDataChanged();
		if (JSInfoDebug.getTraceServiceProviderTableModel())
		{
			JSInfoDebug.out("ServiceProviderTableModel.setName(): end");
		}
	}



	private void cache()
	{
		if (JSInfoDebug.getTraceServiceProviderTableModel())
		{
			JSInfoDebug.out("ServiceProviderTableModel.cache(): begin.");
		}
		m_serviceProviderCache.clear();
		Iterator	services = null;
		try
		{
			if (JSInfoDebug.getTraceServiceProviderTableModel())
			{
				JSInfoDebug.out("ServiceProviderTableModel.cache(): invoking service method.");
			}
			services = (Iterator) m_providersMethod.invoke(null, new Object[]{m_class});
			if (JSInfoDebug.getTraceServiceProviderTableModel())
			{
				JSInfoDebug.out("ServiceProviderTableModel.cache(): method returned.");
			}
		}
		catch (Throwable e)
		{
			if (JSInfoDebug.getTraceServiceProviderTableModel()
			    || JSInfoDebug.getTraceAllExceptions())
			{
				JSInfoDebug.out(e);
			}
		}
		if (services != null)
		{
			if (JSInfoDebug.getTraceServiceProviderTableModel())
			{
				JSInfoDebug.out("ServiceProviderTableModel.cache(): iterator present.");
			}
			while (services.hasNext())
			{
				Object	provider = services.next();
				m_serviceProviderCache.add(provider);
				if (JSInfoDebug.getTraceServiceProviderTableModel())
				{
					JSInfoDebug.out("ServiceProviderTableModel.cache(): " + provider);
				}
			}
		}
		else
		{
			if (JSInfoDebug.getTraceServiceProviderTableModel())
			{
				JSInfoDebug.out("ServiceProviderTableModel.cache(): no configs.");
			}
		}
		if (JSInfoDebug.getTraceServiceProviderTableModel())
		{
			JSInfoDebug.out("ServiceProviderTableModel.cache(): end.");
		}
	}



	public int getRowCount()
	{
		if (JSInfoDebug.getTraceServiceProviderTableModel())
		{
			JSInfoDebug.out("ServiceProviderTableModel.getRowCount(): " + m_serviceProviderCache.size());
		}
		return m_serviceProviderCache.size();
	}



	public int getColumnCount()
	{
		return sm_astrColumnNames.length;
	}



	public Class getColumnClass(int nColumn)
	{
		return String.class;
	}



	public String getColumnName(int nColumn)
	{
		return sm_astrColumnNames[nColumn];
	}



	public Object getValueAt(int nRow, int nColumn)
	{
		switch (nColumn)
		{
		case 0:
			return m_serviceProviderCache.get(nRow)/*.getClass().getName()*/;

		default:
			return null;
		}
	}
}



/*** ServiceProviderTableModel.java ***/
