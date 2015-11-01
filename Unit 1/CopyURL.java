package unit1.copyurl;

import textio.TextIO;

import java.net.*;
import java.io.*;

public class CopyURL {

	private static void copyStream(InputStream in, OutputStream out)
		throws IOException {
		
		int oneByte = in.read();
		while (oneByte >= 0) { // negative value indicates end-of-stream
			out.write(oneByte);
			oneByte = in.read();
		}
	}
	
	public static void main(String[] args) {
		
		// Declare and initialize InputStream and OutputStream to null.
		InputStream source = null;
		OutputStream copy = null;
		String urlString;  // The url from user input.
		String outFileName;
		File file;
		boolean okToOverwrite = false;
		
		// Read the URL and file name as Strings from user.
		System.out.println("Enter a url: ");
		urlString = TextIO.getln();
		urlString = urlString.toLowerCase();
		if ( ! (urlString.startsWith("http://")
				|| urlString.startsWith("ftp://")
				|| urlString.startsWith("file://"))) {
			urlString = "http://" + urlString;
		}
		System.out.println();
		
		System.out.println("Enter a file name to copy data to: ");
		outFileName = TextIO.getln();
		
		file = new File(outFileName);
		
		while (file.exists()) {
			System.out.println("Output file exists.  OK to overwrite? Y/N");
			okToOverwrite = TextIO.getBoolean();
			
			if (okToOverwrite) {
				break;
			} else {
				System.out.println("Enter a new file name: ");
				outFileName = TextIO.getlnWord();
				file = new File(outFileName);
			}
		}
		System.out.println("Using: " + urlString);
		
		// Create URL object, InputStream, and FileOutputStream.
		// Copy data to selected file, afterwards, ensure streams
		// are closed.
		try {
			URL url = new URL(urlString);
			source = url.openStream();
			copy = new FileOutputStream(outFileName);
			copyStream(source, copy);
		} catch (MalformedURLException e) {
			System.out.println(urlString + " is not a legal URL.");
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.out.println("Error: Output file \""
				+ outFileName + "\" cannot be opened.");
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("Error: Web address may not exist or "
				+ "there was an error writing to the output file.");
			System.out.println(e.getMessage());
		} finally {
			if (source != null) {
				try {
					source.close();
				} catch (IOException e) {
					System.out.println("Error closing the input stream.");
				}
			}
			if (copy != null) {
				try {
					copy.close();
				} catch (IOException e) {
					System.out.println("Error closing the output stream.");
				}
			}
		}  // end finally
	}  // end main
}