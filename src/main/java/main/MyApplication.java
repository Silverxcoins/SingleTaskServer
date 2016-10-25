package main;

import controllers.*;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("singletask/api")
public class MyApplication extends Application {
    @Override
    public Set<Object> getSingletons() {
        final HashSet<Object> objects = new HashSet<>();
        objects.add(new UserController());
        objects.add(new CategoryController());
        objects.add(new TaskController());
        objects.add(new VariantController());
        objects.add(new TaskVariantController());
        return objects;
    }
}
