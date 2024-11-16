package Server;

import java.time.LocalTime;
import java.util.ArrayList;

/**
 * A chain of messages between two users
 */
public class MessageChain implements MessageChainInterface {
    /**
     * An arraylist of messages, the message array will have a format of:
     * User identifier, timestamp as a LocalTime in string format, message contents
     */
    private ArrayList<String[]> messages;
    private User userRight; // this might be replaced with user object
    private User userLeft; // this might be replaced with user object
    private int messageChainID;
    private boolean deleted;
    private String fileName;

    /**
     * DO NOT CALL DIRECTLY, to make a new messageChain registered with the database,
     * call {@code makeMessageChain} from
     * the {@code Database} class
     *
     * <p>Makes a new MessageChain Object between two users,
     * Right is the local user and left is the remote user.
     * @param userRight
     * @param userLeft
     */
    public MessageChain(User userRight, User userLeft, int messageChainID) throws CantMessageException {
        if (userLeft.hasBlocked(userRight)) {
            throw new CantMessageException("This user has you blocked!");
        }

        this.userLeft = userLeft;
        this.userRight = userRight;
        this.messageChainID = messageChainID;
        deleted = false;
        messages = new ArrayList<>();

        fileName = userLeft.getId() + "_" + userRight.getId() + ".txt";
    }

    public String getFileName() {
        return fileName;
    }

    public User getUserLeft() {
        return userLeft;
    }

    public User getUserRight() {
        return userRight;
    }

    public void deleteChain() {
        messages.clear();
        userRight = null;
        userLeft = null;
        fileName = "DELETED";
        deleted = true;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public int getMessageChainID() {
        return messageChainID;
    }

    public String[] newMessage(String contents, int senderID) {
        String time = LocalTime.now().toString();
        return new String[]{String.valueOf(senderID), time, contents};
    }

    public void addNewMessage(String[] message) {
        messages.add(message);
    }

    public ArrayList<String[]> getMessages() {
        return messages;
    }
    /**
     * attempts to send message to the other user, if this fails, return false
     * @param message
     * @return
     */
    public boolean sendMessage(String[] message) {
        try {
            throw new RuntimeException("Method not implemented");
        } catch (Exception e) {
            return true;
        }
    }

    public boolean deleteMessage(int index) {
        try {
            messages.remove(index);
            return true;
        } catch (IndexOutOfBoundsException i) {
            return false;
        }
    }

    public boolean editMessage(int index, String[] newMessage) {
        try {
            messages.set(index, newMessage);
            return true;
        } catch (IndexOutOfBoundsException i) {
            return false;
        }
    }

}
