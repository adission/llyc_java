package cn.com.lanlyc.base.persistence;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import cn.com.lanlyc.base.util.ConstantUtils;

/**
 * 实体字段映射关系
 */
public class ModelMapper {
    
    /** 表名映射 */
    private String table = null; // 表名
    
    private Field primaryKey = null; // 主键名称
    
    /** 字段映射 */
    private Map<Field, String> fieldMapper = new HashMap<Field, String>();
    
    /**
     * 加载映射信息
     * @param cls 要映射的类
     */
    public void load(Class<? extends Serializable> cls) {
        TableMapper tableMapper = cls.getAnnotation(TableMapper.class);
        if (tableMapper != null && !tableMapper.value().isEmpty()) {
            table = tableMapper.value();
        } else {
            String name = ConstantUtils.getConstant("db.table.prefix") + cls.getSimpleName();
            name = name.substring(0, 1).toLowerCase() + name.substring(1);
            table = mapping(name);
        }
        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (!field.getName().equals("serialVersionUID") && !field.isAnnotationPresent(NotFieldMapper.class)) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(FieldMapper.class)) {
                    FieldMapper fieldMapper = field.getAnnotation(FieldMapper.class);
                    if (!fieldMapper.value().isEmpty()) {
                        this.fieldMapper.put(field, fieldMapper.value());
                    } else {
                        this.fieldMapper.put(field, mapping(field.getName()));
                    }
                } else if (field.isAnnotationPresent(PrimaryKeyMapper.class)) {
                    PrimaryKeyMapper primaryKeyMapper = field.getAnnotation(PrimaryKeyMapper.class);
                    primaryKey = field;
                    if (!primaryKeyMapper.value().isEmpty()) {
                        this.fieldMapper.put(field, primaryKeyMapper.value());
                    } else {
                        this.fieldMapper.put(field, mapping(field.getName()));
                    }
                    
                } else {
                    String name = field.getName();
                    this.fieldMapper.put(field, mapping(name));
                }
            } else {
                continue;
            }
        }
    }
    
    /** 自动映射规则 */
    private String mapping(String name) {
        return name.replaceAll(ConstantUtils.getConstant("db.mapping.ref"), ConstantUtils.getConstant("db.mapping.to")).toUpperCase();
    }
    
    public String getTable() {
        return table;
    }
    
    public Field getPrimaryKey() {
        return primaryKey;
    }
    
    public Map<Field, String> getFieldMapper() {
        return fieldMapper;
    }
}
