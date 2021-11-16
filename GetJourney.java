package com.ripplestreet.notification.poc;
/* Created by Seemant Shukla on 11/11/21 */

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.GetJourneyRequest;
import software.amazon.awssdk.services.pinpoint.model.GetJourneyResponse;

public class GetJourney {

    public static void main(String[] args) {

        PinpointClient pinpoint = PinpointClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(CredentialUtility.getCredentails())
                .build();

        String applicationId = "21009cac4e174273b9ccfd6b7446c2e5";
        String journeyId = "614dc500b5304867a908f79db412fb62";

        GetJourneyResponse response = getJourneyResponse(pinpoint, applicationId, journeyId);

        System.out.println("Journey : " + response);

        pinpoint.close();
    }

    private static GetJourneyResponse getJourneyResponse(PinpointClient pinpointClient, String applicationId, String journeyId) {

        //APPLICATION_ID_FIELD,JOURNEY_ID_FIELD
        GetJourneyRequest getJourneyRequest = GetJourneyRequest.builder()
                .applicationId(applicationId)
                .journeyId(journeyId)
                .build();
        GetJourneyResponse response = pinpointClient.getJourney(getJourneyRequest);
        return response;
    }

}
