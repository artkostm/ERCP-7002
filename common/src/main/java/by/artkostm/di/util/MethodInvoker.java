package by.artkostm.di.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class to create an instance of a class
 * 
 * @author Artsiom_Chuiko
 *
 */
public final class MethodInvoker {
    
    private static final Logger LOG = LogManager.getLogger(MethodInvoker.class);
    
    private MethodInvoker() {}
    
    /**
     *  To invoke a static method from <code>className</code> class
     *  
     * @param jcl - custom jar class loader
     * @param className
     * @param methodName
     * @param args
     * @return the result of dispatching the method represented by this class on className with parameters args
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws ClassNotFoundException
     */
    public static Object invokeStatic(Class<?> clazz, String methodName, Object[] args) 
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, 
                   NoSuchMethodException, SecurityException, ClassNotFoundException{
        if( args == null || args.length == 0 )
            return clazz.getMethod(methodName).invoke(null);
        
        Class<?>[] types = new Class[args.length];
        
        for( int i = 0; i < args.length; i++ )
            types[i] = args[i].getClass();
        LOG.debug("invoking "+methodName+" method of "+ clazz.getCanonicalName() +" class");
        return clazz.getMethod(methodName, types).invoke(null, args);
    }
    
    /**
     * To invoke the method on the object
     * 
     * @param object
     * @param methodName
     * @param args
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws ClassNotFoundException
     */
    public static Object invoke(Object object, String methodName, Object[] args) 
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, 
                   NoSuchMethodException, SecurityException, ClassNotFoundException{
        final Class<?> clazz = object.getClass();
        if( args == null || args.length == 0 )
            return clazz.getMethod(methodName).invoke(object);
        
        Class<?>[] types = new Class[args.length];
        
        for( int i = 0; i < args.length; i++ )
            types[i] = args[i].getClass();
        LOG.debug("invoking "+methodName+" method of "+ clazz.getCanonicalName() +" class");
        return clazz.getMethod(methodName, types).invoke(object, args);
    }
    
    /**
     * To invoke a constructor of the object of the specified class
     * 
     * @param jcl
     * @param className
     * @param args
     * @return an instance of the specified class
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public static Object invokeConstructor(Class<?> clazz, Object[] args) 
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, 
                IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
        if(args == null || args.length == 0)
            return clazz.newInstance();
        
        Class<?>[] types = new Class[args.length];
        
        for(int i = 0; i < args.length; i++)
            types[i] = args[i].getClass();
        LOG.debug("creating new instance of "+clazz.getCanonicalName()+" class");
        return clazz.getConstructor(types).newInstance(args);
    }
    
    /**
     * To invoke a constructor of the object of the specified class
     * 
     * @param jcl
     * @param className
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public static Object invokeConstroctor(Class<?> clazz) 
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, 
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

        return invokeConstructor(clazz, null);
    }
}
