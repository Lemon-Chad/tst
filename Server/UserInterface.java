package Server;

public interface UserInterface {
    public String getUsername();

    public void setUsername(String username);

    public boolean setPassword(String oldPassword, String newPassword);

    public void addFriend(User user);

    public void removeFriend(User user);

    public void blockUser(User user);

    public void unblockUser(User user);

    public boolean validatePassword(String password);

    public int getId();

    public boolean equals(Object o);

    public boolean hasBlocked(User user);

    public boolean hasFriended(User user);

}
