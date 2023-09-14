# Computer-Networks-As1
This project was focused on a File Retrieval Protocol.

The aim of the protocol is to provide a mechanism to retrieve files from a service based on UDP datagrams. The encoding of the header information of this protocol should be implemented in a binary format.
The protocol involves a number of actors: One or more clients, an ingress node, and one or more workers. A client issues requests for files to an ingress node and receives replies from this node. The ingress node processes requests, forwards them to one of the workers that are associated with it, and forwards replies to clients that have send them. The header information that you included in your packets has to support the identification of the requested action, the transfer of files - potentially consisting of a number of packets and the management of the workers by the server.

The basic functionality that the file request protocol has to provide is to support requests for files from a server which then distributes the requests to workers.

<img width="576" alt="Screenshot 2023-09-14 at 18 28 34" src="https://github.com/JamesOC3310/Computer-Networks-As1/assets/98289189/b2029f13-1026-4ef2-a0ed-67fe922cfd6b">
