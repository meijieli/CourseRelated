package com.amazonaws.samples;

import java.util.Arrays;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupResult;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.IpRange;

public class CreateSecurityGroup {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
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
        
        
        // Create a security group
        try {
			CreateSecurityGroupRequest csgr = new CreateSecurityGroupRequest();
			csgr.withGroupName("JavaSecurityGroup").withDescription("Security group for minihw1");
			
			CreateSecurityGroupResult createSecurityGroupResult = ec2.createSecurityGroup(csgr);
			System.out.println(String.format("Security Group created: [%s]", 
					createSecurityGroupResult.getGroupId()));
		} catch (AmazonServiceException ase) {
			// if we attempt to create a security group with the same name as an exsiting security
	        // group, createSecurityGroup throws an exception
			System.out.println(ase.getMessage());
		}
        
        
        IpPermission ipPermission_SSH = new IpPermission();
        IpPermission ipPermission_HTTP = new IpPermission();
        IpRange ipRange = new IpRange().withCidrIp("0.0.0.0/0");
        ipPermission_SSH.withIpv4Ranges(ipRange)
        			.withIpProtocol("tcp")
        			.withFromPort(22)
        			.withToPort(22);
        ipPermission_HTTP.withIpv4Ranges(ipRange)
          				 .withIpProtocol("tcp")
          				 .withFromPort(80)
          				 .withToPort(80);
    
        try {
			AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest = 
					new AuthorizeSecurityGroupIngressRequest();
			authorizeSecurityGroupIngressRequest.withGroupName("JavaSecurityGroup")
												.withIpPermissions(
						Arrays.asList(new IpPermission[]{ipPermission_SSH, ipPermission_HTTP}));
			
			ec2.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest);
			System.out.println("Authorize ingress completed.");
		} catch (AmazonServiceException ase) {
			// The ingress ip range has already been authorized
			System.out.println(ase.getMessage());
		}
	}
}
