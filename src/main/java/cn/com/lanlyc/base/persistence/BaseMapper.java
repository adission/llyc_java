package cn.com.lanlyc.base.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 持久化基类接口
 * @author 
 */
public interface BaseMapper<T extends Serializable> {
    
    /**
     * 添加一个实体，会将null值保存到对应的字段中
     * @param model
     * @return 执行成功的记录数
     */
    int save(T model);
    
    /**
     * 添加一个实体
     * @param model
     * @param saveNullColumn 是否将null值保存到对应的字段中，true-是，false-否
     * @return 执行成功的记录数
     * @author 
     */
    int save(T model, boolean saveNullColumn);
    
    /**
     * 添加一个实体，会将null值保存到对应的字段中，并返回主键值
     * @param model
     * @return 主键值
     */
    Number saveAutoKey(T model);
    
    /**
     * 添加一个实体，并返回主键值
     * @param model
     * @param saveNullColumn 是否将null值保存到对应的字段中，true-是，false-否
     * @return 主键值
     * @author 
     */
    Number saveAutoKey(T model, boolean saveNullColumn);
    
    /**
     * 批量添加实体
     * @param modelList 实体集合
     * @return 对应集合的执行成功的记录数数组
     * @author 
     */
    int[] save(Collection<T> modelList);
    
    /**
     * 更新一个实体，会将null值更新到对应的字段中
     * @param model
     * @return 执行成功的记录数
     * @author 
     */
    int update(T model);
    
    /**
     * 更新一个实体
     * @param model
     * @param updateNullColumn 是否将null值更新到对应的字段中，true-是，false-否
     * @return 执行成功的记录数
     * @author 
     */
    int update(T model, boolean updateNullColumn);
    
    /**
     * 批量更新实体
     * @param modelList 实体集合
     * @return 对应集合的执行成功的记录数数组
     * @author 
     */
    int[] update(Collection<T> modelList);
    
    /**
     * 根据主键获得实体
     * @param id
     * @return 实体
     * @author 
     */
    T get(Serializable id);
    
    /**
     * 获得单实体所有数据
     * 
     * @return 实体
     * @author 
     */
    List<T> getAll();
    
    /**
     * 根据主键删除实体
     * @param id
     * @return 执行成功的记录数
     * @author 
     */
    int delete(Serializable id);
    
    /**
     * 批量删除实体
     * @param ids 主键集合
     * @return 对应集合的执行成功的记录数数组
     * @author 
     */
    <I extends Serializable> int[] delete(Collection<I> ids);
    
}
