/*
 *	ControlPropertiesPanel.java
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

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.CompoundControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.EnumControl;
import javax.sound.sampled.FloatControl;



public class ControlPropertiesPanel
extends JPanel
{
	private JTextField	m_nameTextField;
	private JTextField	m_classTextField;
	private JPanel		m_booleanPanel;
	private JTextField	m_booleanTrueStateLabelTextField;
	private JTextField	m_booleanFalseStateLabelTextField;
	private JPanel		m_compountPanel;
	private JPanel		m_enumPanel;
	private JList		m_enumValuesList;
	private JPanel		m_floatPanel;
	private JTextField	m_floatMinimumTextField;
	private JTextField	m_floatMaximumTextField;
	private JTextField	m_floatPrecisionTextField;
	private JTextField	m_floatUpdatePeriodTextField;
	private JTextField	m_floatMinLabelTextField;
	private JTextField	m_floatMidLabelTextField;
	private JTextField	m_floatMaxLabelTextField;
	private JTextField	m_floatUnitsTextField;
	private JPanel		m_nullPanel;




	public ControlPropertiesPanel()
	{
		setLayout(new BorderLayout());
		JPanel	generalPanel = new JPanel();
		generalPanel.setLayout(new GridLayout(0, 2));
		generalPanel.add(new JLabel("Name/Type:"));
		m_nameTextField = new JTextField();
		m_nameTextField.setEditable(false);
		generalPanel.add(m_nameTextField);
		generalPanel.add(new JLabel("Class:"));
		m_classTextField = new JTextField();
		m_classTextField.setEditable(false);
		generalPanel.add(m_classTextField);
		add(generalPanel, BorderLayout.NORTH);

		JPanel	propertiesPanel = new JPanel();

		m_booleanPanel = new JPanel();
		m_booleanPanel.setLayout(new GridLayout(0, 2));
		m_booleanPanel.add(new JLabel("Type:"));
		m_booleanPanel.add(new JLabel("BooleanControl"));
		m_booleanPanel.add(new JLabel("State label for true:"));
		m_booleanTrueStateLabelTextField = new JTextField();
		m_booleanTrueStateLabelTextField.setEditable(false);
		m_booleanPanel.add(m_booleanTrueStateLabelTextField);
		m_booleanPanel.add(new JLabel("State label for false:"));
		m_booleanFalseStateLabelTextField = new JTextField();
		m_booleanFalseStateLabelTextField.setEditable(false);
		m_booleanPanel.add(m_booleanFalseStateLabelTextField);
		propertiesPanel.add(m_booleanPanel);

		m_compountPanel = new JPanel();
		m_compountPanel.setLayout(new GridLayout(0, 2));
		m_compountPanel.add(new JLabel("Type:"));
		m_compountPanel.add(new JLabel("CompoundControl"));
		m_compountPanel.add(new JLabel("See tree view for member controls"));
		propertiesPanel.add(m_compountPanel);

		m_enumPanel = new JPanel();
		m_compountPanel.add(new JLabel("Type:"));
		m_compountPanel.add(new JLabel("EnumControl"));
		m_enumValuesList = new JList();
		JScrollPane	scrollPane = new JScrollPane(m_enumValuesList);
		m_enumPanel.add(scrollPane);
		propertiesPanel.add(m_enumPanel);

		m_floatPanel = new JPanel();
		m_floatPanel.setLayout(new GridLayout(0, 2));
		m_floatPanel.add(new JLabel("Type:"));
		m_floatPanel.add(new JLabel("FloatControl"));
		m_floatPanel.add(new JLabel("Minimum:"));
		m_floatMinimumTextField = new JTextField();
		m_floatMinimumTextField.setEditable(false);
		m_floatPanel.add(m_floatMinimumTextField);
		m_floatPanel.add(new JLabel("Maximum:"));
		m_floatMaximumTextField = new JTextField();
		m_floatMaximumTextField.setEditable(false);
		m_floatPanel.add(m_floatMaximumTextField);
		m_floatPanel.add(new JLabel("Precision:"));
		m_floatPrecisionTextField = new JTextField();
		m_floatPrecisionTextField.setEditable(false);
		m_floatPanel.add(m_floatPrecisionTextField);
		m_floatPanel.add(new JLabel("Update Period:"));
		m_floatUpdatePeriodTextField = new JTextField();
		m_floatUpdatePeriodTextField.setEditable(false);
		m_floatPanel.add(m_floatUpdatePeriodTextField);
		m_floatPanel.add(new JLabel("Min Label:"));
		m_floatMinLabelTextField = new JTextField();
		m_floatMinLabelTextField.setEditable(false);
		m_floatPanel.add(m_floatMinLabelTextField);
		m_floatPanel.add(new JLabel("Mid Label:"));
		m_floatMidLabelTextField = new JTextField();
		m_floatMidLabelTextField.setEditable(false);
		m_floatPanel.add(m_floatMidLabelTextField);
		m_floatPanel.add(new JLabel("Max Label:"));
		m_floatMaxLabelTextField = new JTextField();
		m_floatMaxLabelTextField.setEditable(false);
		m_floatPanel.add(m_floatMaxLabelTextField);
		m_floatPanel.add(new JLabel("Units:"));
		m_floatUnitsTextField = new JTextField();
		m_floatUnitsTextField.setEditable(false);
		m_floatPanel.add(m_floatUnitsTextField);
		propertiesPanel.add(m_floatPanel);

		m_nullPanel = new JPanel();
		m_nullPanel.add(new JLabel("no control selected"));
		propertiesPanel.add(m_nullPanel);
		add(propertiesPanel, BorderLayout.CENTER);

		setControl(null);
	}



	public void setControl(Control control)
	{
		if (JSInfoDebug.getTraceControlPropertiesPanel()) { JSInfoDebug.out("ControlPropertiesPanel.setControl(): begin"); }
		if (control != null)
		{
			m_nameTextField.setText(control.getType().toString());
			m_classTextField.setText(control.getClass().getName());
		}
		else
		{
			m_nameTextField.setText("---");
			m_classTextField.setText("---");
		}
		if (control instanceof BooleanControl)
		{
			if (JSInfoDebug.getTraceControlPropertiesPanel()) { JSInfoDebug.out("ControlPropertiesPanel.setControl(): BooleanControl branch"); }
			m_booleanPanel.setVisible(true);
			m_compountPanel.setVisible(false);
			m_enumPanel.setVisible(false);
			m_floatPanel.setVisible(false);
			m_nullPanel.setVisible(false);
			setValues((BooleanControl) control);
		}
		else if (control instanceof CompoundControl)
		{
			if (JSInfoDebug.getTraceControlPropertiesPanel()) { JSInfoDebug.out("ControlPropertiesPanel.setControl(): CompoundControl branch"); }
			m_booleanPanel.setVisible(false);
			m_compountPanel.setVisible(true);
			m_enumPanel.setVisible(false);
			m_floatPanel.setVisible(false);
			m_nullPanel.setVisible(false);
			setValues((CompoundControl) control);
		}
		else if (control instanceof EnumControl)
		{
			if (JSInfoDebug.getTraceControlPropertiesPanel()) { JSInfoDebug.out("ControlPropertiesPanel.setControl(): EnumControl branch"); }
			m_booleanPanel.setVisible(false);
			m_compountPanel.setVisible(false);
			m_enumPanel.setVisible(true);
			m_floatPanel.setVisible(false);
			m_nullPanel.setVisible(false);
			setValues((EnumControl) control);
		}
		else if (control instanceof FloatControl)
		{
			if (JSInfoDebug.getTraceControlPropertiesPanel()) { JSInfoDebug.out("ControlPropertiesPanel.setControl(): FloatControl branch"); }
			m_booleanPanel.setVisible(false);
			m_compountPanel.setVisible(false);
			m_enumPanel.setVisible(false);
			m_floatPanel.setVisible(true);
			m_nullPanel.setVisible(false);
			setValues((FloatControl) control);
		}
		else
		{
			if (JSInfoDebug.getTraceControlPropertiesPanel()) { JSInfoDebug.out("ControlPropertiesPanel.setControl(): null branch"); }
			m_booleanPanel.setVisible(false);
			m_compountPanel.setVisible(false);
			m_enumPanel.setVisible(false);
			m_floatPanel.setVisible(false);
			m_nullPanel.setVisible(true);
		}
		
		validate();
		if (JSInfoDebug.getTraceControlPropertiesPanel()) { JSInfoDebug.out("ControlPropertiesPanel.setControl(): end"); }
	}



	private void setValues(BooleanControl control)
	{
		if (JSInfoDebug.getTraceControlPropertiesPanel()) { JSInfoDebug.out("ControlPropertiesPanel.setValues(BooleanControl): begin"); }
		m_booleanTrueStateLabelTextField.setText(control.getStateLabel(true));
		m_booleanFalseStateLabelTextField.setText(control.getStateLabel(false));
		if (JSInfoDebug.getTraceControlPropertiesPanel()) { JSInfoDebug.out("ControlPropertiesPanel.setValues(BooleanControl): end"); }
	}



	private void setValues(CompoundControl control)
	{
		if (JSInfoDebug.getTraceControlPropertiesPanel()) { JSInfoDebug.out("ControlPropertiesPanel.setValues(CompoundControl): begin"); }
		if (JSInfoDebug.getTraceControlPropertiesPanel()) { JSInfoDebug.out("ControlPropertiesPanel.setValues(CompoundControl): end"); }
	}



	private void setValues(EnumControl control)
	{
		if (JSInfoDebug.getTraceControlPropertiesPanel()) { JSInfoDebug.out("ControlPropertiesPanel.setValues(EnumControl): begin"); }
		Object[]	aPossibleValues = control.getValues();
		m_enumValuesList.setListData(aPossibleValues);
		if (JSInfoDebug.getTraceControlPropertiesPanel()) { JSInfoDebug.out("ControlPropertiesPanel.setValues(EnumControl): end"); }
	}



	private void setValues(FloatControl control)
	{
		if (JSInfoDebug.getTraceControlPropertiesPanel()) { JSInfoDebug.out("ControlPropertiesPanel.setValues(FloatControl): begin"); }
		m_floatMinimumTextField.setText("" + control.getMinimum());
		m_floatMaximumTextField.setText("" + control.getMaximum());
		m_floatPrecisionTextField.setText("" + control.getPrecision());
		m_floatUpdatePeriodTextField.setText("" + control.getUpdatePeriod());
		m_floatMinLabelTextField.setText(control.getMinLabel());
		m_floatMidLabelTextField.setText(control.getMidLabel());
		m_floatMaxLabelTextField.setText(control.getMaxLabel());
		m_floatUnitsTextField.setText(control.getUnits());
		if (JSInfoDebug.getTraceControlPropertiesPanel()) { JSInfoDebug.out("ControlPropertiesPanel.setValues(FloatControl): end"); }
	}



} 



/*** ControlPropertiesPanel.java ***/
