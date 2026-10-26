package utb.fai;

import java.net.*;
import java.io.*;

public class EmailSender {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    
    public EmailSender(String host, int port) throws UnknownHostException, IOException {
        socket = new Socket(host, port);
        writer = new PrintWriter(socket.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // Read the server's initial response
        reader.readLine();
    }

    
    public void send(String from, String to, String subject, String text) throws IOException {
        writer.println("EHLO localhost");
        reader.readLine();

        writer.println("MAIL FROM:<" + from + ">");
        reader.readLine();

        writer.println("RCPT TO:<" + to + ">");
        reader.readLine();

        writer.println("DATA");
        reader.readLine();

        writer.println("Subject: " + subject);
        writer.println("From: " + from);
        writer.println("To: " + to);
        writer.println();
        writer.println(text);
        writer.println(".");
        reader.readLine();
    }

    /*
     * Sends QUIT and closes the socket
     */
    public void close() {
        try {
            if (writer != null) {
                writer.println("QUIT");
            }
            if (reader != null) {
                reader.readLine();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readResponse() throws IOException {
        String line = reader.readLine();
        if (line != null && line.length() >= 4 && line.charAt(3) == '-') {
            while ((line = reader.readLine()) != null) {
                if (line.length() >= 4 && line.charAt(3) == ' ') {
                    break;
                }
            }
        }
    }
}
