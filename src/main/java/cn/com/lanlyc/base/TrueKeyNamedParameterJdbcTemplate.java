package cn.com.lanlyc.base;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.ParsedSql;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class TrueKeyNamedParameterJdbcTemplate extends NamedParameterJdbcTemplate {
    
    public TrueKeyNamedParameterJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }
    
    /**
     * 根据给定的sql,paramSource,resultSetType创建PreparedStatementCreator
     * @param sql 查询语句
     * @param paramSource 参数容器绑定器
     * @param resultSetType - 结果集类型，它是 ResultSet.TYPE_FORWARD_ONLY、ResultSet.TYPE_SCROLL_INSENSITIVE 或 ResultSet.TYPE_SCROLL_SENSITIVE 之一
     * @return 相应于resultSetType的PreparedStatementCreator
     * @author 
     */
    protected PreparedStatementCreator getPreparedStatementCreator(String sql, SqlParameterSource paramSource, int resultSetType) {
        ParsedSql parsedSql = getParsedSql(sql);
        String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
        Object[] params = NamedParameterUtils.buildValueArray(parsedSql, paramSource, null);
        List<SqlParameter> declaredParameters = NamedParameterUtils.buildSqlParameterList(parsedSql, paramSource);
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(sqlToUse, declaredParameters);
        pscf.setResultSetType(resultSetType);
        return pscf.newPreparedStatementCreator(params);
    }
    
    /**
     * MSSQL分页专用查询
     * @param sql 查询语句
     * @param paramMap 参数Map
     * @param rowMapper 结果映射器
     * @param startRow 开始获取的记录位置
     * @return 分页结果集
     * @throws DataAccessException
     * @author 
     */
    public <T> List<T> queryMSSQLPage(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper, int startRow) throws DataAccessException {
        return getJdbcOperations().query(getPreparedStatementCreator(sql, new MapSqlParameterSource(paramMap), ResultSet.TYPE_SCROLL_INSENSITIVE),
                                         new RowMapperMSSQLPagingResultSetExtractor<T>(rowMapper, startRow));
    }
    
    
class RowMapperMSSQLPagingResultSetExtractor<T> implements ResultSetExtractor<List<T>> {
        
        private final RowMapper<T> rowMapper;
        
        private int startRow;
        
        /**
         * 创建一个RowMapperMSSQLPagingResultSetExtractor
         * @param rowMapper 用于转换每条记录的映射器
         * @param startRow 分页查询起始行
         * @author 
         */
        public RowMapperMSSQLPagingResultSetExtractor(RowMapper<T> rowMapper, int startRow) {
            this.rowMapper = rowMapper;
            this.startRow = startRow > 0 ? startRow : 0;
        }
        
        public List<T> extractData(ResultSet rs) throws SQLException {
            List<T> results = new ArrayList<T>();
            int rowNum = 0;
            if (startRow > 0 && !rs.absolute(startRow)) return results;
            while (rs.next()) {
                results.add(this.rowMapper.mapRow(rs, rowNum++));
            }
            return results;
        }
        
    }
    
}
