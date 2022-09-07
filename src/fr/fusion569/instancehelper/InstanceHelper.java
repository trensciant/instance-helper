package fr.fusion569.instancehelper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public final class InstanceHelper {

    private InstanceHelper() {
    }

    public static Object newInstance(URL urlClassFolder, String className, Class<?>[] constructorClassArguments, Object... constructorArguments) {
        final URLClassLoader classLoader;
        final Class<?> loadedClass;
        final Constructor<?> constructor;

        try {
            classLoader = new URLClassLoader(new URL[]{urlClassFolder});
            loadedClass = classLoader.loadClass(className);
            constructor = loadedClass.getDeclaredConstructor(constructorClassArguments);

            constructor.setAccessible(true);
            classLoader.close();
            return constructor.newInstance(constructorArguments);
        } catch(IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException |
                IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object newInstance(File classFolder, String className, Class<?>[] constructorClassArguments, Object... constructorArguments) {
        try {
            return newInstance(classFolder.toURI().toURL(), className, constructorClassArguments, constructorArguments);
        } catch(MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        final Object obj = newInstance(new File("C://Users/Papa/Desktop/"), "User", new Class[]{
                String.class, int.class
        }, "Paul", 15);
        final Field f = obj.getClass().getDeclaredField("age");

        f.setAccessible(true);
        System.out.println(f.get(obj));
    }
}
