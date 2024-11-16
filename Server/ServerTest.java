package Server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for Server class
 */
public class ServerTest {

    private Server server;

    // Creates two users
    @BeforeEach
    public void setUp() {
        DatabaseSuper.Database db = new DatabaseSuper.Database();
        System.out.println(db.makeUser("Test User", "TestPassword123!"));
        System.out.println(db.makeUser("Test User 2", "TestPassword456!"));

        server = new Server(8234);
    }

    // Tests if the constructors initialize successfully
    @Test
    public void testConstructor() {
        assertEquals(server.getPortNumber(), 8234);
    }

    // Tests if server properly responds to calls
    @Test
    public void testServer() {
        server.start();

        try (
                Socket socket = new Socket("localhost", server.getPortNumber());
                BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter pw = new PrintWriter(socket.getOutputStream())
        ) {
            assertEquals(bfr.readLine(), "Success");
            pw.println("Test User");
            pw.flush();
            assertEquals(bfr.readLine(), "Success");

            pw.println("TestPassword123!");
            pw.flush();
            assertEquals(bfr.readLine(), "Success");

            pw.println("Test User 2");
            pw.flush();
            bfr.readLine(); // Ignore this value for now.

            assertEquals(bfr.readLine(), "continue");
            pw.println("create chain");
            pw.println("Test User 2");
            pw.flush();
            assertEquals(bfr.readLine(), "Success");

            assertEquals(bfr.readLine(), "continue");
            pw.println("send message");
            pw.println("Hello world!");
            pw.println("Test User 2");
            pw.flush();
            assertEquals(bfr.readLine(), "Success");

            assertEquals(bfr.readLine(), "continue");
            pw.println("block");
            pw.println("Test User 2");
            pw.flush();

            assertEquals(bfr.readLine(), "continue");
            pw.println("unblock");
            pw.println("Test User 2");
            pw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        server.shutDown();
    }
}
