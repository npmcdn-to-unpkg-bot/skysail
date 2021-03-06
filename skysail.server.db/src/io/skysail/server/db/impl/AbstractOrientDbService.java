package io.skysail.server.db.impl;

import org.restlet.engine.util.StringUtils;

import com.orientechnologies.orient.core.exception.ODatabaseException;

import io.skysail.server.db.DbConfigurationProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractOrientDbService {

    static final String DB_URL = "db.url";
    static final String DEFAULT_DB_URL = "memory:skysail";
    static final String DEFAULT_DB_USERNAME = "admin";
    static final String DEFAULT_DB_PASSWORD = "admin";

    protected DbConfigurationProvider provider;
    protected boolean started = false;

    public void updated(DbConfigurationProvider provider) {
        this.provider = provider;
        if (provider == null || provider.getConfig() == null) {
            log.warn("DbConfigurationProvider was null or has null-config, stopping db");
            stopDb();
            return;
        }
        try {
            startDb();
            registerShutdownHook();
        } catch (ODatabaseException e) {
            if (!e.getMessage().startsWith("Database instance has been released to the pool.")) {
                throw e;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                log.error(e1.getMessage(), e1);
            }
        }
    }

    protected abstract void registerShutdownHook();

    protected abstract void startDb();

    protected abstract void stopDb();

    protected String getDbUrl() {
        if (provider == null || provider.getConfig() == null) {
            return DEFAULT_DB_URL;
        }
        String url = provider.getConfig().getUrl();
        if (url != null) {
            return url;
        }
        return DEFAULT_DB_URL;
    }

    protected String getDbUsername() {
        if (provider == null || provider.getConfig() == null) {
            log.warn("falling back to default username as provider is null");
            return DEFAULT_DB_USERNAME;
        }
        String username = provider.getConfig().getUsername();
        if (StringUtils.isNullOrEmpty(username)) {
            log.warn("falling back to default username as username is null or empty");
            return DEFAULT_DB_USERNAME;
        }
        return username;
    }

    protected String getDbPassword() {
        if (provider == null || provider.getConfig() == null) {
            log.warn("falling back to default password as provider is null");
            return DEFAULT_DB_PASSWORD;
        }
        String password = provider.getConfig().getPassword();
        if (StringUtils.isNullOrEmpty(password)) {
            log.warn("falling back to default password as password is null or empty");
            return DEFAULT_DB_PASSWORD;
        }
        return password;
    }

}
