package com.ripplestreet.notification.poc;
/* Created by Seemant Shukla on 30/10/21 */

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.*;

import java.util.HashMap;
import java.util.Map;

public class UpdateEmailTemplate {

    public static void main(String[] args) {

        PinpointClient pinpoint = PinpointClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(CredentialUtility.getCredentails())
                .build();

        UpdateEmailTemplateResponse response = updateEmailTemplate(pinpoint);
        System.out.println("Updated Template : " + response);
        pinpoint.close();
    }

    public static UpdateEmailTemplateResponse updateEmailTemplate(PinpointClient pinpointClient) {
        try {

            Map<String, String> tagMap = new HashMap<>();
            tagMap.put("Test", "Tag-1");

            //DEFAULT_SUBSTITUTIONS_FIELD, HTML_PART_FIELD, RECOMMENDER_ID_FIELD, SUBJECT_FIELD, TAGS_FIELD, TEMPLATE_DESCRIPTION_FIELD, TEXT_PART_FIELD
            EmailTemplateRequest emailTemplateRequest = EmailTemplateRequest.builder()
                    .htmlPart("I am HTML Part-1")
                    .subject("I am subject-1")
                    .tags(tagMap)
                    .templateDescription("I am template description-1")
                    .textPart("I am the text part-1")
                    .build();
            //EMAIL_TEMPLATE_REQUEST_FIELD, TEMPLATE_NAME_FIELD
//            CreateEmailTemplateRequest createEmailTemplateRequest = CreateEmailTemplateRequest.builder()
//                    .templateName("MyNewTemplate-1")
//                    .emailTemplateRequest(emailTemplateRequest)
//                    .build();

            UpdateEmailTemplateRequest updateEmailTemplateRequest = UpdateEmailTemplateRequest.builder()
                    .emailTemplateRequest(emailTemplateRequest)
                    .templateName("Birthday_Sweepstakes")
                    .build();

            UpdateEmailTemplateResponse response = pinpointClient.updateEmailTemplate(updateEmailTemplateRequest);
            return response;
        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }
        return null;
    }

}
