package io.skysail.server.restlet.filter;

import io.skysail.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.restlet.response.Wrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityWasDeletedFilter<R extends SkysailServerResource<T>, T extends Identifiable> extends AbstractResourceFilter<R, T> {

    public EntityWasDeletedFilter(SkysailApplication skysailApplication) {
    }

    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        String infoMessage = resource.getClass().getSimpleName() + ".deleted.success";
        responseWrapper.addInfo(infoMessage);
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
