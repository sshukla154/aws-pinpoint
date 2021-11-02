package com.frontier.notification.poc;
/* Created by Seemant Shukla on 19/10/21 */

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreateEndpoint1 {

    public static void main(String[] args) {

        String appId = "123";
        PinpointClient pinpoint = PinpointClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(CredentialUtility.getCredentails())
                .build();

        EndpointResponse response = createEndpoint(pinpoint, appId);
        System.out.println("Got Endpoint: " + response.id());
        pinpoint.close();
    }

    public static EndpointResponse createEndpoint(PinpointClient client, String appId) {
        String endpointId = "ep-003";
        System.out.println("Endpoint ID: " + endpointId);

        try {

            EndpointRequest endpointRequest = createEndpointRequestData();

            UpdateEndpointRequest updateEndpointRequest = UpdateEndpointRequest.builder()
                    .applicationId(appId)
                    .endpointId(endpointId)
                    .endpointRequest(endpointRequest)
                    .build();

            UpdateEndpointResponse updateEndpointResponse = client.updateEndpoint(updateEndpointRequest);
            System.out.println("Update Endpoint Response: " + updateEndpointResponse.messageBody());

            GetEndpointRequest getEndpointRequest = GetEndpointRequest.builder()
                    .applicationId(appId)
                    .endpointId(endpointId)
                    .build();
            GetEndpointResponse getEndpointResponse = client.getEndpoint(getEndpointRequest);

            System.out.println(getEndpointResponse.endpointResponse().address());
            System.out.println(getEndpointResponse.endpointResponse().channelType());
            System.out.println(getEndpointResponse.endpointResponse().applicationId());
            System.out.println(getEndpointResponse.endpointResponse().endpointStatus());
            System.out.println(getEndpointResponse.endpointResponse().requestId());
            System.out.println(getEndpointResponse.endpointResponse().user());

            return getEndpointResponse.endpointResponse();

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return null;
    }

    private static EndpointRequest createEndpointRequestData() {

        try {
            Map<String, Collection<String>> userAttributes = new HashMap<>();
            userAttributes.put("FirstName", List.of("s"));
            userAttributes.put("LastName", List.of("s"));
            userAttributes.put("isActive", List.of("TRUE"));
            userAttributes.put("Age", List.of("22"));
            EndpointUser user = EndpointUser.builder()
                    .userId("uid-1234")
                    .userAttributes(userAttributes)
                    .build();

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
            String nowAsISO = df.format(new Date());

            return EndpointRequest.builder()
                    .address("s.shukla.154@gmail.com")
                    .channelType(ChannelType.EMAIL)
                    .effectiveDate(nowAsISO).optOut("NONE")
                    .user(user)
                    .build();

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

        return null;
    }

}
