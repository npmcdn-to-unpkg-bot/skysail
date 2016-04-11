package io.skysail.server.converter.wrapper;

import java.nio.charset.Charset;
import java.security.Principal;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import io.skysail.api.um.UserManagementProvider;
import io.skysail.server.restlet.resources.SkysailServerResource;

public class STUserWrapper {

    private static final String DEMO = "demo";

	private static final String ANONYMOUS = "anonymous";
	
    private UserManagementProvider userManagementProvider;
    private String peerName;
	private SkysailServerResource<?> resource;

    public STUserWrapper(UserManagementProvider ump, SkysailServerResource<?> resource, String peerName) {
        this.userManagementProvider = ump;
		this.resource = resource;
        this.peerName = peerName;
    }

    public Object getPrincipal() {
        Principal principal = userManagementProvider.getAuthenticationService().getPrincipal(resource.getRequest());
        return principal.getName().equals(ANONYMOUS) ? null : principal;
        
    }

    public Object getUsername() {
    	userManagementProvider.getAuthenticationService().getPrincipal(resource.getRequest());
    	String authorization = resource.getRequest().getHeaders().getFirstValue("Authorization");
    	
    	if (authorization != null && authorization.startsWith("Basic")) {
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));
            return credentials.split(":",2)[0];
    	}
        return ANONYMOUS;
    }

    public boolean isDeveloper() {
        return true;//subject.hasRole("developer");
    }

    public boolean isAdmin() {
        return true;//subject.hasRole("admin");
    }

    public boolean isDemoUser() {
        return ((String)getUsername()).equals(DEMO);
    }

    public String getBackend() {
        if (peerName == null || peerName.trim().length() == 0) {
            return "";
        }
        return "["+peerName+"] ";
    }

    public List<String> getPeers() {
    	return Collections.emptyList();
    }
}
