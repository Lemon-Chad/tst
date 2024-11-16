package Server;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A serverside user class, do not send to client.
 */
public class User implements Serializable, UserInterface {
    private int id;
    private String username;
    private String password;
    private ArrayList<User> blocked;
    private ArrayList<User> friends;

    /**
     * DO NOT CALL DIRECTLY, to make a new user registered with the database, call {@code makeUser} from
     * the {@code Database} class
     * @param username The user's username
     * @param password The user's password
     * @param number The ID number for the user
     */
    public User(String username, String password, int number) {
        this.username = username;
        this.password = password;

        blocked = new ArrayList<>();
        friends = new ArrayList<>();

        id = number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    protected String getPassword() {
        return password;
    }

    /**
     * @param oldPassword The current password
     * @param newPassword The password to change to
     * @return {@code true} if the password updated successfully
     */
    public boolean setPassword(String oldPassword, String newPassword) {
        if (password.equals(oldPassword)) {
            password = newPassword;
            return true;
        } else {
            return false;
        }
    }

    public void addFriend(User user) {
        friends.add(user);
    }

    public void removeFriend(User user) {
        friends.remove(user);
    }

    public void blockUser(User user) {
        blocked.add(user);
    }

    public void unblockUser(User user) {
        blocked.remove(user);
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    public int getId() {
        return id;
    }

    public boolean equals(Object o) {
        if (o instanceof User) {
            User user = (User) o;

            return (id == user.getId());
        } else {
            return false;
        }
    }

    public boolean hasBlocked(User user) {
        return blocked.contains(user);
    }

    public boolean hasFriended(User user) {
        return friends.contains(user);
    }
}
