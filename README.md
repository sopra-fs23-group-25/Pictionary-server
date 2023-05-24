<p align="center">
  <img width="111" height="111" src="https://github.com/sopra-fs23-group-25/Pictionary-server/assets/99895243/1763c6dd-00e9-46a5-8dcc-82fb3385507a">
</p>
<h1 align="center">SoPra FS23 Group 25 - Pictionary Server</h1>

## Introduction
We created a web-based version of Pictionary, a fun draw and guess game, where users can play against each other in real-time.  The game can be played in a user's preferred language. We used Google Translate API to allow users with different chosen languages to play together.			

### Hosted on:
- Client: https://sopra-fs23-group-25-client.oa.r.appspot.com/
- Server: https://sopra-fs23-group-25-server.oa.r.appspot.com/

## Technologies
<img src="https://user-images.githubusercontent.com/91155454/170843203-151000ab-db93-4750-b4f4-ba4060a23d53.png" width="16" height="16" /> [**Java**](https://java.com/) : backend implementation.

<img src="https://user-images.githubusercontent.com/91155454/170885686-bd14da8d-5070-49ac-b88d-baa2e20729bf.svg" width="16" height="16" /> [**Gradle**](https://gradle.org/) : build automation.

<img src="https://user-images.githubusercontent.com/91155454/170842503-3a531289-1afc-4b9c-87c1-cc120d9229ce.svg" style='visibility:hidden;' width="16" height="16" /> [**REST**](https://en.wikipedia.org/wiki/Representational_state_transfer) : communication between server and client.	

<img src="https://user-images.githubusercontent.com/91155454/170843632-39007803-3026-4e48-bb78-93836a3ea771.png" style='visibility:hidden;' width="16" height="16" /> [**WebSocket**](https://en.wikipedia.org/wiki/WebSocket) : communication between server and client as well as client to client communication.	
		
<img src="https://github.com/get-icon/geticon/blob/master/icons/github-icon.svg" width="16" height="16" /> [**GitHub**](https://github.com/) : version control, tracability and planning.

## Roadmap
Possible Feature Ideas to add in the Future.
- Image recognition that allows a user to play against a CPU.
- Saving and sharing of images painted during a turn.
- Add more languages to choose from.
- Add synonym-recognition to Translator to increase consistency of Translations.


## High Level Components
The [`Controllers`](https://github.com/sopra-fs23-group-25/Pictionary-server/tree/main/src/main/java/ch/uzh/ifi/hase/soprafs23/controller) handle and process the REST calls from the client and pass them onto the [`Services`](https://github.com/sopra-fs23-group-25/Pictionary-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/service.java). A major part of the application is handled by the [`GameService`](https://github.com/sopra-fs23-group-25/Pictionary-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/service/GameService.java) and [`TurnService`](https://github.com/sopra-fs23-group-25/Pictionary-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/service/TurnService.java). The GameService keeps track of all players and their overall ranking. It also handles the role rotation making sure each player is assigned the role of painter exactly once per round. Finally, it also controls the number of turns and rounds. The TurnService generates the word that is painted and handles the translation and evaluation of each guess.

This Repo uses WebSockets to stream data used between clients. Once A User joins a Lobby  they connect to various different WebSocketEndpoints. These Endpoints are implemented in the [`WebsocketController`](https://github.com/sopra-fs23-group-25/Pictionary-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/controller/WebSocketController.java)
Most of these Endpoints are simply a way to relay messages from the dedicated Host (the player who started the lobby) to all its players. Only in a few Cases the WebSockets interact with other Classes. Most important among these, is the Interaction with [`WebsocketService`](https://github.com/sopra-fs23-group-25/Pictionary-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/service/WebSocketService.java) to access the lobbyRepository and retrieve all data in the corresponding game. This is used to check which players are in a lobby or who has disconnected.

To Translate a Users guess we created a [`Translator`](https://github.com/sopra-fs23-group-25/Pictionary-server/tree/main/src/main/java/ch/uzh/ifi/hase/soprafs23/translator) class which uses the GoogleTranslate API to translate individual Strings. The Translator Class encapsulates this API and provides Helper-methods to simplify the Translation Workflow.
All [`Guesses`](https://github.com/sopra-fs23-group-25/Pictionary-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/Guess.java) that get submitted are translated to English before they are stored in the [`Game Entity`](https://github.com/sopra-fs23-group-25/Pictionary-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/Game.java).

## Launch and Deployment
Download your IDE of choice (e.g., [IntelliJ](https://www.jetbrains.com/idea/download/), [Visual Studio Code](https://code.visualstudio.com/), or [Eclipse](http://www.eclipse.org/downloads/)). Make sure Java 17 is installed on your system (for Windows, please make sure your `JAVA_HOME` environment variable is set to the correct version of Java).
>Hint: this Project has originally been built with IntelliJ using openJDK17

### IntelliJ
> Make sure to install the Google Cloud Code Plugin
1. File -> Open... -> SoPra server template
2. Accept to import the project as a `gradle project`
3. To build right click the `build.gradle` file and choose `Run Build`

### VS Code
> Make sure to install the Google Cloud Code Extension

The following extensions can help you get started more easily:
-   `vmware.vscode-spring-boot`
-   `vscjava.vscode-spring-initializr`
-   `vscjava.vscode-spring-boot-dashboard`
-   `vscjava.vscode-java-pack`

**Note:** You'll need to build the project first with Gradle, just click on the `build` command in the _Gradle Tasks_ extension. Then check the _Spring Boot Dashboard_ extension if it already shows `soprafs23` and hit the play button to start the server. If it doesn't show up, restart VS Code and check again. 

## Building with Gradle
You can use the local Gradle Wrapper to build the application.
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

You can verify that the server is running by visiting `localhost:8080` in your browser.

### Test

```bash
./gradlew test
```
**Note:** If The Translator Tests fail, ensure that you have set up your Google Cloud Credentials and that they have access to Cloud Translate.
The Easiest way to do so is Get a ssh key and adding it to the System variables. Make Sure to name it: GOOGLE_APPLICATION_CREDENTIALS

**Example of System Variable:**
> GOOGLE_APPLICATION_CREDENTIALS=C:\.ssh\Keys\GoogleTranslateServiceKey.json

## Authors
- [Joana Cieri](https://github.com/jo-ana-c)
- [Leo Engelberger](https://github.com/LeoEngelberger)
- [Pablo Chacon Pino](https://github.com/pcplusgit)
- [Nico Camillo Zala](https://github.com/nczala)

Contact: leo.engelberger@gmail.com

## Acknowledgements
We would like to thank the SoPra FS23 teaching team, especially our tutor [Dennys Huber](https://github.com/devnnys) for supervising our project and for his advice throughout the semester.

## Licence
[GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html)

