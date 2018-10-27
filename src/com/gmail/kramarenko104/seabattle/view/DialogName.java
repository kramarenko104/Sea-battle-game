package com.gmail.kramarenko104.seabattle.view;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class DialogName extends JDialog {

	public String getName() {
        String result = JOptionPane.showInputDialog(DialogName.this, "What is your name? ");
		return result;
	}
}
