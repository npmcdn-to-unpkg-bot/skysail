package io.skysail.server.app.webconsole.bundles.test;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Response;

import io.skysail.server.app.webconsole.WebconsoleApplication;
import io.skysail.server.app.webconsole.bundles.BundlesResource;
import io.skysail.server.app.webconsole.osgi.OsgiService;
import io.skysail.server.testsupport.ResourceTestBase2;

@RunWith(MockitoJUnitRunner.class)
public class BundlesResourceTest extends ResourceTestBase2 {

    @Mock
    private OsgiService osgiService;

    @Before
    public void setup() throws Exception {
        super.setUp(new WebconsoleApplication());

        resource = new BundlesResource();
        resource.setRequest(request);

        inject(WebconsoleApplication.class, "osgiService", osgiService);
    }

    @Test
    public void getEntity_delegates_to_osgiService() {
        resource.init(context, request, new Response(request));
        resource.getEntity();
        Mockito.verify(osgiService).getBundleDescriptors();
    }

    @Test
    public void getLinks_returns_values() {
        assertThat(resource.getLinks().size(), greaterThan(0));
    }

}
