#!/usr/bin/env python

"""
CSEE W4119 Computer Networks --- Project 1

Video CDN

@author: Shenzhi Zhang   sz2695    
"""

import sys, time, thread, random, select, os
from socket import socket, AF_INET, SOCK_STREAM



# -----------------------Global Variables-------------------------#
max_connection = 5      # the maximum value is system-dependent (usually 5)
buffer_size = 4096      # buffer_size = 4096
log_file = None         # the path to log file
aerfa = 0.1             # aerfa used in smoothing
fake_ip = ''            # the fake ip for this proxy
web_server = ''         # the ip of the web server this proxy is directed to
bit_rate_table = []     # the bit rate table obtained from .f4m file
t_cur = 0               # current estimated throughput
#-----------------------------------------------------------------#



# ------------------------myProxy Class---------------------------#
class myProxy:
    '''
        For each new connection (new thread), create myProxy instance.
        Making this part of code a class is not necessary, but
        it's easier to pass parameters between different functions
        by setting common parameters as class fields.
    '''
    #---------------------------------------------#
    def __init__(self, con, address):
        '''
            Constructor
            @param_in:
                con: the connection between browser and proxy
                address: the ip address of the browser client
            @func:
                Create a myProxy instance and call 'proxy_receive_from_browser'.
                When the reply has been received by the browser,
                write log and close connections.
        '''
        #print('new thread set up')
        self.browserProxy = con                 # TCP connection between browser and proxy
	self.again_flag = False
        self.proxy_receive_from_browser()       # receive data from browser

        # bit rate selection, forward, receive, send back, timing, etc

        self.write_log()                        # write log
        self.browserProxy.close()               # close connection
        self.proxyServer.close()
    #---------------------------------------------#



    #---------------------------------------------#
    def write_log(self):
        '''
            Write log to 'log_file' based on estimated throughput
        '''
        global t_cur, log_file, web_server
        if 'Seg' in self.proxyPath:
            t_new = int(8 * float(self.content_length) / float(self.requestTime) / 1000)        # new throughput estimation
            t_cur = (1 - aerfa) * t_cur + aerfa * t_new                                         # smoothing
            #print(t_cur)

            t = int(time.time())                                                                # current time
            # log record
            s = ' '.join([str(t), str(self.requestTime), str(t_new), str(round(t_cur)), str(self.bit_rate), web_server, self.proxyPath])
            log_file.write(s + '\n')                                                            # write log record to log_file      
    #---------------------------------------------#



    #---------------------------------------------#
    def proxy_receive_from_browser(self):
        '''
            @func:
                The proxy receives data from browser,
                parses the request into different fields
                then call 'proxy_connect_server'
        '''
        data = ''                           # received data
        flag = True
        while flag:                         # until the end of first line
            data = self.browserProxy.recv(buffer_size)
            end = data.find('\n')           # locate '\n'
            if end != -1:                   
                flag = False
        # we need to modify self.path when we forward http request to the web server
        (self.command, self.path, self.protocol) = data[:end + 1].split()   # split different fields
	self.rest_of_request = data[end + 1 : ]                             # store the rest part of the http request
        self.proxy_connect_server()         # set up the tcp connection between proxy and web server
    #---------------------------------------------#



    #---------------------------------------------#
    def proxy_connect_server(self):
        '''
            @func:
                The proxy sets up its connection with the web server
        '''
        global fake_ip, web_server
        self.proxyServer = socket(AF_INET, SOCK_STREAM)
        self.proxyServer.bind((fake_ip, random.randint(5000, 60000)))   # assign a random port number(atypical bind operation)
        self.proxyServer.connect((web_server, 8080))                    # connect to web_server at port 8080
        #print('successfully connect to the web server')
        self.bitrate_selection()                                        # adaptive bit rate selection
    #---------------------------------------------#



    #---------------------------------------------#
    def bitrate_selection(self):
        '''
            @func:
                Select the proper bit rate and change .f4m file title
        '''
        
        self.proxyPath = self.path
	#print(self.proxyPath)
        self.again_flag = False
	if 'big_buck_bunny.f4m' in self.proxyPath:      # instead return nolist file to the browser
	    self.againPath = self.path
            self.proxyPath = self.path.replace('big_buck_bunny.f4m', 'big_buck_bunny_nolist.f4m')
	    self.f4m_flag = True                        # indicate fetching .f4m file (used in 'proxy_receive_from_server')
	else:
	    self.f4m_flag = False
	
	if 'Seg' in self.proxyPath:                     # requesting a video chunk
		self.bit_rate = get_bitRate()           # select bit rate from bit_rate_table
		#print(self.bit_rate)
		self.change_bit_rate()                  # change the bit rate indicated in the http request   
	#print(self.proxyPath)
	self.proxy_send_to_server()                     # proxy forwards the modified request to server
    #---------------------------------------------#

	

    #---------------------------------------------#
    def change_bit_rate(self):
        '''
            @func:
                Parse the http request, replace the original bit rate
                with the bit rate given by get_bitRate()
        '''
	index1 = self.proxyPath.find('Seg')             # locate 'Seg'
	#print('index1' + str(index1))
	index2 = -1
	for i in range(0, index1 - 1):
	    if self.proxyPath[i] == '/':                # find the last '/' before 'Seg'
		index2 = i
	# change the number between '/' and 'Seg' to selected bit rate
	temp = self.proxyPath[0 : index2 + 1] + str(self.bit_rate) + self.proxyPath[index1 :]
	#print(temp)
	self.proxyPath = temp
    #---------------------------------------------#

	

    #---------------------------------------------#
    def proxy_send_to_server(self):
        '''
            @func:
            The proxy forwards the modified http request to the web server.
            Set up two timestamps.
            Call 'proxy_receive_from_server'.
        '''
        
        startTime = time.time()                      # first timestamp
        new_request = self.command + ' ' + self.proxyPath + ' ' + self.protocol + '\n' + self.rest_of_request
        self.proxyServer.send(new_request)           # send http request to web server
        #print('data successfully forwarded to the web server')
        self.proxy_receive_from_server()             # receive data from server
        #print('data successfully received by the proxy')
        endTime = time.time()                        # second timestamp
        self.requestTime = endTime - startTime       # calculate time elapsed (for calculating throughput)



    #---------------------------------------------#
    def proxy_receive_from_server(self):
        '''
            @func:
                The proxy receives reply from web server.
                Tally up the received length and compare it with the
                content-length field to decide the end of data
        '''
        # the socket has to wait before it's ready to read
	self.content_length = 1
	received_length = 0         # length of data received so far
	temp = ''
	#print('proxy waiting for response')
        while received_length < self.content_length:
            # [read, write, ex] = select.select(rlist, wlist, xlist[, timeout])
            [read, _, error] = select.select([self.proxyServer], [], [self.proxyServer], 3)
            if error:
		print('error occurred')
                break
            if read:
                data = self.proxyServer.recv(buffer_size)               # receive data
                if data:                                                # if there is data received
 		    if self.again_flag:
			self.fetch_list(data)
			break
		    if self.f4m_flag:
			temp = temp + data
		    else:		                             
                        self.browserProxy.send(data)                        # send the message back to browser
                    pos_CL = data.find('Content-Length: ')		# look for the index of 'Content-Length'
		    if pos_CL != -1:                                    # 'Content-Length' found		    
		    	self.content_length = int(data[pos_CL + 16 : ].split('\n', 1)[0])   # parse number out
			#print(self.content_length)	    
		    if '\r\n\r\n' in data:                              # get the body part
		    	[header, body] = data.split('\r\n\r\n', 1)	    	
			received_length = received_length + len(body)   # tally up received length
		    else:
			received_length = received_length + len(data)	# no header in this message
	if self.f4m_flag:
	    self.again_flag = True
	    self.f4m_flag = False
	    self.proxyPath = self.againPath
	    self.proxy_send_to_server()
	    self.browserProxy.send(temp)
	#print(received_length)
    #---------------------------------------------#    



    #---------------------------------------------#
    def fetch_list(self, content):
        '''
            @func:
            When the browser requests .f4m file, the proxy
            fetches .f4m file for itself
        '''
	global bit_rate_table, t_cur
	print(content)
    	while True:
	    index = content.find('bitrate')         # find 'bitrate' field
	    if index == -1:                         # no more, break
		break
	    index = index + 9                       # points to the leading digit of the number
	    content = content[index : ]
	    [rate, content] = content.split('"', 1) # split the bit rate 
	    bit_rate_table.append(int(rate))        # append this bit rate to bit_rate_table
	bit_rate_table = sorted(bit_rate_table)     # sort bit_rate_table
	print(bit_rate_table)
	t_cur = bit_rate_table[0]
    #---------------------------------------------#
                    
    
    
#---------------------------------------------#
def start():
    '''
        @func:
            Read in parameters from command line and create
            the socket of browser and proxy.
            Initialize proxy instance by starting a new thread.
    '''
    global log_file, aerfa, fake_ip, web_server     # allow access of command line parameters in other functions
    log_file = open(sys.argv[1], 'w')
    aerfa = float(sys.argv[2])
    proxyPort = int(sys.argv[3])
    fake_ip = sys.argv[4]
    web_server = sys.argv[5]

    proxySocket = socket(AF_INET, SOCK_STREAM)      # initiate socket
    proxySocket.bind(('', proxyPort))               # bind socket for listening
    proxySocket.listen(max_connection)              # start listening for incoming connections

    while True:
        # open a new thread for concurrent connections
        thread.start_new_thread(myProxy, proxySocket.accept())

    proxySocket.close()                             # close the connection
#---------------------------------------------#



#---------------------------------------------#
def get_bitRate():
    '''
        @func:
            Get the highest offered bit rate the connection can support
        @param-ret:
            The selected bit rate
    '''
    global bit_rate_table, t_cur
    selected_bit_rate = bit_rate_table[0]           # default: lowest bit rate
    for bitRate in bit_rate_table:
        if 1.5 * bitRate < t_cur:                   # if the connection can support a higher bit rate
            selected_bit_rate = bitRate             # update the selected bitRate
    return selected_bit_rate
#---------------------------------------------#



if __name__ == "__main__":
    start()
