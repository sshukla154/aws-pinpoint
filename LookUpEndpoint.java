package com.frontier.notification.poc;
/* Created by Seemant Shukla on 19/10/21 */
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.EndpointResponse;
import software.amazon.awssdk.services.pinpoint.model.GetEndpointResponse;
import software.amazon.awssdk.services.pinpoint.model.PinpointException;
import software.amazon.awssdk.services.pinpoint.model.GetEndpointRequest;

public class LookUpEndpoint {

        public static void main(String[] args) {

            String appId = "112";
            String endpoint = "ep-003";//Endpoint is segment id
            System.out.println("Looking up an endpoint point with ID: " + endpoint);

            PinpointClient pinpoint = PinpointClient.builder()
                    .region(Region.US_EAST_1)
                    .credentialsProvider(CredentialUtility.getCredentails())
                    .build();

            lookupPinpointEndpoint(pinpoint, appId, endpoint);
            pinpoint.close();
        }

        //snippet-start:[pinpoint.java2.lookup.main]
        public static void lookupPinpointEndpoint(PinpointClient pinpoint, String appId, String endpoint ) {

            try {
                GetEndpointRequest appRequest = GetEndpointRequest.builder()
                        .applicationId(appId)
                        .endpointId(endpoint)
                        .build();

                GetEndpointResponse result = pinpoint.getEndpoint(appRequest);
                EndpointResponse endResponse = result.endpointResponse();

                // Uses the Google Gson library to pretty print the endpoint JSON.
                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                        .setPrettyPrinting()
                        .create();

                String endpointJson = gson.toJson(endResponse);
                System.out.println(endpointJson);

            } catch (PinpointException e) {
                System.err.println(e.awsErrorDetails().errorMessage());
                System.exit(1);
            }
            System.out.println("Done");
        }

    }
