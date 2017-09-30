"""
COMS W4701 Artificial Intelligence - Programming Homework 1

In this assignment you will implement and compare different search strategies
for solving the n-Puzzle, which is a generalization of the 8 and 15 puzzle to
squares of arbitrary size (we will only test it with 8-puzzles for now). 
See Courseworks for detailed instructions.

@author: Shenzhi Zhang (sz2695)
"""

import time
goal_state = ()
#------------------------------------#
def state_to_string(state):
    row_strings = [" ".join([str(cell) for cell in row]) for row in state]
    return "\n".join(row_strings)



#------------------------------------#
def swap_cells(state, i1, j1, i2, j2):
    """
    Returns a new state with the cells (i1,j1) and (i2,j2) swapped. 
    """
    value1 = state[i1][j1]
    value2 = state[i2][j2]
    
    new_state = []
    for row in range(len(state)): 
        new_row = []
        for column in range(len(state[row])): 
            if row == i1 and column == j1: 
                new_row.append(value2)
            elif row == i2 and column == j2:
                new_row.append(value1)
            else: 
                new_row.append(state[row][column])
        new_state.append(tuple(new_row))
    return tuple(new_state)
    


#------------------------------------#
def get_successors(state):
    """
    This function returns a list of possible successor states resulting
    from applicable actions. 
    The result should be a list containing (Action, state) tuples. 
    For example [("Up", ((1, 4, 2),(0, 5, 8),(3, 6, 7))), 
                 ("Left",((4, 0, 2),(1, 5, 8),(3, 6, 7)))] 
    """ 
  
    child_states = []
    hole_position = []

    # the position of the hole
    for i, row in enumerate(state):         # search for the hole
        if 0 in row:
            hole_position = [i, row.index(0)]
            break
    
    max_length = len(state) - 1             # maximum index for this board
    if hole_position[1] != max_length:      # move left is a valid move 
        child_states.append(("Left", swap_cells(state, hole_position[0],
            hole_position[1], hole_position[0], hole_position[1] + 1)))
    if hole_position[1] != 0:               # move right is a valid move
        child_states.append(("Right", swap_cells(state, hole_position[0],
            hole_position[1], hole_position[0], hole_position[1] - 1)))
    if hole_position[0] != max_length:      # move up is a valid move
        child_states.append(("Up", swap_cells(state, hole_position[0],
            hole_position[1], hole_position[0] + 1, hole_position[1])))
    if hole_position[0] != 0:               # move down is a valid move
        child_states.append(("Down", swap_cells(state, hole_position[0],
            hole_position[1], hole_position[0] - 1, hole_position[1])))

    return child_states



#------------------------------------#          
def goal_test(state):           # test whether a state is the goal state
    """
    Returns True if the state is a goal state, False otherwise. 
    """    
    return goal_state == state  # judge goal_state is equivalent with state



#------------------------------------#
def get_goal(n):
    """
    Returns the goal state for a n * n board
    """
    goal = []
    row = []
    for i in range(n * n):
        row.append(i)
        if (i + 1) % 3 == 0:
            goal.append(tuple(row))
            row = []
    return tuple(goal)



#------------------------------------#  
def bfs(state):
    """
    Breadth first search.
    Returns three values: A list of actions, the number of states expanded, and
    the maximum size of the frontier.  
    """
    parents = {}        # dictionary that matches a state with its parent
    actions = {}        # dictionary that matches a state to the action leads to it

    states_expanded = 0 # the number of states that have been explored
    max_frontier = 0    # the maximum number of states ever in the frontier

    # itialization
    frontier = [state]
    explored = set()
    seen = set()    
    seen.add(state)
    max_frontier = 1
    parents[state] = []
    actions[state] = []

    while (len(frontier) > 0):
        cur_state = frontier.pop(0)             # dequeue the first state in frontier
        explored.add(cur_state)
        states_expanded = states_expanded + 1   # increment number of states explored

        if (goal_test(cur_state)):
            return recover_path(state, cur_state, parents, actions), \
                   states_expanded, max_frontier

        for successors in get_successors(cur_state):
            next_state = successors[1]
            if next_state not in seen:
                seen.add(next_state)            # add new state to seen
                frontier.append(next_state)     # add new state to frontier
                parents[next_state] = cur_state # bookkeeping
                actions[next_state] = successors[0]

        max_frontier = max(max_frontier, len(frontier)) # update max number of states on frontier

                               


#------------------------------------#
def recover_path(state, goal, parents, actions):
    """
    Utility function used to recover the path to the goal
    """
    
    path = []
    cur_state = goal
    while(cur_state != state):
        path = [actions[cur_state]] + path
        cur_state = parents[cur_state]
    return path


    
#------------------------------------#    
def dfs(state):
    """
    Depth first search.
    Returns three values: A list of actions, the number of states expanded, and
    the maximum size of the frontier.  
    """

    parents = {}        # dictionary that matches a state with its parent
    actions = {}        # dictionary that matches a state to the action leads to it

    states_expanded = 0 # the number of states that have been explored
    max_frontier = 0    # the maximum number of states ever in the frontier

    # itialization
    frontier = [state]
    explored = set()
    seen = set()    
    seen.add(state)
    max_frontier = 1
    parents[state] = []
    actions[state] = []

    while (len(frontier) > 0):
        cur_state = frontier.pop()              # pop the last state in frontier
        explored.add(cur_state)
        states_expanded = states_expanded + 1   # increment number of states explored

        if (goal_test(cur_state)):
            return recover_path(state, cur_state, parents, actions), \
                   states_expanded, max_frontier

        for successors in get_successors(cur_state):
            next_state = successors[1]
            if next_state not in seen:
                seen.add(next_state)            # add new state to seen
                frontier.append(next_state)     # add new state to frontier
                parents[next_state] = cur_state # bookkeeping
                actions[next_state] = successors[0]

        max_frontier = max(max_frontier, len(frontier)) # update max number of states on frontier



#------------------------------------#
def misplaced_heuristic(state):
    """
    Returns the number of misplaced tiles.
    """

    number = 0
    for i in range(len(state)):
        for j in range(len(state[i])):
            if state[i][j] != goal_state[i][j] and state[i][j] != 0:
                number = number + 1
    
    return number



#------------------------------------#
def manhattan_heuristic(state):
    """
    For each misplaced tile, compute the manhattan distance between the current
    position and the goal position. THen sum all distances. 
    """

    goal_dic = {}
    state_dic = {}
    for row in range(len(state)):
        for column in range(len(state)):
            goal_dic[goal_state[row][column]] = [row, column]
            state_dic[state[row][column]] = [row, column]

    manhattan_dis = 0;
    for i in range(1, len(state) * len(state)):
        dis = abs(goal_dic[i][0] - state_dic[i][0]) + \
                  abs(goal_dic[i][1] - state_dic[i][1])
        manhattan_dis = manhattan_dis + dis

    return manhattan_dis



#------------------------------------#
def best_first(state, heuristic = misplaced_heuristic):
    """
    Breadth first search using the heuristic function passed as a parameter.
    Returns three values: A list of actions, the number of states expanded, and
    the maximum size of the frontier.  
    """

    from heapq import heappush
    from heapq import heappop

    parents = {}        # dictionary that matches a state with its parent
    actions = {}        # dictionary that matches a state to the action leads to it
    costs = {}
    costs[state] = heuristic(state)
    
    states_expanded = 0 # the number of states that have been explored
    max_frontier = 0    # the maximum number of states ever in the frontier

    # itialization
    frontier = []
    heappush(frontier, (costs[state], state))   # push into priority queue
    explored = set()
    seen = set()    
    seen.add(state)
    max_frontier = 1
    parents[state] = []
    actions[state] = []

    while (len(frontier) > 0):
        cur_state = heappop(frontier)[1]        # dequeue the first state in frontier
        explored.add(cur_state)
        states_expanded = states_expanded + 1   # increment number of states explored

        if (goal_test(cur_state)):
            return recover_path(state, cur_state, parents, actions), \
                   states_expanded, max_frontier

        for successors in get_successors(cur_state):
            next_state = successors[1]
            if next_state not in seen:
                seen.add(next_state)            # add new state to seen
                costs[next_state] = heuristic(next_state)               # calculate the heuristic cost of this state
                heappush(frontier, (costs[next_state], next_state))     # add new state to frontier
                parents[next_state] = cur_state # bookkeeping
                actions[next_state] = successors[0]

        max_frontier = max(max_frontier, len(frontier)) # update max number of states on frontier




#------------------------------------#
def astar(state, heuristic = misplaced_heuristic):
    """
    A-star search using the heuristic function passed as a parameter. 
    Returns three values: A list of actions, the number of states expanded, and
    the maximum size of the frontier.  
    """
    # You might want to use these functions to maintain a priority queue

    from heapq import heappush
    from heapq import heappop

    parents = {}
    actions = {}
    costs = {}
    costs[state] = 0        # the actual cost of initial state is 0
   
    states_expanded = 0
    max_frontier = 0

    # itialization
    frontier = []
    heappush(frontier, (costs[state] + heuristic(state), state))   # push into priority queue
    explored = set()
    seen = set()    
    seen.add(state)
    max_frontier = 1
    parents[state] = []
    actions[state] = []

    while (len(frontier) > 0):
        cur_state = heappop(frontier)[1]        # dequeue the first state in frontier
        explored.add(cur_state)
        states_expanded = states_expanded + 1   # increment number of states explored

        if (goal_test(cur_state)):
            return recover_path(state, cur_state, parents, actions), \
                   states_expanded, max_frontier

        for successors in get_successors(cur_state):
            next_state = successors[1]
            if next_state not in seen:
                seen.add(next_state)            # add new state to seen
                costs[next_state] = costs[cur_state] + 1     # calculate the total cost of this state
                heappush(frontier, (costs[next_state] + heuristic(next_state), next_state))     # add new state to frontier
                parents[next_state] = cur_state # bookkeeping
                actions[next_state] = successors[0]
##            else:
##                if costs[cur_state] + 1 < costs[next_state]:
##                    if (costs[next_state] + heuristic(next_state), next_state) in frontier:
##                        frontier.remove((costs[next_state] + heuristic(next_state), next_state))
##                        costs[next_state] = costs[cur_state] + 1
##                        heappush(frontier, (costs[next_state] + heuristic(next_state), next_state))
##                        parents[next_state] = cur_state
##                        actions[next_state] = successors[0]

        max_frontier = max(max_frontier, len(frontier)) # update max number of states on frontier




#------------------------------------#
def print_result(solution, states_expanded, max_frontier):
    """
    Helper function to format test output. 
    """
    if solution is None: 
        print("No solution found.")
    else: 
        print("Solution has {} actions.".format(len(solution)))
    print("Total states exppanded: {}.".format(states_expanded))
    print("Max frontier size: {}.".format(max_frontier))




#------------------------------------#
if __name__ == "__main__":

    #Easy test case
    test_state = ((1, 4, 2),
                  (0, 5, 8), 
                  (3, 6, 7))   

    goal_state = get_goal(len(test_state))      # generate goal_state for the test_state
    print(state_to_string(test_state))
    print()

    print("====BFS====")
    start = time.time()
    solution, states_expanded, max_frontier = bfs(test_state) 
    end = time.time() 
    print_result(solution, states_expanded, max_frontier)
    if solution is not None:
        print(solution)
    print("Total time: {0:.3f}s".format(end-start))

    print() 
    print("====DFS====") 
    start = time.time()
    solution, states_expanded, max_frontier = dfs(test_state)
    end = time.time()
    print_result(solution, states_expanded, max_frontier)
    print("Total time: {0:.3f}s".format(end-start))

    print() 
    print("====Greedy Best-First (Misplaced Tiles Heuristic)====") 
    start = time.time()
    solution, states_expanded, max_frontier = best_first(test_state, misplaced_heuristic)
    end = time.time()
    print_result(solution, states_expanded, max_frontier)
    print("Total time: {0:.3f}s".format(end-start))
    
    print() 
    print("====A* (Misplaced Tiles Heuristic)====") 
    start = time.time()
    solution, states_expanded, max_frontier = astar(test_state, misplaced_heuristic)
    end = time.time()
    print_result(solution, states_expanded, max_frontier)
    print("Total time: {0:.3f}s".format(end-start))

    print()
    print('For this part, I nullified the code to perform update')
    print("====A* (Total Manhattan Distance Heuristic)====") 
    start = time.time()
    solution, states_expanded, max_frontier = astar(test_state, manhattan_heuristic)
    end = time.time()
    print_result(solution, states_expanded, max_frontier)
    print("Total time: {0:.3f}s".format(end-start))

    for o in range(5):
        print()
    print("===============================================")
    print("===============================================")
    print("===============================================")
    for o in range(5):
        print()
    print('harder test case')
    # Another test case
    #More difficult test case
    test_state = ((7, 2, 4),
                  (5, 0, 6), 
                  (8, 3, 1)) 
    print(state_to_string(test_state))
    print()

    print("====BFS====")
    start = time.time()
    solution, states_expanded, max_frontier = bfs(test_state) 
    end = time.time() 
    print_result(solution, states_expanded, max_frontier)
    if solution is not None:
        print(solution)
    print("Total time: {0:.3f}s".format(end-start))

    print() 
    print("====DFS====") 
    start = time.time()
    solution, states_expanded, max_frontier = dfs(test_state)
    end = time.time()
    print_result(solution, states_expanded, max_frontier)
    print("Total time: {0:.3f}s".format(end-start))

    print() 
    print("====Greedy Best-First (Misplaced Tiles Heuristic)====") 
    start = time.time()
    solution, states_expanded, max_frontier = best_first(test_state, misplaced_heuristic)
    end = time.time()
    print_result(solution, states_expanded, max_frontier)
    print("Total time: {0:.3f}s".format(end-start))
    
    print() 
    print("====A* (Misplaced Tiles Heuristic)====") 
    start = time.time()
    solution, states_expanded, max_frontier = astar(test_state, misplaced_heuristic)
    end = time.time()
    print_result(solution, states_expanded, max_frontier)
    print("Total time: {0:.3f}s".format(end-start))

    print()
    print('For this part, I nullified the code to perform update')
    print("====A* (Total Manhattan Distance Heuristic)====") 
    start = time.time()
    solution, states_expanded, max_frontier = astar(test_state, manhattan_heuristic)
    end = time.time()
    print_result(solution, states_expanded, max_frontier)
    print("Total time: {0:.3f}s".format(end-start))
