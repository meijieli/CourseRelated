from socket import socket, AF_INET, SOCK_STREAM

if __name__ == '__main__':
    """
    The server side of a TCP connection
    """

    # An arbitrary port number
    serverPort = 12000

    # Step 1: Create a socket
    # int socket(int domain, int type, int protocol)
    # serverSocket = socket(AF_INET, SOCK_STREAM, [IPPROTE_TCP])
    # last parameter is not necessary
    # python will know that STREAM refers to TCP
    serverSocket = socket(AF_INET,SOCK_STREAM)

    # Step 2: Bind the socket to a specific port   
    serverSocket.bind(('',serverPort))

    # Step 3: Set up the listening socket to accept connections
    # just for TCP: listen
    # means the receiver is ready to receive data
    # here we have a socket, the ip address bounds to any ip
    # remain active for other users but here we set MAXPENDING be 1  
    serverSocket.listen(1)
    
    print('The server is ready to receive')

    while True:
        # Step 4:
        # just for TCP: accept
        # in UDP there are just two sockets, one for client and one for server
        # but in TCP, accept will create a socket for each user
        # here, the ip address bounds to the specific user
        # this user's further communication will go through this new socket
        # by default blocks until a connection request arrives
        connectionSocket, addr = serverSocket.accept()

        # Step 5: receive message
        sentence = connectionSocket.recv(1024).decode()
        # capitalize the received message
        capitalizedSentence = sentence.upper()
        # send the message back
        connectionSocket.send(capitalizedSentence.encode())
        print ("server handled: " + str(addr) + " with message: " + sentence)
        # Step 6: close the connection
        connectionSocket.close()
