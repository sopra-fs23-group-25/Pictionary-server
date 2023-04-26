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


## Week 3

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


