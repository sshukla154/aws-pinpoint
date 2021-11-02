package com.frontier.notification.poc;
/* Created by Seemant Shukla on 25/10/21 */

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.*;

public class GetTemplateByName {
    public static void main(String[] args) {

        String templateName = "MyNewTemplate-1";
        PinpointClient pinpoint = PinpointClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(CredentialUtility.getCredentails())
                .build();

        EmailTemplateResponse templateResponse = getTemplateByName(pinpoint, templateName);

        System.out.println("Response : " + templateResponse);
        pinpoint.close();
    }

    private static EmailTemplateResponse getTemplateByName(PinpointClient client, String templateName) {

        try {
            EmailTemplateResponse response = client.getEmailTemplate(GetEmailTemplateRequest.builder()
                    .templateName(templateName)
                    .build()).
                    emailTemplateResponse();
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
