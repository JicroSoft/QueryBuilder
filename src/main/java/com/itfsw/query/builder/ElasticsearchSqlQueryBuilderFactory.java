/*
 * Copyright (c) 2019.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.itfsw.query.builder;

import java.util.ArrayList;
import java.util.List;

import com.itfsw.query.builder.config.ElasticSqlQueryBuilderConfig;
import com.itfsw.query.builder.config.SqlQueryBuilderConfig;
import com.itfsw.query.builder.support.builder.SqlBuilder;
import com.itfsw.query.builder.support.filter.SqlInjectionAttackFilter;
import com.itfsw.query.builder.support.parser.AbstractSqlRuleParser;
import com.itfsw.query.builder.support.parser.IRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.BeginsWithRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.BetweenRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.ContainsRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.DefaultGroupParser;
import com.itfsw.query.builder.support.parser.elasticsql.EndsWithRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.EqualRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.GreaterOrEqualRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.GreaterRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.INRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.IsEmptyRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.IsNotEmptyRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.IsNotNullRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.IsNullRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.LessOrEqualRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.LessRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.NotBeginsWithRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.NotBetweenRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.NotContainsRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.NotEndsWithRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.NotEqualRuleParser;
import com.itfsw.query.builder.support.parser.elasticsql.NotInRuleParser;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: Junfeng.Li
 * @time:2019/10/31 17:03
 * ---------------------------------------------------------------------------
 */
public class ElasticsearchSqlQueryBuilderFactory extends AbstractQueryBuilderFactory {
    private ElasticSqlQueryBuilderConfig config;   // 配置

    /**
     * 构造函数
     * @param config
     */
    public ElasticsearchSqlQueryBuilderFactory(ElasticSqlQueryBuilderConfig config) {
        super();
        this.config = config;

        // ------------------------ filter ---------------------------
        filters.add(new SqlInjectionAttackFilter(this.config.getDbType()));    // sql 注入

        // ------------------------- group parser ----------------------
        groupParser = new DefaultGroupParser();

        // ---------------------- rule parser ----------------------------
        ruleParsers.add(new EqualRuleParser());
        ruleParsers.add(new NotEqualRuleParser());
        ruleParsers.add(new INRuleParser());
        ruleParsers.add(new NotInRuleParser());
        ruleParsers.add(new LessRuleParser());
        ruleParsers.add(new LessOrEqualRuleParser());
        ruleParsers.add(new GreaterRuleParser());
        ruleParsers.add(new GreaterOrEqualRuleParser());
        ruleParsers.add(new BetweenRuleParser());
        ruleParsers.add(new NotBetweenRuleParser());
        ruleParsers.add(new BeginsWithRuleParser());
        ruleParsers.add(new NotBeginsWithRuleParser());
        ruleParsers.add(new ContainsRuleParser());
        ruleParsers.add(new NotContainsRuleParser());
        ruleParsers.add(new EndsWithRuleParser());
        ruleParsers.add(new NotEndsWithRuleParser());
        ruleParsers.add(new IsEmptyRuleParser());
        ruleParsers.add(new IsNotEmptyRuleParser());
        ruleParsers.add(new IsNullRuleParser());
        ruleParsers.add(new IsNotNullRuleParser());
    }

    /**
     * 构造函数
     */
    public ElasticsearchSqlQueryBuilderFactory() {
        this(new ElasticSqlQueryBuilderConfig());
    }

    /**
     * 获取builder
     * @return
     */
    public SqlBuilder builder() {
        List<IRuleParser> sqlRuleParsers = new ArrayList<>();
        for (IRuleParser parser : ruleParsers) {
            if (parser instanceof AbstractSqlRuleParser) {
                sqlRuleParsers.add(parser);
            }
        }
        return new SqlBuilder(groupParser, sqlRuleParsers, filters);
    }
}
