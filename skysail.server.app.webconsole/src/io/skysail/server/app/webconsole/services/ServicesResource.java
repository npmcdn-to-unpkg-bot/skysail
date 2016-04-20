package io.skysail.server.app.webconsole.services;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.webconsole.BundleResource;
import io.skysail.server.app.webconsole.WebconsoleApplication;
import io.skysail.server.restlet.resources.ListServerResource;

public class ServicesResource extends ListServerResource<ServiceDescriptor> {

	public ServicesResource() {
		super(BundleResource.class);
		setDescription("returns the OSGi bundles of the framework");
	}
	
	@Override
	public List<ServiceDescriptor> getEntity() {
		return ((WebconsoleApplication)getApplication()).getOsgiServices();
	}
	
	@Override
	public List<Link> getLinks() {
		return super.getLinks(ServicesResource.class);
	}

}