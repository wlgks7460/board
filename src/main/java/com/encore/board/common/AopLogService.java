package com.encore.board.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class AopLogService {

//    aop의 대상이 되는 controller, service 등을 정의
//    @Pointcut("excution(* com.encore.board..controller..*.*(..))")
    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void controllerPointcut(){

    }

////    방식 1 . before, after 사용
//    @Before("controllerPointcut()")
//    public void beforeController(JoinPoint joinPoint){
//    log.info("Before Controller");
//    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//    HttpServletRequest req = servletRequestAttributes.getRequest();
//    ObjectMapper objectMapper = new ObjectMapper();
//    ObjectNode objectNode = objectMapper.createObjectNode();
//    objectNode.put("Method Name", joinPoint.getSignature().getName());
//    objectNode.put("CRUD Name", req.getMethod());
//    Map<String, String[]> paramMap = req.getParameterMap();
//    ObjectNode objectNodeDetail = objectMapper.valueToTree(paramMap);
//    objectNode.set("user inputs", objectNodeDetail);
//    log.info("user request info" + objectNode);
//
////        매서드가 실행되기 전에 인증, 입력값 검증등을 수행하는 용도로 사용하는 사전 단계
//    }
//
//    @After("controllerPointcut()")
//    public void afterController(){
//        log.info("end controller");
//    }

//    방식 2. Around 사용 (가장 빈번히 사용)

    @Around("controllerPointcut()")
//    join point란 aop 대상으로 하는 컨ㄴ트롤러의 특정 매서드를 의미
    public Object controllerLogger(ProceedingJoinPoint proceedingJoinPoint){

//        log.info("request method" + proceedingJoinPoint.getSignature().toString());

//        사용자의 요청값을 출력하기 위해 httpServletRequest 객체를 꺼내는 로직
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = servletRequestAttributes.getRequest();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("Method Name", proceedingJoinPoint.getSignature().getName());
        objectNode.put("CRUD Name", req.getMethod());
        Map<String, String[]> paramMap = req.getParameterMap();
        ObjectNode objectNodeDetail = objectMapper.valueToTree(paramMap);
        objectNode.set("user inputs", objectNodeDetail);
        log.info("user request info" + objectNode);

        try{
//            본래의 컨트롤러 매서드 호출하는 부분.
            return proceedingJoinPoint.proceed();
        }catch (Throwable e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            log.info("end controller");
        }

    }

}
