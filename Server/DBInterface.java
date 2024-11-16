package Server;

public interface DBInterface {

    String dbFolderLocation = "DATABASE/"; // May change
    String messageChainFolderLocation = dbFolderLocation + "MESSAGES/";
    String userFileLocation = dbFolderLocation + "users.txt";
    //String messageChainFileLocation = messageChainFolderLocation + "messages.txt";

}
