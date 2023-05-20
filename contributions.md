# SoPra Group 25 - Contributions
## Week 1 

 
|  Name|  Issue(s)| Description |
|--|--|--|
|Leo|#35, #58, #59 | created Lobby-endpoints |
||#64|implement Lobby entity|
||#108|setup Google Translate API|
||#107|implementation Translator class|
|Joana|#24, #52, #25|created User-endpoints|
||#27|implemented registration logic|
||#31, #33, #28|implemented login&logout logic|
||#51, #53|implemented udpate-user logic|
|Nico|#3, #20, #14|lobby-overview page|
||#21|routing between lobby-overview and lobby-settings|
||#22, #23, #24|lobby-setting page|
|||refactoring of login&registration|
|Pepe|#2|created registration page|
||#1, #4, #5|created login page|
||#15|implemented routing between login&registration page|

## Week 2
|  Name|  Issue(s)| Description |
|--|--|--|
|Leo|#83| websockets server |
||#38|add user to lobby functionality|
||#109|guess entity|
||#64|implement Lobby entity|
|Joana|#105|reset game of a lobby - functionality and endpoint|
||#69|game endpoints|
||#27|creation and storing of game object|
||#67|start game logic (init)|
|Nico|#33, #37, #34, #10|drawing board component with drawing functionality and paint toolbar|
||#40|gamepage inlcuding text-field for guesses|
||#9, #38|component to establish websocket connection|
||#13, #25|lobby joining and redirecting to game-page|
||#7, #9, #38|weboscket functionality sending/receiving messages|
||#12|component for player ranking (list)|
||#8, #6|drawingboard eventlistener only for painter, not guesser|
|Pepe|#51|pop-up window to display results of game|
||#52|play again button and functionality|
||#53|exit button and functionality|
||#45|display end of turn results|


## Week 4

|  Name|  Issue(s)| Description |
|--|--|--|
|Leo|#88 #113 #114 #162 #163| implemented and refactored Translator class, added Singleton Pattern and queue  |
||#95| integrated Translator into TurnService and add function to translate entire turns|
||#107 #108| Refactored Implementation of API to ensure only single connection is opened|
||#164 #165| App engine configuration, updated yml workflow/main.yml & app.yaml |
|Joana|#105|reset game of a lobby - functionality and endpoint|
||#161|Turn Entity, Controller, Service|
||#73|implement role distribution and storing in game|
||#93|implement functionality to update the total score for each player|
||#101|implement update how many rounds have been played and how many are left|
||#87|guess validation and point calculation|
|Pablo|#42| get points and role of each user at the beginning of a new turn|
||#44|ranking|
||#45|display results and guesses at the end of each turn|
||#47|display final painting, leader board and new painter|
||#52|display results of game|
||#55|implement guess model|
||#46, #56|display assigned word|
|Nico|#48, #49|countdown timer|
||#49|host informs all players when timer is down and sends next game-state|
||#27|start game functionality|
||#32, #31|render game page according to roles / game-state|
||#40, #41, #54|component and functionality to submit guess|
||#12|display players who joined lobby over websockets|
||#50|display how many rounds are left|
||#36|implemented rendering of drawing board and toolbar|

## Week 5

|  Name|  Issue(s)| Description |
|--|--|--|
|Leo|#169| Fixed some bug of Translator |
||#171| implemented translation of turns when client send get request|
||#167| started fixing translator issues on Google App Engine|
||#114| added lower case regime to minimize wrong translations and achieve higher reproducibility|
|Joana||Joker for Contributions|
|Nico|#28, #30|close lobby button and functionality|
||#68, #69|multi-language translation for LobbyOverview, LobbySettings|
|Pablo|#16 #17 #19| User settings, for username password and language without authentication|

## Week 6
|  Name|  Issue(s)| Description |
|--|--|--|
|Leo|#194|Fixed issues with english speaking users|
||#195|Fixed issue with translating entire turns|
||#182 #183| Handle a Host leaving a Lobby/Game|
||#47| handle a player leaving the Lobby|
|Joana|#177|user can not join a lobby twice|
||#176, #178|refactor login and session to allow the page to be displayed in the users language|
|Nico|#73|leave lobby button and functionality for player (not host)|
||#76|handle when host disconnects from websocket connection during game|
||#78|show intermediate screen to players when host closes lobby or disconnects from websocket connection|
|Pablo|#18, #19|implement user settings authentification and functionality|
||#51|update trophy and styling|

## Week 7
|  Name|  Issue(s)| Description |
|--|--|--|
|Leo|#179|fixed mistakes in word assignment logic, and ensured reset once all words used|
||#205|React with websocket to a rest call once all players hav eguessed and send corresponding GameState|
||#201|fixed sonarcloud flagged bugs|
||#196|improved Translator consistency|
|Joana|#206|update turn controller and service to check if guess is last one such that turn can be ended early|
||#207, #208|modify update user in service and controller to check for unchanged values, empty strings, strings with only space|
||#213|removed unnecessary code, minor refactors|
||#27, #59|addition to already closed tasks: lobby name, registration inputs must each contain at least one character|
|Nico|#84, #85|time-out and functionality which checks if painter is inactive & ends turn earlier if it is the case|
||#86|implemented intermediate component before turn-result that shows why turn finished (all-guessed/ painter-inactive/ time-up)|
||#81|automatic refresh lobbies in overview|
||#70|translation of whole Game Page|
||#79|handle response for last guess|
||#19|refactored&optimized User Settings|
||#74|current round & current turn display|
||#75|refactored drawing board (fit for 13" & 14" desktops)|
||#80|refactored and clean-up game Page|
|Pablo|#47|display answers in red/green|
||#17|authentication for profile editing|
||#71|Game access and game guarding|

## Week 8
|  Name|  Issue(s)| Description |
|--|--|--|
|Leo|#243|enable storing large strings to accommodate Images|
||#215|Test WebSocketController & TestSocketService|
||#213|final refactors, cleanups|
|Joana|#70|Finish translation of all pages german, translation of all pages french|
||#213|final refactors, cleanups|
||#212|finished up tests and reached a line coverage of 83% (without websocket tests)|
||#217|write 3 integration tests|
||#241 #242|recieving and storing of drawings in game|
||#246 #242|check and add new words to play|
|Nico|#|description|
||#|description|
|Pablo|#|Joker Contributions|
