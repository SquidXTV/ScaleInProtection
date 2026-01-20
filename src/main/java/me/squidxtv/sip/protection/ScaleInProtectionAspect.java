package me.squidxtv.sip.protection;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ScaleInProtectionAspect {

    private final ScaleInProtectionManager protectionManager;

    public ScaleInProtectionAspect(ScaleInProtectionManager protectionManager) {
        this.protectionManager = protectionManager;
    }

    @Around("@annotation(ProtectedFromScaleIn)")
    public Object aroundProtectedMethod(ProceedingJoinPoint pjp) throws Throwable {
        protectionManager.enter();

        try {
            return pjp.proceed();
        } finally {
            protectionManager.exit();
        }
    }

}
