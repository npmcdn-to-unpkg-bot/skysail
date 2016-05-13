package io.skysail.api.um;

import java.util.Set;

import org.osgi.annotation.versioning.ProviderType;
import org.restlet.security.Role;

/**
 * Implementors of this interface are able to determine a set of roles for a
 * given username, e.g. by looking up the user associated with the username in a
 * database and returning the set of assigned roles for that user.
 *
 * Based on the assigned roles the framework will decide if a users request to a
 * given resource should be authorized.
 *
 */
@ProviderType
@FunctionalInterface
public interface AuthorizationService {

    Set<Role> getRolesFor(String username);

}
