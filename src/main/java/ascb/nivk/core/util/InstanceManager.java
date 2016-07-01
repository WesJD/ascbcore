package ascb.nivk.core.util;

import org.reflections.Reflections;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class InstanceManager<T> {

    private final List<T> instances = new ArrayList<>();

    public InstanceManager(String packageSearch) {
        new Reflections(packageSearch).getSubTypesOf((Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]).forEach(clazz -> {
            try {
                instances.add(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public T getByClass(Class<? extends T> clazz) {
        return instances.stream().filter(AbstractSCBClass -> clazz.getClass().equals(clazz)).findFirst().orElse(null);
    }

    public Collection<T> getAll() {
        return Collections.unmodifiableCollection(instances);
    }

}
