<a id="readme-top"></a>






<br />
<div align="center">
  
<h3 align="center">CS-180 Team Project</h3>

  <p align="center">
    Social Media Platform
    <br />
    <a href="https://github.com/shreyvedantham/CS-180-Final-Project"><strong>Explore the docs »</strong></a>
    <br />
    <br />
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-using">Built Using</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#database">Database</a></li>
    <li><a href="#dbioController">DBIOController</a></li>
    <li><a href="#messagechain">MessageChain</a></li>
    <li><a href="#cantmessageexception">CantMessageException</a></li>
  </ol>
</details>
  


<!-- ABOUT THE PROJECT -->
## About The Project
**Currently no screenshots, will be once GUIs are implemented.**
[![Product Name Screen Shot][product-screenshot]](https://example.com)

Our project utilizes network IO to simulate a social media platform, in which a user can create and manage an account, manage a friends list, and send direct messages.  

<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Built Using
* JUnit - https://junit.org/junit5/

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

Follow these instructions to use and install our software.

### Prerequisites

_These are things you will need to have before being able to use our software._
* Java - Either via an IDE like [IntelliJ](https://www.jetbrains.com/idea/), or running Java straight ([Java](https://www.java.com/en/))

### Installation
_This is how to install our software._
1. Download required .java files:
<ul>
  <li><a href="https://github.com/shreyvedantham/CS-180-Final-Project/blob/main/CantMessageException.java">CantMessageException</a></li>
  <li><a href="https://github.com/shreyvedantham/CS-180-Final-Project/blob/main/DBIOController.java">DBIOController</a></li>
  <li><a href="https://github.com/shreyvedantham/CS-180-Final-Project/blob/main/DBInterface.java">DBInterface</a></li>
  <li><a href="https://github.com/shreyvedantham/CS-180-Final-Project/blob/main/Database.java">Database</a></li>
  <li><a href="https://github.com/shreyvedantham/CS-180-Final-Project/blob/main/MessageChain.java">MessageChain</a></li>
  <li><a href="https://github.com/shreyvedantham/CS-180-Final-Project/blob/main/MessageChainInterface.java">MessageChainInterface</a></li>
  <li><a href="https://github.com/shreyvedantham/CS-180-Final-Project/blob/main/User.java">User</a></li>
  <li><a href="https://github.com/shreyvedantham/CS-180-Final-Project/blob/main/UserInterface.java">UserInterface</a></li>
</ul>

2. **There is currently no main method - implementation will come later with GUI implementation - cannot currently run**

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage

_This is how our project can be used, and what it looks like in use._

**Again, this will come later when we have GUI implementation.**
<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- ROADMAP -->
## Roadmap

- [x] Database
- [x] User login and account creation
- [x] Phase 1 documentation 
- [ ] Direct Messaging and message chains
- [ ] Server implementation
- [ ] Server linked to database
- [ ] Phase 2 documentation
- [ ] GUI
- [ ] Phase 3 documentation

<p align="right">(<a href="#readme-top">back to top</a>)</p>



## Database
Fields:
<ul>
  <li>tempUser
    
  * Temporary object of User class used to remove users
  </li>
  
  <li>tempMessageChain
    
  * Temporary object of MessageChain class
  </li>

  <li>userArrayList
  
  * ArrayList that holds all Users in the database
  </li>
  
  <li>messageChainArrayList
  
  * ArrayList that holds all MessageChains in the database
  </li>

  <li>userToUpdate
  
  * ArrayList of Arraylists of Users, that holds all updated versions of userArrayList (added when runUserWrite is called)
  </li>
  
  <li>messageChainToUpdate
  
  * ArrayList of Arraylists of MessageChains, that holds all updated versions of messageChainArrayList (added when runMessageWrite is called)
  </li>

  <li>userWrite
  
  * The DBIOController thread for writing users
  </li>
  
  <li>messageWrite
  
  * The DBIOController thread for writing message chains
  </li>

  <li>dbFolder
  
  * The directory in which the database is stored
  </li>
  
  <li>userFile
  
  * The file of users to be used for input
  </li>
  
  <li>messageChainFile
  
  * The file of message chains to be used for input
  </li>

  <li>userInput
  
  * ObjectInputStream for reading in the arraylist of users in userFile
  </li>
  
  <li>messageChainInput
  
  * ObjectInputStream for reading in the arraylist of message chains in messageChainFile
  </li>
</ul>

Methods - Any of these methods that modify userArrayList or messageChainArrayList call runMessageWrite or runUserWrite respectively, at the end of the method:
<ul>
  <li>runUserWrite
  
  * Start userWrite DBIOController thread if not already alive.
  </li>

  <li>runMessageWrite
    
  * Start messageWrite DBIOController thread if not already alive.
  </li>

  <li>getUserSize
    
  * Simple getter for size of userArrayList.
  </li>

  <li>makeUser
    
  * creates a new user and adds to userArrayList, unless the new user's password equals one already used by another user.
  </li>

  <li>getUser
    
  * Simple getter for returning the user at a specified index in userArrayList.
  </li>

  <li>updateUser
    
  * Rewrite the user at specified index to the new specified user.
  </li>

  <li>removeUser
    
  * Delete user at specified index by setting its username and password to "DELETED"
  </li>

  <li>getMessageChainSize
    
  * Simple getter for returning size of the messageChainArrayList
  </li>

  <li>makeMessageChain
    
  * Makes a new messageChain with the specified users, and officiates it with the database. Returns the location of this MessageChain if successful, -1 for a blocked user, and -2 for other errors. 
  </li>

  <li>getMessageChain
    
  * Simple getter for getting a MessageChain from specified index in messageChainArrayList.
  </li>

  <li>updateMessageChain
    
  * Rewrite the message chain at specified index to the new specified message chain.
  </li>

  <li>addMessageToChain
    
  * Adds a message to a chain at a specified index in messageChainArrayList
  </li>

  <li>deleteMessageChain
    
  * Calls deleteChain of MessageChain on message chain at specified index in messageChainArrayList
  </li>
</ul>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## DBIOController
Fields:
<ul>
  <li>mode
    
  * Either "user" or "messageChain", tells the controller what type of thing to edit in the database.
  </li>
  <li>USERSYNC
    
  * Lock for synchronizing when editing users in the database.
  </li>
   <li>MESSAGESYNC
    
  * Lock for synchronizing when editing message chains in the database.
  </li>
   <li>userOut
    
  * ObjectOutputStream for writing users.
  </li>
   <li>messageOut
    
  * ObjectOutputStream for writing message chains.
  </li>
</ul>

Methods:
<ul>
  <li>run
  
  * Based on the mode, write using that output stream, based on the database's userToUpdate or messageChainToUpdate.
  </li>
</ul>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## MessageChain
Fields:
<ul>
  <li>messages
    
  * An arraylist of messages in the form of a String array, the message array will have a format of: User identifier, timestamp as a LocalTime in string format, message contents.

  </li>
  <li>userRight
    
  * Local user for sending messages
  </li>
  <li>userLeft
    
  * Remote user for sending messages
  </li>
  <li>messageChainID
    
  * Unique ID for message chains, used to locate in database.
  </li>
  <li>deleted
    
  * Boolean to say whether or not this message chain has been deleted.
  </li>
</ul>

Methods:
<ul>
  <li>deleteChain
  
  * clears messages and defaults User objects. Sets deleted to true.
  </li>
  <li>isDeleted
  
  * Simple getter for boolean deleted.
  </li>
  <li>newMessage
  
  * Creates a new message in the form of a String array, with the senderID, time, and contents.
  </li>
  <li>addNewMessage
  
  * adds a message array (form String[]) to messages.
  </li>
  <li>getMessages
  
  * Simple getter for messages.
  </li>
  <li>sendMessage
  
  * Attempts to send message to the other user, if this fails, return false. **Currently not implemented**
  </li>
  <li>deleteMessage
  
  * Removes message from messages at specified index. Returns true if successful.
  </li>

</ul>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## CantMessageException
* Simple exception that calls super(message) for a message passed into the constructor. This is thrown when a message cannot be processed, whether because of a user being blocked, or another error.

<p align="right">(<a href="#readme-top">back to top</a>)</p>




<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[product-screenshot]: images/screenshot.png
