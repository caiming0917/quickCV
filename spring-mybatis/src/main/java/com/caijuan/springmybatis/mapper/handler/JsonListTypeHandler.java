package com.caijuan.springmybatis.mapper.handler;

import org.apache.ibatis.executor.result.ResultMapException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class JsonListTypeHandler extends BaseTypeHandler<List<?>> {


    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<?> objects, JdbcType jdbcType) throws SQLException {
        if (objects == null) {
            try {
                preparedStatement.setNull(i, JdbcType.OTHER.TYPE_CODE);
            } catch (SQLException e) {
                throw new TypeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . "
                        + "Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. "
                        + "Cause: " + e, e);
            }
        } else {
            try {
                preparedStatement.setObject(i, FastJsonUtil.toJSON(objects), JdbcType.OTHER.TYPE_CODE);
            } catch (Exception e) {
                throw new TypeException("Error setting non null for parameter #" + i + " with JdbcType " + jdbcType
                        + " . "
                        + "Try setting a different JdbcType for this parameter or a different configuration property. "
                        + "Cause: " + e, e);
            }
        }
    }

    @Override
    public List<?> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        List<?> result;
        try {
            String value = resultSet.getString(s);
            result = value == null ? null : FastJsonUtil.parse(value, List.class);
        } catch (Exception e) {
            throw new ResultMapException(
                    "Error attempting to get column '" + s + "' from result list.  Cause: " + e, e);
        }
        if (resultSet.wasNull()) {
            return new ArrayList<>();
        } else {
            return result;
        }
    }

    @Override
    public List<?> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        List<?> result;
        try {
            String value = resultSet.getString(i);
            result = value == null ? null : FastJsonUtil.parse(value, List.class);
        } catch (Exception e) {
            throw new ResultMapException(
                    "Error attempting to get column #" + i + " from result list.  Cause: " + e, e);
        }
        if (resultSet.wasNull()) {
            return new ArrayList<>();
        } else {
            return result;
        }
    }

    @Override
    public List<?> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        List<?> result;
        try {
            String value = callableStatement.getString(i);
            result = value == null ? null : FastJsonUtil.parse(value, List.class);
        } catch (Exception e) {
            throw new ResultMapException(
                    "Error attempting to get column #" + i + " from callable statement.  Cause: " + e, e);
        }
        if (callableStatement.wasNull()) {
            return new ArrayList<>();
        } else {
            return result;
        }
    }
}
