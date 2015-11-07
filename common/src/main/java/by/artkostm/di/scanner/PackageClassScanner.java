package by.artkostm.di.scanner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PackageClassScanner implements ClassScanner<String>
{

    @Override
    public Set<Class<?>> scan(String... packageNames)
    {
        if (packageNames == null || packageNames.length == 0)
        {
            throw new IllegalStateException("Package names can't be null or empty");
        }
        final Set<Class<?>> classes = new HashSet<>();
        for (final String packageName : packageNames)
        {
            try
            {
                classes.addAll(getClasses(packageName));
            }
            catch (ClassNotFoundException | IOException e)
            {
                throw new RuntimeException("Cannot get classes from " + packageName + "package.", e);
            }
        }
        return classes;
    }
    
    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private static Set<Class<?>> getClasses(String packageName) 
            throws IOException, ClassNotFoundException
    {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        final String path = packageName.replace(PACKAGE_SEPARATOR_CHAR, DIRECTORY_SEPARATOR_CHAR);
        final Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) 
        {
            final URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        Set<Class<?>> classes = new HashSet<>();
        for (final File directory : dirs) 
        {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }
    
    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> findClasses(final File directory, final String packageName) 
            throws ClassNotFoundException 
    {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) 
        {
            return classes;
        }
        final File[] files = directory.listFiles();
        for (File file : files) 
        {
            if (file.isDirectory()) 
            {
                assert !file.getName().contains(PACKAGE_SEPARATOR);
                classes.addAll(findClasses(file, packageName + PACKAGE_SEPARATOR + file.getName()));
            } 
            else if (file.getName().endsWith(CLASS_FILE_EXTENTION)) 
            {
                classes.add(Class.forName(
                    packageName + 
                    PACKAGE_SEPARATOR + 
                    file.getName().substring(0, file.getName().length() - CLASS_FILE_EXTENTION.length())
                ));
            }
        }
        return classes;
    }
}
