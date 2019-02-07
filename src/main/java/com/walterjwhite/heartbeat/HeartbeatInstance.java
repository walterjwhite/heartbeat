package com.walterjwhite.heartbeat;

import java.util.concurrent.*;
import org.aspectj.lang.ProceedingJoinPoint;

/** Method invocation that is to be time constrained. */
public class HeartbeatInstance {
  private static final transient ScheduledExecutorService HEARTBEAT_EXECUTOR_SERVICE =
      Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2 - 1);

  protected final transient ProceedingJoinPoint proceedingJoinPoint;

  protected final Heartbeatable heartbeatable;

  public HeartbeatInstance(final ProceedingJoinPoint proceedingJoinPoint) {
    super();

    this.proceedingJoinPoint = proceedingJoinPoint;
    this.heartbeatable = (Heartbeatable) proceedingJoinPoint.getTarget();
  }

  public Object call() throws Throwable {
    final ScheduledFuture future = scheduleHeartbeat();
    try {
      return proceedingJoinPoint.proceed();
    } finally {
      cancelHeartbeat(future);
    }
  }

  /**
   * Attempts to cancel the interruption of the target method.
   *
   * @param future the future to cancel
   * @return false if the task could not be cancelled
   */
  protected boolean cancelHeartbeat(ScheduledFuture future) {
    if (!future.isCancelled() && !future.isDone()) {
      return future.cancel(true);
    }

    return true;
  }

  /**
   * Schedules the heartbeat with an initial delay and period to be the same value.
   *
   * @return the future queuedJob to cancel the current invocation
   */
  protected ScheduledFuture scheduleHeartbeat() {
    return HEARTBEAT_EXECUTOR_SERVICE.scheduleAtFixedRate(
        new HeartbeatRunnable(),
        heartbeatable.getHeartbeatInterval().getSeconds(),
        heartbeatable.getHeartbeatInterval().getSeconds(),
        TimeUnit.SECONDS);
  }

  private class HeartbeatRunnable implements Runnable {
    @Override
    public void run() {
      heartbeatable.onHeartbeat();
    }
  }
}
