package me.squidxtv.sip.web;

import me.squidxtv.sip.protection.AWSScaleInProtectionManager;
import me.squidxtv.sip.protection.ProtectedFromScaleIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FakeJobController {

    private static final Logger log = LoggerFactory.getLogger(FakeJobController.class);

    private final AWSScaleInProtectionManager protectionManager;

    public FakeJobController(AWSScaleInProtectionManager protectionManager) {
        this.protectionManager = protectionManager;
    }

    @GetMapping("/protect")
    @ProtectedFromScaleIn
    public String fakeProtectedJob(@RequestParam(defaultValue = "60000") long duration) throws InterruptedException {
        log.info("Starting fake job for {} ms. Currently {} sections active.", duration, protectionManager.getActiveSections());

        Thread.sleep(duration);

        log.info("Finished fake job after {} ms. Currently {} sections active.", duration, protectionManager.getActiveSections());
        return "Job completed in " + duration + " ms.";
    }

}
