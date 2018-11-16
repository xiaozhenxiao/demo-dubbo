package com.jd.util.convert;

import com.jd.util.Money;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MoneyTypeHandler extends BaseTypeHandler<Money> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Money parameter, JdbcType jdbcType) throws SQLException {
        if(parameter == null) {
            ps.setLong(i, 0);
        } else {
            ps.setLong(i, parameter.getCent());
        }
    }

    @Override
    public Money getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Money money = new Money();
        money.setCent(rs.getLong(columnName));
        return money;
    }

    @Override
    public Money getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Money money = new Money();
        money.setCent(rs.getLong(columnIndex));
        return money;
    }

    @Override
    public Money getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        Money money = new Money();
        money.setCent(cs.getLong(columnIndex));
        return money;
    }

}
