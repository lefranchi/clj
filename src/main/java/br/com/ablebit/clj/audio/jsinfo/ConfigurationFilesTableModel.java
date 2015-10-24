/*
 *	ConfigurationFilesTableModel.java
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

import  java.io.IOException;

import  java.util.List;
import  java.util.Enumeration;
import  java.util.ArrayList;

import  javax.swing.table.AbstractTableModel;



public class ConfigurationFilesTableModel
extends AbstractTableModel
{
	private static final String[] sm_astrColumnNames =
	{
		"Location"
	};

	private static final String	BASE_NAME = "META-INF/services/";

	private String		m_strName;
	private Class		m_class;
	private List		m_configurationFilesCache;



	public ConfigurationFilesTableModel()
	{
		m_configurationFilesCache = new ArrayList();
	}



	public void setName(String strName)
	{
		m_strName = strName;
		if (JSInfoDebug.getTraceConfigurationFilesTableModel())
		{
			JSInfoDebug.out("ConfigurationFilesTableModel.setName(): setting name: " + strName);
		}
		try
		{
			m_class = Class.forName(strName);
		}
		catch (ClassNotFoundException e)
		{
			if (JSInfoDebug.getTraceConfigurationFilesTableModel()
			    || JSInfoDebug.getTraceAllExceptions())
			{
				JSInfoDebug.out(e);
			}
		}
		cache();
		fireTableDataChanged();
	}



	private void cache()
	{
		if (JSInfoDebug.getTraceConfigurationFilesTableModel())
		{
			JSInfoDebug.out("ConfigurationFilesTableModel.cache(): trying to build cache.");
		}
		m_configurationFilesCache.clear();
		String	strFullName = BASE_NAME + m_strName;
		Enumeration	configs = null;
		try
		{
			configs = ClassLoader.getSystemResources(strFullName);
		}
		catch (IOException e)
		{
			if (JSInfoDebug.getTraceConfigurationFilesTableModel()
			    || JSInfoDebug.getTraceAllExceptions())
			{
				JSInfoDebug.out(e);
			}
		}
		if (configs != null)
		{
			if (JSInfoDebug.getTraceConfigurationFilesTableModel())
			{
				JSInfoDebug.out("ConfigurationFilesTableModel.cache(): Enumeration present.");
			}
			while (configs.hasMoreElements())
			{
				Object	config = configs.nextElement();
				m_configurationFilesCache.add(config);
				if (JSInfoDebug.getTraceConfigurationFilesTableModel())
				{
					JSInfoDebug.out("ConfigurationFilesTableModel.cache(): " + config);
				}
			}
		}
	}



	public int getRowCount()
	{
		if (JSInfoDebug.getTraceConfigurationFilesTableModel())
		{
			JSInfoDebug.out("ConfigurationFilesTableModel.getRowCount(): row count: " + m_configurationFilesCache.size());
		}
		return m_configurationFilesCache.size();
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
			return m_configurationFilesCache.get(nRow)/*.getClass().getName()*/;

		default:
			return null;
		}
	}
}



/*** ConfigurationFilesTableModel.java ***/
