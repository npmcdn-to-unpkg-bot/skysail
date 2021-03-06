package io.skysail.server.app.ref.singleentity.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.ref.singleentity.Account;
import io.skysail.server.app.ref.singleentity.SingleEntityApplication;
import io.skysail.server.app.ref.singleentity.SingleEntityRepository;
import io.skysail.server.restlet.resources.EntityServerResource;

public class AccountResource extends EntityServerResource<Account> {

    private String id;
    private SingleEntityApplication app;
    private SingleEntityRepository repository;

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (SingleEntityApplication) getApplication();
        repository = (SingleEntityRepository) app.getRepository(Account.class);
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutAccountResource.class);
    }

    @Override
    public Account getEntity() {
        return (Account) app.getRepository(Account.class).findOne(id);
    }

}
