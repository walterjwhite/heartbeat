package com.walterjwhite.heartbeat;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Allows methods to do some sort of "heartbeat" activity while they're running to ensure other
 * systems know they're still running during long-running tasks.
 */
@Aspect
public class HeartbeatAspect {
  @Around(
      "execution(* *(..)) && @annotation(com.walterjwhite.heartbeat.annotation.Heartbeat) && !within(com.walterjwhite.heartbeat.*)")
  public Object doHeartbeat(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    return new HeartbeatInstance(proceedingJoinPoint).call();
  }
}
