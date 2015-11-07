package by.artkostm.di.scanner;

import java.util.Set;

public interface ClassScanner<Resource>
{
    public static final String CLASS_FILE_EXTENTION = ".class";
    public static final String PACKAGE_SEPARATOR = ".";
    public static final char PACKAGE_SEPARATOR_CHAR = '.';
    public static final String DIRECTORY_SEPARATOR = "/";
    public static final char DIRECTORY_SEPARATOR_CHAR = '/';
    
    @SuppressWarnings("unchecked")
    Set<Class<?>> scan(Resource... res);
}
