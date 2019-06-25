package com.xtaller.party.core.service.impl;

import com.xtaller.party.core.service.IHitsService;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Service
public class HitsService implements IHitsService {

    private Connection conn;

    private Connection getConnection() {
        try {
            if (conn == null) {
                String url = "jdbc:mysql://10.1.151.88:3306/party_show?serverTimezone=UTC&useSSL=false&tinyInt1isBit=false";
                String user = "party";
                String password = "oDvQhfmBx8NahySw";
                return DriverManager.getConnection(url, user, password);
            } else {
                return conn;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean hit(String id, String type) {
        PreparedStatement ps;
        try {

            //根据类型确定表名
            String table = "";
            if ("article".equals(type)) {
                table = "article";
            } else if ("notice".equals(type)) {
                table = "notice";
            } else {
                return false;
            }
            //获取点击数
            ps = getConnection().prepareStatement("select hits from " + table + " where id = '" + id + "'");
            ResultSet rs = ps.executeQuery();
            int hits = -1;
            int result = -1;
            while (rs.next()) {
                hits = rs.getInt("hits");
                hits = hits + 1;
            }
            if (hits != -1) {
                ps.clearParameters();
                ps = getConnection().prepareStatement("update " + table + " set hits = " + hits + " where id = '" + id + "'");
                result = ps.executeUpdate();
            }
            ps.close();
            if (result == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
