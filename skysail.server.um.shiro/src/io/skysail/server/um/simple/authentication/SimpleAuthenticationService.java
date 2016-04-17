package io.skysail.server.um.simple.authentication;

import io.skysail.api.um.AuthenticationService;
import io.skysail.server.um.simple.FileBasedUserManagementProvider;
import io.skysail.server.um.simple.SkysailCookieAuthenticator;
import io.skysail.server.utils.PasswordUtils;

import java.io.*;
import java.security.Principal;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.Subject;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.security.Authenticator;
import org.restlet.security.User;

@Slf4j
public class SimpleAuthenticationService implements AuthenticationService {

    private FileBasedUserManagementProvider provider;

    public SimpleAuthenticationService(FileBasedUserManagementProvider provider) {
        this.provider = provider;
    }

    @Override
    public Authenticator getApplicationAuthenticator(Context context) {
    	return getResourceAuthenticator(context);
    }

    @Override
    public Authenticator getResourceAuthenticator(Context context) {
        CacheManager cacheManager = null;
        if (provider != null) {
            cacheManager = this.provider.getCacheManager();
        } else {
            log.info("no cacheManager available in {}", this.getClass().getName());
        }
        // https://github.com/qwerky/DataVault/blob/master/src/qwerky/tools/datavault/DataVault.java
        return new SkysailCookieAuthenticator(context, "SKYSAIL_SHIRO_DB_REALM", "thisHasToBecomeM".getBytes(),
                cacheManager);
    }
    
    public Principal getPrincipal(Request request) {
    	Object principal = SecurityUtils.getSubject().getPrincipal();
    	return new Principal() {
			@Override
			public String getName() {
				return principal.toString();
			}
		};
    }



    public void updatePassword(User user, String newPassword) {
        validateUser(user);
        updateConfigFile(user, newPassword);
        //clearCache(user.getName());
        SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());
    }

    private void updateConfigFile(User user, String newPassword) {
        String bCryptHash = PasswordUtils.createBCryptHash(newPassword);
        String fileInstallDir = System.getProperty("felix.fileinstall.dir");
        String configFileName = fileInstallDir + java.io.File.separator
                + FileBasedUserManagementProvider.class.getName() + ".cfg";
        Properties properties = new Properties();

        File configFile = new File(configFileName);
        try {
            properties.load(new FileReader(configFileName));
            properties.replace(user.getName() + ".password", bCryptHash);
            properties.store(new FileWriter(configFile), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void validateUser(User user) {
        UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), user.getSecret());
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
    }

	@Override
	public boolean isAuthenticated(Request request) {
		return SecurityUtils.getSubject().isAuthenticated();
	}

}