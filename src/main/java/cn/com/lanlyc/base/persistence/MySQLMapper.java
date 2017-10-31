package cn.com.lanlyc.base.persistence;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * 实现MySQL查询方法的持久层基类
 * @param <T>
 */
public abstract class MySQLMapper<T extends Serializable> extends BaseMapperAbstract<T> {
    
    @Override
    protected <M extends Serializable> List<M> getPage(CharSequence sql, Map<String, Object> paramMap, int startRow, int pageSize, Class<M> model) {
        if (paramMap == null) paramMap = new HashMap<String, Object>();
        StringBuilder pagingSQL = new StringBuilder(sql.length() + 30);
        pagingSQL.append(sql);
        if (startRow > 0) {
            pagingSQL.append(" LIMIT :_startRow, :_pageSize");
            paramMap.put("_startRow", startRow);
        } else {
            pagingSQL.append(" LIMIT :_pageSize");
        }
        paramMap.put("_pageSize", pageSize);
        printSQL.info("执行语句:" + pagingSQL);
        printSQL.info("执行参数:" + paramMap);
        return this.getJdbcTemplate().query(pagingSQL.toString(), paramMap, new BeanPropertyRowMapper<M>(model));
    }
    
    
    
}
