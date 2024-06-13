package sample.server.aspect;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
public class TransactionLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(TransactionLoggingAspect.class);

    @Before("@annotation(transactional)")
    public void beforeTransaction(Transactional transactional) {
        logger.info("Transaction started");
    }

    @AfterReturning("@annotation(transactional)")
    public void afterTransactionCommit(Transactional transactional) {
        logger.info("Transaction committed");
    }

    @AfterThrowing(pointcut = "@annotation(transactional)", throwing = "ex")
    public void afterTransactionRollback(JoinPoint joinPoint, Transactional transactional, Exception ex) {
        String methodName = joinPoint.getSignature().toShortString();
        logger.error("Transaction rolled back for method: {}. Error: {}", methodName, ex.getMessage());
    }

    @After("@annotation(transactional) && execution(* *.*(..))")
    public void afterTransactionRollback(JoinPoint joinPoint, Transactional transactional) {
        String methodName = joinPoint.getSignature().toShortString();
        if (!(joinPoint.getSignature() instanceof MethodSignature)) {
            return;
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        if (!(methodSignature.getMethod().getExceptionTypes().length > 0)) {
            logger.info("Transaction rolled back for method: {}", methodName);
        }
    }
}