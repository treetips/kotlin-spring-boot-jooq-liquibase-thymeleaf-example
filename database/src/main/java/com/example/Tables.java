/*
 * This file is generated by jOOQ.
 */
package com.example;


import com.example.tables.Address;
import com.example.tables.Prefecture;
import com.example.tables.Role;
import com.example.tables.User;

import javax.annotation.Generated;


/**
 * Convenience access to all tables in work
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * 住所
     */
    public static final Address ADDRESS = com.example.tables.Address.ADDRESS;

    /**
     * 都道府県
     */
    public static final Prefecture PREFECTURE = com.example.tables.Prefecture.PREFECTURE;

    /**
     * 役割
     */
    public static final Role ROLE = com.example.tables.Role.ROLE;

    /**
     * ユーザ
     */
    public static final User USER = com.example.tables.User.USER;
}
