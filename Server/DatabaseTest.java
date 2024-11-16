package Server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Different Test Cases for Database class
 */
public class DatabaseTest {

    private DatabaseSuper.Database database;
    private int userRight;
    private int userLeft;
    private int messageChain;

    // Creates database
    @BeforeEach
    public void setUp() {
        database = new DatabaseSuper.Database();
        userRight = database.makeUser("userRight", "password123");
        userLeft = database.makeUser("userLeft", "password456");
    }

    // Tests if the constructor initializes successfully
    @Test
    public void testConstructor() {
        assertNotNull(database, "Database is not initialized");
    }

    // Tests if the database can properly make users
    @Test
    public void testMakeUsers() {
        assertEquals(userRight, 0, "User not being properly created!");
        assertEquals(userLeft, 1, "User not being properly created!");

        assertEquals(database.getUserSize(), 2, "Users not being properly added to list!");
    }

    // Tests if the database can properly make message chains
    @Test
    public void testMakeMessageChain() {
        messageChain = database.makeMessageChain(database.getUser(userLeft), database.getUser(userRight));

        assertEquals(messageChain, 0, "Message chain not being properly created!");
        assertEquals(database.getMessageChainSize(), 1, "Message chain not being added to list!");

        assertTrue(
                database.addMessageToChain(
                        messageChain,
                        database.getMessageChain(messageChain).newMessage(
                                "Hello world!", userRight
                        )
                ), "Messages not being added to chain!"
        );

        database.getUser(userLeft).blockUser(database.getUser(userRight));
        messageChain = database.makeMessageChain(database.getUser(userLeft), database.getUser(userRight));
        assertEquals(messageChain, -1, "Users should be blocked and unable to message!");

        database.getUser(userLeft).unblockUser(database.getUser(userRight));

        assertTrue(database.deleteMessageChain(messageChain), "Message chain not being deleted!");
    }
}
