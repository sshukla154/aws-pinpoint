package com.frontier.notification.poc;
/* Created by Seemant Shukla on 25/10/21 */

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.*;

public class UpdateChannel {
    public static void main(String[] args) {

        String appId = "";
        PinpointClient pinpoint = PinpointClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(CredentialUtility.getCredentails())
                .build();

        EmailChannelResponse getResponse = getEmailChannel(pinpoint, appId);

        System.out.println("Response : " + getResponse);
        pinpoint.close();
    }

    private static EmailChannelResponse getEmailChannel(PinpointClient client, String appId) {

        try {
            UpdateEmailChannelRequest updateRequest = UpdateEmailChannelRequest.builder()
                    .applicationId(appId)
                    .emailChannelRequest(EmailChannelRequest.builder()
                            .identity("")
                            .fromAddress("Seemant Shukla <er.seemantshukla@gmail.com>")
                            .enabled(true)
                            .build())
                    .build();

            EmailChannelResponse response = client.updateEmailChannel(updateRequest).emailChannelResponse();

            System.out.println("Channel state is " + response);
            return response;

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return null;
    }

    private static void toggleSmsChannel(PinpointClient client, String appId, SMSChannelResponse getResponse) {
        boolean enabled = true;

        if (getResponse.enabled()) {
            enabled = false;
        }

        try {
            SMSChannelRequest request = SMSChannelRequest.builder()
                    .enabled(enabled)
                    .build();

            UpdateSmsChannelRequest updateRequest = UpdateSmsChannelRequest.builder()
                    .smsChannelRequest(request)
                    .applicationId(appId)
                    .build();

            UpdateSmsChannelResponse result = client.updateSmsChannel(updateRequest);

            System.out.println("Channel state: " + result.smsChannelResponse().enabled());
        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
    //snippet-end:[pinpoint.java2.updatechannel.main]

}
