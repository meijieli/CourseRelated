Othello AI Agent
===
Inroduction:
---
We will implement an AI player for the game Reversi, also known as Othello. 
For the rules of this game see here: https://en.wikipedia.org/wiki/Reversi (Links to an external site.)Links to an external site.. Our version of the game differs from these rules in one minor point: The game ends as soon as one of the players has no legal moves left. 

Files: 
---
* othello_gui.py - Contains a simple graphical user interface (GUI) for Othello. 
* othello_game.py - Contains the game "manager", which stores the current game state and communicates with different player AIs. 
* othello_shared.py - Functions for computing legal moves, captured disks, and successor game states. These are shared between the game manager, the GUI and the AI players. 
* randy_ai.py - Randy is an "AI" player that randomly selects a legal move.
* othello_ai.py - The ai that uses minimax and alpha-beta pruning algorithm

Example:
---
![image](https://github.com/Shenzhi-ZHANG/CourseRelated/blob/master/Artificial_Intelligence/Homework2/othello_game.png)
