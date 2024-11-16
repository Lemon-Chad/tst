package Server;

import java.io.*;
import java.util.ArrayList;

public class DBIOController extends Thread implements DBInterface {
    private String mode;

    private static final Object USERSYNC = new Object();
    private ArrayList<Boolean> messageSyncs;
    private static final Object MESSAGESYNC = new Object();

    private ObjectOutputStream userOut;
    private ObjectOutputStream messageOut;

    File userFile;
    File messageChainFolder;
    File tempFile;

    /**
     * sets up database IO, continually saves changes to files
     * @param toUpdate can be {@code "user"} or {@code "messageChain"}
     */
    public DBIOController(String toUpdate) {
        mode = toUpdate;

        userFile = new File(userFileLocation);
        messageChainFolder = new File(messageChainFolderLocation);

        messageSyncs = new ArrayList<>();

        try {
            userOut = new ObjectOutputStream(new FileOutputStream(userFile, false));
            //messageOut = new ObjectOutputStream(new FileOutputStream(messageChainFolder, false));
            System.out.println("Outputs created successfully");
        } catch (FileNotFoundException f) {
            System.out.println("Files not found");
        } catch (IOException i) {
            System.out.println("OOS can't");
        }

        System.out.println("controller with mode: " + mode);
    }

    public void run() {
        if (mode.equals("user")) {
            synchronized (USERSYNC) {
                while (!DatabaseSuper.Database.userToUpdate.isEmpty()) {
                    try {
                        userOut.writeObject(DatabaseSuper.Database.userToUpdate.get(0));
                        if (!DatabaseSuper.Database.userToUpdate.isEmpty())
                            DatabaseSuper.Database.userToUpdate.remove(0);
                    } catch (IOException i) {
                        System.out.println("Write error on user");
                    }
                }
            }
        } else if (mode.equals("messageChain")) {

            while (!DatabaseSuper.Database.messageChainToUpdate.isEmpty()) {
                for (int i = 0; i < DatabaseSuper.Database.messageChainToUpdate.size(); i++) {
                    if (!messageSyncs.get(i)) {
                        messageSyncs.set(i, true);

                        tempFile = new File(DatabaseSuper.Database.messageChainToUpdate.get(i).getFileName());

                        try {
                            messageOut = new ObjectOutputStream(new FileOutputStream(tempFile, false));

                            messageOut.writeObject(DatabaseSuper.Database.messageChainToUpdate.get(i));
                            DatabaseSuper.Database.messageChainToUpdate.remove(i);
                        } catch (IOException e) {
                            System.out.println("IO error with file: " + tempFile.getName());
                        }

                        if (messageOut != null) {
                            try {
                                messageOut.close();
                            } catch (IOException e) {
                                System.out.println("Can't close messageOut");
                            }
                        }

                        messageSyncs.set(i, false);
                    }
                }
            }
        } else {
            System.out.println("unrecognised mode: " + mode);
        }
    }
}