package com.npichuzhkin.javacodetest.dao;

import com.npichuzhkin.javacodetest.models.Wallet;
import com.npichuzhkin.javacodetest.utils.exceptions.NoSuchWalletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WalletDAO implements DAO{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public WalletDAO (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Wallet findById(UUID id) throws NoSuchWalletException {
        return jdbcTemplate.query("SELECT * FROM wallet WHERE id=? FOR UPDATE", new Object[]{id}, new BeanPropertyRowMapper<>(Wallet.class)).stream().findAny().orElseThrow(() -> new NoSuchWalletException("No wallet with this ID was found"));
    }

    @Override
    public void update(Wallet wallet) {
        jdbcTemplate.update("UPDATE wallet SET amount=? WHERE id=?", wallet.getAmount(), wallet.getId());
    }
}
