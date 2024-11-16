package Server;

import java.io.*;
import java.net.*;

public class ClientHandler extends Thread implements ClientHandlerInterface {
    Socket socket;
    static boolean stop;

    private User user;
    
    public ClientHandler(Socket socket) {
        this.socket = socket;
        stop = false;
    }
    
    public void shutDown() {
        stop = true;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void run() {
        try (
                BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter pw = new PrintWriter(socket.getOutputStream())
        ) {
            DatabaseSuper.Database db = new DatabaseSuper.Database();

            pw.println("Success");
            pw.flush();

            // Validate username
            String username;
            boolean usernameFound = false;
            User currentUser = null;

            while (!usernameFound) {
                username = bfr.readLine();
                System.out.printf("User: %s%n", username);
                for (User user: db.getAllUsers()) {
                    System.out.println(user.getUsername());
                    if (user.getUsername().equals(username)) {
                        currentUser = user;
                        usernameFound = true;
                        break;
                    }
                }
            }

            pw.println("Success");
            pw.flush();
            setUser(currentUser);

            // Validate password
            String password = bfr.readLine();
            while (!currentUser.validatePassword(password)) {
                password = bfr.readLine();
            }
            pw.println("Success");
            pw.flush();

            for (MessageChain chain: db.getAllMessageChains()) {
                if (chain.getFileName().contains(currentUser.getUsername())) {
                    pw.println(chain.getMessageChainID());
                    pw.flush();
                }
            }

            String userToChatWithStr = bfr.readLine();
            User userToChatWith = null;
            for (User user: db.getAllUsers()) {
                if (user.getUsername().equals(userToChatWithStr)) {
                    userToChatWith = user;
                }
            }

            broken:
            {
                for (MessageChain chain : db.getAllMessageChains()) {
                    if (chain.getFileName().contains(currentUser.getUsername()) &&
                            chain.getFileName().contains(userToChatWith.getUsername())) {
                        pw.println(chain.getMessageChainID());
                        pw.flush();
                        break broken;
                    }
                }

                pw.println("-1");
                pw.flush();
            }

            String userAction;
            String message;
            String oldMessage;
            String newMessage;
            String otherUserUsername;
            User otherUser = null;
            int chainIndex = -1;
            MessageChain chain = null;
            int messageIndex = -1;

            //noinspection InfiniteLoopStatement
            while (true) {
                if (stop) {
                    pw.println("shutdown"); // Tells client to shutdown, which will then be handled in Client class.
                    pw.flush();
                    socket.close();
                } else {
                    pw.println("continue");
                    pw.flush();
                }
                
                userAction = bfr.readLine();
                switch (userAction) {
                    case "send message":
                        message = bfr.readLine();
                        otherUserUsername = bfr.readLine();

                        for (User user: db.getAllUsers()) {
                            if (user.getUsername().equals(otherUserUsername)) {
                                otherUser = user;
                                break;
                            }
                        }

                        if (otherUser.hasBlocked(currentUser)) {
                            pw.println("Fail");
                            pw.flush();
                            break;
                        }
                        
                        for (int i = 0; i < db.getAllMessageChains().size(); i++) {
                            if (db.getAllMessageChains().get(i).getUserLeft().getUsername().equals(otherUserUsername)) {
                                chain = db.getAllMessageChains().get(i);
                                break;
                            }
                        }

                        chain.addNewMessage(new String[] {currentUser.getUsername(), message});
                        pw.println("Success");
                        pw.flush();
                        break;

                    case "delete message": 
                        message = bfr.readLine();
                        otherUserUsername = bfr.readLine();

                        for (User user: db.getAllUsers()) {
                            if (user.getUsername().equals(otherUserUsername)) {
                                otherUser = user;
                                break;
                            }
                        }
                        
                        for (int i = 0; i < db.getAllMessageChains().size(); i++) {
                            if (db.getAllMessageChains().get(i).getUserLeft().getUsername().equals(otherUserUsername)) {
                                chain = db.getAllMessageChains().get(i);
                                break;
                            }
                        }

                        for (int i = 0; i < chain.getMessages().size(); i++) {
                            if (chain.getMessages().get(i)[1].equals(message)) {
                                messageIndex = i;
                                break;
                            }
                        }

                        chain.deleteMessage(messageIndex);
                        break;

                    case "edit message": 
                        oldMessage = bfr.readLine();
                        newMessage = bfr.readLine();
                        otherUserUsername = bfr.readLine();

                        for (User user: db.getAllUsers()) {
                            if (user.getUsername().equals(otherUserUsername)) {
                                otherUser = user;
                                break;
                            }
                        }

                        if (otherUser.hasBlocked(currentUser)) {
                            pw.println("Fail");
                            pw.flush();
                            break;
                        }
                        
                        for (int i = 0; i < db.getAllMessageChains().size(); i++) {
                            if (db.getAllMessageChains().get(i).getUserLeft().getUsername().equals(otherUserUsername)) {
                                chain = db.getAllMessageChains().get(i);
                                break;
                            }
                        }

                        for (int i = 0; i < chain.getMessages().size(); i++) {
                            if (chain.getMessages().get(i)[1].equals(oldMessage)) {
                                messageIndex = i;
                                break;
                            }
                        }

                        chain.editMessage(messageIndex, new String[]{currentUser.getUsername(), newMessage});
                        pw.println("Success");
                        pw.flush();
                        break;

                    case "create chain": 
                        otherUserUsername = bfr.readLine();
                        for (User user: db.getAllUsers()) {
                            if (user.getUsername().equals(otherUserUsername)) {
                                otherUser = user;
                                break;
                            }
                        }

                        if (otherUser.hasBlocked(currentUser)) {
                            pw.println("Fail");
                            pw.flush();
                            break;
                        }

                        db.makeMessageChain(currentUser, otherUser);
                        pw.println("Success");
                        pw.flush();
                        break;

                    case "delete chain": 
                        otherUserUsername = bfr.readLine();
                        for (User user: db.getAllUsers()) {
                            if (user.getUsername().equals(otherUserUsername)) {
                                otherUser = user;
                                break;
                            }
                        }
                        
                        for (int i = 0; i < db.getAllMessageChains().size(); i++) {
                            if (db.getAllMessageChains().get(i).getUserLeft().getUsername().equals(otherUserUsername)) {
                                chainIndex = i;
                                break;
                            }
                        }
                        db.deleteMessageChain(chainIndex);
                        break;
                    case "block":
                        String userToBlock = bfr.readLine();

                        for (User user: db.getAllUsers()) {
                            if (user.getUsername().equals(userToBlock)) {
                                otherUser = user;
                                break;
                            }
                        }

                        currentUser.blockUser(otherUser);
                        break;
                    case "unblock":
                        String userToUnblock = bfr.readLine();

                        for (User user: db.getAllUsers()) {
                            if (user.getUsername().equals(userToUnblock)) {
                                otherUser = user;
                                break;
                            }
                        }

                        currentUser.unblockUser(otherUser);
                        break;
                    case "friend": 
                        String userToFriend = bfr.readLine();

                        for (User user: db.getAllUsers()) {
                            if (user.getUsername().equals(userToFriend)) {
                                otherUser = user;
                                break;
                            }
                        }

                        if (otherUser.hasBlocked(currentUser)) {
                            pw.println("Fail 1");
                            pw.flush();
                            break;
                        } else if (currentUser.hasBlocked(otherUser)) {
                            pw.println("Fail 2");
                            pw.flush();
                            break;
                        }

                        otherUser.addFriend(currentUser);
                        currentUser.addFriend(otherUser);
                        pw.println("Success");
                        pw.flush();
                        break;
                    case "unfriend": 
                        String userToUnfriend = bfr.readLine();

                        for (User user: db.getAllUsers()) {
                            if (user.getUsername().equals(userToUnfriend)) {
                                otherUser = user;
                                break;
                            }
                        }

                        if (otherUser.hasBlocked(currentUser)) {
                            break;
                        }

                        otherUser.removeFriend(currentUser);
                        currentUser.removeFriend(otherUser);
                        break;
                }
            }

        } catch (IOException ignored) {
        }
    }
}
