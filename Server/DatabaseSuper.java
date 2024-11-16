package Server;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DatabaseSuper {

    public static void main(String[] args) {
        Database db = new Database();
        System.out.println(db.makeUser("Test User", "TestPassword123!"));
        System.out.println(db.makeUser("Test User 2", "TestPassword456!"));
    }

    public static class Database implements DBInterface {
        private User tempUser;
        private MessageChain tempMessageChain;

        private ArrayList<User> userArrayList;
        private ArrayList<MessageChain> messageChainArrayList;
        private ArrayList<String> messageChainOrganizer;

        public static ArrayList<ArrayList<User>> userToUpdate;
        public static ArrayList<MessageChain> messageChainToUpdate;

        private DBIOController userWrite;
        private DBIOController messageWrite;

        private File dbFolder;
        private File userFile;
        private File messageChainFolder;

        private ObjectInputStream userInput;
        private ObjectInputStream messageChainInput;

        public Database() {
            dbFolder = new File(dbFolderLocation);
            userFile = new File(userFileLocation);
            messageChainFolder = new File(messageChainFolderLocation);

            userArrayList = new ArrayList<>();
            messageChainArrayList = new ArrayList<>();
            messageChainOrganizer = new ArrayList<>();

            userToUpdate = new ArrayList<>();
            messageChainToUpdate = new ArrayList<>();

            try {
                if (!dbFolder.exists()) {
                    boolean dir = dbFolder.mkdir();
                    System.out.println("Created DATABASE folder: " + dir);
                }

                if (!userFile.exists()) {
                    userFile.createNewFile();
                    System.out.println("Created new userFile");
                }

                if (!messageChainFolder.exists()) {
                    messageChainFolder.mkdir();
                    System.out.println("Created new messageChainFolder");
                }
            } catch (IOException i) {
                System.out.println("Error creating database files or folders");
            }

            try {
                if (userFile.length() > 0) {
                    userInput = new ObjectInputStream(new FileInputStream(userFile));

                    //noinspection unchecked
                    userArrayList = (ArrayList<User>) userInput.readObject();

                    System.out.println("Users: " + userArrayList.size());
                    userInput.close();
                }

                int totalMessageChainsToRead;
                totalMessageChainsToRead = Objects.requireNonNull(messageChainFolder.listFiles()).length;

                if (totalMessageChainsToRead > 0) {

                    messageChainArrayList.ensureCapacity(totalMessageChainsToRead);
                    messageChainOrganizer.ensureCapacity(totalMessageChainsToRead);

                    //noinspection ConstantConditions
                    for (File f : messageChainFolder.listFiles()) {
                        messageChainInput = new ObjectInputStream(new FileInputStream(f));

                        tempMessageChain = (MessageChain) messageChainInput.readObject();

                        messageChainArrayList.set(tempMessageChain.getMessageChainID(), tempMessageChain);
                        messageChainOrganizer.set(tempMessageChain.getMessageChainID(), f.getName());

                        tempMessageChain = null;
                        messageChainInput.close();
                    }

                    //messageChainInput = new ObjectInputStream(new FileInputStream(messageChainFolder));

                    //messageChainArrayList = (ArrayList<MessageChain>) messageChainInput.readObject();

                    System.out.println("Messagechains: " + messageChainArrayList.size());
                    //messageChainInput.close();
                }

            } catch (FileNotFoundException f) {
                System.out.println("Some file was not found, and I/O could not be built.");
            } catch (ClassNotFoundException c) {
                System.out.println("Required class not found.");
            } catch (IOException i) {
                System.out.println("An I/O error occurred while creating OIS." + Arrays.toString(i.getStackTrace()));
            }

            System.out.println("Database started!");

            userWrite = new DBIOController("user");
            messageWrite = new DBIOController("messageChain");

        }

        private void runUserWrite(ArrayList<User> updated) {
            userToUpdate.add(updated);
            if (!userWrite.isAlive()) {
                userWrite.start();
            }
        }

        private void runMessageWrite(MessageChain updated) {
            messageChainToUpdate.add(updated);
            if (messageWrite.isAlive()) {
                messageWrite.start();
            }
        }

        public int getUserSize() {
            return userArrayList.size();
        }

        public int makeUser(String username, String password) {

            for (User u : userArrayList) {
                if (u.getUsername().equals(username)) {
                    return -1;
                }
            }

            userArrayList.add(new User(username, password, userArrayList.size()));

            runUserWrite(userArrayList);

            return (userArrayList.size() - 1);
        }

        public ArrayList<User> getAllUsers() {
            return userArrayList;
        }

        public User getUser(int user) {
            return userArrayList.get(user);
        }

        protected int findUser(User user) {
            return userArrayList.indexOf(user);
        }

        public void updateUser(int target, User update) {
            userArrayList.set(target, update);

            runUserWrite(userArrayList);
        }

        public void removeUser(int target) {
            tempUser = new User("DELETED", "DELETED", target);
            userArrayList.set(target, tempUser);
            tempUser = null;

            runUserWrite(userArrayList);
        }

        public ArrayList<MessageChain> getAllMessageChains() {
            return messageChainArrayList;
        }

        public int getMessageChainSize() {
            return messageChainArrayList.size();
        }

        /**
         * Makes a new messageChain with the specified users, and officiates it with the database
         *
         * @param userRight the "sender" or initiator of the conversation
         * @param userLeft  the "receiver" or other person in the conversation
         * @return the location of this {@code MessageChain} in the list if successful,
         * if this fails, returns {@code -1} for blocked user,
         * returns {@code -2} for all other errors
         */
        public int makeMessageChain(User userRight, User userLeft) {
            try {
                messageChainArrayList.add(new MessageChain(userRight, userLeft, messageChainArrayList.size()));
                messageChainOrganizer.add(userLeft.getId() + "_" + userRight.getId() + ".txt");

                runMessageWrite(messageChainArrayList.get(messageChainArrayList.size() - 1));

                return (messageChainArrayList.size() - 1);
            } catch (CantMessageException c) {
                if (c.getMessage().equals("This user has you blocked!")) {
                    return -1;
                } else {
                    return -2;
                }
            }
        }

        public MessageChain getMessageChain(int messageChain) {
            return messageChainArrayList.get(messageChain);
        }

        public boolean updateMessageChain(int target, MessageChain messageChain) {
            if ((messageChainArrayList.size() > target) && (!messageChainArrayList.get(target).isDeleted())) {
                messageChainArrayList.set(target, messageChain);

                runMessageWrite(messageChainArrayList.get(target));

                return true;
            } else {
                return false;
            }
        }

        public boolean addMessageToChain(int target, String[] message) {
            if ((messageChainArrayList.size() > target) && (!messageChainArrayList.get(target).isDeleted())) {
                messageChainArrayList.get(target).addNewMessage(message);

                runMessageWrite(messageChainArrayList.get(target));

                return true;
            } else {
                return false;
            }
        }

        public boolean deleteMessageChain(int target) {
            if ((messageChainArrayList.size() > target) && (!messageChainArrayList.get(target).isDeleted())) {
                messageChainArrayList.get(target).deleteChain();

                runMessageWrite(messageChainArrayList.get(target));

                return true;
            } else {
                return false;
            }
        }
    }
}
