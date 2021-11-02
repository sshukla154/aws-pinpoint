package com.frontier.notification.poc;
/* Created by Seemant Shukla on 19/10/21 */

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * CREATE:
 * epid-001 - Shashank Shukla - uid-111 - 001@gmail.com
 * epid-002 - Shivam Shukla - uid-222 - er@gmail.com
 * UPDATE:
 * epid-003 - Dev Shukla - uid-333 - 154@gmail.com
 * epid-004 - Ved Shukla - uid-444 - seemant@gmail.com
 */

public class CreateEndpoint {

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
        // TODO: Change endPointId
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
            HashMap<String, List<String>> customAttributes = new HashMap<>();
            customAttributes.put("team", Arrays.asList("Lakers", "Warriors"));

            EndpointDemographic demographic = EndpointDemographic.builder().appVersion("1.0").make("apple").model("iPhone").modelVersion("7").platform("ios").platformVersion("10.1.1").timezone("America/Los_Angeles").build();

            EndpointLocation location = EndpointLocation.builder().city("Los Angeles").country("US").latitude(34.0).longitude(-118.2).postalCode("90068").region("CA").build();

            Map<String, Double> metrics = new HashMap<>();
            metrics.put("health", 100.00);
            metrics.put("luck", 75.00);

            Map<String, Collection<String>> userAttributes = new HashMap<>();
            // TODO: Change FirstName LastName and userId
            userAttributes.put("FirstName", List.of("Dev"));
            userAttributes.put("LastName", List.of("Shukla"));
            userAttributes.put("isActive", List.of("TRUE"));
            userAttributes.put("Age", List.of("15"));
            EndpointUser user = EndpointUser.builder()
                    .userId("uid-333")
                    .userAttributes(userAttributes)
                    .build();

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
            String nowAsISO = df.format(new Date());

            // TODO: Change EmailId
            EndpointRequest endpointRequest = EndpointRequest.builder()
                    .address("s.shukla.154@gmail.com").attributes(customAttributes)
                    .channelType(ChannelType.EMAIL).demographic(demographic)
                    .effectiveDate(nowAsISO).location(location)
                    .metrics(metrics).optOut("NONE")
                    .user(user).endpointStatus("ACTIVE")
                    .build();

            return endpointRequest;

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

        return null;
    }

}
