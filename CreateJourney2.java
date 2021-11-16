package com.ripplestreet.notification.poc;
/* Created by Seemant Shukla on 11/11/21 */

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CreateJourney2 {
    public static void main(String[] args) {

        PinpointClient pinpoint = PinpointClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(CredentialUtility.getCredentails())
                .build();

        String applicationId = "21009cac4e174273b9ccfd6b7446c2e5";
        String segmentId = "db9c49b35a5a471a87cd8f71f2dba389";

        CreateJourneyResponse response = getCreateJourneyResponse(pinpoint, applicationId, segmentId);

        System.out.println("Created Journey : " + response);
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
//                .endTime(Instant.parse("2021-12-05T12:59:45Z"))
//                .startTime(Instant.parse("2021-11-12T14:04:45Z"))
                .timezone("UTC+05:30")
                .build();

        QuietTime quiteTime = QuietTime.builder()
                .start("17:45")
                .end("19:45")
                .build();

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
                .filterType(FilterType.SYSTEM)
                .build();

        EventStartCondition eventStartCondition = EventStartCondition.builder()
                .eventFilter(eventFilter)
                .segmentId(segmentId)
                .build();

        //EventStartCondition OR SegmentStartCondition
        /**
         * In our case considering the journey is started by selecting SEGMENT from the list of segments
         * */
        StartCondition startCondition = StartCondition.builder()
                .description("Start Condition")
//                .eventStartCondition(eventStartCondition)
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
                        .templateName("Welcome_Template")
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
                .build();

        Activity thankyouEmailActivity = Activity.builder()
                .description("Thankyou Email Activity")
                .email(EmailMessageActivity.builder()
                        .templateName("Thankyou_Template")
                        .build())
                .build();

        Activity reminderEmailActivity = Activity.builder()
                .description("Reminder Email Activity")
                .email(EmailMessageActivity.builder().
                        templateName("Reminder_Template")
                        .build())
                .build();

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
                                                .messageActivity("FirstEmailActivity")
                                                .build())
                                        .build())
                                .operator(Operator.ANY)
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
                .localTime(false)
                .name("Schedule-Journey-POC-1")
//                .quietTime(quiteTime)
                .refreshFrequency(null)
                .schedule(journeySchedule)
                .startActivity("FirstEmailActivity")
                .startCondition(startCondition)
                .state(State.DRAFT)
                .build();
        return writeJourneyRequest;
    }
}
