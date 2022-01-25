package ru.job4j.chat.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PatchUtil {

    private PatchUtil() {

    }

    public static <T> T patch(T requested, T original) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = original.getClass().getDeclaredMethods();
        Map<String, Method> namePerMethod = new HashMap<>();
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namePerMethod.put(name, method);
            }
        }
        for (String name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                Method getMethod = namePerMethod.get(name);
                Method setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new IllegalArgumentException("Invalid properties mapping");
                }
                Object newValue = getMethod.invoke(requested);
                if (newValue != null) {
                    setMethod.invoke(original, newValue);
                }
            }
        }
        return original;
    }
}
