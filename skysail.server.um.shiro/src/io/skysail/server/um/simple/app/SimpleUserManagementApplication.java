package io.skysail.server.um.simple.app;

import java.util.Arrays;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.event.EventAdmin;

import io.skysail.api.um.RestletRolesProvider;
import io.skysail.server.ApplicationContextId;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;
import io.skysail.server.um.simple.app.users.resources.CurrentUserResource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true)
@Slf4j
public class SimpleUserManagementApplication extends SkysailApplication implements RestletRolesProvider, ApplicationProvider, MenuItemProvider {

    private static final String APP_NAME = "usermanagement";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;

    public SimpleUserManagementApplication() {
        super(APP_NAME);
        setDescription("Central User Configuration Application");
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/user.png");
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder.authorizeRequests().startsWithMatcher("/currentUser").permitAll();
    }

    @Override
    protected void attach() {
        super.attach();

        log.debug("attaching routes to UserManagementApplication #" + this.hashCode());

        router.setAuthorizationDefaults(anyOf("usermanagement.user", "admin"));

       // router.attach(new RouteBuilder("",  UsersResource.class).authorizeWith(anyOf("admin")));
       // router.attach(new RouteBuilder("/",  UsersResource.class).authorizeWith(anyOf("admin")));
//        router.attach(new RouteBuilder("/about",  UserManagementAboutResource.class));
//        router.attach(new RouteBuilder("/api", ApiResource.class));
//        router.attach(new RouteBuilder("/entities", EntitiesResource.class));
//        router.attach(new RouteBuilder("/entities/{name}", EntitiesResource.class));
//
//        router.attach(new RouteBuilder("/roles", RolesResource.class));

        router.attach(new RouteBuilder("/currentUser", CurrentUserResource.class));

//        router.attach(new RouteBuilder("/users", UsersResource.class).authorizeWith(anyOf("admin")));
//        router.attach(new RouteBuilder("/users/", PostUserResource.class).authorizeWith(anyOf("admin")));
//        router.attach(new RouteBuilder("/users/{username}", UserResource.class));
//        router.attach(new RouteBuilder("/users/{id}/", PutUserResource.class));
//        router.attach(new RouteBuilder("/users/{username}/groups", UserGroupsResource.class));
//        router.attach(new RouteBuilder("/users/{username}/password/", UserPasswordResource.class));
//
//        router.attach(new RouteBuilder("/groups", GroupsResource.class));
//        router.attach(new RouteBuilder("/groups/", GroupResource.class));
//        router.attach(new RouteBuilder("/groups/{group}", GroupResource.class));
//
//        router.attach(new RouteBuilder("/registrations", RegistrationsResource.class));
//        router.attach(new RouteBuilder("/registrations/", PostRegistrationResource.class).noAuthenticationNeeded());
//        router.attach(new RouteBuilder("/registrationsAdmin/{id}", RegistrationResource.class));
//        router.attach(new RouteBuilder("/registrationsAdmin/{id}/", PutRegistrationResource.class));
//
//        router.attach(new RouteBuilder("/registrations/{registrationId}", new ConfirmationRedirector(getContext()))
//                .noAuthenticationNeeded());
//        router.attach(new RouteBuilder("/registrations/{registrationId}/confirmation/", PostConfirmationResource.class)
//                .noAuthenticationNeeded());

    }

    @Override
    public List<MenuItem> getMenuEntries() {
        MenuItem menuItem = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath(), this);
        menuItem.setCategory(MenuItem.Category.ADMIN_MENU);
        return Arrays.asList(menuItem);
    }

}
