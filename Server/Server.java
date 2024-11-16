package Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server extends Thread {
    private final int portNumber;
    private final List<ClientHandler> handles;
    private boolean running;

    public Server(int portNumber) {
        this.portNumber = portNumber;
        running = true;
        handles = new ArrayList<>();
    }

    public int getPortNumber() {
        return portNumber;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (running) {
                Socket clientSocket = serverSocket.accept();

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
                handles.add(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutDown() {
        running = false;
        for (ClientHandler handle : handles)
            handle.shutDown();
    }

    public static void main(String[] args) throws InterruptedException {
        int portNumber = 6968;
        Server server = new Server(portNumber);
        server.start();
        Scanner scan = new Scanner(System.in);

        while (true) {
            if (scan.hasNextLine() && scan.nextLine().equals("shutdown")) {
                server.shutDown();
                server.join();
                break;
            }
        }
    }
}
