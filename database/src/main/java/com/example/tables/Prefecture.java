/*
 * This file is generated by jOOQ.
 */
package com.example.tables;


import com.example.Indexes;
import com.example.Keys;
import com.example.Work;
import com.example.tables.records.PrefectureRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * 都道府県
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Prefecture extends TableImpl<PrefectureRecord> {

    private static final long serialVersionUID = 771895316;

    /**
     * The reference instance of <code>work.prefecture</code>
     */
    public static final Prefecture PREFECTURE = new Prefecture();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PrefectureRecord> getRecordType() {
        return PrefectureRecord.class;
    }

    /**
     * The column <code>work.prefecture.prefecture_cd</code>. 都道府県コード
     */
    public final TableField<PrefectureRecord, String> PREFECTURE_CD = createField("prefecture_cd", org.jooq.impl.SQLDataType.CHAR(2).nullable(false), this, "都道府県コード");

    /**
     * The column <code>work.prefecture.prefecture_name</code>. 都道府県名
     */
    public final TableField<PrefectureRecord, String> PREFECTURE_NAME = createField("prefecture_name", org.jooq.impl.SQLDataType.VARCHAR(191), this, "都道府県名");

    /**
     * Create a <code>work.prefecture</code> table reference
     */
    public Prefecture() {
        this(DSL.name("prefecture"), null);
    }

    /**
     * Create an aliased <code>work.prefecture</code> table reference
     */
    public Prefecture(String alias) {
        this(DSL.name(alias), PREFECTURE);
    }

    /**
     * Create an aliased <code>work.prefecture</code> table reference
     */
    public Prefecture(Name alias) {
        this(alias, PREFECTURE);
    }

    private Prefecture(Name alias, Table<PrefectureRecord> aliased) {
        this(alias, aliased, null);
    }

    private Prefecture(Name alias, Table<PrefectureRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("都道府県"));
    }

    public <O extends Record> Prefecture(Table<O> child, ForeignKey<O, PrefectureRecord> key) {
        super(child, key, PREFECTURE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Work.WORK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.PREFECTURE_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<PrefectureRecord> getPrimaryKey() {
        return Keys.KEY_PREFECTURE_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<PrefectureRecord>> getKeys() {
        return Arrays.<UniqueKey<PrefectureRecord>>asList(Keys.KEY_PREFECTURE_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Prefecture as(String alias) {
        return new Prefecture(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Prefecture as(Name alias) {
        return new Prefecture(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Prefecture rename(String name) {
        return new Prefecture(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Prefecture rename(Name name) {
        return new Prefecture(name, null);
    }
}
