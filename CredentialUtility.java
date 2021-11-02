package com.frontier.notification.poc;
/* Created by Seemant Shukla on 19/10/21 */

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

public class CredentialUtility {

    public static final String AWS_ACCESS_KEY = "";
    public static final String AWS_SECRET_KEY = "";

    public static AwsCredentialsProvider getCredentails(){
        AwsCredentialsProvider awsCredentialsProvider = () -> new AwsCredentials() {
            @Override
            public String accessKeyId() {
                return AWS_ACCESS_KEY;
            }

            @Override
            public String secretAccessKey() {
                return AWS_SECRET_KEY;
            }
        };
        return awsCredentialsProvider;
    }

}
