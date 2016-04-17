package io.skysail.server.app.demo.timetable.course.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.timetable.course.Course;
import io.skysail.server.db.DbClassName;
import io.skysail.server.restlet.resources.ListServerResource;



public class CoursesResourceGen extends ListServerResource<Course> {

    private DemoApplication app;

    public CoursesResourceGen() {
        super(CourseResourceGen.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Courses");
    }

    @Override
    protected void doInit() {
        app = (DemoApplication) getApplication();
    }

    @Override
    public List<?> getEntity() {
       //return repository.find(new Filter(getRequest()));
        String sql = "SELECT from " + DbClassName.of(Course.class) + " WHERE #" + getAttribute("id") + " IN in('pages')";
        return null;//((SpaceRepository)app.getRepository(Space.class)).execute(Course.class, sql);   
    }

    public List<Link> getLinks() {
       return super.getLinks(PostCourseResource.class);
    }
}