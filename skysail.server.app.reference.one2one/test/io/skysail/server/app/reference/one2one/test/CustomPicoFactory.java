package io.skysail.server.app.reference.one2one.test;

import cucumber.runtime.java.picocontainer.PicoFactory;

public class CustomPicoFactory extends PicoFactory {
    public CustomPicoFactory() {
        addClass(DomainAutomationApi.class);
    }
}
