package cn.com.lanlyc.base.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 对类的操作工具包
 * @author 
 */
public class ClassUtils {
    
    /**
     * 获取类的泛型类型
     * @param cls 要获取的类
     * @return 类的泛型类型
     * @author 
     */
    public static Type[] getActualTypeArguments(Class<?> cls) {
        ParameterizedType type = ((ParameterizedType) cls.getGenericSuperclass());
        return type.getActualTypeArguments();
    }
    
    /**
     * 获取指定调用次序的命名空间+方法名
     * @param idx 层次
     * @return
     */
    public static String getStatementName(int idx) {
        Exception exception = new Exception();
        StackTraceElement[] stackTraces = exception.getStackTrace();
        if (idx > stackTraces.length - 1) {
            return null;
        }
        StringBuffer statementName = new StringBuffer();
        statementName.append(stackTraces[idx].getClassName());
        statementName.append(".");
        statementName.append(stackTraces[idx].getMethodName());
        return statementName.toString();
    }
    
    /**
     * 获得对象的指定属性
     * @param obj
     * @param property 属性名称
     * @return 返回属性对象，属性不存在则返回null
     * @author 
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Object getProperty(Object obj, String property) {
        if ("".equals(DataUtils.defaultString(property))) {
            return null;
        }
        Class clazz = obj.getClass();
        Method method = null;
        try {
            method = clazz.getMethod("is" + property.substring(0, 1).toUpperCase() + property.substring(1), null);
        } catch (NoSuchMethodException e) {
            try {
                method = clazz.getMethod("get" + property.substring(0, 1).toUpperCase() + property.substring(1), null);
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            }
        }
        if (method != null) {
            try {
                return method.invoke(obj, null);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    /**
     * 深克隆对象，对象及对象属性必须实现序列化
     * @param object
     * @return 返回被深克隆后的对象
     * @throws IllegalArgumentException object未实现序列化
     * @author 
     */
    public static Object deepClone(Object object) {
        if (!(object instanceof Serializable)) throw new IllegalArgumentException("object未实现序列化");
        try {
            // 将对象写到流里
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            new ObjectOutputStream(bo).writeObject(object);
            // 从流里读出来
            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            return new ObjectInputStream(bi).readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
