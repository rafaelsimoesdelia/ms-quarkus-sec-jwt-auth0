package org.acme.security.healthcheck;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HealthResource {

    @Liveness
    public HealthCheck checkLiveness() {
	return () -> HealthCheckResponse.up("Application is alive");
    }

    @Readiness
    public HealthCheck checkReadiness() {
	return () -> HealthCheckResponse.up("Application is ready");
    }
}
