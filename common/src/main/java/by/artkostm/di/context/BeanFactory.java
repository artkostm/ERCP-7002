package by.artkostm.di.context;

/**
 * The base interface for application context
 * @author Artsiom_Chuiko
 *
 */
public interface BeanFactory
{
    /**
     * 
     * @param name - the name of a bean
     * @return null if the context doesn't contain a bean with <code>name</code>
     */
    Object getBean(String name);
    
    /**
     * 
     * @param clazz - the class of a bean
     * @return null if the context doesn't contain a bean with Class <code>clazz</code>
     */
    Object getBean(Class<?> clazz);
    
    /**
     * 
     * @param name - the name of a bean
     * @return true if the context contains a bean with <code>name</code>
     */
    boolean containtsBean(String name);
}
