package com.frontier.notification.poc;
/* Created by Seemant Shukla on 19/10/21 */

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.UpdateEndpointsBatchResponse;
import software.amazon.awssdk.services.pinpoint.model.EndpointUser;
import software.amazon.awssdk.services.pinpoint.model.EndpointBatchItem;
import software.amazon.awssdk.services.pinpoint.model.ChannelType;
import software.amazon.awssdk.services.pinpoint.model.EndpointBatchRequest;
import software.amazon.awssdk.services.pinpoint.model.PinpointException;
import software.amazon.awssdk.services.pinpoint.model.UpdateEndpointsBatchRequest;

import java.util.*;

public class BatchOfEndpoints {

    public static void main(String[] args) {
        String applicationId = "12312";
        PinpointClient pinpoint = PinpointClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(CredentialUtility.getCredentails())
                .build();

        updateEndpointsViaBatch(pinpoint, applicationId);
        pinpoint.close();
    }

    public static void updateEndpointsViaBatch(PinpointClient pinpoint, String applicationId) {

        try {

            Map sshuklaAttributes = new HashMap<String, List>();
            sshuklaAttributes.put("attributes", Arrays.asList("Music", "Books"));

            Map sshuklaName = new HashMap<String, List>();
            sshuklaName.put("name", Arrays.asList("Seemant", "Shukla"));

            EndpointUser sshukla154 = EndpointUser.builder().userId("uid-001").userAttributes(sshuklaName).build();

            // Create an EndpointBatchItem object for Richard Roe.
            EndpointBatchItem sshuklaEmailEndpoint = EndpointBatchItem.builder()
                    .channelType(ChannelType.EMAIL).address("s.shukla.154@gmail.com")
                    .id("ep-001").attributes(sshuklaAttributes).user(sshukla154)
                    .build();

            Map samAttributes = new HashMap<String, List>();
            samAttributes.put("interests", Arrays.asList("Cooking", "Politics", "Finance"));

            Map samName = new HashMap<String, List>();
            samName.put("name", Arrays.asList("Sam", "Adidas"));

            EndpointUser samAdidas = EndpointUser.builder().userId("uid-002").userAttributes(samName).build();

            // Create an EndpointBatchItem object for Sam Adidas.
            EndpointBatchItem samAdidasEmailEndpoint = EndpointBatchItem.builder()
                    .channelType(ChannelType.SMS).address("samadidas001@gmail.com")
                    .id("example_endpoint_2").attributes(samAttributes).user(samAdidas)
                    .build();

            // Adds multiple endpoint definitions to a single request object.
            EndpointBatchRequest endpointList = EndpointBatchRequest.builder()
                    .item(sshuklaEmailEndpoint)
                    .build();

            // Create the UpdateEndpointsBatchRequest.
            UpdateEndpointsBatchRequest batchRequest = UpdateEndpointsBatchRequest.builder()
                    .applicationId(applicationId).endpointBatchRequest(endpointList)
                    .build();

            //  Updates the endpoints with Amazon Pinpoint.
            UpdateEndpointsBatchResponse result = pinpoint.updateEndpointsBatch(batchRequest);
            System.out.format("Update endpoints batch result: %s\n",
                    result.messageBody().message());

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
    //snippet-end:[pinpoint.java2.update_batch.main]
}
