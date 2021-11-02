package com.frontier.notification.poc;
/* Created by Seemant Shukla on 18/10/21 */

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.*;

public class CreateCampaign_1 {
    public static void main(String[] args) {

        String appId = "dfesf123123";
        String segmentId = "12312312";
        PinpointClient pinpoint = PinpointClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(CredentialUtility.getCredentails())
                .build();

        createPinCampaign(pinpoint, appId, segmentId);
        pinpoint.close();
    }

    //snippet-start:[pinpoint.java2.createcampaign.main]
    public static void createPinCampaign(PinpointClient pinpoint, String appId, String segmentId) {

        CampaignResponse result = createCampaign(pinpoint, appId, segmentId);
        System.out.println("Campaign " + result.name() + " created.");
        System.out.println(result.description());

    }

    public static CampaignResponse createCampaign(PinpointClient client, String appID, String segmentID) {

        try {
            Schedule schedule = Schedule.builder()
                    .startTime("IMMEDIATE")
                    .frequency("ONCE")
                    .build();

            CampaignEmailMessage campaignEmailMessage = CampaignEmailMessage.builder()
                    .fromAddress("er.seemantshukla@gmail.com")
                    .build();

            MessageConfiguration messageConfiguration = MessageConfiguration.builder()
                    .emailMessage(campaignEmailMessage)
                    .build();

            TemplateConfiguration templateConfiguration = TemplateConfiguration.builder()
                    .emailTemplate(Template.builder().name("MyTemplate1").version(String.valueOf(1)).build())
                    .build();

            CampaignLimits limits = CampaignLimits.builder().daily(100).total(100).maximumDuration(60).messagesPerSecond(1).build();

            WriteCampaignRequest request = WriteCampaignRequest.builder()
                    .description("This Campaign is created from Java - For POC purpose only")
                    .schedule(schedule)
                    .name("MyJavaCampaign-UsingTemplate")
                    .segmentId(segmentID)
                    .messageConfiguration(messageConfiguration)
                    .templateConfiguration(templateConfiguration)
                    .limits(limits)
                    .build();

            CreateCampaignResponse result = client.createCampaign(
                    CreateCampaignRequest.builder()
                            .applicationId(appID)
                            .writeCampaignRequest(request).build()
            );

            System.out.println("Campaign ID: " + result.campaignResponse().id());

            return result.campaignResponse();

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

        return null;
    }
}
