#!/usr/bin/env python3
# -*- coding: utf-8 -*

"""
COMS W4701 Artificial Intelligence - Programming Homework 2

An AI player for Othello. This is the template file that you need to  
complete and submit. 

@author: Shenzhi Zhang   sz2695 
"""

import random
import sys
import time
from heapq import heappush
from heapq import heappop

# You can use the functions in othello_shared to write your AI 
from othello_shared import find_lines, get_possible_moves, get_score, play_move

# caching states by a dictionary
board_states = {}

def compute_utility(board, color):
    """
    Computer the utility for color x.
    Dark tries to maximize the utility.
    Light tries to minimize the utility.
    """
    scores = get_score(board)

    # From max's view:
    # if player == dark, return scores[0] - scores[1]
    # if player == light, return scores[1] - scores[0]
    return scores[color - 1] - scores[other_color(color) - 1]



############ MINIMAX ###############################

def minimax_min_node(board, color):
    """
    Method for min node.
    If a terminal stage is reached, return current utility.
    Else recursively call method for max node on each possible move.
    """

    if (board, color) in board_states:               # if this board state has already been explored
        return board_states[(board, color)]
    if len(get_possible_moves(board, other_color(color))) == 0:  # terminal state
        return compute_utility(board, color)
    else:
        min_max = float('inf')
        for x in get_possible_moves(board, other_color(color)):              # for every possible move of this min node
            next_board = play_move(board, other_color(color), x[0], x[1])    # get the result board of this move
            max_value = minimax_max_node(next_board, color)                  # recursively call max node
            min_max = min(min_max, max_value)                                # keep record of current min_max
        board_states[(board, color)] = min_max
        return min_max


def minimax_max_node(board, color):
    """
    Method for max node.
    If a terminal stage is reached, return current utility.
    Else recursively call method for min node on each possible move.
    """

    if (board, color) in board_states:               # if this board state has already been explored
        return board_states[(board, color)]
    if len(get_possible_moves(board, color)) == 0:
        return compute_utility(board, color)
    else:
        max_min = float('-inf')
        for x in get_possible_moves(board, color):              # for every possible move of this max node
            next_board = play_move(board, color, x[0], x[1])    # get the result board of this move
            min_value = minimax_min_node(next_board, color)     # recursively call min node
            max_min = max(max_min, min_value)                   # keep record of current max_min
        board_states[(board, color)] = max_min
        return max_min

        
def select_move_minimax(board, color):
    """
    Given a board and a player color, decide on a move using ordinary minimax. 
    The return value is a tuple of integers (i,j), where
    i is the column and j is the row on the board.
    """

    result = []
    max_min = float('-inf')
    for x in get_possible_moves(board, color):              # for every possible move of this max node
        next_board = play_move(board, color, x[0], x[1])    # get the result board of this move
        min_value = minimax_min_node(next_board, color)     # recursively call min_node
        if max_min < min_value:                             # keep record of max_min and its corresponding move
            max_min = min_value
            result = [x[0], x[1]]
    return result[0], result[1]                             # return the move with max min value



def other_color(color):
    """
    A utility method to return the other color
    """
    if color == 1:
        return 2
    else:
        return 1



############ ALPHA-BETA PRUNING #####################
def alphabeta_min_node(board, color, alpha, beta, level = 0, limit = float('inf')):  
    """
    Method for min node using alpha beta pruning.
    If a terminal stage is reached, return current utility.
    Else recursively call method for max node on each possible move and do cutoffs/updates.
    """
    #---------------------Part 3: Caching States-----------------------#
    if (board, color) in board_states:               # if this board state has already been explored
        return board_states[(board, color)]
    #---------------------Part 3: Caching States-----------------------#

    #---------------------Part 4: Node Ordering------------------------#
    if limit == level or len(get_possible_moves(board, other_color(color))) == 0: 
        return compute_utility(board, color)
    #---------------------Part 4: Node Ordering------------------------#
    else:
        # order the child states
        ordered_states = []
        for x in get_possible_moves(board, other_color(color)):              # for every possible move of this min node
            next_board = play_move(board, other_color(color), x[0], x[1])    # get the result board of this move
            # put board state with maximum utility at the root of heap
            heappush(ordered_states, (compute_utility(next_board, color), next_board))    
            
        cur = float('inf')                    # keep record the min_max for this min node       
        while(len(ordered_states) != 0):
            max_value = alphabeta_max_node(heappop(ordered_states)[1], color, alpha, beta, level + 1, limit)
            cur = min(cur, max_value)
            if (cur <= alpha):                # min_max has dropped below parent max node's max_min value, cut off
                board_states[(board, color)] = cur     # put state into dictionary
                return cur
            beta = min(beta, cur)             # update current min_max for this node
        board_states[(board, color)] = cur
        return cur                            # cut off didn't occur, return min_max



def alphabeta_max_node(board, color, alpha, beta, level = 0, limit = float('inf')):
    """
    Method for max node using alpha beta pruning.
    If a terminal stage is reached, return current utility.
    Else recursively call method for min node on each possible move and do cutoffs/updates.
    """
    
    #---------------------Part 3: Caching States-----------------------#  
    if (board, color) in board_states:         # if this board state has already been explored
        return board_states[(board, color)]
    #---------------------Part 3: Caching States-----------------------#
    

    #---------------------Part 4: Node Ordering------------------------#
    if level == limit or len(get_possible_moves(board, color)) == 0:          
        return compute_utility(board, color)
    #---------------------Part 4: Node Ordering------------------------#
    
    else:
        ordered_states = []
        for x in get_possible_moves(board, color):                          # for every possible move of this max node
            next_board = play_move(board, color, x[0], x[1])                # get the result board of this move 
            heappush(ordered_states, (-1 * compute_utility(next_board, color), next_board))
        
        cur = float('-inf')           # keep record the max_min for this max node      
        while(len(ordered_states) != 0):                                          
            min_value = alphabeta_min_node(heappop(ordered_states)[1], color, alpha, beta, level + 1, limit)
            cur = max(cur, min_value)
            if cur >= beta:           # max_min has reached above parent min node's min_max value, cut off
                board_states[(board, color)] = cur     # put state into dictionary
                return cur
            alpha = max(alpha, cur)   # update current max_min for this node
        board_states[(board, color)] = cur
        return cur                    # cut off didn't occur, return max_min





def select_move_alphabeta(board, color): 
    """
    Given a board and a player color, decide on a move using ordinary minimax. 
    The return value is a tuple of integers (i,j), where
    i is the column and j is the row on the board.
    """

    result = []
    cur = float('-inf')
    alpha = float('-inf')               # initialize alpha and beta
    beta = float('inf')
    ordered_states = []
    for x in get_possible_moves(board, color):              # for every possible move of this max node
        next_board = play_move(board, color, x[0], x[1])    # get the result board of this move
        heappush(ordered_states, (-1 * compute_utility(next_board, color), next_board, x))

    while(len(ordered_states) != 0):
        next_one = heappop(ordered_states)
        
        ######### Without Depth Limit Call ###########
 #       min_value = alphabeta_min_node(next_one[1], color, alpha, beta) 
        
        ######### Depth Limit Call ##########
        min_value = alphabeta_min_node(next_one[1], color, alpha, beta, 1, 6) # should be able to run 6 levels, 5 is safer
 
        if cur < min_value:
            cur = min_value
            result = [next_one[2][0], next_one[2][1]]       # keep record of the move leads to max_min
        alpha = max(alpha, cur)
    return result[0], result[1]         # return the recorded move





####################################################
def run_ai():
    """
    This function establishes communication with the game manager. 
    It first introduces itself and receives its color. 
    Then it repeatedly receives the current score and current board state
    until the game is over. 
    """
    print("Minimax AI")  # First line is the name of this AI  
    color = int(input()) # Then we read the color: 1 for dark (goes first), 
                         # 2 for light. 

    while True: # This is the main loop 
        # Read in the current game status, for example:
        # "SCORE 2 2" or "FINAL 33 31" if the game is over.
        # The first number is the score for player 1 (dark), the second for player 2 (light)
        next_input = input() 
        status, dark_score_s, light_score_s = next_input.strip().split()
        dark_score = int(dark_score_s)
        light_score = int(light_score_s)

        if status == "FINAL": # Game is over. 
            print 
        else: 
            board = eval(input()) # Read in the input and turn it into a Python
                                  # object. The format is a list of rows. The 
                                  # squares in each row are represented by 
                                  # 0 : empty square
                                  # 1 : dark disk (player 1)
                                  # 2 : light disk (player 2)
                    
            # Select the move and send it to the manager 
            #movei, movej = select_move_minimax(board, color)      # Use Minimax algorithm
            movei, movej = select_move_alphabeta(board, color)     # Use alphabeita
            print("{} {}".format(movei, movej)) 


if __name__ == "__main__":
    run_ai()
