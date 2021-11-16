package com.ripplestreet.notification.poc;
/* Created by Seemant Shukla on 11/11/21 */

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CreateJourney1 {
    public static void main(String[] args) {

        PinpointClient pinpoint = PinpointClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(CredentialUtility.getCredentails())
                .build();

        String applicationId = "21009cac4e174273b9ccfd6b7446c2e5";
        String segmentId = "db9c49b35a5a471a87cd8f71f2dba389";

        CreateJourneyResponse response = getCreateJourneyResponse(pinpoint, applicationId, segmentId);

        System.out.println("Updated Template : " + response);
        pinpoint.close();
    }

    private static CreateJourneyResponse getCreateJourneyResponse(PinpointClient pinpointClient, String applicationId, String segmentId) {

        WriteJourneyRequest writeJourneyRequest = getWriteJourneyRequest(segmentId);

        //APPLICATION_ID_FIELD, WRITE_JOURNEY_REQUEST_FIELD
        CreateJourneyRequest createJourneyRequest = CreateJourneyRequest.builder()
                .applicationId(applicationId)
                .writeJourneyRequest(writeJourneyRequest)
                .build();

        CreateJourneyResponse response = pinpointClient.createJourney(createJourneyRequest);
        return response;
    }

    private static WriteJourneyRequest getWriteJourneyRequest(String segmentId) {
        JourneyLimits journeyLimits = JourneyLimits.builder()
                .dailyCap(1)
                .endpointReentryCap(1)
                .messagesPerSecond(1)
                .build();

        JourneySchedule journeySchedule = JourneySchedule.builder()
                .endTime(Instant.parse("2023-05-05T12:59:45Z"))
                .startTime(Instant.parse("2021-11-12T14:04:45Z"))
                .timezone("UTC")
                .build();

//        QuietTime quiteTime = QuietTime.builder()
//                .start("")
//                .end("")
//                .build();

        //DIMENSION_TYPE_FIELD, VALUES_FIELD
        SetDimension setDimension = SetDimension.builder()
                .dimensionType(DimensionType.INCLUSIVE)
                .values("Hello")
                .build();

        Map<String, AttributeDimension> attributes = new HashMap<>();
        attributes.put("Attributes-1", AttributeDimension.builder()
                .attributeType(AttributeType.INCLUSIVE)
                .values("Value-1")
                .build());
        attributes.put("Attributes-2", AttributeDimension.builder()
                .attributeType(AttributeType.EXCLUSIVE)
                .values("Value-2")
                .build());

        Map<String, MetricDimension> metrics = new HashMap<>();
        metrics.put("Metric-1", MetricDimension.builder()
                .comparisonOperator("")
                .value(11.0)
                .build());
        metrics.put("Metric-2", MetricDimension.builder()
                .comparisonOperator("")
                .value(10.0)
                .build());

        EventDimensions eventDimensions = EventDimensions.builder()
                .attributes(attributes)
                .eventType(setDimension)
                .metrics(metrics)
                .build();

        EventFilter eventFilter = EventFilter.builder()
                .dimensions(eventDimensions)
                .filterType(FilterType.ENDPOINT)
                .build();

        EventStartCondition eventStartCondition = EventStartCondition.builder()
                .eventFilter(eventFilter)
                .segmentId(segmentId)
                .build();

        StartCondition startCondition = StartCondition.builder()
                .description("Start Condition")
                //.eventStartCondition(eventStartCondition)
                .segmentStartCondition(SegmentCondition.builder()
                        .segmentId(segmentId)
                        .build())
                .build();

        Activity firstEmailActivity = Activity.builder()
                .description("First Email Activity")
                .email(EmailMessageActivity.builder()
                        .messageConfig(JourneyEmailMessage.builder()
                                .fromAddress("er.seemantshukla@gmail.com")
                                .build())
                        .nextActivity("WaitActivity")
                        .templateName("MyTemplate2")
                        .build())
                .holdout(HoldoutActivity.builder()
                        .percentage(100)
                        .nextActivity("WaitActivity")
                        .build())
                .build();

        Activity waitActivity = Activity.builder()
                .description("Wait Activity")
                .waitValue(WaitActivity.builder()
                        .nextActivity("ConditionalSplitActivity")
                        .waitTime(WaitTime.builder()
                                .waitFor("PT1H")
                                .build())
                        .build())
                .holdout(HoldoutActivity.builder()
                        .percentage(100)
                        .nextActivity("ConditionalSplitActivity")
                        .build())
                .build();

        Activity thankyouEmailActivity = Activity.builder()
                .description("Thankyou Email Activity")
                .email(EmailMessageActivity.builder()
                        .templateName("MyNewTemplate-1")
                        .build())
                .build();

        Activity reminderEmailActivity = Activity.builder()
                .description("Thankyou Email Activity")
                .email(EmailMessageActivity.builder().
                        templateName("CreateTemplate-Test-1")
                        .build())
                .build();

//        Activity messageActivity = Activity.builder()
//                .email(EmailMessageActivity.builder().
//                        templateName("CreateTemplate-Test-1")
//                        .build())
//                .build();

        Activity conditionalSplitActivity = Activity.builder()
                .description("Conditional Split Activity")
                .conditionalSplit(ConditionalSplitActivity.builder()
                        .condition(Condition.builder()
                                .conditions(SimpleCondition.builder()
                                        .eventCondition(EventCondition.builder()
                                                .dimensions(EventDimensions.builder()
                                                        .eventType(SetDimension.builder()
                                                                .dimensionType(DimensionType.INCLUSIVE)
                                                                .values("_email.open")
                                                                .build())
                                                        .metrics(metrics)
                                                        .attributes(attributes)
                                                        .build())
                                                .messageActivity("MessageActivity")
                                                .build())
                                        .build())
                                .build())
                        .trueActivity("ThankyouEmailActivity")
                        .falseActivity("ReminderEmailActivity")
                        .build())
                .build();

        Map<String, Activity> activities = new HashMap<>();
        activities.put("FirstEmailActivity", firstEmailActivity);
        activities.put("WaitActivity", waitActivity);
        activities.put("ConditionalSplitActivity", conditionalSplitActivity);
        activities.put("ThankyouEmailActivity", thankyouEmailActivity);
        activities.put("ReminderEmailActivity", reminderEmailActivity);

        WriteJourneyRequest writeJourneyRequest = WriteJourneyRequest.builder()
                .activities(activities)
                .creationDate(String.valueOf(LocalDate.now()))
                .lastModifiedDate(String.valueOf(LocalDate.now()))
                .limits(journeyLimits)
                .localTime(true)
                .name("My-Test-Journey-POC")
                //.quietTime(quiteTime)
                .refreshFrequency(null)
                .schedule(journeySchedule)
                .startActivity("FirstEmailActivity")
                .startCondition(startCondition)
                .state(State.DRAFT)
                .build();
        return writeJourneyRequest;
    }
}
