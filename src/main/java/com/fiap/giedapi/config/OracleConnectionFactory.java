package com.fiap.giedapi.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class OracleConnectionFactory {

    private static final Properties PROPS = loadProperties();


    private OracleConnectionFactory() {}

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = OracleConnectionFactory.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new IllegalStateException("Arquivo application.properties não encontrado!");
            }
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar propriedades do banco", e);
        }
        return props;
    }

    public static Connection getConnection() throws SQLException {
        String url = PROPS.getProperty("db.url");
        String user = PROPS.getProperty("db.user");
        String pass = PROPS.getProperty("db.password");

        return DriverManager.getConnection(url, user, pass);
    }
}
