package Client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client implements Runnable {
    private final String hostname;
    private final int portNumber;

    public Client(String hostname, int portNumber) {
        this.hostname = hostname;
        this.portNumber = portNumber;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public String getHostname() {
        return hostname;
    }

    @Override
    public void run() {
        try (
                Scanner s = new Scanner(System.in);
                Socket socket = new Socket(hostname, portNumber);
                BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter pw = new PrintWriter(socket.getOutputStream())
        ) {
            String username = s.nextLine(); // all scanner statements will be replaced with popups later
            pw.println(username);
            pw.flush();
            while (!(bfr.readLine().equals("Success"))) {
                username = s.nextLine();
                pw.println(username);
                pw.flush();
            }

            String password = s.nextLine();
            pw.println(password);
            pw.flush();
            while (!bfr.readLine().equals("Success")) {
                password = s.nextLine();
                pw.println(password);
                pw.flush();
            }

            String userToChatWith = s.nextLine();
            pw.println(userToChatWith);
            pw.flush();

            String userAction;
            String status;
            String message;
            String stop;

            while (true) {
                stop = bfr.readLine();
                if (stop.equals("shutdown")) {
                    break;
                }

                userAction = s.nextLine();
                pw.println(userAction);
                pw.flush();

                switch (userAction) {
                    case "send message":
                        message = s.nextLine();
                        pw.println(message);

                        userToChatWith = s.nextLine();
                        pw.println(userToChatWith);
                        pw.flush();

                        status = bfr.readLine();
                        if (status.equals("Fail")) {
                            System.out.println("Failed to send: " + userToChatWith + " has blocked you."); // will be replaced with a popup later
                        }
                        break;
                    case "delete message":
                        message = s.nextLine();
                        pw.println(message);
                        userToChatWith = s.nextLine();
                        pw.println(userToChatWith);
                        pw.flush();
                        break;
                    case "edit message":
                        String oldMessage = s.nextLine();
                        pw.println(oldMessage);
                        String newMessage = s.nextLine();
                        pw.println(newMessage);
                        userToChatWith = s.nextLine();
                        pw.println(userToChatWith);
                        pw.flush();

                        status = bfr.readLine();
                        if (status.equals("Fail")) {
                            System.out.println("Failed to send: " + userToChatWith + " has blocked you."); // will be replaced with a popup later
                        }
                        break;
                    case "create chain":
                        userToChatWith = s.nextLine();
                        pw.println(userToChatWith);
                        pw.flush();

                        status = bfr.readLine();
                        if (status.equals("Fail")) {
                            System.out.println("Failed to send: " + userToChatWith + " has blocked you."); // will be replaced with a popup later
                        }
                        break;
                    case "delete chain":
                        userToChatWith = s.nextLine();
                        pw.println(userToChatWith);
                        pw.flush();
                        break;
                    case "block":
                        String userToBlock = s.nextLine();
                        pw.println(userToBlock);
                        pw.flush();
                        break;
                    case "unblock":
                        String userToUnblock = s.nextLine();
                        pw.println(userToUnblock);
                        pw.flush();
                        break;
                    case "friend":
                        String userToFriend = s.nextLine();
                        pw.println(userToFriend);
                        pw.flush();

                        status = bfr.readLine();
                        if (status.equals("Fail 1")) {
                            System.out.println("Failed to friend: " + userToFriend + " has blocked you."); // will be replaced with a popup later
                        } else if (status.equals("Fail 2")) {
                            System.out.println("Failed to friend: you have blocked " + userToFriend + "."); // will be replaced with a popup later
                        }
                        break;
                    case "unfriend":
                        String userToUnfriend = s.nextLine();
                        pw.println(userToUnfriend);
                        pw.flush();
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int portNumber = 6968;
        String hostname = "localhost"; 

        Client client = new Client(hostname, portNumber);
        client.run();
    }
}