package ascb.nivk.core.util;

import org.reflections.Reflections;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class InstanceManager<T> {

    private final List<T> instances = new ArrayList<>();

    public InstanceManager(Class<T> superclass, String packageSearch) {
        new Reflections(packageSearch).getSubTypesOf(superclass).forEach(clazz -> {
            try {
                instances.add(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public T getByClass(Class<? extends T> clazz) {
        return instances.stream().filter(instance -> instance.getClass().equals(clazz)).findFirst().orElse(null);
    }

    public Collection<T> getAll() {
        return Collections.unmodifiableCollection(instances);
    }

}
