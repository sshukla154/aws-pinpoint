package com.frontier.notification.poc;
/* Created by Seemant Shukla on 18/10/21 */

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.*;

public class CreateCampaign {
    public static void main(String[] args) {

        String appId = "er123";
        String segmentId = "21321sdfda";
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

//            Message defaultMessage = Message.builder()
//                    .action(Action.OPEN_APP)
//                    .body("Hi, I am Message body !11")
//                    .title("Test Title | From Java")
//                    .build();

            CampaignEmailMessage campaignEmailMessage = CampaignEmailMessage.builder()
                    .fromAddress("er.seemantshukla@gmail.com")
                    .htmlBody("<!DOCTYPE html>\n    <html lang=\"en\">\n    <head>\n    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n</head>" +
                            "\n<body>\nHi {{User.UserAttributes.FirstName}}, \n" +
                            "\nCongratulations updating you age {{User.UserAttributes.age}} and Last Name {{User.UserAttributes.LastName}} !\n" +
                            "\n\n\n\nI am using PLACEHOLDER in this email</body>\n</html>")
                    .title("Test Email | Amazon Pinpoint Service | Java POC | Segment From UI")
                    .build();

            MessageConfiguration messageConfiguration = MessageConfiguration.builder()
//                    .defaultMessage(defaultMessage)
                    .emailMessage(campaignEmailMessage)
                    .build();

            WriteCampaignRequest request = WriteCampaignRequest.builder()
                    .description("This Campaign is created from Java - For POC purpose only")
                    .schedule(schedule)
                    .name("MyJavaCampaign")
                    .segmentId(segmentID)
                    .messageConfiguration(messageConfiguration)
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
