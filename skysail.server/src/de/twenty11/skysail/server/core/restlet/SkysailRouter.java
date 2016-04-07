package de.twenty11.skysail.server.core.restlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.restlet.Restlet;
import org.restlet.ext.raml.RamlSpecificationRestlet;
import org.restlet.resource.Finder;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Filter;
import org.restlet.routing.Router;
import org.restlet.routing.TemplateRoute;
import org.restlet.security.Authenticator;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;

import com.google.common.base.Predicate;

import io.skysail.api.um.AuthenticatorProvider;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.EntityFactory;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkysailRouter extends Router {

    private Map<String, RouteBuilder> pathRouteBuilderMap = new ConcurrentHashMap<String, RouteBuilder>();

    private Predicate<String[]> defaultRolesPredicate;

    private ApiVersion apiVersion;

    private SkysailApplication skysailApplication;

    public SkysailRouter(SkysailApplication skysailApplication) {
        super(skysailApplication.getContext());
        this.skysailApplication = skysailApplication;
        
        RamlSpecificationRestlet ramlSpecificationRestlet = skysailApplication.getRamlSpecificationRestlet(getContext());
        skysailApplication.attachRamlDocumentationRestlet(this, "/v1/raml", ramlSpecificationRestlet);
    }

    public TemplateRoute attach(String pathTemplate, Class<? extends ServerResource> targetClass) {
        log.warn("please use a RouteBuilder to attach this resource: {}", targetClass);
        return attach(pathTemplate, createFinder(targetClass));
    }

    public void attach(RouteBuilder routeBuilder) {

        String pathTemplate = routeBuilder.getPathTemplate(apiVersion);
        pathRouteBuilderMap.put(pathTemplate, routeBuilder);
        if (routeBuilder.getTargetClass() == null) {
            attachForTargetClassNull(routeBuilder);
            return;
        }

        if (ListServerResource.class.isAssignableFrom(routeBuilder.getTargetClass())) {
            String metadataPath = pathTemplate + "!meta";
            RouteBuilder metaRouteBuilder = new RouteBuilder(metadataPath, routeBuilder.getTargetClass());
            log.info("routing path '{}' -> '{}' -> '{}'", metadataPath, "RolesPredicateAuthorizer", metaRouteBuilder
                    .getTargetClass().getName());
            attach(metadataPath, createIsAuthenticatedAuthorizer(metaRouteBuilder));
        }

        if (!routeBuilder.needsAuthentication()) {
            attachForNoAuthenticationNeeded(routeBuilder);
            return;
        }
        Restlet isAuthenticatedAuthorizer = createIsAuthenticatedAuthorizer(routeBuilder);
        log.info("routing path '{}' -> '{}' -> '{}'", pathTemplate, "RolesPredicateAuthorizer", routeBuilder
                .getTargetClass().getName());
        attach(pathTemplate, isAuthenticatedAuthorizer);

        updateApplicationModel(routeBuilder);
    }

    private void updateApplicationModel(RouteBuilder routeBuilder) {
        ApplicationModel applicationModel = skysailApplication.getApplicationModel();
        if (applicationModel == null) {
        	log.warn("applicationModel is null");
        	return;
        }

        Class<? extends ServerResource> targetClass = routeBuilder.getTargetClass();
        if (targetClass != null) {
            try {
                SkysailServerResource<?> resourceInstance = (SkysailServerResource<?>) targetClass.newInstance();
                Class<? extends Identifiable> parameterizedType = getResourcesGenericType(resourceInstance);
                applicationModel.addOnce(EntityFactory.createFrom(parameterizedType));
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            log.warn("targetClass was null");
        }
    }

    public void detachAll() {
        getRoutes().clear();
    }

    public RouteBuilder getRouteBuilder(String pathTemplate) {
        return pathRouteBuilderMap.get(pathTemplate);
    }

    public Map<String, RouteBuilder> getRouteBuilders() {
        return Collections.unmodifiableMap(pathRouteBuilderMap);
    }

    /**
     * provides, for a given skysail server resource, the path templates the
     * resource was attached to.
     *
     * @param cls
     * @return List of path templates
     */
    // TODO maybe use getRouteBuildersForResource instead?
    public List<String> getTemplatePathForResource(Class<? extends ServerResource> cls) {
        List<String> result = new ArrayList<>();
        for (Entry<String, RouteBuilder> entries : pathRouteBuilderMap.entrySet()) {
            if (entries.getValue() == null || entries.getValue().getTargetClass() == null) {
                continue;
            }
            if (entries.getValue().getTargetClass().equals(cls)) {
                result.add(entries.getKey());
            }
        }
        return result;
    }

    public List<RouteBuilder> getRouteBuildersForResource(Class<?> cls) {
        List<RouteBuilder> result = new ArrayList<>();
        for (Entry<String, RouteBuilder> entry : pathRouteBuilderMap.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            if (entry.getValue().getTargetClass() == null) {
                Restlet restlet = entry.getValue().getRestlet();
                if (restlet == null) {
                    continue;
                }
                handleRestlet(cls, result, entry, restlet);
                continue;
            }
            if (entry.getValue().getTargetClass().equals(cls)) {
                result.add(entry.getValue());
            }
        }
        return result;
    }

    private void handleRestlet(Class<?> cls, List<RouteBuilder> result, Entry<String, RouteBuilder> entries,
            Restlet restlet) {
        if (restlet instanceof Filter) {
            Restlet next = ((Filter) restlet).getNext();
            if (next == null) {
                return;
            }
            if (next.getClass().equals(cls)) {
                result.add(entries.getValue());
                return;
            }
            handleRestlet(cls, result, entries, next);
        } else if (restlet instanceof Finder) {
            Class<? extends ServerResource> targetClass = ((Finder) restlet).getTargetClass();
            if (targetClass == null) {
                return;
            }
            if (targetClass.equals(cls)) {
                result.add(entries.getValue());
                return;
            }

        }
    }

    public Map<String, RouteBuilder> getRoutesMap() {
        return Collections.unmodifiableMap(pathRouteBuilderMap);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (pathRouteBuilderMap != null) {
            for (String key : pathRouteBuilderMap.keySet()) {
                sb.append(key).append(": ").append(pathRouteBuilderMap.get(key)).append("\n");
            }
        }
        return sb.toString();
    }

    public void setAuthorizationDefaults(Predicate<String[]> predicate) {
        this.defaultRolesPredicate = predicate;

    }

    private Restlet createIsAuthenticatedAuthorizer(RouteBuilder routeBuilder) {
        Predicate<String[]> predicateToUse = routeBuilder.getRolesForAuthorization() == null ? defaultRolesPredicate
                : routeBuilder.getRolesForAuthorization();
        routeBuilder.authorizeWith(predicateToUse);

        RolesPredicateAuthorizer authorizer = new RolesPredicateAuthorizer(predicateToUse);
        authorizer.setContext(getContext());
        authorizer.setNext(routeBuilder.getTargetClass());
        
        //if (routeBuilder.getAuthenticator() != null) {
        	//Authenticator authenticator = routeBuilder.getAuthenticator();
        	
        AuthenticatorProvider authenticatorProvider = skysailApplication.getAuthenticatorProvider();
        Authenticator authenticator = authenticatorProvider.getAuthenticator(skysailApplication.getContext());
        	MapVerifier verifier = new MapVerifier();
        	verifier.getLocalSecrets().put("user", "pass".toCharArray());
        	((ChallengeAuthenticator)authenticator).setVerifier(verifier);
        	
        	authenticator.setNext(authorizer);
        	return authenticator;
//        }
//        
//        
//        Authorizer isAuthenticatedAuthorizer = new AuthenticatedAuthorizer();
//        isAuthenticatedAuthorizer.setNext(authorizer);
//        return isAuthenticatedAuthorizer;
    }

    private void attachForNoAuthenticationNeeded(RouteBuilder routeBuilder) {
        log.info("routing path '{}' -> '{}'", routeBuilder.getPathTemplate(apiVersion), routeBuilder.getTargetClass()
                .getName());
        attach(routeBuilder.getPathTemplate(apiVersion), routeBuilder.getTargetClass());
    }

    private void attachForTargetClassNull(RouteBuilder routeBuilder) {
        Restlet restlet = routeBuilder.getRestlet();
        if (restlet == null) {
            throw new IllegalStateException("RouteBuilder with neither TargetClass nor Restlet defined!");
        }
        log.info("routing path '{}' -> Restlet '{}'", routeBuilder.getPathTemplate(apiVersion), restlet.getClass()
                .getSimpleName());
        restlet.setContext(getContext());
        attach(routeBuilder.getPathTemplate(apiVersion), restlet);
        updateApplicationModel(routeBuilder);

    }

    public void setApiVersion(ApiVersion apiVersion) {
        this.apiVersion = apiVersion;
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Identifiable> getResourcesGenericType(SkysailServerResource<?> resourceInstance) {
        return (Class<? extends Identifiable>) resourceInstance.getParameterizedType();
    }


}
