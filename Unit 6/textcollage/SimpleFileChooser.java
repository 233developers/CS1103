package textcollage;


import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * This class provides a slightly simplified interface to one of Java's
 * standard JFileChooser dialogs.  An object of type SimpleFileChooser
 * has methods that allow the user to select files for input or output.
 * If the object is used several times, the same JFileChooser is used
 * each time.  By default, the dialog box is set to the user's home
 * directory the first time it is used, and after that it remembers the
 * current directory between one use and the next.  However, methods
 * are provided for setting the current directory.  (Note:  On Windows,
 * the user's home directory will probably mean the user's "My Documents"
 * directory".)
 */
public class SimpleFileChooser {

	private JFileChooser dialog;  // The dialog, which is created when needed.

	/**
	 * Reset the default directory in the dialog box to the user's home 
	 * directory.  The next time the dialog appears, it will show the 
	 * contents of that directory.
	 */
	public void setDefaultDirectory() {
		if (dialog != null)
			dialog.setCurrentDirectory(null);
	}

	/**
	 * Set the default directory for the dialog box.  The next time the 
	 * dialog appears, it will show the contents of that directory.
	 * @param directoryName A File object that specifies the directory name.
	 * If this name is null, then the user's home directory will be used.
	 */
	public void setDefaultDirectory(String directoryName) {
		if (dialog == null)
			dialog = new JFileChooser();
		dialog.setCurrentDirectory(new File(directoryName));
	}

	/**
	 * Set the default directory for the dialog box.  The next time the 
	 * dialog appears, it will show the contents of that directory.
	 * @param directoryName The name of the new default directory.  If the 
	 * name is null, then the user's home directory will be used.
	 */
	public void setDefaultDirectory(File directory) {
		if (dialog == null)
			dialog = new JFileChooser();
		dialog.setCurrentDirectory(directory);
	}
	
	/**
	 * Show a dialog box where the user can select a file for reading.
	 * This method simply returns <code>getInputFile(null,null)</code>.
	 * @see #getInputFile(Component, String)
	 * @return the selected file, or null if the user did not select a file.
	 */
	public File getInputFile() {
		return getInputFile(null,null);
	}

	/**
	 * Show a dialog box where the user can select a file for reading.
	 * This method simply returns <code>getInputFile(parent,null)</code>.
	 * @see #getInputFile(Component, String)
	 * @return the selected file, or null if the user did not select a file.
	 */
	public File getInputFile(Component parent) {
		return getInputFile(parent,null);
	}

	/**
	 * Show a dialog box where the user can select a file for reading.
	 * If the user cancels the dialog by clicking its "Cancel" button or
	 * the Close button in the title bar, then the return value of this
	 * method is null.  Otherwise, the return value is the selected file.
	 * Note that the file has to exist, but it is not guaranteed that the
	 * user is allowed to read the file.
	 * @param parent If the parent is non-null, then the window that contains
	 * the parent component becomes the parent window of the dialog box.  This
	 * means that the window is "blocked" until the dialog is dismissed.  Also,
	 * the dialog box's position on the screen should be based on the position of
	 * the window.  Generally, you should pass your application's main window or
	 * panel as the value of this parameter.
	 * @param dialogTitle  a title to be displayed in the title bar of the dialog
	 * box.  If the value of this parameter is null, then the dialog title will
	 * be "Select Input File".
	 * @return the selected file, or null if the user did not select a file.
	 */
	public File getInputFile(Component parent, String dialogTitle) {
		if (dialog == null)
			dialog = new JFileChooser();
		if (dialogTitle != null)
			dialog.setDialogTitle(dialogTitle);
		else
			dialog.setDialogTitle("Select Input File");
		int option = dialog.showOpenDialog(parent);
		if (option != JFileChooser.APPROVE_OPTION)
			return null;  // User canceled or clicked the dialog's close box.
		File selectedFile = dialog.getSelectedFile();
		return selectedFile;
	}
	
	/**
	 * Show a dialog box where the user can select a file for writing.
	 * This method simply calls <code>getOutputFile(null,null,null)</code>
	 * @see #getOutputFile(Component, String, String)
	 * @return the selcted file, or null if no file was selected.
	 */
	public File getOutputFile() {
		return getOutputFile(null,null,null);
	}

	/**
	 * Show a dialog box where the user can select a file for writing.
	 * This method simply calls <code>getOutputFile(null,null,null)</code>
	 * @see #getOutputFile(Component, String, String)
	 * @return the selcted file, or null if no file was selected.
	 */
	public File getOutputFile(Component parent) {
		return getOutputFile(parent,null,null);
	}

	/**
	 * Show a dialog box where the user can select a file for writing.
	 * This method calls <code>getOutputFile(parent,dialogTitle,null)</code>
	 * @see #getOutputFile(Component, String, String)
	 * @return the selcted file, or null if no file was selected.
	 */
	public File getOutputFile(Component parent, String dialogTitle) {
		return getOutputFile(parent,dialogTitle,null);
	}


	/**
	 * Show a dialog box where the user can select a file for writing.
	 * If the user cancels the dialog by clicking its "Cancel" button or
	 * the Close button in the title bar, then the return value of this
	 * method is null.  A non-null value indicates that the user specified
	 * a file name and that, if the file exists, then the user wants to
	 * replace that file.  (If the user selects a file that already exists, 
	 * then the user will be asked whether to replace the existing file.)
	 * Note that it is not quaranteed that the selected file is actually
	 * writable; the user might not have permission to create or modify the file.
	 * @param parent If the parent is non-null, then the window that contains
	 * the parent component becomes the parent window of the dialog box.  This
	 * means that the window is "blocked" until the dialog is dismissed.  Also,
	 * the dialog box's position on the screen should be based on the position of
	 * the window.  Generally, you should pass your application's main window or
	 * panel as the value of this parameter.
	 * @param dialogTitle  a title to be displayed in the title bar of the dialog
	 * box.  If the value of this parameter is null, then the dialog title will
	 * be "Select Input File".
	 * @param defaultFile when the dialog appears, this name will be filled in
	 * as the name of the selected file.  If the value of this parameter is null,
	 * then the file name box will be empty.
	 * @return the selected file, or null if the user did not select a file.
	 */
	public File getOutputFile(Component parent, 
	                                 String dialogTitle, String defaultFile) {
		if (dialog == null)
			dialog = new JFileChooser();
		if (dialogTitle != null)
			dialog.setDialogTitle(dialogTitle);
		else
			dialog.setDialogTitle("Select Output File");
		if (defaultFile == null)
			dialog.setSelectedFile(null);
		else
			dialog.setSelectedFile(new File(defaultFile));
		while (true) {
			int option = dialog.showSaveDialog(parent);
			if (option != JFileChooser.APPROVE_OPTION)
				return null;  // User canceled or clicked the dialog's close box.
			File selectedFile = dialog.getSelectedFile();
			if ( ! selectedFile.exists() ) 
				return selectedFile;
			else {  // Ask the user whether to replace the file.
				int response = JOptionPane.showConfirmDialog( parent,
						"The file \"" + selectedFile.getName()
						+ "\" already exists.\nDo you want to replace it?", 
						"Confirm Save",
						JOptionPane.YES_NO_CANCEL_OPTION, 
						JOptionPane.WARNING_MESSAGE );
				if (response == JOptionPane.CANCEL_OPTION)
					return null;  // User does not want to select a file.
				if (response == JOptionPane.YES_OPTION)
					return selectedFile;  // User wants to replace the file
				// A "No" response will cause the file dialog to be shown again.
			}
		}
	}

}
