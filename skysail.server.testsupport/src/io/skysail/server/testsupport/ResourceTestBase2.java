package io.skysail.server.testsupport;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.data.Reference;
import org.restlet.security.Authenticator;

import io.skysail.api.um.AuthenticationService;
import io.skysail.server.app.ServiceListProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.SkysailServerResource;

public class ResourceTestBase2 {

	@Mock
	protected ServiceListProvider serviceListProvider;
	
	@Mock
	protected AuthenticationService authService;
	
	@Mock
	protected Authenticator authenticator;

	@Mock
	protected Request request;

	@Mock
	protected Reference resourceRef;

	protected ConcurrentMap<String, Object> requestAttributes;

	protected Context context;

	protected SkysailApplication application;
	
	protected SkysailServerResource<?> resource;


	public void setUp(SkysailApplication app) {
		this.application = app;
		context = new Context();
		Mockito.when(authService.getApplicationAuthenticator(context)).thenReturn(authenticator);
		Mockito.when(serviceListProvider.getAuthenticationService()).thenReturn(authService);
		requestAttributes = new ConcurrentHashMap<>();
		SkysailApplication.setServiceListProvider(serviceListProvider);
		application.setContext(context);
		application.createInboundRoot();

		org.restlet.Application.setCurrent(application);

		Mockito.when(request.getResourceRef()).thenReturn(resourceRef);
		Mockito.when(request.getAttributes()).thenReturn(requestAttributes);

	}

	protected void inject(Class<?> cls, String fieldName, Object service) throws NoSuchFieldException, IllegalAccessException {
		Field field = cls.getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(application, service);
	}

}