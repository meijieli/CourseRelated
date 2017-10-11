from socket import socket, AF_INET, SOCK_DGRAM

if __name__ == '__main__':
    """
    The server side of UDP connection
    """

    # An arbitrary port number
    serverPort = 12000

    # Step 1: Create a socket
    # int socket(int domain, int type, int protocol)
    # serverSocket = socket(AF_INET, SOCK_DGRAM, [IPPROTO_UDP])
    # last parameter is not necessary because
    # python will know that DGRAM refers to UDP
    # returns a socket descriptor(an integer)
    serverSocket = socket(AF_INET, SOCK_DGRAM)

    # Step2: Bind the socket to a specific port
    serverSocket.bind(('', serverPort))

    print('The server is ready to receive')

    # Step3: Receive and send messages
    while True:
        # receive message
        message, clientAddress = serverSocket.recvfrom(2048)
        # decode message from bytes to string and transfer the string to uppercase
        modifiedMessage = message.decode().upper()
        print('handled client: ' + str(clientAddress)+ ' with message: ' + message.decode())
        serverSocket.sendto(modifiedMessage.encode(), clientAddress)
