package com.amazonaws.samples;
/*
 * Copyright 2010-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.KeyPair;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
/**
 * Welcome to your new AWS Java SDK based project!
 *
 * This class is meant as a starting point for your console-based application that
 * makes one or more calls to the AWS services supported by the Java SDK, such as EC2,
 * SimpleDB, and S3.
 *
 * In order to use the services in this sample, you need:
 *
 *  - A valid Amazon Web Services account. You can register for AWS at:
 *       https://aws-portal.amazon.com/gp/aws/developer/registration/index.html
 *
 *  - Your account's Access Key ID and Secret Access Key:
 *       http://aws.amazon.com/security-credentials
 *
 *  - A subscription to Amazon EC2. You can sign up for EC2 at:
 *       http://aws.amazon.com/ec2/
 *
 */

public class CreateEC2 {

    /*
     * Before running the code:
     *      Fill in your AWS access credentials in the provided credentials
     *      file template, and be sure to move the file to the default location
     *      where the sample code will load the credentials from.
     *      https://console.aws.amazon.com/iam/home?#security_credential
     *
     * WARNING:
     *      To avoid accidental leakage of your credentials, DO NOT keep
     *      the credentials file in your source directory.
     */

    @SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, InterruptedException {
        //============================================================================================//
        //=============================== Submitting a Request =======================================//
        //============================================================================================//

        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file.
         */
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location, and is in valid format.", e);
        }

        // Create the AmazonEC2Client object so we can call various APIs.
		AmazonEC2 ec2 = new AmazonEC2Client(credentials);
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        ec2.setRegion(usEast1);
        
        
        // Create a key pair
        String privateKey = null;
		try {
			CreateKeyPairRequest createKeyPairRequest = new CreateKeyPairRequest();
			createKeyPairRequest.withKeyName("minihw1");        
			CreateKeyPairResult createKeyPairResult = ec2.createKeyPair(createKeyPairRequest);
			KeyPair keyPair = new KeyPair();
			keyPair = createKeyPairResult.getKeyPair();
			privateKey = keyPair.getKeyMaterial();
	        FileWriter fileWriter = new FileWriter("minihw1.pem");
	        PrintWriter printWriter = new PrintWriter(fileWriter);
	        printWriter.print(privateKey);
	        printWriter.close();
		} catch (Exception e1) {		
			System.out.println("The key already exists.");
		}
                
        //Initializes a Run Instance Request
        RunInstancesRequest runInstancesRequest = new RunInstancesRequest();
        
        // Setup the specifications of the launch. This includes the instance type (e.g. t2.micro)
        // and the latest Amazon Linux AMI id available. Note, you should always use the latest
        // Amazon Linux AMI id or another of your choosing.
        runInstancesRequest.withImageId("ami-97785bed")
        				.withInstanceType("t2.micro")
        				.withMinCount(1)
        				.withMaxCount(1)
        				.withKeyName("minihw1")
        				// Created in "CreateSecurityGroup.java"
        				.withSecurityGroups("JavaSecurityGroup");

        @SuppressWarnings("unused")
		RunInstancesResult runInstancesResult = ec2.runInstances(runInstancesRequest);
        
        String ipAdd = null;
        // do something here to get the results after the instance is created
        try {
			Thread.currentThread();			// give server some time to allocate resources
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			System.out.println("An interruption occurs!");
		};
        DescribeInstancesRequest request =  new DescribeInstancesRequest();
        Collection<String> instanceIds = null;
		request.setInstanceIds(instanceIds);
        DescribeInstancesResult result = ec2.describeInstances(request);
        List<Reservation> reservations = result.getReservations();
        List<Instance> instances;
        for(Reservation res : reservations){
        	instances = res.getInstances();
            for( Instance ins:instances){
                System.out.println("Instance ID:" + ins.getInstanceId() + "\t Public IP: " + ins.getPublicIpAddress() + "\t Region: " + ins.getPlacement().getAvailabilityZone());
                if (ins.getPublicIpAddress() != null)
                	ipAdd = ins.getPublicIpAddress();
            }  
        }
        
        ipAdd = ipAdd.replace('.', '-');			// replace . with -
        
        Thread.currentThread();
		Thread.sleep(3000);							// wait 
		
        @SuppressWarnings("unused")
		final Process p1 = Runtime.getRuntime().exec("chmod 400 minihw1.pem");
        String command = "ssh -i \"minihw1.pem\" ec2-user@ec2-" + ipAdd + ".compute-1.amazonaws.com";
        System.out.println(command);
        final Process p2 = Runtime.getRuntime().exec(command);

        new Thread(new Runnable() {
            public void run() {
             BufferedReader input = new BufferedReader(new InputStreamReader(p2.getInputStream()));
             String line = null; 

             try {
            	System.out.println("Waiting for ssh response...");
            	while ((line = input.readLine()) == null){}	// block
            	System.out.println(line);
                while ((line = input.readLine()) != null)
                    System.out.println(line);
             } catch (IOException e) {
                    e.printStackTrace();
             }
            }
        }).start();

        p2.waitFor();  
    }
}

