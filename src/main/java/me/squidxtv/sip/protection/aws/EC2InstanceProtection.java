package me.squidxtv.sip.protection.aws;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.imds.Ec2MetadataClient;
import software.amazon.awssdk.imds.Ec2MetadataResponse;
import software.amazon.awssdk.services.autoscaling.AutoScalingClient;
import software.amazon.awssdk.services.autoscaling.model.DescribeAutoScalingInstancesRequest;
import software.amazon.awssdk.services.autoscaling.model.DescribeAutoScalingInstancesResponse;
import software.amazon.awssdk.services.autoscaling.model.SetInstanceProtectionRequest;

@Component
public class EC2InstanceProtection {

    private final AutoScalingClient asgClient;
    private final Ec2MetadataClient imdsClient;

    private final String instanceId;
    private final String groupName;

    public EC2InstanceProtection() {
        this(AutoScalingClient.create(), Ec2MetadataClient.create());
    }

    public EC2InstanceProtection(AutoScalingClient asgClient, Ec2MetadataClient imdsClient) {
        this.asgClient = asgClient;
        this.imdsClient = imdsClient;
        this.instanceId = fetchInstanceId();
        this.groupName = fetchAutoScalingGroupName(instanceId);
    }

    public void setProtected(boolean protectedFromScaleIn) {
        SetInstanceProtectionRequest request = SetInstanceProtectionRequest.builder()
                .autoScalingGroupName(groupName)
                .instanceIds(instanceId)
                .protectedFromScaleIn(protectedFromScaleIn)
                .build();

        asgClient.setInstanceProtection(request);
    }

    private String fetchInstanceId() {
        Ec2MetadataResponse response = imdsClient.get("/latest/meta-data/instance-id");
        return response.asString().trim();
    }

    private String fetchAutoScalingGroupName(String instanceId) {
        DescribeAutoScalingInstancesRequest request = DescribeAutoScalingInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

        DescribeAutoScalingInstancesResponse response = asgClient.describeAutoScalingInstances(request);

        return response.autoScalingInstances().stream()
                .findFirst()
                .orElseThrow()
                .autoScalingGroupName();
    }

}
