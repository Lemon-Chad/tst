package Server;

import java.util.ArrayList;
public interface MessageChainInterface {

    public String getFileName();

    public void deleteChain();

    public boolean isDeleted();

    public User getUserLeft();

    public User getUserRight();

    public int getMessageChainID();

    public String[] newMessage(String contents, int senderID);
    
    public void addNewMessage(String[] message);

    public ArrayList<String[]> getMessages();

    public boolean sendMessage(String[] message);

    public boolean deleteMessage(int index);

    public boolean editMessage(int index, String[] newMessage);
}