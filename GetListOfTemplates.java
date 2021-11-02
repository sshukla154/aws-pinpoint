package com.notification.poc;
/* Created by Seemant Shukla on 25/10/21 */

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.*;

public class GetListOfTemplates {
    public static void main(String[] args) {

        String templateType = TemplateType.EMAIL.toString();
        PinpointClient pinpoint = PinpointClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(CredentialUtility.getCredentails())
                .build();

        TemplatesResponse listOfTemplates = getListOfTemplates(pinpoint, templateType);

        System.out.println("Templates List : " + listOfTemplates);
        pinpoint.close();
    }

    private static TemplatesResponse getListOfTemplates(PinpointClient client, String templateType) {

        try {
            ListTemplatesRequest listOfTemplates = ListTemplatesRequest.builder().templateType(templateType).build();

            TemplatesResponse listTemplatesResponse =  client.listTemplates(listOfTemplates).templatesResponse();
            return listTemplatesResponse;

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

}
