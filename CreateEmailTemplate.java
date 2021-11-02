package com.frontier.notification.poc;
/* Created by Seemant Shukla on 30/10/21 */

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.CreateEmailTemplateRequest;
import software.amazon.awssdk.services.pinpoint.model.CreateEmailTemplateResponse;
import software.amazon.awssdk.services.pinpoint.model.EmailTemplateRequest;
import software.amazon.awssdk.services.pinpoint.model.PinpointException;

import java.util.HashMap;
import java.util.Map;

public class CreateEmailTemplate {

    public static void main(String[] args) {

        PinpointClient pinpoint = PinpointClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(CredentialUtility.getCredentails())
                .build();

        CreateEmailTemplateResponse response = createEmailTemplate(pinpoint);
        System.out.println("Created Template : " + response);
        pinpoint.close();
    }

    public static CreateEmailTemplateResponse createEmailTemplate(PinpointClient pinpointClient) {
        try {

            Map<String, String> tagMap = new HashMap<>();
            tagMap.put("Test", "Tag");

            //DEFAULT_SUBSTITUTIONS_FIELD, HTML_PART_FIELD, RECOMMENDER_ID_FIELD, SUBJECT_FIELD, TAGS_FIELD, TEMPLATE_DESCRIPTION_FIELD, TEXT_PART_FIELD
            EmailTemplateRequest emailTemplateRequest = EmailTemplateRequest.builder()
                    .htmlPart("I am HTML Part")
                    .subject("I am subject")
                    .tags(tagMap)
                    .templateDescription("I am template description")
                    .textPart("I am the text part")
                    .build();
            //EMAIL_TEMPLATE_REQUEST_FIELD, TEMPLATE_NAME_FIELD
            CreateEmailTemplateRequest createEmailTemplateRequest = CreateEmailTemplateRequest.builder()
                    .templateName("MyNewTemplate-1")
                    .emailTemplateRequest(emailTemplateRequest)
                    .build();

            CreateEmailTemplateResponse response = pinpointClient.createEmailTemplate(createEmailTemplateRequest);
        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return null;
    }

}
