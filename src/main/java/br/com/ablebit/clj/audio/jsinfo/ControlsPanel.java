/*
 *	ControlsPanel.java
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

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.CompoundControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.Mixer;



public class ControlsPanel
extends JPanel
implements TreeSelectionListener
{
	private ControlPropertiesPanel	m_controlPropertiesPanel;



	public ControlsPanel()
	{
		if (JSInfoDebug.getTraceControlsPanel()) { JSInfoDebug.out("ControlsPanel.<init>(): begin"); }
		setLayout(new GridLayout(1, 0));

		DefaultMutableTreeNode	mixersNode = new DefaultMutableTreeNode("Mixers");
		DefaultTreeModel	model = new DefaultTreeModel(mixersNode);
		Mixer.Info[]	mixerInfos = AudioSystem.getMixerInfo();
		for (int i = 0; i < mixerInfos.length; i++)
		{
			if (JSInfoDebug.getTraceControlsPanel()) { JSInfoDebug.out("ControlsPanel.<init>(): mixer: " + mixerInfos[i]); }
			Mixer	mixer = AudioSystem.getMixer(mixerInfos[i]);
			addMixerNode(mixersNode, mixer);
		}

		JTree			tree = new JTree(model);
		tree.addTreeSelectionListener(this);
		JScrollPane	leftScrollPane = new JScrollPane(tree);

		m_controlPropertiesPanel = new ControlPropertiesPanel();
		JScrollPane	rightScrollPane = new JScrollPane(m_controlPropertiesPanel);
		JSplitPane	splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScrollPane, rightScrollPane);
		add(splitPane);
		if (JSInfoDebug.getTraceControlsPanel()) { JSInfoDebug.out("ControlsPanel.<init>(): end"); }
	}



	private static void addMixerNode(DefaultMutableTreeNode parentNode, Mixer mixer)
	{
		if (JSInfoDebug.getTraceControlsPanel()) { JSInfoDebug.out("ControlsPanel.addMixerNode(): begin"); }
		DefaultMutableTreeNode	mixerNode = new DefaultMutableTreeNode(mixer.getMixerInfo().getName());
		parentNode.add(mixerNode);

		// source data lines
		Line.Info[]	sourceLineInfos = mixer.getSourceLineInfo();
		if (sourceLineInfos.length > 0)
		{
			DefaultMutableTreeNode	sourceLinesNode = new DefaultMutableTreeNode("SourceDataLines");
			mixerNode.add(sourceLinesNode);
			addLineNodes(sourceLinesNode, mixer, sourceLineInfos);
		}

		// target data lines
		Line.Info[]	targetLineInfos = mixer.getTargetLineInfo();
		if (targetLineInfos.length > 0)
		{
			DefaultMutableTreeNode	targetLinesNode = new DefaultMutableTreeNode("TargetDataLines");
			mixerNode.add(targetLinesNode);
			addLineNodes(targetLinesNode, mixer, targetLineInfos);
		}

		// controls of the mixer itself
		addControlNodes(mixerNode, mixer);
		if (JSInfoDebug.getTraceControlsPanel()) { JSInfoDebug.out("ControlsPanel.addMixerNode(): end"); }
	}



	private static void addLineNodes(MutableTreeNode parentNode, Mixer mixer, Line.Info[] lineInfos)
	{
		if (JSInfoDebug.getTraceControlsPanel()) { JSInfoDebug.out("ControlsPanel.addLineNodes(): begin"); }
			for (int i = 0; i < lineInfos.length; i++)
			{
				Line	line = null;
				try
				{
					line = mixer.getLine(lineInfos[i]);
					DefaultMutableTreeNode	lineNode = new LineInfoMutableTreeNode(lineInfos[i]);
					parentNode.insert(lineNode, i);
					addControlNodes(lineNode, line);
				}
				catch (LineUnavailableException e)
				{
					JSInfoDebug.out(e);
				}
				catch (IllegalArgumentException ie)
				{
					JSInfoDebug.out(ie);
				}
			}
		if (JSInfoDebug.getTraceControlsPanel()) { JSInfoDebug.out("ControlsPanel.addLineNodes(): end"); }
	}



	private static void addControlNodes(MutableTreeNode parentNode, Line line)
	{
		if (JSInfoDebug.getTraceControlsPanel()) { JSInfoDebug.out("ControlsPanel.addControlNodes(): begin"); }
		if (JSInfoDebug.getTraceControlsPanel()) { JSInfoDebug.out("ControlsPanel.addControlNodes(): line: " + line); }
		Control[]	controls = line.getControls();
		for (int i = 0; i < controls.length; i++)
		{
			DefaultMutableTreeNode	controlNode = new ControlMutableTreeNode(controls[i]);
			parentNode.insert(controlNode, i);
			if (controls[i] instanceof CompoundControl)
			{
				CompoundControl	compControl = (CompoundControl) controls[i];
				addCompoundControlNodes(controlNode, compControl);
			}
		}
		if (JSInfoDebug.getTraceControlsPanel()) { JSInfoDebug.out("ControlsPanel.addControlNodes(): end"); }
	}


	private static void addCompoundControlNodes(MutableTreeNode parentNode, CompoundControl compoundControl)
	{
		// System.out.println("compoundcontrol: " + compoundControl);
		Control[]	controls = compoundControl.getMemberControls();
		for (int i = 0; i < controls.length; i++)
		{
			DefaultMutableTreeNode	controlNode = new ControlMutableTreeNode(controls[i]);
			parentNode.insert(controlNode, i);
			if (controls[i] instanceof CompoundControl)
			{
				CompoundControl	compControl = (CompoundControl) controls[i];
				addCompoundControlNodes(controlNode, compControl);
			}
		}
	}


	// TreeSelectionListener
	public void valueChanged(TreeSelectionEvent event)
	{
		if (JSInfoDebug.getTraceControlsPanel()) { JSInfoDebug.out("ControlsPanel.valueChanged(): begin"); }
		// if (JSInfoDebug.getTraceControlsPanel()) { JSInfoDebug.out("ControlsPanel.valueChanged(): event: " + event); }
		TreePath	path = event.getNewLeadSelectionPath();
		// if (JSInfoDebug.getTraceControlsPanel()) { JSInfoDebug.out("ControlsPanel.valueChanged(): lead path: " + path); }
		Object	leaf = path.getLastPathComponent();
		// if (JSInfoDebug.getTraceControlsPanel()) { JSInfoDebug.out("ControlsPanel.valueChanged(): leaf's class: " + leaf.getClass()); }
		Control	control = null;
		if (leaf instanceof DefaultMutableTreeNode)
		{
			Object	userObject = ((DefaultMutableTreeNode) leaf).getUserObject();
			if (userObject instanceof Control)
			{
				control = (Control) userObject;
			}
		}
		if (JSInfoDebug.getTraceControlsPanel()) { JSInfoDebug.out("ControlsPanel.valueChanged(): control: " + control); }
		m_controlPropertiesPanel.setControl(control);
		if (JSInfoDebug.getTraceControlsPanel()) { JSInfoDebug.out("ControlsPanel.valueChanged(): end"); }
	}
} 


/*** ControlsPanel.java ***/
