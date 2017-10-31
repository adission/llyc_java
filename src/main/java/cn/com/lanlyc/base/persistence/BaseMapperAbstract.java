package cn.com.lanlyc.base.persistence;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import cn.com.lanlyc.base.TrueKeyNamedParameterJdbcTemplate;
import cn.com.lanlyc.base.util.ClassUtils;
import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.FormatUtils;
import cn.com.lanlyc.base.util.Page;

/**
 * 持久层抽象类，实现泛型后，将可直接使用save,update,get及delete方法
 * @param <T>
 * @author 
 */
@SuppressWarnings({"unchecked"})
public abstract class BaseMapperAbstract<T extends Serializable> implements BaseMapper<T> {
    
    /** log4j for print SQL */
    protected Logger printSQL = Logger.getLogger("printSQL");
    
    /**  */
    private static Map<Class<? extends Serializable>, ModelMapper> modelMap = new HashMap<Class<? extends Serializable>, ModelMapper>();
    
    /** 数据连接池 */
    @Resource
    private TrueKeyNamedParameterJdbcTemplate jdbcTemplate;
    
    protected TrueKeyNamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
    
    protected Class<T> generic = (Class<T>) ClassUtils.getActualTypeArguments(this.getClass())[0];
    
    /**
     * 获取当前实现的泛型实体映射关系
     * @return ModelMapper
     * @author 
     */
    private ModelMapper getGenericMapper() {
        if (!modelMap.containsKey(generic)) {
            ModelMapper mapper = new ModelMapper();
            mapper.load(generic);
            modelMap.put(generic, mapper);
        }
        return modelMap.get(generic);
    }
    
    /**
     * 获取当前实现的泛型所映射的所有字段名，可直接拼接在select关键字后使用，代替 * 功能<br>
     * 如：<code>String sql = "SELECT " + getGenericColumn("t") + " FROM t_table t";</code>
     * @param tableAlias 表别名，若没有表别名，可以留空
     * @return 返回映射的所有字段名，当中以逗号", "间隔
     * @author 
     */
    protected String getGenericColumn(String tableAlias) {
        ModelMapper mapper = getGenericMapper();
        if (DataUtils.isNullOrEmpty(tableAlias)) {
            tableAlias = "";
        } else {
            tableAlias = DataUtils.defaultString(tableAlias) + ".";
        }
        ArrayList<String> fieldList = new ArrayList<String>(mapper.getFieldMapper().size());
        try {
            for (Field field : mapper.getFieldMapper().keySet()) {
                fieldList.add(tableAlias + mapper.getFieldMapper().get(field) + " " + field.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fields = fieldList.toString();
        return " " + fields.substring(1, fields.length() - 1) + " ";
    }
    
    @Override
    public int save(T model) {
        return save(model, true);
    }
    
    @Override
    public int save(T model, boolean saveNullColumn) {
        return save(null, model, saveNullColumn);
    }
    
    /**
     * @param table 表名
     * @param model 实体对象
     * @param saveNullColumn 是否将null值保存到对应的字段中，true-是，false-否
     * @return 执行成功的记录数
     * @author 
     */
    protected int save(String table, T model, boolean saveNullColumn) {
        ModelMapper mapper = getGenericMapper();
        ArrayList<String> fieldList = new ArrayList<String>(mapper.getFieldMapper().size());
        ArrayList<String> valueList = new ArrayList<String>(mapper.getFieldMapper().size());
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        if (table == null) {
            sql.append(mapper.getTable());
        } else {
            sql.append(table);
        }
        for (Field field : mapper.getFieldMapper().keySet()) {
            try {
                if (saveNullColumn || field.get(model) != null) {
                    fieldList.add(mapper.getFieldMapper().get(field));
                    valueList.add(":" + mapper.getFieldMapper().get(field));
                    map.put(mapper.getFieldMapper().get(field), field.get(model));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String fields = fieldList.toString();
        sql.append(" (");
        sql.append(fields.substring(1, fields.length() - 1));
        sql.append(") VALUES (");
        String paramesStr = valueList.toString();
        sql.append(paramesStr.substring(1, paramesStr.length() - 1));
        sql.append(")");
        printSQL.info("执行语句:" + sql);
        printSQL.info("执行参数:" + map);
        return getJdbcTemplate().update(sql.toString(), map);
    }
    
    @Override
    public Number saveAutoKey(T model) {
        return saveAutoKey(model, true);
    }

    @Override
    public Number saveAutoKey(T model, boolean saveNullColumn) {
        return saveAutoKey(null, model, saveNullColumn);
    }
    
    /**
     * @param table 表名
     * @param model 实体对象
     * @param saveNullColumn 是否将null值保存到对应的字段中，true-是，false-否
     * @return 主键值
     * @author 
     */
    protected Number saveAutoKey(String table, T model, boolean saveNullColumn) {
        ModelMapper mapper = getGenericMapper();
        ArrayList<String> fieldList = new ArrayList<String>(mapper.getFieldMapper().size());
        ArrayList<String> valueList = new ArrayList<String>(mapper.getFieldMapper().size());
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        if (table == null) {
            sql.append(mapper.getTable());
        } else {
            sql.append(table);
        }
        for (Field field : mapper.getFieldMapper().keySet()) {
            try {
                if (saveNullColumn || field.get(model) != null) {
                    fieldList.add(mapper.getFieldMapper().get(field));
                    valueList.add(":" + mapper.getFieldMapper().get(field));
                    map.put(mapper.getFieldMapper().get(field), field.get(model));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String fields = fieldList.toString();
        sql.append(" (");
        sql.append(fields.substring(1, fields.length() - 1));
        sql.append(") VALUES (");
        String paramesStr = valueList.toString();
        sql.append(paramesStr.substring(1, paramesStr.length() - 1));
        sql.append(")");
        printSQL.info("执行语句:" + sql);
        printSQL.info("执行参数:" + map);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(sql.toString(), new MapSqlParameterSource(map), keyHolder);
        return keyHolder.getKey();
    }
    
    @Override
    public int[] save(Collection<T> modelList) {
        ModelMapper mapper = getGenericMapper();
        ArrayList<String> fieldList = new ArrayList<String>(mapper.getFieldMapper().size());
        ArrayList<String> valueList = new ArrayList<String>(mapper.getFieldMapper().size());
        Map<String, Object>[] maps = new HashMap[modelList.size()];
        for (int i = 0; i < maps.length; maps[i++] = new HashMap<>())
            ;
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(mapper.getTable());
        for (Field field : mapper.getFieldMapper().keySet()) {
            try {
                fieldList.add(mapper.getFieldMapper().get(field));
                valueList.add(":" + mapper.getFieldMapper().get(field));
                int i = 0;
                for (Iterator<T> iterator = modelList.iterator(); iterator.hasNext();) {
                    T model = iterator.next();
                    maps[i++].put(mapper.getFieldMapper().get(field), field.get(model));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String fields = fieldList.toString();
        sql.append(" (");
        sql.append(fields.substring(1, fields.length() - 1));
        sql.append(") VALUES (");
        String paramesStr = valueList.toString();
        sql.append(paramesStr.substring(1, paramesStr.length() - 1));
        sql.append(")");
        printSQL.info("执行语句:" + sql);
        for (int i = 0; i < maps.length; i++) {
            printSQL.info("执行参数" + i + ":" + maps[i]);
        }
        return getJdbcTemplate().batchUpdate(sql.toString(), maps);
    }
    
    @Override
    public int update(T model) {
        return update(model, true);
    }
    
    @Override
    public int update(T model, boolean updateNullColumn) {
        return update(null, model, updateNullColumn);
    }
    
    /**
     * 添加一个实体
     * @param table 表名
     * @param model 实体对象
     * @param updateNullColumn 是否将null值更新到对应的字段中，true-是，false-否
     * @return 执行成功的记录数
     */
    protected int update(String table, T model, boolean updateNullColumn) {
        ModelMapper mapper = getGenericMapper();
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder("UPDATE ");
        if (table == null) {
            sql.append(mapper.getTable());
        } else {
            sql.append(table);
        }
        sql.append(" SET ");
        ArrayList<String> fieldList = new ArrayList<String>(mapper.getFieldMapper().size());
        String where = null;
        for (Field field : mapper.getFieldMapper().keySet()) {
            try {
                if (field.equals(mapper.getPrimaryKey())) {
                    where = mapper.getFieldMapper().get(field) + "=:" + mapper.getFieldMapper().get(field);
                    map.put(mapper.getFieldMapper().get(field), field.get(model));
                } else if (updateNullColumn || field.get(model) != null) {
                    fieldList.add(mapper.getFieldMapper().get(field) + "=:" + mapper.getFieldMapper().get(field));
                    map.put(mapper.getFieldMapper().get(field), field.get(model));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String fields = fieldList.toString();
        sql.append(fields.substring(1, fields.length() - 1));
        sql.append(" WHERE ");
        sql.append(where);
        printSQL.info("执行语句:" + sql);
        printSQL.info("执行参数:" + map);
        return getJdbcTemplate().update(sql.toString(), map);
    }
    
    @Override
    public int[] update(Collection<T> modelList) {
        ModelMapper mapper = getGenericMapper();
        Map<String, Object>[] maps = new HashMap[modelList.size()];
        for (int i = 0; i < maps.length; maps[i++] = new HashMap<>())
            ;
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(mapper.getTable());
        sql.append(" SET ");
        ArrayList<String> fieldList = new ArrayList<String>(mapper.getFieldMapper().size());
        String where = null;
        for (Field field : mapper.getFieldMapper().keySet()) {
            try {
                if (field.equals(mapper.getPrimaryKey())) {
                    where = mapper.getFieldMapper().get(field) + "=:" + mapper.getFieldMapper().get(field);
                } else {
                    fieldList.add(mapper.getFieldMapper().get(field) + "=:" + mapper.getFieldMapper().get(field));
                }
                int i = 0;
                for (Iterator<T> iterator = modelList.iterator(); iterator.hasNext();) {
                    T model = iterator.next();
                    maps[i++].put(mapper.getFieldMapper().get(field), field.get(model));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String fields = fieldList.toString();
        sql.append(fields.substring(1, fields.length() - 1));
        sql.append(" WHERE ");
        sql.append(where);
        printSQL.info("执行语句:" + sql);
        for (int i = 0; i < maps.length; i++) {
            printSQL.info("执行参数" + i + ":" + maps[i]);
        }
        return getJdbcTemplate().batchUpdate(sql.toString(), maps);
    }
    
    @Override
    public T get(Serializable id) {
        return get(null, id);
    }
    
    /**
     * 获得一个实体
     * @param table 表名
     * @param id 主键
     * @return 实体对象
     * @author 
     */
    protected T get(String table, Serializable id) {
        ModelMapper mapper = getGenericMapper();
        StringBuilder sql = new StringBuilder("SELECT ");
        ArrayList<String> fieldList = new ArrayList<String>(mapper.getFieldMapper().size());
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            for (Field field : mapper.getFieldMapper().keySet()) {
                fieldList.add(mapper.getFieldMapper().get(field) + " " + field.getName());
            }
            map.put(mapper.getFieldMapper().get(mapper.getPrimaryKey()), id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fields = fieldList.toString();
        sql.append(fields.substring(1, fields.length() - 1));
        sql.append(" FROM ");
        if (table == null) {
            sql.append(mapper.getTable());
        } else {
            sql.append(table);
        }
        sql.append(" WHERE ");
        sql.append(mapper.getFieldMapper().get(mapper.getPrimaryKey()));
        sql.append("=:");
        sql.append(mapper.getFieldMapper().get(mapper.getPrimaryKey()));
        printSQL.info("执行语句:" + sql);
        printSQL.info("执行参数:" + map);
        List<T> results = getJdbcTemplate().query(sql.toString(), map, new BeanPropertyRowMapper<T>(generic));
        if (results.size() == 0) return null;
        return results.get(0);
    }
    
    @Override
    public List<T> getAll() {
        return getAll(null);
    }
    
    /**
     * 获得一个实体所有数据
     * @param table 表名
     * 
     * @return 实体对象
     * @author 
     */
    protected List<T> getAll(String table) {
        ModelMapper mapper = getGenericMapper();
        StringBuilder sql = new StringBuilder("SELECT ");
        ArrayList<String> fieldList = new ArrayList<String>(mapper.getFieldMapper().size());
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            for (Field field : mapper.getFieldMapper().keySet()) {
                fieldList.add(mapper.getFieldMapper().get(field) + " " + field.getName());
            }
//            map.put(mapper.getFieldMapper().get(mapper.getPrimaryKey()), id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fields = fieldList.toString();
        sql.append(fields.substring(1, fields.length() - 1));
        sql.append(" FROM ");
        if (table == null) {
            sql.append(mapper.getTable());
        } else {
            sql.append(table);
        }
//        sql.append(" WHERE ");
//        sql.append(mapper.getFieldMapper().get(mapper.getPrimaryKey()));
//        sql.append("=:");
//        sql.append(mapper.getFieldMapper().get(mapper.getPrimaryKey()));
        printSQL.info("执行语句:" + sql);
        printSQL.info("执行参数:" + map);
        List<T> results = getJdbcTemplate().query(sql.toString(), map, new BeanPropertyRowMapper<T>(generic));
        if (results.size() == 0) return null;
        return results;
    }
    
    @Override
    public int delete(Serializable id) {
        return delete(null, id);
    }
    
    /**
     * 删除一个实体
     * @param table 表名
     * @param id 主键
     * @return 执行成功的记录数
     * @author 
     */
    protected int delete(String table, Serializable id) {
        ModelMapper mapper = getGenericMapper();
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        if (table == null) {
            sql.append(mapper.getTable());
        } else {
            sql.append(table);
        }
        sql.append(" WHERE ");
        sql.append(mapper.getFieldMapper().get(mapper.getPrimaryKey()));
        sql.append("=:");
        sql.append(mapper.getFieldMapper().get(mapper.getPrimaryKey()));
        try {
            map.put(mapper.getFieldMapper().get(mapper.getPrimaryKey()), id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        printSQL.info("执行语句:" + sql);
        printSQL.info("执行参数:" + map);
        return getJdbcTemplate().update(sql.toString(), map);
    }
    
    @Override
    public <I extends Serializable> int[] delete(Collection<I> ids) {
        return delete(null, ids);
    }
    
    /**
     * 批量删除实体
     * @param table 表名
     * @param ids 主键集合
     * @return 对应集合的执行成功的记录数数组
     * @author 
     */
    protected <I extends Serializable> int[] delete(String table, Collection<I> ids) {
        ModelMapper mapper = getGenericMapper();
        Map<String, Object>[] maps = new HashMap[ids.size()];
        for (int i = 0; i < maps.length; maps[i++] = new HashMap<>())
            ;
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        if (table == null) {
            sql.append(mapper.getTable());
        } else {
            sql.append(table);
        }
        sql.append(" WHERE ");
        sql.append(mapper.getFieldMapper().get(mapper.getPrimaryKey()));
        sql.append("=:");
        sql.append(mapper.getFieldMapper().get(mapper.getPrimaryKey()));
        try {
            int i = 0;
            for (Serializable id : ids) {
                maps[i++].put(mapper.getFieldMapper().get(mapper.getPrimaryKey()), id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        printSQL.info("执行语句:" + sql);
        for (int i = 0; i < maps.length; i++) {
            printSQL.info("执行参数" + i + ":" + maps[i]);
        }
        return getJdbcTemplate().batchUpdate(sql.toString(), maps);
    }
    
    /**
     * 删改、更新方法
     * @param sql 数据库语句
     * @param paramMap 参数Map
     * @return 执行成功的记录数
     * @author 
     */
    protected int execute(CharSequence sql, Map<String, Object> paramMap) {
        printSQL.info("执行语句:" + sql);
        printSQL.info("执行参数:" + paramMap);
        return getJdbcTemplate().update(sql.toString(), paramMap);
    }
    
    protected Number executeAutoKey(CharSequence sql, Map<String, Object> paramMap) {
        printSQL.info("执行语句:" + sql);
        printSQL.info("执行参数:" + paramMap);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(sql.toString(), new MapSqlParameterSource(paramMap), keyHolder);
        return keyHolder.getKey();
    }
    
    /**
     * 批量删改、更新方法
     * @param sql 数据库语句
     * @param paramMap 参数Map
     * @return 执行成功的记录数
     * @author 
     */
    protected int[] batchExecute(CharSequence sql, Map<String, ?>[] batchValues) {
        printSQL.info("执行语句:" + sql);
        printSQL.info("执行参数:" + Arrays.toString(batchValues));
        return getJdbcTemplate().batchUpdate(sql.toString(), batchValues);
    }
    
    /**
     * 查询实体列表
     * @param sql 数据库语句
     * @param paramMap 参数Map
     * @return 实体集合
     * @author 
     */
    protected List<T> findList(CharSequence sql, Map<String, Object> paramMap) {
        printSQL.info("执行语句:" + sql);
        printSQL.info("执行参数:" + paramMap);
        return getJdbcTemplate().query(sql.toString(), paramMap, new BeanPropertyRowMapper<T>(generic));
    }
    
    /**
     * 查询实体列表
     * @param sql 数据库语句
     * @param paramMap 参数Map
     * @param model 实体Class
     * @param <M> model泛型
     * @return model实体集合
     * @author 
     */
    protected <M> List<M> findList(CharSequence sql, Map<String, Object> paramMap, Class<M> model) {
        printSQL.info("执行语句:" + sql);
        printSQL.info("执行参数:" + paramMap);
        return getJdbcTemplate().query(sql.toString(), paramMap, new BeanPropertyRowMapper<M>(model));
    }
    
    /**
     * 查询单一字段结果值集合
     * @param sql 数据库语句
     * @param paramMap 参数Map
     * @param columnType 需要查询的字段类型（如：Integer.class，String.class，Date.class等）
     * @param <M> 字段类型泛型
     * @return 字段值集合
     * @author 
     */
    protected <M extends Serializable> List<M> findListForColumn(CharSequence sql, Map<String, Object> paramMap, Class<M> columnType) {
        printSQL.info("执行语句:" + sql);
        printSQL.info("执行参数:" + paramMap);
        return getJdbcTemplate().queryForList(sql.toString(), paramMap, columnType);
    }
    
    /**
     * 查询单一字段结果值
     * @param sql 数据库语句
     * @param paramMap 参数Map
     * @param columnType 需要查询的字段类型（如：Integer.class，String.class，Date.class等）
     * @param <M> 字段类型泛型
     * @return 字段值对象
     * @author 
     */
    protected <M extends Serializable> M getColumn(CharSequence sql, Map<String, Object> paramMap, Class<M> columnType) {
        printSQL.info("执行语句:" + sql);
        printSQL.info("执行参数:" + paramMap);
        List<M> result = getJdbcTemplate().queryForList(sql.toString(), paramMap, columnType);
        return result.size() > 0 ? result.get(0) : null;
    }
    
    /**
     * 查询整型字段<br>
     * 如查询count
     * @param sql 查询语句
     * @param paramMap 参数Map
     * @return 整型结果
     * @author 
     */
    protected Integer getInt(CharSequence sql, Map<String, Object> paramMap) {
        printSQL.info("执行语句:" + sql);
        printSQL.info("执行参数:" + paramMap);
        return getJdbcTemplate().queryForObject(sql.toString(), paramMap, Integer.class);
    }
    
    /**
     * 查询数字字段<br>
     * 如查询sum,返回数字大小无法确定的使用该函数，防止数字越界
     * @param sql 查询语句
     * @param paramMap 参数Map
     * @return Number结果
     * @author 
     */
    protected Number getNumber(CharSequence sql, Map<String, Object> paramMap) {
        printSQL.info("执行语句:" + sql);
        printSQL.info("执行参数:" + paramMap);
        return getJdbcTemplate().queryForObject(sql.toString(), paramMap, Number.class);
    }
    
    /**
     * 获得一个实体
     * @param sql 查询语句
     * @param paramMap 参数Map
     * @param model 实体Class
     * @param <M> model泛型
     * @return 实体对象
     * @author 
     */
    protected <M extends Serializable> M get(CharSequence sql, Map<String, Object> paramMap, Class<M> model) {
        List<M> result = getPage(sql, paramMap, 0, 1, model);
        // printSQL.info("执行语句:" + sql);
        // printSQL.info("执行参数:" + paramMap);
        // getJdbcTemplate().query(sql.toString(), paramMap, new BeanPropertyRowMapper<M>(model));
        return result.size() > 0 ? result.get(0) : null;
    }
    
    /**
     * 查询分页结果
     * @param sql 查询语句
     * @param paramMap 参数Map
     * @param startRow 分页起始行
     * @param pageSize 每页的记录长度
     * @param model 指定查询的集合实体Class
     * @param <M> model泛型
     * @return M 分页结果集合
     * @author 
     */
    protected abstract <M extends Serializable> List<M> getPage(CharSequence sql, Map<String, Object> paramMap, int startRow, int pageSize, Class<M> model);
    
    /**
     * 查询分页结果
     * @param sql 查询语句
     * @param paramMap 参数Map
     * @param page 分页对象
     * @param model 指定查询的集合实体Class
     * @param <M> model泛型
     * @return M 分页结果对象
     * @author 
     */
    
    protected <M extends Serializable> Page<M> getPage(CharSequence sql, Map<String, Object> paramMap, Page<M> page, Class<M> model) {
        page.setTotalRow(getTotalCount(sql, paramMap));
        page.setResultRows(getPage(sql, paramMap, page.getStartRow(), page.getPageSize(), model));
        return page;
    }
    
    /**
     * 根据列表查询语句自动查询分页总记录数
     * @param sql 查询语句
     * @param paramMap 参数Map
     * @return
     * @author 
     */
    private Integer getTotalCount(CharSequence sql, Map<String, Object> paramMap) {
        // 代码逻辑思路：
        // 1. 从第一个SELECT开始往后匹配到对应的FROM位置，并截断（匹配思路：检查SELECT后面所有成对以及嵌套一层子查询的SELECT/FROM直到第一个不成对的FROM为止）
        // 2. 如果有，则去除主查询中末尾的ORDER BY
        // 3. 在截断后的语句前加上SELECT COUNT(1) 的内容拼成查询分页总记录数的语句
        
        Integer result = 0; // 最终需要返回的结果变量
        Integer fromIndex = 0; // 主查询的FROM关键字的起始位置变量
        // 匹配主查询SELECT后面所有成对以及嵌套一层子查询的SELECT/FROM直到第一个单独出现的FROM为止
        // ((.(?!\\bSELECT\\b|\\bFROM\\b))*.\\bSELECT\\b(.(?!\\bFROM\\b))*.\\bFROM\\b(.(?!\\bSELECT\\b|\\bFROM\\b))*)* 此为一对SELECT/FROM的匹配规则
        Pattern fromPattern = Pattern.compile("^\\s*\\bSELECT\\b((.(?!\\bSELECT\\b|\\bFROM\\b))*.\\bSELECT\\b((.(?!\\bSELECT\\b|\\bFROM\\b))*.\\bSELECT\\b(.(?!\\bFROM\\b))*.\\bFROM\\b(.(?!\\bSELECT\\b|\\bFROM\\b))*)*(.(?!\\bFROM\\b))*.\\bFROM\\b((.(?!\\bSELECT\\b|\\bFROM\\b))*.\\bSELECT\\b(.(?!\\bFROM\\b))*.\\bFROM\\b(.(?!\\bSELECT\\b|\\bFROM\\b))*)*(.(?!\\bSELECT\\b|\\bFROM\\b))*)*(.(?!\\bFROM\\b))*",
                                              Pattern.CASE_INSENSITIVE);
        Matcher fromMatcher = fromPattern.matcher(sql);
        if (fromMatcher.find()) {
            // 加上被替换的内容长度，以及后面紧跟着主查询FROM的1个字符。因为最后一个字符是(.(?!\\bFROM\\b))
            fromIndex += fromMatcher.group().length() + 1;
            Pattern namedParamPattern = Pattern.compile(":[\\w\\-]+"); // 匹配预处理名字的编译器
            String prefixSQL = sql.toString().substring(0, fromIndex);
            String suffixSQL = sql.toString().substring(fromIndex);
            Matcher namedParamMatcher = namedParamPattern.matcher(prefixSQL);
            Map<String, Object> removeMap = new HashMap<String, Object>(); // 存储需要移除的预处理参数
            while (namedParamMatcher.find()) { // 查找仅在FROM前出现过的预处理名字参数，并从paramMap中移除
                if (!suffixSQL.matches(".*" + namedParamMatcher.group() + "\\b.*")) {
                    String key = namedParamMatcher.group().substring(1);
                    removeMap.put(key, paramMap.get(key));
                    paramMap.remove(key);
                }
            }
            // 去除主查询的ORDER BY。为简单处理，本处未考虑ORDER BY使用预处理参数的情况
            // 匹配主查询ORDER BY的编译器[规则：从ORDER BY到行尾中可含成对括号且不含后半个括号的内容，成对括号中支持最多嵌套3层括号]
            // (([^\\(\\)]*\\([^\\(\\)]*\\))*[^\\)]*)* 此为成对括号的正则，在其中，对其自身做了三次递归嵌套
            Pattern orderByPattern = Pattern.compile("\\bORDER\\s+BY\\b([^\\(\\)]*\\((([^\\(\\)]*\\((([^\\(\\)]*\\([^\\(\\)]*\\))*[^\\)]*)*\\))*[^\\)]*)*\\))*[^\\)]*$", Pattern.CASE_INSENSITIVE);
            Matcher orderByMatcher = orderByPattern.matcher(suffixSQL);
            if (orderByMatcher.find()) {
                suffixSQL = suffixSQL.substring(0, orderByMatcher.start());
            }
            result = getInt("SELECT COUNT(1) " + suffixSQL, paramMap);
            if (paramMap != null) {
            	paramMap.putAll(removeMap); // 查询完成后，将移除的预处理参数重新置入paramMap，以免影响后面的列表查询
			}
        } else {
        }
        return result;
    }
    
    /**
     * 查询当前泛型实体的分页结果
     * @param sql 查询语句
     * @param paramMap 参数Map
     * @param startRow 分页起始行
     * @param pageSize 每页的记录长度
     * @return 分页结果集合
     * @author 
     */
    protected List<T> getPage(CharSequence sql, Map<String, Object> paramMap, int startRow, int pageSize) {
        return getPage(sql, paramMap, startRow, pageSize, generic);
    }
    
    /**
     * 验证字符串字段参数
     * @param str
     * @return 如果为null或空字符串，则返回false，否则返回true
     * @author 
     */
    protected boolean isValid(String str) {
        return !DataUtils.isNullOrEmpty(str);
    }
    
    /**
     * 验证整型字段参数
     * @param integer
     * @return 如果为null，则返回false，否则返回true
     * @author 
     */
    protected boolean isValid(Integer integer) {
        return integer != null;
    }
    
    /**
     * 验证整型字段参数
     * @param dou
     * @return 如果为null，则返回false，否则返回true
     * @author 
     */
    protected boolean isValid(Double dou) {
        return dou != null;
    }
    
    /**
     * 验证数组参数的有效性
     * @param objArray
     * @return 如果为null或数组长度为0，则返回false，否则返回true
     * @author 
     */
    protected boolean isValid(Object[] objArray) {
        return objArray != null && objArray.length > 0;
    }
    
    /**
     * 验证集合参数的有效性
     * @param objArray
     * @return 如果为null或数组长度为0，则返回false，否则返回true
     * @author 
     */
    protected boolean isValid(Collection<?> list) {
        return list != null && list.size() > 0;
    }
    
    /**
     * 验证是否UUID字符串
     * @param uid
     * @return 如果为null或不符合UUID规则，则返回false，否则返回true
     * @author 
     */
    protected boolean isValidUid(String uid) {
        return DataUtils.isUid(uid);
    }
    
    /**
     * 验证日期格式字符串字段参数
     * @param date 日期字符串
     * @return 当date可以为FormatUtils.autoDate方法转换时，返回true，否则返回false
     * @author 
     */
    protected boolean isValidDate(String date) {
        if (DataUtils.isNullOrEmpty(date)) return false;
        StringBuilder patternRegex = new StringBuilder("\\d{4}(-\\d{1,2}){2}"); // 2012-03-21, 2012-3-21
        patternRegex.append("|\\d{4}(-\\d{1,2}){2} \\d{1,2}(:\\d{1,2}){2}"); // 2012-03-21 18:11:43
        patternRegex.append("|\\d{4}(-\\d{1,2}){2} \\d{1,2}(:\\d{1,2}){2}\\.\\d{1,3}"); // 2012-03-21 18:11:43.567
        patternRegex.append("|([A-Z][a-z]{2} ){2}\\d{1,2} \\d{1,2}(:\\d{1,2}){2} [A-Z]{3} \\d{4}"); // Wed Mar 21 18:11:43 CST 2012
        patternRegex.append("|\\d{4}(/\\d{1,2}){2}"); // 2012/03/21, 2012/3/21
        patternRegex.append("|\\d{4}(/\\d{1,2}){2} \\d{1,2}(:\\d{1,2}){2}"); // 2012/03/21 18:11:43
        patternRegex.append("|\\d{4}(/\\d{1,2}){2} \\d{1,2}(:\\d{1,2}){2}\\.\\d{1,3}"); // 2012/03/21 18:11:43.567
        patternRegex.append("|\\d{4}(\\.\\d{1,2}){2}"); // 2012.03.21, 2012.3.21
        patternRegex.append("|\\d{4}(\\.\\d{1,2}){2} \\d{1,2}(:\\d{1,2}){2}"); // 2012.03.21 18:11:43
        patternRegex.append("|\\d{4}(\\.\\d{1,2}){2} \\d{1,2}(:\\d{1,2}){2}\\.\\d{1,3}"); // 2012.03.21 18:11:43.567
        patternRegex.append("|(\\d{1,2}/){2}\\d{4}"); // 21/03/2012, 21/3/2012
        patternRegex.append("|[\\-]?\\d+"); // 20120321 或 1332324703000
        return date.matches(patternRegex.toString());
    }
    
    /**
     * 转换成java.sql.Date，以便给数据库识别参数类型，以免产生隐式转换，无法使用索引查询
     * @param date 日期字符串
     * @return 当date可以为FormatUtils.autoDate方法转换时，则返回对应日期对象，否则为null
     * @author 
     */
    protected java.sql.Date toSQLDate(String date) {
        return toSQLDate(FormatUtils.autoDate(date));
    }
    
    /**
     * 转换成java.sql.Date，以便给数据库识别参数类型，以免产生隐式转换，无法使用索引查询
     * @param date 日期字符串
     * @return 当date可以为FormatUtils.autoDate方法转换时，则返回对应日期对象，否则为null
     * @author 
     */
    protected java.sql.Date toSQLDate(Date date) {
        return date == null ? null : new java.sql.Date(date.getTime());
    }

}
