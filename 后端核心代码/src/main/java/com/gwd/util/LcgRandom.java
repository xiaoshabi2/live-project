package com.gwd.util;

import com.gwd.entity.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @program: java8
 * @description:
 * @author: ChenYu
 * @create: 2019-04-21 16:38
 **/
public class LcgRandom {
    public final AtomicLong seed = new AtomicLong();
    public final static long C = 1;
    public final static long A = 48271;
    public final static long M = (1L << 31) - 1;

    public LcgRandom(int seed) {
        this.seed.set(seed);
    }

    public LcgRandom() {
        this.seed.set(System.nanoTime());
    }

    public long nextLong() {
        seed.set(System.nanoTime());
        return (A * seed.longValue() + C) % M;
    }

    public int nextInt(int number) {
        return new Long((A * System.nanoTime() + C) % number).intValue();
    }

    public static List<User> getResult(Map<String, Integer> users, Integer awardNum) {
        Iterator it = users.entrySet().iterator();
        List<User> listUser = new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String name = (String) entry.getKey();
            Integer weight = (Integer) entry.getValue();
            // System.out.println(name + "  " + weight);
            listUser.add(new User(name, weight));
        }
        Random random = new Random();
        // 对所有参与的用户进行随机排序
        Collections.sort(listUser, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return random.nextInt(2) - 1;
            }
        });
        int i = 0;
        int size = listUser.size();
        LcgRandom lcg = new LcgRandom();
        List<User> awardList = new ArrayList<>();
        if (size > 0) {
            while (i < awardNum) {
                int ran = lcg.nextInt(size);
                // 对水群的用户降低获奖权重
                if (listUser.get(ran).getWeight() > 0) {
                    listUser.get(ran).setWeight(listUser.get(ran).getWeight() - 1);
                } else {
                    awardList.add(listUser.get(ran));
                    listUser.remove(ran);
                    size = listUser.size();
                    i++;
                }
            }
        }
        return awardList;
    }
}

