# <img src="https://github.com/sopra-fs23-group-25/Pictionary-server/assets/99895243/1763c6dd-00e9-46a5-8dcc-82fb3385507a" width="21" height="21" /> [**PictionaryLogo**](https://java.com/](https://sopra-fs23-group-25-client.oa.r.appspot.com/register) Pictionary
### SoPra FS23 Group 25

## Introduction
We created a web-based version of Pictionary, a fun draw and guess game, where users play against each other in real-time.  The game can be played in a user's preferred language. We used Google Translate API to allow users with different chosen languages to play together.		
	

### Hosted on:
- Client: https://sopra-fs23-group-25-client.oa.r.appspot.com/
- Server: https://sopra-fs23-group-25-server.oa.r.appspot.com/

## Technologies
<img src="https://user-images.githubusercontent.com/91155454/170843203-151000ab-db93-4750-b4f4-ba4060a23d53.png" width="16" height="16" /> [**Java**](https://java.com/) : backend implementation.

<img src="https://github.com/sopra-fs23-group-25/Pictionary-server/assets/99895243/dcd30e0f-8428-4c82-9ba4-c9c15640de5e" width="16" height="16" /> [**JavaScript**](https://javascript.com/) : frontend implementation.	

<img src="https://github.com/sopra-fs23-group-25/Pictionary-server/assets/99895243/bb2eafc7-5ed4-4ebd-970e-343441a5b40c" width="16" height="16" /> [**React**](https://react.dev/) : UI development.	

<img src="https://user-images.githubusercontent.com/91155454/170885686-bd14da8d-5070-49ac-b88d-baa2e20729bf.svg" width="16" height="16" /> [**Gradle**](https://gradle.org/) : build automatation.

<img src="https://user-images.githubusercontent.com/91155454/170842503-3a531289-1afc-4b9c-87c1-cc120d9229ce.svg" style='visibility:hidden;' width="16" height="16" /> [**REST**](https://en.wikipedia.org/wiki/Representational_state_transfer) : communication between server and client.	

<img src="https://user-images.githubusercontent.com/91155454/170843632-39007803-3026-4e48-bb78-93836a3ea771.png" style='visibility:hidden;' width="16" height="16" /> [**WebSocket**](https://en.wikipedia.org/wiki/WebSocket) : communication between server and client.	
		
<img src="https://github.com/get-icon/geticon/blob/master/icons/github-icon.svg" width="16" height="16" /> [**GitHub**](https://github.com/) : version control, tracability and planning.

## High Level Components
The [`Controllers`](https://github.com/sopra-fs23-group-25/Pictionary-server/tree/main/src/main/java/ch/uzh/ifi/hase/soprafs23/controller) handle and process the REST calls from the client and pass them onto the [`Services`]. A major part of the application is handled by the [`GameService`](https://github.com/sopra-fs23-group-25/Pictionary-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/service/GameService.java) and [`TurnService`](https://github.com/sopra-fs23-group-25/Pictionary-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/service/TurnService.java). The GameService keeps track of all players and their overall ranking. It also handles the role rotation making sure each player is assigned the role of painter exactly once per round. Finally, it also controls the number of turns and rounds. The TurnService generates the word that is painted and handles the translation and evaluation of each guess.

Something on how game / turnservice interact with websockets

[`WebsocketService`](https://github.com/sopra-fs23-group-25/Pictionary-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/service/WebSocketService.java) : Leo.

## Launch and Deployment

## Roadmap
- Image recognition that allows a user to play against a CPU.
- Saving and sharing of images painted during a turn.
- Add more languages to choose from.

## Authors
- [Joana Cieri](https://github.com/jo-ana-c)
- [Leo Engelberger](https://github.com/pcplusgit)
- [Pablo Chacon Pino](https://github.com/LeoEngelberger)
- [Nico Camillo Zala](https://github.com/nczala)

Contact: leo.engelberger@gmail.com

## Acknowledgements
The template for this project was provided by [Roy Rutishauser](https://github.com/royru).
We would like to thank [Dennys Huber](https://github.com/devnnys) for supervising our project and for his advice throughout the semester.

## Licence



