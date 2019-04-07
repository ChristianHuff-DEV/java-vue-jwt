package io.betweendata.auth.user;

import com.codahale.metrics.health.HealthCheck;

/**
 * Dummy health check just for demonstration purposes.
 */
public class ExampleHealthCheck extends HealthCheck {

	public ExampleHealthCheck() {

	}

	@Override
	protected Result check() throws Exception {
		return Result.healthy();
	}

}
