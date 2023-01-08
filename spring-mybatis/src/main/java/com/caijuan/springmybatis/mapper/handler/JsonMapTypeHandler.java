package com.caijuan.springmybatis.mapper.handler;

import org.apache.ibatis.executor.result.ResultMapException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class JsonMapTypeHandler extends BaseTypeHandler<Map> {

    private static final String ERROR = "Error attempting to get column '";
    private static final String CAUSE = "' from result list.  Cause: ";

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Map map, JdbcType jdbcType) throws SQLException {
        if (map == null) {
            try {
                preparedStatement.setNull(i, JdbcType.OTHER.TYPE_CODE);
            } catch (SQLException e) {
                throw new TypeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . "
                        + "Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. "
                        + "Cause: " + e, e);
            }
        } else {
            try {
                preparedStatement.setObject(i, FastJsonUtil.toJSON(map), JdbcType.OTHER.TYPE_CODE);
            } catch (Exception e) {
                throw new TypeException("Error setting non null for parameter #" + i + " with JdbcType " + jdbcType
                        + " . "
                        + "Try setting a different JdbcType for this parameter or a different configuration property. "
                        + "Cause: " + e, e);
            }
        }
    }

    @Override
    public Map getNullableResult(ResultSet resultSet, String s) throws SQLException {
        try {

            String value = resultSet.getString(s);
            return value != null ? FastJsonUtil.parse(value, Map.class) : new HashMap();
        } catch (Exception e) {
            throw new ResultMapException(
                   ERROR + s + CAUSE + e, e);
        }
    }

    @Override
    public Map getNullableResult(ResultSet resultSet, int i) throws SQLException {
        try {
            String value = resultSet.getString(i);
            return value != null ? FastJsonUtil.parse(value, Map.class) : new HashMap();
        } catch (Exception e) {
            throw new ResultMapException(
                    ERROR + i + CAUSE + e, e);
        }
    }

    @Override
    public Map getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        try {
            String value = callableStatement.getString(i);
            return value != null ? FastJsonUtil.parse(value, Map.class) : new HashMap();
        } catch (Exception e) {
            throw new ResultMapException(
                    ERROR + i + CAUSE + e, e);
        }
    }
}
