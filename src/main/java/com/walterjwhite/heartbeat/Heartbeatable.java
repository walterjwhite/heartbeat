package com.walterjwhite.heartbeat;

import java.time.Duration;

public interface Heartbeatable {
  /**
   * Invoked every getHeartbeatInterval() until the original invocation ends either successfully or
   * on error.
   */
  void onHeartbeat();

  /**
   * The interval for which to invoke onHeartbeat().
   *
   * @return
   */
  Duration getHeartbeatInterval();
}
