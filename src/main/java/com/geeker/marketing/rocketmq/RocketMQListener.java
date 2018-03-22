package com.geeker.marketing.rocketmq;

import org.springframework.context.event.EventListener;

import java.lang.annotation.*;

/**
 * 
 * @author jiangjb
 *
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EventListener
public @interface RocketMQListener {
	
	String topic();
	
	String tag();
	
	String condition() default "";
}
