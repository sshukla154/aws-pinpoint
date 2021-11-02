package com.frontier.notification.poc;
/* Created by Seemant Shukla on 21/10/21 */

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.EndpointRequest;
import software.amazon.awssdk.services.pinpoint.model.EndpointUser;
import software.amazon.awssdk.services.pinpoint.model.ChannelType;
import software.amazon.awssdk.services.pinpoint.model.UpdateEndpointRequest;
import software.amazon.awssdk.services.pinpoint.model.UpdateEndpointResponse;
import software.amazon.awssdk.services.pinpoint.model.PinpointException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddExampleUser {

    public static void main(String[] args) {
        String applicationId = args[0];
        String endPointId = args[1];
        PinpointClient pinpoint = PinpointClient.builder()
                .region(Region.US_EAST_1)
                .build();

        updatePinpointEndpoint(pinpoint, applicationId, endPointId);
        pinpoint.close();
    }

    //snippet-start:[pinpoint.java2.update_endpoint.main]
    public static void updatePinpointEndpoint(PinpointClient pinpoint, String applicationId, String endPointId) {

        try {
            List<String> wangXiList = new ArrayList<String>();
            wangXiList.add("cooking");
            wangXiList.add("running");
            wangXiList.add("swimming");

            Map myMapWang = new HashMap<String, List>();
            myMapWang.put("interests", wangXiList);

            List<String> myNameWang = new ArrayList<String>();
            myNameWang.add("Wang ");
            myNameWang.add("Xiulan");

            Map wangName = new HashMap<String, List>();
            wangName.put("name", myNameWang);

            EndpointUser wangMajor = EndpointUser.builder()
                    .userId("example_user_10")
                    .userAttributes(wangName)
                    .build();

            // Create an EndpointBatchItem object for Mary Major.
            EndpointRequest wangXiulanEndpoint = EndpointRequest.builder()
                    .channelType(ChannelType.EMAIL)
                    .address("wang_xiulan@example.com")
                    .attributes(myMapWang)
                    .user(wangMajor)
                    .build();

            // Adds multiple endpoint definitions to a single request object.
            UpdateEndpointRequest endpointList = UpdateEndpointRequest.builder()
                    .applicationId(applicationId)
                    .endpointRequest(wangXiulanEndpoint)
                    .endpointId(endPointId)
                    .build();

            UpdateEndpointResponse result = pinpoint.updateEndpoint(endpointList);

            System.out.format("Update endpoint result: %s\n", result.messageBody().message());

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
    //snippet-end:[pinpoint.java2.update_endpoint.main]

}
