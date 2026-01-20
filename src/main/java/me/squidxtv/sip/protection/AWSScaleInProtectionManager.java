package me.squidxtv.sip.protection;

import me.squidxtv.sip.protection.aws.EC2InstanceProtection;
import org.springframework.stereotype.Component;

@Component
public class AWSScaleInProtectionManager implements ScaleInProtectionManager {

    private final EC2InstanceProtection ec2InstanceProtection;
    private int activeSections = 0;

    public AWSScaleInProtectionManager(EC2InstanceProtection ec2InstanceProtection) {
        this.ec2InstanceProtection = ec2InstanceProtection;
    }

    @Override
    public synchronized void enter() {
        if (activeSections == 0) {
            ec2InstanceProtection.setProtected(true);
        }

        activeSections++;
    }

    @Override
    public synchronized void exit() {
        if (activeSections <= 0) {
            throw new IllegalStateException("Exit called more times than enter.");
        }

        activeSections--;

        if (activeSections == 0) {
            ec2InstanceProtection.setProtected(false);
        }
    }

    @Override
    public synchronized int getActiveSections() {
        return activeSections;
    }
}
