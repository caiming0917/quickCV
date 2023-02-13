package com.caijuan.springmybatis.mapper.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UuidTypeHandler extends BaseTypeHandler<UUID> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, UUID uuid, JdbcType jdbcType) throws SQLException {
        preparedStatement.setObject(i, uuid);
    }

    @Override
    public UUID getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String result;
        return (result = resultSet.getString(s)) == null ? null : UUID.fromString(result);
    }

    @Override
    public UUID getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String result;
        return (result = resultSet.getString(i)) == null ? null : UUID.fromString(result);
    }

    @Override
    public UUID getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String result;
        return (result = callableStatement.getString(i)) == null ? null : UUID.fromString(result);
    }
}
