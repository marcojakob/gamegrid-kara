package ch.gamegrid.kara.actors;

import javax.swing.JOptionPane;

/**
 * KaraIO extends the functionality of Kara by adding input/output methods.
 * 
 * <i>KaraIO erweitert die Funktionalitaet von Kara mit Methoden fuer
 * Input/Output.</i>
 * 
 * @author Marco Jakob (majakob@gmx.ch)
 */
public abstract class KaraIO extends Kara {

	/**
	 * Displays the specified message as info to the user <br>
	 * <i>Zeigt die angegebene Nachricht dem Benutzer an</i>
	 */
	public void displayMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Info",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Asks the user for an integer in a dialog <br>
	 * <i>Fragt den Benutzer nach einer ganzen Zahl in einem Dialog</i>
	 * 
	 * @param message
	 *            the message that should be displayed in the dialog
	 * @return the number that the user entered
	 */
	public int intInput(String message) {
		int n = 0;
		boolean correctValue = false;

		// Ask until the user entered a correct value
		while (!correctValue) {
			String inputValue = JOptionPane.showInputDialog(message);

			if (inputValue != null) {
				try {
					n = Integer.valueOf(inputValue);
					correctValue = true;
				} catch (NumberFormatException e) {
					// Not a valid number
					displayMessage("<html>Please enter a valid integer!<p><i>Bitte eine ganze Zahl eingeben!</i></html>");
				}
			} else {
				// User pushed cancel or closed the window
//				displayMessage("<html>Please enter an integer!<p><i>Bitte eine Zahl eingeben!</i></html>");
				
				// Exit the program. Must do this because we cannot return a valid value.
				// Better would be if the caller would handle this case but this would me more difficult.
				System.exit(0);
			}
		}

		return n;
	}

	/**
	 * Asks the user for a floating point number in a dialog <br>
	 * <i>Fragt den Benutzer nach einer Gleitkommazahl in einem Dialog</i>
	 * 
	 * @param message
	 *            the message that should be displayed in the dialog
	 * @return the number that the user entered
	 */
	public double doubleInput(String message) {
		double d = 0;
		boolean correctValue = false;

		// Ask until the user entered a correct value
		while (!correctValue) {
			String inputValue = JOptionPane.showInputDialog(message);

			if (inputValue != null) {
				try {
					d = Double.valueOf(inputValue);
					correctValue = true;
				} catch (NumberFormatException e) {
					// Not a valid number
					displayMessage("<html>Please enter a valid number!<p><i>Bitte eine Gleitkommazahl eingeben!</i></html>");
				}
			} else {
				// User pushed cancel or closed the window
//				displayMessage("<html>Please enter a number!<p><i>Bitte eine Zahl eingeben!</i></html>");
				
				// Exit the program. Must do this because we cannot return a valid value.
				// Better would be if the caller would handle this case but this would me more difficult.
				System.exit(0);
			}
		}

		return d;
	}
}
