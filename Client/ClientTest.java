package Client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for Client class
 */
public class ClientTest {

    private Client client;

    // Creates two users
    @BeforeEach
    public void setUp() {
        client = new Client("localhost", 8234);
    }

    // Tests if the constructors initialize successfully
    @Test
    public void testConstructor() {
        assertEquals(client.getHostname(), "localhost");
        assertEquals(client.getPortNumber(), 8234);
    }
}
