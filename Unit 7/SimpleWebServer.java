package simplewebserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.Date;
import java.util.Scanner;

/**
 * This program is a simple multi-threaded web server.
 * <p>
 * The server has a list of available files that can be downloaded 
 * by a client.  A client can also download the list of files.  When the
 * connection is opened, a client sends one of two possible commands
 * to the server: "index" or "get <file-name>". The server replies to
 * the first command by sending the list of available files.
 * It responds to the second with a one-line message, containing
 * either "OK" or "ERROR". If the message contains "OK", it is
 * followed by the contents of the file with the specified
 * name. The "ERROR" message indicates that the specified
 * file does not exist on the server. (The server can also
 * respond with the message "unsupported command" if the command
 * it reads is not one of the two possible legal commands.)
 * 
 * The files that the server can serve should all be contained in
 * the directory indicated by the constant ROOT_DIRECTORY.  This can
 * be adjusted to a directory of your choosing on your computer, or it
 * defaults to the current working directory.
 * 
 * This version of the program defines a multi-threaded
 * server that uses a thread pool.  The threads handle
 * all communication with the clients.  The main program
 * simply accepts connections and puts them into a queue.
 * The connection-handling threads in the thread pool remove
 * connections from the queue as they become available.
 * 
 * @author Ryan Coon
 */
public class SimpleWebServer {

	/**
	 * The server listens on this port. Note that the port number must
	 * be greater than 1024 and lest than 65535.
	 */
	private final static int LISTENING_PORT = 50500;

	/**
	 * The server's root directory.  I used a directory on my home computer
	 * to store the files that SimpleWebServer would serve up.  For running the
	 * server on your computer, user.dir will make the server's root directory
	 * the current working directory that you've saved the file in.
	 */
	private final static String ROOT_DIRECTORY = System.getProperty("user.dir");
	//private final static String ROOT_DIRECTORY = "G:\\temp\\rootDirectory";
	
    /**
     * The number of threads in the thread pool.
     */
    private static final int THREAD_POOL_SIZE = 6;
    
    /**
     * The length of the ArrayBlockingQueue of connections.
     * This should not be too big, since connections in the
     * queue are waiting for service and hopefully won't 
     * spend too long in the queue.
     */
    private static final int CONNECTION_QUEUE_SIZE = 3;
    
    /**
     * The queue that is used to send connections from the
     * main program to the connection-handling threads.
     * A connection is represented by a connected Socket.
     */
    private static ArrayBlockingQueue<Socket> connectionQueue;
	
	/**
	 * Main program creates the thread pool and then opens a server
	 * socket and listens for connection requests.
	 * 
	 * @param args Command line args (not used).
	 */
	public static void main(String[] args) {
		
		ServerSocket serverSocket;
		Socket connection;
		
        /* Create the connection queue.  We want to do this before 
         * creating the threads, which need to use the queue.
         */
        connectionQueue = new ArrayBlockingQueue<Socket>(CONNECTION_QUEUE_SIZE);
        
        /* Create the thread pool and start the threads.  Note that 
         * there is no need to keep references to the threads, since
         * there is nothing to do with them in this program
         * after they have been started.
         */        
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            ConnectionHandler worker = new ConnectionHandler();
            worker.start();
        }
		
		try {
			serverSocket = new ServerSocket(LISTENING_PORT);
		} catch (Exception e) {
			System.out.println("Failed to create listening socket.");
			return;
		}
		System.out.println("Listening on port " + LISTENING_PORT);
		
		/* Listen for connection requests from clients.  For each 
         * connection, add the connected socket to the connection
         * queue.  The server runs until the program is terminated, 
         * for example by a CONTROL-C. 
         */
		try {
			while (true) {
				connection = serverSocket.accept();
				System.out.println("\nConnection from "
								+ connection.getRemoteSocketAddress());
				connectionQueue.add(connection);
			}
		} catch (Exception e) {
			System.out.println("Server socket shut down unexpectedly!");
			System.out.println("Error: " + e);
			System.out.println("Exiting.");
		}

		try {
			serverSocket.close();
		} catch (Exception e) {
			System.out.println("Error closing the server socket.");
		}
	} // end main()

    /**
     * The class that defines the connection-handling threads in the
     * thread pool.  The thread runs in an infinite loop in which
     * it removes a connected socket from the connection queue and
     * calls the handleConnection() method for that socket.
     */
    private static class ConnectionHandler extends Thread {

        ConnectionHandler() {
            setDaemon(true);
        }
        public void run() {
            while (true) {
                try {
                    Socket connection = connectionQueue.take();
                    handleConnection(connection);
                }
                catch (Exception e) {
                }
            }
        }
    }
	
    /**
     * This method processes the connection with one client.
     * It creates streams for communicating with the client,
     * reads a command from the client, and carries out that
     * command.  The connection is also logged to standard output.
     * If no errors are encountered, a "200 OK" response is sent.
     * Errors 400, 403, 404, 500, and 501 are implemented 
     * according to their descriptions.  If the requested file
     * is a directory, an HTML directory listing response is sent. 
     */
	private static void handleConnection(Socket connection) {

		Scanner in;
		PrintWriter outgoing;
		OutputStream out;
		String command = "";
		String fileName = "";
		String protocol = "";		
		
		try {
			in = new Scanner(connection.getInputStream());
			outgoing = new PrintWriter(connection.getOutputStream());
			out = connection.getOutputStream();

			command = in.next();

			if (command.equalsIgnoreCase("index")) {
				sendIndex(new File(ROOT_DIRECTORY), outgoing);
			} else if (!command.equalsIgnoreCase("get")) {
				System.out.println("ERROR: unsupported command.");
				sendErrorResponse(501, out);
			} else {
				fileName = in.next();
				protocol = in.next();		
			
				if (!protocol.equalsIgnoreCase("HTTP/1.1") &&
							!protocol.equalsIgnoreCase("HTTP/1.0")) {
					System.out.println("ERROR: Bad request.  Not HTTP/1.1 or HTTP/1.0.");
					sendErrorResponse(400, out);
				} else {

					File file = new File(ROOT_DIRECTORY + fileName);
	
					System.out.println("Attempting to retrieve file at: "
									+ file.toString());
	
					if (file.isDirectory()) {
						sendDirectoryListing(file, outgoing);
					} else if (file.exists() && file.canRead()) {
						String goodStatus = protocol + " 200 OK\r\n";
						outgoing.print(goodStatus);
	
						outgoing.print("Connection: close\r\n");
	
						String type = getMimeType(file.getName());
						outgoing.print("Content-Type: " + type + "\r\n");
	
						long fileLength = file.length();
						outgoing.print("Content-Length: " + fileLength + "\r\n");
						outgoing.print("\r\n");
						outgoing.flush();
	
						sendFile(file, out);
					} else {
						if (file.exists() && !file.canRead()) {
							System.out.println("ERROR: Permission to read file denied.");
							sendErrorResponse(403, out);
						} else if (!file.exists()) {
							System.out.println("ERROR: File does not exist on this server.");
							sendErrorResponse(404, out);
						}
						outgoing.flush();
					}
				}
			}

		} catch (Exception e) {
			System.out.println("ERROR " + connection.getInetAddress() + " "
							+ command + " " + e);
			try {
				OutputStream newOut = connection.getOutputStream();
				sendErrorResponse(500, newOut);
			} catch (Exception ex) {
				System.out.println("Error sending Internal Server Error response.");
			}
		} finally {
			try {
				connection.close();
			} catch (IOException e) {
			}
		}
	} // end handleConnection
	
	/**
	 * Returns the MIME type in String format.
	 * 
	 * @param fileName The filename from which to determine the MIME type.
	 * @return MIME type in String format.
	 */
	private static String getMimeType(String fileName) {
		int pos = fileName.lastIndexOf('.');
		if (pos < 0) // no file extension in name
			return "x-application/x-unknown";
		String ext = fileName.substring(pos + 1).toLowerCase();
		if (ext.equals("txt"))
			return "text/plain";
		else if (ext.equals("html"))
			return "text/html";
		else if (ext.equals("htm"))
			return "text/html";
		else if (ext.equals("css"))
			return "text/css";
		else if (ext.equals("js"))
			return "text/javascript";
		else if (ext.equals("java"))
			return "text/x-java";
		else if (ext.equals("jpeg"))
			return "image/jpeg";
		else if (ext.equals("jpg"))
			return "image/jpeg";
		else if (ext.equals("png"))
			return "image/png";
		else if (ext.equals("gif"))
			return "image/gif";
		else if (ext.equals("ico"))
			return "image/x-icon";
		else if (ext.equals("class"))
			return "application/java-vm";
		else if (ext.equals("jar"))
			return "application/java-archive";
		else if (ext.equals("zip"))
			return "application/zip";
		else if (ext.equals("xml"))
			return "application/xml";
		else if (ext.equals("xhtml"))
			return "application/xhtml+xml";
		else
			return "x-application/x-unknown";
		// Note: x-application/x-unknown is something made up;
		// it will probably make the browser offer to save the file.
	} // end getMimeType

	/**
	 * Sends an HTML error response over the <code>socketOut</code> OutputStream.
	 * 
	 * @param errorCode An int matching one of the implemented error codes.
	 * @param socketOut An OutputStream over which to send the error response.
	 */
	private static void sendErrorResponse(int errorCode, OutputStream socketOut) {
		
		String protocol = "HTTP/1.1";
		String statusDescription = " ";
		String statusMessage = "";
		
		switch (errorCode) {
		case 400:
			statusDescription += "400 Bad Request";
			statusMessage += "The syntax of the request is bad.";
			break;
		case 403:
			statusDescription += "403 Forbidden";
			statusMessage += "The server does not have permission to read the file.";
			break;
		case 404:
			statusDescription += "404 Not Found";
			statusMessage += "The resource that you requested does not exist on this server.";
			break;
		case 500:
			statusDescription += "500 Internal Server Error";
			statusMessage += "There has been an error in handling the connection.";
			break;
		case 501:
			statusDescription += "501 Not Implemented";
			statusMessage += "The command received has not been implemented.";
			break;
		default:
			statusDescription += "500 Internal Server Error";
			statusMessage += "There has been an error in handling the connection.";
			break;
		}
		
		try {
			PrintWriter out = new PrintWriter(socketOut);
			
			out.print(protocol + statusDescription + "\r\n");
			out.print("Connection: close\r\n");
			out.print("Content-Type: text/html\r\n");
			out.print("\r\n");
			out.print("<html><head><title>Error</title></head><body>\r\n");
			out.print("<h2>Error:" + statusDescription + "</h2>\r\n");
			out.print("<p>" + statusMessage + "</p>\r\n");
			out.print("</body></html>\r\n");
			out.flush();
			
			out.close();
		} catch (Exception e) {
			// Nothing to do if error occurs while attempting to send error message.
		}		
	} // end sendErrorResponse
	
	/**
	 * Send the File <code>file</code> over the OutputStream <code>socketOut</code>.
	 * File is sent as a stream of bytes.
	 * 
	 * @param file File object to be sent.
	 * @param socketOut OutputStream over which the file is to be sent.
	 * @throws IOException If any errors are encountered while creating the
	 *     InputStream or OutputStream.
	 */
	private static void sendFile(File file, OutputStream socketOut)
					throws IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		OutputStream out = new BufferedOutputStream(socketOut);
		while (true) {
			int x = in.read(); // read one byte from file
			if (x < 0)
				break; // end of file reached
			out.write(x); // write the byte to the socket
		}
		out.flush();
		in.close();
	}

	/**
	 * This is called by the handleConnection() method in response to an "INDEX"
	 * command from the client. Send the list of files in the server's directory.
	 */
	private static void sendIndex(File directory, PrintWriter outgoing)
					throws Exception {
		String[] fileList = directory.list();
		for (int i = 0; i < fileList.length; i++)
			outgoing.println(fileList[i]);
		outgoing.flush();
		outgoing.close();
		if (outgoing.checkError())
			throw new Exception("Error while transmitting data.");
	}
	
	/**
	 * Send an HTML Directory Listing response if the requested file is
	 * a directory.
	 * 
	 * @param directory File object that is a directory from which to obtain
	 *     a listing.
	 * @param outgoing PrintWriter through which to send the HTML response.
	 * @throws Exception If an error is encountered while attempting to list
	 *     the files in the directory or while sending the Directory Listing.
	 */
	private static void sendDirectoryListing(File directory,
					PrintWriter outgoing) throws Exception {

		File[] files = directory.listFiles();

		outgoing.print("HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n"
						+ "\r\n" + "<h1>Directory Listing</h1>" + "<h3>"
						+ directory.getPath() + "</h3>"
						+ "<table border=\"0\" cellspacing=\"8\">"
						+ "<tr><td><b>Filename</b><br></td><td align=\"right\"><b>Size</b></td>"
						+ "<td><b>Last Modified</b></td></tr>"
						+ "<tr><td><b><a href=\"../\">../</b><br></td><td></td><td></td></tr>");

		for (int i = 0; i < files.length; i++) {
			directory = files[i];
			
			if (directory.isDirectory()) {
				outgoing.print("<tr><td><b><a href=\"" + directory.getPath()
								+ directory.getName() + "/\">"
								+ directory.getName()
								+ "/</a></b></td><td></td><td></td></tr>");
			} else {
				outgoing.print("<tr><td><a href=\"" + directory.getPath()
								+ directory.getName() + "\">"
								+ directory.getName()
								+ "</a></td><td align=\"right\">"
								+ directory.length() + "</td><td>"
								+ new Date(directory.lastModified()).toString()
								+ "</td></tr>");
			}
		}
		outgoing.print("</table><hr>\r\n");
		outgoing.flush();
	}
} // end class SimpleWebServer