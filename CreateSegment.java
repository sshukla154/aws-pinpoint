package com.frontier.notification.poc;
/* Created by Seemant Shukla on 18/10/21 */

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.*;

import java.util.HashMap;
import java.util.Map;

public class CreateSegment {

    public static void main(String[] args) {
        String appId = "123";

        PinpointClient pinpoint = PinpointClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(CredentialUtility.getCredentails())
                .build();

        SegmentResponse result = createSegment(pinpoint, appId);
        System.out.println("Segment " + result.name() + " created.");
        System.out.println(result.segmentType());
        pinpoint.close();

    }

    //snippet-start:[pinpoint.java2.createsegment.main]
    public static SegmentResponse createSegment(PinpointClient client, String appId) {

        try {
            Map<String, AttributeDimension> segmentAttributes = new HashMap<>();
//            segmentAttributes.put("Team", AttributeDimension.builder().build());
//                    .attributeType(AttributeType.INCLUSIVE)
//                    .attributeType(AttributeType.INCLUSIVE)
//                    .values("Lakers")
//                    .build());

            RecencyDimension recencyDimension = RecencyDimension.builder().build();
//                    .duration("DAY_30")
//                    .recencyType("ACTIVE")
//                    .build();

            SegmentBehaviors segmentBehaviors = SegmentBehaviors.builder()
                    .recency(recencyDimension)
                    .build();

            SegmentDemographics segmentDemographics = SegmentDemographics
                    .builder()
                    .build();

            SegmentLocation segmentLocation = SegmentLocation
                    .builder()
                    .build();

            SegmentDimensions dimensions = SegmentDimensions
                    .builder()
                    .build();

            WriteSegmentRequest writeSegmentRequest = WriteSegmentRequest.builder()
                    .name("MyJavaSegment")
//                    .segmentGroups(SegmentGroupList.builder().groups(SegmentGroup.builder().sourceType(SourceType.ALL).build()).build())
//                    .dimensions(dimensions)
                    .build();

            CreateSegmentRequest createSegmentRequest = CreateSegmentRequest.builder()
                    .applicationId(appId)
                    .writeSegmentRequest(writeSegmentRequest)
                    .build();

            CreateSegmentResponse createSegmentResult = client.createSegment(createSegmentRequest);
            System.out.println("Segment ID: " + createSegmentResult.segmentResponse().id());
            System.out.println("Done");
            return createSegmentResult.segmentResponse();

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
//            System.exit(1);
        }
        return null;
    }

}
