# Build Your Own Internet

Introduction
---
In   this   assignment,   we   will   learn   how   to   build   and   operate   a   **layer-3**   network   using   traditional distributed   routing   protocols,   how   different   networks   managed   by   different   organizations interconnect   with   each   other,   and   how   protocols,   configuration,   and   policy   combine   in   Internet routing.

More   specifically,   we   will   first   learn   how   to   set   up   a   valid   forwarding   state   within     an autonomous   system   (AS)   using   **OSPF**,   an   intra-domain   routing   protocol.   Then,   we   will learn   how   to   set   up   valid   forwarding   state   between   different   ASes,   so   that   an   end-host   in   one   AS (e.g.,   your   laptop   connected   to   the   university   wireless   network)   can   communicate   with   an end-host   in   another   AS   (e.g.,   Google’s   server).   To   do   that,   you   will   need   to   use   the   only inter-domain   routing   protocol   deployed   today:   **BGP**.   After   that,   we   will   implement different   **BGP   policies**   to   reflect   business   relationships   or   traffic   engineering   that   exist   in   the   real Internet.   We   will   configure   both   **OSPF**   and   **BGP**   through   the    **Quagga   software   routing suite** ,   which   runs   on   several   virtual   routers   in   our   virtual   machine   (VM).

Network Topology
---

**Figure 1**: The Network Topology Within an AS

                                        ![image](https://github.com/Shenzhi-ZHANG/CourseRelated/blob/master/Computer_Networks/Project2/Network%20Topology%20Within%20AS.png)

**Figure 2**: AS-level Network Topology

                                        ![image](https://github.com/Shenzhi-ZHANG/CourseRelated/blob/master/Computer_Networks/Project2/AS-level%20Network%20Topology.png)

Files
---

Entire configuration directory **configs** is included.

Each sub-directory **router-name** contains the detailed configuration at a router **router-name** in **AS9**.
