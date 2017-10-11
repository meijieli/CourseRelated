from socket import socket, AF_INET, SOCK_DGRAM

if __name__ == '__main__':
    """
    The client side of UDP connection.
    """

    # Use myself as server
    serverName = '127.0.0.1'
    # An arbitrary port number
    serverPort = 12000

    # Step 1: Create a socket
    # int socket(int domain, int type, int protocol)
    # serverSocket = socket(AF_INET, SOCK_DGRAM, [IPPROTO_UDP])
    # last parameter is not necessary because
    # python will know that DGRAM refers to UDP
    # returns a socket descriptor(an integer)
    clientSocket = socket(AF_INET, SOCK_DGRAM)

    # get a message from keyboard
    message = input('Input lowercase sentence:')

    # Step2: send message
    clientSocket.sendto(message.encode(),(serverName, serverPort))

    # Step3: receive returned message from the server
    modifiedMessage, serverAddress = clientSocket.recvfrom(2048)
    print(modifiedMessage.decode())

    # Step4: close the connection
    clientSocket.close()
