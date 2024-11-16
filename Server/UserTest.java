package Server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Different Test Cases for User class
 */
public class UserTest {

    private User userRight;
    private User userLeft;

    // Creates two users
    @BeforeEach
    public void setUp() {
        userRight = new User("userRight", "password123", 1);
        userLeft = new User("userLeft", "password456", 2);
    }

    // Tests if the constructors initialize successfully
    @Test
    public void testConstructor() {
        assertNotNull(userRight, "User right is not initialized");
        assertNotNull(userLeft, "User left is not initialized");

        assertEquals(userRight.getId(), 1, "ID not properly initialized!");
        assertEquals(userLeft.getId(), 2, "ID not properly initialized!");

        assertEquals(userRight.getUsername(), "userRight", "Username not properly initialized!");
        assertEquals(userLeft.getUsername(), "userLeft", "Username not properly initialized!");

        assertTrue(userRight.validatePassword("password123"), "Password not properly initialized!");
        assertTrue(userLeft.validatePassword("password456"), "Password not properly initialized!");
    }

    // Tests if users can properly friend and unfriend each other
    @Test
    public void testFriends() {
        userLeft.addFriend(userRight);  // Block the userRight

        assertTrue(userLeft.hasFriended(userRight), "Users should be friended!");

        userLeft.removeFriend(userRight);
        assertFalse(userLeft.hasFriended(userRight), "Users should no longer be friended!");
    }

    // Tests if users can properly block and unblock each other
    @Test
    public void testBlocked() {
        userLeft.blockUser(userRight);  // Block the userRight

        assertTrue(userLeft.hasBlocked(userRight), "Users should be blocked!");

        userLeft.unblockUser(userRight);
        assertFalse(userLeft.hasBlocked(userRight), "Users should no longer be blocked!");
    }

    // Tests if equals works
    @Test
    public void testEquals() {
        assertNotEquals(userLeft, userRight, "Users should not be equal!");
        assertEquals(userLeft, userLeft, "Users should be equal!");

        User userLeft2 = new User("userLeft", "password456", 2);
        assertEquals(userLeft, userLeft2, "Users should be equal!");
    }

    // Test if password systems work
    @Test
    public void testPassword() {
        assertTrue(userLeft.validatePassword("password456"), "Password should be valid!");
        assertFalse(userLeft.setPassword("blargh", "password789"), "Password should not be set!");
        assertTrue(userLeft.setPassword("password456", "password789"), "Password should be set!");
        assertFalse(userLeft.validatePassword("password456"), "Password should be different!");
        assertTrue(userLeft.validatePassword("password789"), "Password should be valid!");
        assertTrue(userLeft.setPassword("password789", "password456"), "Password should be set!");
    }
}
