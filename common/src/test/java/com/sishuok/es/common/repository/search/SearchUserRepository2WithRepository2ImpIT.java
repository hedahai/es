/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sishuok.es.common.repository.search;

import com.sishuok.es.common.entity.Sex;
import com.sishuok.es.common.entity.User;
import com.sishuok.es.common.entity.search.filter.AndCondition;
import com.sishuok.es.common.entity.search.filter.Condition;
import com.sishuok.es.common.entity.search.SearchOperator;
import com.sishuok.es.common.entity.search.SearchRequest;
import com.sishuok.es.common.entity.search.Searchable;
import com.sishuok.es.common.entity.search.filter.OrCondition;
import com.sishuok.es.common.repository.UserRepository2;
import com.sishuok.es.common.test.BaseUserIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * <p>测试查询条件</p>
 * <p>User: Zhang Kaitao
 * <p>Date: 13-1-15 下午12:21
 * <p>Version: 1.0
 */
public class SearchUserRepository2WithRepository2ImpIT extends BaseUserIT {


    @Autowired
    private UserRepository2 userRepository2;

    @Test
    public void testEqForEnum() {
        int count = 15;
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.getBaseInfo().setSex(Sex.male);
            userRepository2.save(user);
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("baseInfo.sex_eq", "male");
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(count, userRepository2.count(search));
    }

    @Test
    public void testNotEqForEnum() {
        int count = 15;
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.getBaseInfo().setSex(Sex.male);
            userRepository2.save(user);
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("baseInfo.sex_ne", "male");
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(0, userRepository2.count(search));
    }

    @Test
    public void testEqForInteger() {
        int count = 15;
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.getBaseInfo().setAge(15);
            userRepository2.save(user);
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("baseInfo.age_eq", "15");
        SearchRequest search = new SearchRequest(searchParams);
        assertEquals(count, userRepository2.count(search));
    }


    @Test
    public void testEqForDate() throws ParseException {
        int count = 15;
        String dateStr = "2012-01-15 16:59:00";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.setRegisterDate(df.parse(dateStr));
            userRepository2.save(user);
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("registerDate_eq", dateStr);
        SearchRequest search = new SearchRequest(searchParams);
        assertEquals(count, userRepository2.count(search));
    }


    @Test
    public void testLtAndGtForDate() throws ParseException {
        int count = 15;
        String dateStr = "2012-01-15 16:59:00";
        String dateStrFrom = "2012-01-15 16:58:00";
        String dateStrEnd = "2012-01-15 16:59:01";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.getBaseInfo().setBirthday(new Timestamp(df.parse(dateStr).getTime()));
            userRepository2.save(user);
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("baseInfo.birthday_gt", dateStrFrom);
        searchParams.put("baseInfo.birthday_lt", dateStrEnd);
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(count, userRepository2.count(search));
    }


    @Test
    public void testLteAndGteForDate() throws ParseException {
        int count = 15;
        String dateStr = "2012-01-15 16:59:00";
        String dateStrFrom = "2012-01-15 16:59:00";
        String dateStrEnd = "2012-01-15 16:59:00";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.getBaseInfo().setBirthday(new Timestamp(df.parse(dateStr).getTime()));
            userRepository2.save(user);
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("baseInfo.birthday_gte", dateStrFrom);
        searchParams.put("baseInfo.birthday_lte", dateStrEnd);
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(count, userRepository2.count(search));
    }


    @Test
    public void testIsNotNull() throws ParseException {
        int count = 15;
        for (int i = 0; i < count; i++) {
            User user = createUser();
            userRepository2.save(user);
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("username_isNotNull", null);
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(count, userRepository2.count(search));
    }

    @Test
    public void testInWithList() throws ParseException {
        List<Long> uuids = new ArrayList<Long>();
        int count = 15;
        for (int i = 0; i < count; i++) {
            User user = createUser();
            userRepository2.save(user);
            uuids.add(user.getId());
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("id_in", uuids);
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(count, userRepository2.count(search));
    }


    @Test
    public void testInWithArray() throws ParseException {
        List<Long> uuids = new ArrayList<Long>();
        int count = 15;
        for (int i = 0; i < count; i++) {
            User user = createUser();
            userRepository2.save(user);
            uuids.add(user.getId());
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("id_in", uuids.toArray(new Long[count]));
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(count, userRepository2.count(search));
    }

    @Test
    public void testInWithSingleValue() throws ParseException {
        List<Long> uuids = new ArrayList<Long>();
        int count = 15;
        for (int i = 0; i < count; i++) {
            User user = createUser();
            userRepository2.save(user);
            uuids.add(user.getId());
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("id_in", uuids.get(0));
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(1, userRepository2.count(search));
    }


    @Test
    public void testOr() {
        int count = 15;
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.getBaseInfo().setAge(i);
            userRepository2.save(user);
        }
        Searchable search = Searchable.newSearchable();
        search.or(
                Condition.newCondition("baseInfo.age", SearchOperator.gt, 13),
                Condition.newCondition("baseInfo.age", SearchOperator.lt, 1)
        );

        assertEquals(2, userRepository2.count(search));
    }



    @Test
    public void testAndOr() {
        int count = 15;
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.getBaseInfo().setAge(i);
            userRepository2.save(user);
        }
        Searchable search = Searchable.newSearchable();
        Condition condition11 = Condition.newCondition("baseInfo.age", SearchOperator.gte, 0);
        Condition condition12 = Condition.newCondition("baseInfo.age", SearchOperator.lte, 2);
        AndCondition and1 = AndCondition.and(condition11, condition12);

        Condition condition21 = Condition.newCondition("baseInfo.age", SearchOperator.gte, 3);
        Condition condition22 = Condition.newCondition("baseInfo.age", SearchOperator.lte, 5);

        AndCondition and2 = AndCondition.and(condition21, condition22);

        search.or(and1, and2);

        assertEquals(6, userRepository2.count(search));
    }



    @Test
    public void testOrAnd() {
        int count = 15;
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.getBaseInfo().setAge(i);
            userRepository2.save(user);
        }
        Searchable search = Searchable.newSearchable();

        Condition condition11 = Condition.newCondition("baseInfo.age", SearchOperator.eq, 3);
        Condition condition12 = Condition.newCondition("baseInfo.age", SearchOperator.eq, 5);
        OrCondition or1 = OrCondition.or(condition11, condition12);

        Condition condition21 = Condition.newCondition("baseInfo.age", SearchOperator.eq, 3);
        Condition condition22 = Condition.newCondition("baseInfo.age", SearchOperator.eq, 4);

        OrCondition or2 = OrCondition.or(condition21, condition22);

        //( =3 or =5) and (=3 or =4)
        search.and(or1, or2);

        assertEquals(1, userRepository2.count(search));
    }


    @Test
    public void testNestedOrAnd() {
        int count = 15;
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.getBaseInfo().setAge(i);
            userRepository2.save(user);
        }
        Searchable search = Searchable.newSearchable();

        Condition condition11 = Condition.newCondition("baseInfo.age", SearchOperator.eq, 3);

        Condition condition12 = Condition.newCondition("baseInfo.age", SearchOperator.lte, 4);
        Condition condition13 = Condition.newCondition("baseInfo.age", SearchOperator.gte, 4);

        OrCondition or11 = OrCondition.or(condition12, condition13);
        OrCondition or1 = OrCondition.or(condition11, or11);

        Condition condition21 = Condition.newCondition("baseInfo.age", SearchOperator.eq, 3);
        Condition condition22 = Condition.newCondition("baseInfo.age", SearchOperator.eq, 4);

        OrCondition or2 = OrCondition.or(condition21, condition22);

        //( =3 or (>=4 and <=4)) and (=3 or =4)
        search.and(or1, or2);

        assertEquals(2, userRepository2.count(search));
    }


}
