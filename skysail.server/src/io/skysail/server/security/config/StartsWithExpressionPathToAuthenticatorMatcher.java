package io.skysail.server.security.config;

import io.skysail.server.app.ApiVersion;
import lombok.NonNull;
import lombok.ToString;

@ToString
public class StartsWithExpressionPathToAuthenticatorMatcher extends AbstractPathToAuthenticatorMatcher {

	private String startsWith;

	public StartsWithExpressionPathToAuthenticatorMatcher(ApiVersion apiVersion, @NonNull String startsWith) {
		this.startsWith = apiVersion == null ? startsWith : apiVersion.getVersionPath() + startsWith;
	}

	@Override
	public boolean match(@NonNull String path) {
		return path.startsWith(startsWith);
	}

}