package cn.com.lanlyc.base.util;

import java.util.HashMap;

/**
 * 通用Map类，支持构造函数构造Map对象<br>
 * 支持将key和value直接导出数组<br>
 * 其中key必须为<code>String</code>类型
 */
public class QMap extends HashMap<String, Object> {
    
    /**
     * 
     */
    private static final long serialVersionUID = -324416658750013370L;
    
    public QMap() {};
    
    /**
     * 可构造映射集的构造函数
     * <dl>
     * <dt>example:</dt>
     * <dd>new QMap("key1",Obj1,"key2",Obj2,"key3",Obj3...)</dd>
     * </dl>
     * @param params
     */
    public QMap(Object... params) {
        int j = params.length;
        if (j % 2 != 0) {
            j--;
        }
        for (int i = 0; i < j; i += 2) {
            if (i != params.length) {
                this.put((String) params[i], params[i + 1]);
            }
        }
    }
    
    /**
     * 添加映射关联的快捷方法
     * <p>
     * <dl>
     * <dt>eg:</dt>
     * <dd>Qmap q = new QMap("key1",Obj1)</dd>
     * <dl>
     * <dl>
     * <dd>.add("key2",Obj2)</dd>
     * <dd>.add("key3",Obj3)</dd>
     * <dd>.add("key4",Obj4);</dd>
     * </dl>
     * </dl> </dl>
     * </p>
     * @param key
     * @param val
     * @return QMap自身
     */
    public QMap add(String key, Object val) {
        this.put(key, val);
        return this;
    }
    
    /**
     * @return 将key导出为字符串数组
     */
    public String[] getKeys() {
        return this.keySet().toArray(new String[this.size()]);
    }
    
    /**
     * @return 将value导出为对象数组
     */
    public Object[] getValues() {
        return this.values().toArray();
    }
}
