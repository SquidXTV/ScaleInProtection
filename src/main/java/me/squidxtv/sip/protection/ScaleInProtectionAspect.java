package me.squidxtv.sip.protection;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ScaleInProtectionAspect {

    private static final Logger log = LoggerFactory.getLogger(ScaleInProtectionAspect.class);

    private final ScaleInProtectionManager protectionManager;

    public ScaleInProtectionAspect(ScaleInProtectionManager protectionManager) {
        this.protectionManager = protectionManager;
    }

    @Around("@annotation(ProtectedFromScaleIn)")
    public Object aroundProtectedMethod(ProceedingJoinPoint pjp) throws Throwable {
        log.info("Entering aspect");
        protectionManager.enter();

        try {
            return pjp.proceed();
        } finally {
            protectionManager.exit();
            log.info("Exiting aspect");
        }
    }

}
