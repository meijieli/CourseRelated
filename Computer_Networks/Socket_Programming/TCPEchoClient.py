from socket import socket, AF_INET, SOCK_STREAM

if __name__ == '__main__':
    """
    The client side of a TCP connection
    """
    
    # server is myself   
    serverName = '127.0.0.1'
    # An arbitrary port number 
    serverPort = 12000
    
    # Step 1: Create a socket
    # int socket(int domain, int type, int protocol)
    # serverSocket = socket(AF_INET, SOCK_STREAM, [IPPROTE_TCP])
    # last parameter is not necessary
    # python will know that STREAM refers to TCP
    clientSocket = socket(AF_INET, SOCK_STREAM)

    # Step 2: 
    # Just for TCP: connect
    # Connect the client socket to a server listening socket
    clientSocket.connect((serverName,serverPort))
    
    sentence = input('Input lowercase sentence:')

    # Step 3: send message
    clientSocket.send(sentence.encode())

    # Step 4: receive echoed back message
    modifiedSentence = clientSocket.recv(1024)
    print ('From Server:' +  modifiedSentence.decode())

    # Step 5: close the connection
    clientSocket.close()
