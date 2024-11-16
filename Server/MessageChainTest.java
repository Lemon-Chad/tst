package Server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

/**
 * Different Test Cases for MessageChain class, uses User class since some methods hinge on User methods.
 * Some tests are incomplete since back and forth from client and server is not implemented
 */
public class MessageChainTest {

    private User userRight;
    private User userLeft;
    private MessageChain messageChain;

    // Creates two users + messageChainID and inputs them into messageChain constructor
    @BeforeEach
    public void setUp() {
        userRight = new User("userRight", "password123", 1);
        userLeft = new User("userLeft", "password456", 2);
        userLeft.unblockUser(userRight);  // Ensure userRight is not blocked by userLeft
        try {
            messageChain = new MessageChain(userRight, userLeft, 1001);
        } catch (CantMessageException e) {
            fail("Initialization failed with exception: " + e.getMessage());
        }
    }

    // Tests if the constructor initializes correctly
    @Test
    public void testConstructor() {
        assertNotNull(messageChain, "MessageChain is not initialized");
        assertFalse(messageChain.isDeleted(), "MessageChain should not be marked as deleted initially");
        assertTrue(messageChain.getMessages().isEmpty(), "MessageChain messages list should be empty initially");
    }

    // Tests if code checks for a blocked user and returns the correct message
    @Test
    public void testConstructor_UserBlocked() {
        userLeft.blockUser(userRight);  // Block the userRight

        Exception exception = assertThrows(
                CantMessageException.class,
                () -> new MessageChain(userRight, userLeft, 1002)
        );

        assertEquals("This user has you blocked!", exception.getMessage(), "Incorrect exception message");
    }

    // Tests if new messages are properly added into the messages ArrayList,
    // senderId is correct, and timestamp is not null
    @Test
    public void testNewMessage() {
        try {
            String[] message = messageChain.newMessage("Message test 123!", userRight.getId());
            assertEquals(String.valueOf(userRight.getId()), message[0], "Sender ID does not match");
            assertEquals("Message test 123!", message[2], "Message content does not match");
            assertNotNull(message[1], "Timestamp should not be null");
        } catch (Exception e) {
            fail("testNewMessage failed with exception: " + e.getMessage());
        }
    }

    // Tests if the ArrayList size is correct after adding a new message
    @Test
    public void testAddNewMessage() {
        try {
            String[] message = messageChain.newMessage("New Test Message!", userRight.getId());
            messageChain.addNewMessage(message);

            ArrayList<String[]> messagesList = messageChain.getMessages();
            assertEquals(1, messagesList.size(), "Messages list length should be one initially");
            assertArrayEquals(message, messagesList.get(0), "Added message does not match expected message");
        } catch (Exception e) {
            fail("testAddNewMessage failed with exception: " + e.getMessage());
        }
    }

    // Tests deleteChain method to see if messageChain values match expected values when deleted
    @Test
    public void testDeleteChain() {
        try {
            String[] message = messageChain.newMessage("New Test Message!", userRight.getId());
            messageChain.addNewMessage(message);

            assertEquals(1, messageChain.getMessages().size(), "Messages list length should be one initially");

            messageChain.deleteChain();

            assertTrue(messageChain.getMessages().isEmpty(), "Messages list should be empty after deletion");
            assertTrue(messageChain.isDeleted(), "MessageChain should be marked as deleted");

        } catch (Exception e) {
            fail("testDeleteChain failed with exception: " + e.getMessage());
        }
    }

    // Tests sendMessage method
    @Test
    public void testSendMessage() {
        try {
            String[] message = messageChain.newMessage("Test Message", userRight.getId());
            messageChain.addNewMessage(message);

            ArrayList<String[]> messagesList = messageChain.getMessages();
            assertEquals(1, messagesList.size(), "Messages list length should be one initially");
            assertArrayEquals(message, messagesList.get(0), "Sent message does not match expected message");
        } catch (Exception e) {
            fail("testSendMessage failed with exception: " + e.getMessage());
        }
    }

    // Tests deleteMessage method with valid index
    @Test
    public void testDeleteMessage_ValidIndex() {
        try {
            String[] message = messageChain.newMessage("Hello, again!", userRight.getId());
            messageChain.addNewMessage(message);

            assertEquals(1, messageChain.getMessages().size(), "Messages list should initially contain one message");

            assertTrue(messageChain.deleteMessage(0), "Deleting message at a valid index should return true");
            assertEquals(0, messageChain.getMessages().size(), "Messages list should be empty after valid deletion");
        } catch (Exception e) {
            fail("testDeleteMessage_ValidIndex failed with exception: " + e.getMessage());
        }
    }

    // Tests deleteMessage method with invalid index
    @Test
    public void testDeleteMessage_InvalidIndex() {
        try {
            assertFalse(messageChain.deleteMessage(10), "Deleting message at an invalid index should return false");
        } catch (Exception e) {
            fail("testDeleteMessage_InvalidIndex failed with exception: " + e.getMessage());
        }
    }
}