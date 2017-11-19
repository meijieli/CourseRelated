# Video CDN


1 Overview
===

In this project, you will explore aspects of how streaming video works. In particular, you will implement adaptive bitrate selection.

1.1 In the Real World
---
Clients trying to stream a video first issue a DNS query to resolve the service’s domain name to an IP address for one of the content servers operated by a content delivery network (CDN). The CDN’s authoritative DNS server selects the “best” content server for each particular client based on 
>>(1) the client’s IP address (from which it learns the client’s geographic or network location) and 

>>(2) current load on the content servers (which the servers periodically report to the DNS server)

Once the client has the IP address for one of the content servers, it begins requesting chunks of the video the user requested. The video is encoded at multiple bitrates. As the client player receives video data, it calculates the throughput of the transfer and monitors how much video it has buffered to play, and it requests the highest bitrate the connection can support without running out of video in the playback buffer.

1.2 Your System
---
Implementing an entire CDN is clearly a tall order, so let’s simplify things. First, your entire system will run on one host; we’re providing a network simulator (described in Development Environment) that will allow you to run several processes with arbitrary IP addresses on one machine. Our simulator also allows you to assign arbitrary link characteristics (bandwidth and latency) to the path between each pair of “end hosts”(processes). For this project, you will do your development and testing using a virtual machine we provide.

**Browser**. You’ll use an off­the­shelf web browser to play videos served by your CDN (via your proxy).

**Proxy**. Rather than modify the video player itself, you will implement adaptive bitrate selection in an HTTP proxy. The player requests chunks with standard HTTP GET requests. Your proxy will intercept these and modify them to retrieve whichever bitrate your algorithm deems appropriate. To simulate multiple clients, you will launch multiple instances of your proxy.

**Web Server**. Video content will be served from an off-the-shelf web server (Apache). As with the proxy, you can run multiple instances of Apache on different fake IP addresses to simulate a CDN with several content servers. However, in the assignment, rather than using DNS redirection like a CDN would, the proxy will contact a particular server via its IP address (without a DNS lookup). A possible (ungraded) future extension to the project could include implementing a DNS server that could, based on distance or network conditions from a proxy to various web servers, decide which server to direct the proxy to.
  
2 Video Bitrate Adaptation
===

Many video players monitor how quickly they receive data from the server and use this throughput value to request higher or lower quality encodings of the video, aiming to stream the highest quality encoding that the connection can handle. Rather than modifying an existing video client to perform bitrate adaptation, you will implement this functionality in an HTTP proxy through which your browser will direct requests.
        
2.1 Implementation Details
---
You are implementing a simple HTTP proxy. It accepts connections from web browsers, modifies video chunk requests as described below, opens a connection with the web server’s IP address, and forwards the modified request to the server. Any data (the video chunks) returned by the server should be forwarded, unmodified, to the browser.


Your proxy should listen for connections from a browser on any IP address on the port specified as a command line argument (see below). Your proxy should accept multiple concurrent connections from web browsers by starting a new thread or process for each new request. When it connects to a server, it should first bind the socket to the fake IP address specified on the command line (note that this is somewhat atypical: you do not ordinarily bind() a client socket before connecting). 

2.2.1 Throughput Calculation
---

Your proxy could estimate each stream’s throughput once per chunk as follows. Note the start time, ts, of each chunk request (i.e., include “time” and save a current timestamp using time.time() when your proxy receives a request from the player). Save another timestamp, tf , when you have finished receiving the chunk from the server. Now, given the size of the chunk, B, you can compute the throughput, T, your proxy saw for this chunk (to get the size of each chunk, parse the received data and find the “Content-Length” parameter)
  
To smooth your throughput estimate, your proxy should use an exponentially-weighted moving average (EWMA). Every time you make a new measurement (Tnew), update your current throughput estimate as follows:

'''
T_current = αT_new + (1-α)T_current
'''

The constant 0 ≤ α ≤ 1 controls the tradeoff between a smooth throughput estimate (α closer to 0) and one that reacts quickly to changes (α closer to 1). You will control α via a command line argument. When a new stream starts, set Tcurrent to the lowest available bitrate for that video.

2.2.2 Choosing a Bitrate
---

Once your proxy has calculated the connection’s current throughput, it should select the highest offered bitrate the connection can support. For this project, we say a connection can support a bitrate if the average throughput is at least 1.5 times the bitrate. For example, before your proxy should request chunks encoded at 1000 Kbps, its current throughput estimate should be at least 1.5 Mbps.

Your proxy should learn which bitrates are available for a given video by parsing the manifest file (the “.f4m” initially requested at the beginning of the stream). The manifest is encoded in XML; each encoding of the video is described by a <media> element, whose bitrate attribute you should find.

Your proxy replaces each chunk request with a request for the same chunk at the selected bitrate (in Kbps) by modifying the HTTP request’s Request­URI. Video chunk URIs are structured as follows:

'''
/path/to/video/<bitrate>Seq<num>-Frag<num>
'''

For example, suppose the player requests fragment 3 of chunk 2 of the video Big Buck Bunny at 500 Kbps:

'''
/path/to/video/500Seg2-Frag3
'''

To switch to a higher bitrate, e.g., 1000 Kbps, the proxy should modify the URI like this:
’‘’
/path/to/video/1000Seg2-Frag3

IMPORTANT: When the video player requests big_buck_bunny.f4m, you should instead return big_buck_bunny_nolist.f4m. This file does not list the available bitrates, preventing the video player from attempting its own bitrate adaptation. Your proxy should, however, fetch big_buck_bunny.f4m for itself (i.e., don’t return it to the client) so you can parse the list of available encodings as described above.
 
 2.2.3 Logging
 ---
We require that your proxy create a log of its activity in a very particular format. After each request, it should append the following line to the log:
<time> <duration> <tput> <avg­tput> <bitrate> <server­ip> <chunkname> time: The current time in seconds since the epoch.
duration: A floating point number representing the number of seconds it took to download this chunk from the server to the proxy.
tput: The throughput you measured for the current link in Kbps.
avg­tput: Your current EWMA throughput estimate in Kbps.
bitrate: The bitrate your proxy requested for this chunk in Kbps.
server­ip: The IP address of the server to which the proxy forwarded this request
chunkname: The name of the file your proxy requested from the server (that is, the modified file name in the modified HTTP GET message).

2.2.4 Running the Proxy
---
You should create an executable Python script called proxy under the proxy directory, which should be invoked as follows:
./proxy <log> <alpha> <listen­port> <fake­ip> <web­server­ip> log: The file path to which you should log the messages described in Logging.
alpha: A float in the range [0, 1]. Uses this as the coefficient in your EWMA throughput estimate.
listen­port: The TCP port your proxy should listen on for accepting connections from your browser.
fake­ip: Your proxy should bind to this IP address for outbound connections to the web servers. The fake­ip can only be one of the clients’ IP addresses under the network topology you specified (see Network Simulation). You should not bind your proxy listen socket to this IP address—bind that socket to any IP address that can route to it. (i.e. by calling mySocket.listen((“”, <listen­port>))
web­server­ip: Your proxy should accept an argument specifying the IP address of the web server from which it should request video chunks. It can only be one of the servers’ IP addresses under the network topology you specified (see Network Simulation).
To play a video through your proxy, point a browser on your VM to the URL http://localhost:<listen­port>/index.html. (You can also configure VirtualBox’s port forwarding to send traffic from <listen­port> on the host machine to <listen­port> on your VM; this way you can play the video from the web browser on the host.)
See instructions for making your script executable in the section Hand In.
3 Development Environment
For the project, we are providing a virtual machine pre­configured with the software you will need. We strongly recommend that you do all development and testing in this VM; your code must run correctly on this image as we will be using it for grading. This section describes the VM and the starter code it contains.
3.1 Virtual Box
---
The virtual machine disk (VMDK) we provide was created using VirtualBox, though you may be able to use it with other virtualization software. VirtualBox is a free download for Windows, OSX, and Linux on https://www.virtualbox.org. And please download the VM instance here, and then import it to your own VirtualBox. Please set the number of processors according to your host machine (By selecting your imported VM image and go t
     
 3.2 Starter Files
 ---
You will find the following files in /home/networks/project1­starter. This directory is a git repository; if we find bugs in the starter code and commit fixes, you can get the update versions with a git pull.
common Common code used by our network simulation and LSA generation scripts. lsa
lsa/genlsa.py Generates LSAs for a provided network topology. (LSAs are not used in this version of the project, so you can ignore them.)
netsim
netsim/netsim.py This script controls the simulated network; see Network Simulation.
netsim/tc setup.py This script adjusts link characteristics (BW and latency) in the simulated network. It is called by netsim.py; you do not need to interact with it directly.
netsim/apache setup.py This file contains code used by netsim.py to start and stop Apache instances on the IP addresses in your .servers file; you do not need to interact with it directly.
grapher.py A script to produce plots of link utilization, fairness, and smoothness from log files. (See Requirements.)
topos topos/topo1
topos/topo1/topo1.clients A list of IP addresses, one per line, for the proxies. (Used by netsim.py to create a fake network interface for each proxy.)
topos/topo1/topo1.servers A list of IP addresses, one per line, for the video servers. (Used by netsim.py to create a fake interface for each server.)
  
 topos/topo1/topo1.dns A single IP address for your DNS server. (Used by netsim.py to create a fake interface for the DNS server.) However, in this project you will ignore DNS and let your proxy connect to one of the video server directly by IP address.
topos/topo1/topo1.links A list of links in the simulated network. (Used by genlsa.py.) topos/topo1/topo1.bottlenecks A list of bottleneck links to be used in topo1.events.
(See §4.3.)
topos/topo1/topo1.events A list of changes in link characteristics (BW and latency) to “play.” See the comments in the file. (Used by netsim.py.)
topos/topo1/topo1.lsa A list of LSAs heard by the DNS server in this topology. You can ignore it for this project.
topos/topo1/topo1.pdf A picture of the network. topos/topo2

3.3 Network Simulation
---
To test your system, you will run everything (proxies, servers, and DNS server) on a simulated network in the VM. You control the simulated network with the netsim.py script. You need to provide the script with a directory containing a network topology, which consists of several files. We provide two sample topologies; feel free to create your own. See Starter Files for a description of each of the files comprising a topology. Note that netsim.py requires that each constituent file’s prefix match the name of the topology (e.g. in the topo1 directory, files are named topo1.clients, topo1.servers, etc.).
To start the network from the netsim directory: ./netsim.py <topology> start
<topology> is the path of the topology file, e.g. ../topos/topos1 for topology 1
Starting the network creates a fake network interface for each IP address in the .clients, .servers files; this allows your proxies, Apache instances to bind to these IP addresses.
To stop it once started (thereby removing the fake interfaces), run:
 
 ./netsim.py <topology> stop
To facilitate testing your adaptive bitrate selection, the simulator can vary the bandwidth and latency of an link designated as a bottleneck in your topology’s .bottlenecks file. (Bottleneck links must be declared because our simulator limits you to adjusting the characteristics of only one link between any pair of endpoints. This also means that some topologies simply cannot be simulated by our simulator.) To do so, add link changes to the .events file you pass to netsim.py. Events can run automatically according to timings specified in the file or they can wait to run until triggered by the user (see topos/topo1/topo1.events for an example). When your .events file is ready, tell netsim.py to run it:
./netsim.py <topology> run
Note that you must start the network before running any events. You can issue the run commands as many times as you want without restarting the network. You may modify the .events file between runs, but you must not modify any other topology files, including the .bottlenecks file, without restarting the network. Also note that the links stay as the last event configured them even when netsim.py finishes running.

3.4 Apache
---
You will use the Apache web server to serve the video files. Netsim.py automatically starts an instance of Apache for you on each IP address listed in your topology’s .servers file. Each instance listens on port 8080 and is configured to serve files from /var/www; we have put sample video chunks here for you.
