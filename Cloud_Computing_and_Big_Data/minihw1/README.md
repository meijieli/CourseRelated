First task on AWS
==

The task in this mini-homework is to automate the creation on EC2 instances. 

Using the AWS SDK, write a program that creates a new EC2 instance. 
To create an EC2 instance, the program needs to create the following elements programmatically:
* Security Group
* Key Pair
* Specify an image id
 
After a new instance is created, parse the returned response to print the following:

* external IP address 
* which region instance was created
* instance ID

Do remember to use the correct key-pair and the security group with the correct access settings to make sure you can SSH into these instances.

