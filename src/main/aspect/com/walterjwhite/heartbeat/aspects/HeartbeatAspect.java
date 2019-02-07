package com.walterjwhite.heartbeat;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Heartbeat enforcer, let's public methods run for 1s (by default, unless otherwise specified) then
 * interrupts them.
 */
@Aspect
public class HeartbeatAspect {
  @Around(
          "execution(* *(..)) && @annotation(com.walterjwhite.heartbeat.annotation.Heartbeat)")
  public Object doHeartbeat(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    return new HeartbeatInstance(proceedingJoinPoint).call();
  }
}
