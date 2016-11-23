package com.dynamic;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.configuration.xml.XMLConfigurer;
import net.sf.oval.guard.Guard;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class Application {
    public static void main(String ...args) throws IOException, InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        String generationPath = System.getProperty("generation.dir");
        String schemaPath = System.getProperty("schema.path");
        String className = System.getProperty("class.name");
        String validationSchemePath = System.getProperty("validation.schema.path");
        String command = String.format("xjc -d %s -p com.dynamic %s", generationPath, schemaPath);

        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        String pathToClass = String.format("%s/com/dynamic/%s.java", generationPath, className);
        compiler.run(null, null, null, new File(pathToClass).getPath());

        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class sysclass = URLClassLoader.class;

        try {
            Method method = sysclass.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(sysloader, new Object[]{new File(generationPath).toURI().toURL()});
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }

        Class<?> cls = sysloader.loadClass(String.format("com.dynamic.%s", className));
        Object instance = cls.newInstance();

        XMLConfigurer xmlConfigurer = new XMLConfigurer(new File(validationSchemePath));
        Guard guard = new Guard(xmlConfigurer);

        List<ConstraintViolation> errors = guard.validate(instance);
        System.out.println(errors);
    }
}
