package testwebserver;

import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * This program is a client for the SimpleWebServer server. The
 * server has a list of available files that can be
 * downloaded by the client. The client can also download
 * the list of files. When the connection is opened, the
 * client sends one of two possible commands to the server:
 * "index" or "get <file-name>". The server replies to
 * the first command by sending the list of available files.
 * It responds to the second with a one-line message, containing
 * either "OK" or "ERROR". If the message contains "OK", it is
 * followed by the contents of the file with the specified
 * name. The "ERROR" message indicates that the specified
 * file does not exist on the server. (The server can also
 * respond with the message "unsupported command" if the command
 * it reads is not one of the two possible legal commands.)
 * 
 * The client program works with user supplied values for the
 * server's IP address and the requested file name to be
 * retrieved.
 * 
 * @author A.Nonymous
 */
public class TestWebServer {

	private static final int LISTENING_PORT = 50500;  // Should match port for the SimpleWebServer.
	private static Scanner in;  // Scanner object used throughout the main while loop.
	
	public static void main(String[] args) {

		String computer; // Name or IP address of server.
		Socket connection; // Socket for communicating with that computer.
		PrintWriter outgoing; // Stream for sending a command to the server.
		BufferedReader incoming; // Stream for reading data from the connection.
		String command; // Command to send to the server.


		/* Get the server name and the message to send to the server. */

		in = new Scanner(System.in);
		System.out.println("Enter the IP address of the server.");
		System.out.print("(\"localhost\" for running the server on your own computer.)\n> ");
		computer = in.nextLine();

		// If the user enters an invalid response, an "INDEX" command will be sent.
		int selectedChoice = 1;

		while (true) {

			try {
				System.out.println("\nEnter 1 to see a list of files available on the server,");
				System.out.print("2 to enter a filename and get its contents, or 0 to quit.\n> ");
				String choice = in.next();
				selectedChoice = Integer.parseInt(choice);
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid integer choice.");
			}

			if (selectedChoice == 1) {
				command = "INDEX";
				System.out.println();
			} else if (selectedChoice == 0) {
				System.out.println("Goodbye.");
				break;
			} else if (selectedChoice == 3) {
				command = "TEST";  // Used to test 501 Not Implemented Error response.
			} else {
				System.out.print("\nEnter a filename:\n> ");
				String fileName = in.next();

				command = "GET " + "\\" + fileName + " HTTP/1.1";
				System.out.println("\nCommand sent: " + command);
			}

			try {
				connection = new Socket(computer, LISTENING_PORT);
				incoming = new BufferedReader(new InputStreamReader(
								connection.getInputStream()));
				outgoing = new PrintWriter(connection.getOutputStream());
				outgoing.println(command);
				outgoing.flush(); 
			} catch (Exception e) {
				System.out.println("Can't make connection to server at \""
								+ computer + "\".");
				System.out.println("Error:  " + e);
				return;
			}

			/* Read and process the server's response to the command. */

			try {
				if (selectedChoice == 1) {
					// The command was "index". Read and display lines
					// from the server until the end-of-stream is reached.
					System.out.println("File list from server:");
					while (true) {
						String line = incoming.readLine();
						if (line == null)
							break;
						System.out.println("   " + line);
					}
				} else {
					// The command was "get <file-name>". Read the server's
					// response message. If the message is "OK", get the file.
					String message = incoming.readLine();

					CharSequence goodStatus = "OK";

					if (!message.contains(goodStatus)) {
						System.out.println("Error:");
						System.out.println("Message from server: " + message);
						
						String nextMessage = incoming.readLine();
						while (nextMessage != null) {
							System.out.println(nextMessage);
							nextMessage = incoming.readLine();
						}
						return;
					}

					while (true) {
						// Copy lines from incoming to the file until
						// the end of the incoming stream is encountered.
						String line = incoming.readLine();
						if (line == null)
							break;
						System.out.println(line);
					}
				}
			} catch (Exception e) {
				System.out.println("Sorry, an error occurred while reading data from the server.");
				System.out.println("Error: " + e);
			} 
			finally {
				try {
					connection.close();
				} catch (IOException e) {
				}
			}
		} // end while

		in.close();

	} // end main()

} // end class TestWebServer