package com.frontier.notification.poc;
/* Created by Seemant Shukla on 19/10/21 */

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.EndpointResponse;
import software.amazon.awssdk.services.pinpoint.model.GetUserEndpointsRequest;
import software.amazon.awssdk.services.pinpoint.model.GetUserEndpointsResponse;
import software.amazon.awssdk.services.pinpoint.model.PinpointException;

import java.util.List;

public class ListEndpointIds {

    public static void main(String[] args) {
        String applicationId = "123";
        String userId = "uid-123";
        PinpointClient pinpoint = PinpointClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(CredentialUtility.getCredentails())
                .build();

        listAllEndpoints(pinpoint, applicationId, userId);
        pinpoint.close();
    }

    //snippet-start:[pinpoint.java2.list_endpoints.main]
    public static void listAllEndpoints(PinpointClient pinpoint,
                                        String applicationId,
                                        String userId) {

        try {
            GetUserEndpointsRequest endpointsRequest = GetUserEndpointsRequest.builder()
                    .userId(userId)
                    .applicationId(applicationId)
                    .build();

            GetUserEndpointsResponse response = pinpoint.getUserEndpoints(endpointsRequest);
            List<EndpointResponse> endpoints = response.endpointsResponse().item();

            // Display the results
            for (EndpointResponse endpoint : endpoints) {
                System.out.println("The channel type is: " + endpoint.channelType());
                System.out.println("The address is  " + endpoint.address());
            }

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

}
