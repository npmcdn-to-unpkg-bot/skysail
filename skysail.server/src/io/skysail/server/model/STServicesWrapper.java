package io.skysail.server.model;

import java.util.Set;

import io.skysail.api.search.SearchService;
import io.skysail.server.menus.*;
import io.skysail.server.menus.MenuItem.Category;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.MenuItemUtils;

public class STServicesWrapper {

    private Set<MenuItemProvider> menuProviders;
    private SkysailServerResource<?> resource;
    private SearchService searchService;

    public STServicesWrapper(Set<MenuItemProvider> menuProviders, SearchService searchService,
            SkysailServerResource<?> resource) {
        this.menuProviders = menuProviders;
        this.searchService = searchService;
        this.resource = resource;
    }

    public Set<MenuItem> getMainMenuItems() {
        return MenuItemUtils.getMenuItems(menuProviders, resource, MenuItem.Category.APPLICATION_MAIN_MENU);
    }

    public Set<MenuItem> getFrontendMenuItems() {
        return MenuItemUtils.getMenuItems(menuProviders, resource, MenuItem.Category.FRONTENDS_MAIN_MENU);
    }

    public Set<MenuItem> getDesignerAppMenuItems() {
        return MenuItemUtils.getMenuItems(menuProviders, resource, MenuItem.Category.DESIGNER_APP_MENU);
    }

    public Set<MenuItem> getAdminMenuItems() {
        Set<MenuItem> menuItems = MenuItemUtils.getMenuItems(menuProviders, resource, MenuItem.Category.ADMIN_MENU);
        menuItems.addAll(MenuItemUtils.getMenuItems(menuProviders, resource, Category.ADMIN_MAIN_MENU_INTERACTIVITY));
        return menuItems;
    }

    public Set<MenuItem> getDesignerAppItems() {
        return MenuItemUtils.getMenuItems(menuProviders, resource, MenuItem.Category.DESIGNER_APP_MENU);
    }

    public SearchService getSearchService() {
        return searchService;
    }



}
