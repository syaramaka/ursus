package com.doj.ursus.impl;

import com.doj.ursus.dao.UserDao;
import com.doj.ursus.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private final String INSERT_SQL = "INSERT INTO ursus.users(name,address,email) values(?,?,?)";
    private final String FETCH_SQL = "select record_id, name, address, email from ursus.users";
    private final String FETCH_SQL_BY_ID = "select * from ursus.users where record_id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void insert(User user) {

    }

    @Override
    public User findById(int id) {
        return null;
    }

    public User create(final User user) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getName());
                ps.setString(2, user.getAddress());
                ps.setString(3, user.getEmail());
                return ps;
            }
        }, holder);

        int newUserId = holder.getKey().intValue();
        user.setId(newUserId);
        return user;
    }

    public List findAll() {
        System.out.println(" in findall method");
        return jdbcTemplate.query(FETCH_SQL, new UserMapper());
    }

    public User findUserById(int id) {
        //return jdbcTemplate.queryForObject(FETCH_SQL_BY_ID, new Object[] { id }, new UserMapper());
        return (User) jdbcTemplate.queryForObject(FETCH_SQL_BY_ID, new Object[] { id }, new UserMapper());
    }

}

class UserMapper implements RowMapper {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("record_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("address"));
        user.setEmail(rs.getString("email"));
        return user;
    }

}

