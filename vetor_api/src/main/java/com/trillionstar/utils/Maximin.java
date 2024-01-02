package com.trillionstar.utils;

import java.util.*;

public class Maximin {
    // DATA
    String[] allCandidate;

    // STEP 1
    List<List<int[]>> vec_list;
    List<Float> weights;
    List<List<List<Integer>>> up;
    List<List<List<Integer>>> down;

    // STEP 2
    List<Integer> NW;
    List<Integer> NcW;
    List<Integer> NL;
    List<Integer> NcL;

    boolean flag_nw;
    boolean flag_ncw;
    boolean flag_nl;
    boolean flag_ncl;

    public List<int[]> extern(List<int[]> item) {
        for (int i = 0; i < item.size(); i++) {
            int tail = ((int[]) item.get(i))[1];
            for (int j = 0; j < item.size(); j++) {
                int head = ((int[]) item.get(j))[0];
                if (tail == head) {
                    int a = ((int[]) item.get(i))[0];
                    int b = ((int[]) item.get(j))[1];
                    int[] c = {a, b};
                    boolean flag = true;
                    for (int l = 0; l < item.size(); l++) {
                        if (Arrays.toString(item.get(l)).equals(Arrays.toString(c))) {
                            flag = false;
                            break;
                        } else
                            continue;
                    }
                    if (flag) {
                        item.add(c);
                    }
                }
            }
        }
        return item;
    }

    public List<Integer> getOneUp(List<int[]> list, int aim) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (((int[]) list.get(i))[1] == aim) {
                result.add(((int[]) list.get(i))[0]);
            }
        }
        result.add(aim);
        return result;
    }

    public List<Integer> getOneDown(List<int[]> list, int aim) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (((int[]) list.get(i))[0] == aim) {
                result.add(((int[]) list.get(i))[1]);
            }
        }
        result.add(aim);
        return result;
    }

    public void Winner() {
        NW = new ArrayList<>();
        NcW = new ArrayList<>();

        boolean flag_end = false;
        float s_c_w1;

        for (int i = 0; i < allCandidate.length; i++) {
            int c = Integer.parseInt(allCandidate[i]);
            System.out.println("候选人" + c);
            flag_nw = true;
            flag_ncw = true;
            flag_end = false;

            Long start = System.currentTimeMillis();
            for (int j = 0; j < allCandidate.length; j++) {
                if (!flag_end) {
                    int w = Integer.parseInt(allCandidate[j]);
                    for (int k = 0; k < allCandidate.length; k++) {
                        int w1 = Integer.parseInt(allCandidate[k]);
                        if (w1 != w && w1 != c && w != c && !flag_end) {

                            s_c_w1 = 0;
                            float[] s_w_d = new float[allCandidate.length];
                            for (int i1 = 0; i1 < vec_list.size(); i1++) {
                                boolean flag = false;
                                for (int[] ints : vec_list.get(i1)) {
                                    if (ints[0] == c && ints[1] == w1) {
                                        flag = true;
                                        break;
                                    }
                                }
                                if (flag) {//c > w1
                                    s_c_w1 += weights.get(i1);
                                    for (String s : allCandidate) {
                                        int d = Integer.parseInt(s);
                                        if (d != w && !up.get(w - 1).get(i1).contains(d)) {
                                            s_w_d[d - 1] += weights.get(i1);
                                        }
                                    }
                                } else { // c !> w1
                                    boolean flag_update = true;
                                    for (int[] ints : vec_list.get(i1)) {
                                        if (ints[0] == w1 && ints[1] == c) {
                                            flag_update = false;
                                        }
                                    }
                                    if (flag_update) {
                                        int[] b = new int[2];
                                        b[0] = w1;
                                        b[1] = c;
                                        List<int[]> updateList = new ArrayList<>(vec_list.get(i1));
                                        updateList.add(b);
                                        updateList = extern(updateList);
                                        List<Integer> new_up_w = getOneUp(updateList, w);
                                        //List<Integer> new_up_w1 = up.get(w1-1).get(i1);
                                        //if(!new_up_w.contains(w1)){
                                        //    new_up_w1.add(w);
                                        //}
                                        for (String s : allCandidate) {
                                            int d = Integer.parseInt(s);
                                            if (d != w && !new_up_w.contains(d)) {
                                                //if(new_up_w.contains(c) && !new_up_w1.contains(d)){
                                                s_w_d[d - 1] += weights.get(i1);
                                                //}
                                            }
                                        }
                                    } else {
                                        //List<Integer> new_up_w1 = up.get(w1-1).get(i1);
                                        //if(!up.get(w-1).get(i1).contains(w1)){
                                        //    new_up_w1.add(w);
                                        //}
                                        for (String s : allCandidate) {
                                            int d = Integer.parseInt(s);
                                            if (d != w && !up.get(w - 1).get(i1).contains(d)) {
                                                //if(up.get(w-1).get(i1).contains(c)&&!new_up_w1.contains(d)){
                                                s_w_d[d - 1] += weights.get(i1);
                                                //}
                                            }
                                        }
                                    }
                                }
                            }
                            // STEP 2.4
                            int fnw = 1;
                            int fncw = 1;
                            for (int s = 0; s < s_w_d.length; s++) {
                                if (s != w - 1) {
                                    if (s_w_d[s] >= s_c_w1) {
                                        fnw += 1;
                                        if (s_w_d[s] > s_c_w1) {
                                            fncw += 1;
                                        }
                                    }
                                }
                            }
                            if (fnw == s_w_d.length) {
                                flag_nw = false;
                            }
                            if (fncw == s_w_d.length) {
                                flag_ncw = false;
                            }
                            if (!flag_nw && !flag_ncw) {
                                flag_end = true;
                                break;
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            if (flag_nw) {
                NW.add(c);
            }
            if (flag_ncw) {
                NcW.add(c);
            }
            Long end = System.currentTimeMillis();
            System.out.println("NW的时间是" + (end - start) + "ms");
        }
    }

    public void Loser() {
        NL = new ArrayList<>();
        NcL = new ArrayList<>();
        boolean flag_end = false;
        float s_v1_l;

        for (int i = 0; i < allCandidate.length; i++) {
            int l = Integer.parseInt(allCandidate[i]);
            System.out.println("候选人" + l);
            flag_nl = true;
            flag_ncl = true;
            flag_end = false;

            Long start = System.currentTimeMillis();
            for (int j = 0; j < allCandidate.length; j++) {
                if (!flag_end) {
                    int v = Integer.parseInt(allCandidate[j]);
                    for (int k = 0; k < allCandidate.length; k++) {
                        int v1 = Integer.parseInt(allCandidate[k]);
                        if (v1 != v && v1 != l && v != l && !flag_end) {

                            s_v1_l = 0;
                            float[] s_e_v = new float[allCandidate.length];
                            for (int i1 = 0; i1 < vec_list.size(); i1++) {
                                boolean flag = false;
                                for (int[] ints : vec_list.get(i1)) {
                                    if (ints[0] == v1 && ints[1] == l) {
                                        flag = true;
                                        break;
                                    }
                                }
                                if (flag) {//v1 > l
                                    s_v1_l += weights.get(i1);
                                    for (String s : allCandidate) {
                                        int e = Integer.parseInt(s);
                                        if (e != v && !down.get(v - 1).get(i1).contains(e)) {
                                            s_e_v[e - 1] += weights.get(i1);
                                        }
                                    }
                                } else { // v1 !> l
                                    boolean flag_update = true;
                                    for (int[] ints : vec_list.get(i1)) {
                                        if (ints[0] == l && ints[1] == v1) {
                                            flag_update = false;
                                        }
                                    }
                                    if (flag_update) {
                                        int[] b = new int[2];
                                        b[0] = l;
                                        b[1] = v1;
                                        List<int[]> updateList = new ArrayList<>(vec_list.get(i1));
                                        updateList.add(b);
                                        updateList = extern(updateList);
                                        List<Integer> new_down = getOneDown(updateList, v);
                                        for (String s : allCandidate) {
                                            int e = Integer.parseInt(s);
                                            if (e != v && !new_down.contains(e)) {
                                                s_e_v[e - 1] += weights.get(i1);
                                            }
                                        }
                                    } else {
                                        for (String s : allCandidate) {
                                            int e = Integer.parseInt(s);
                                            if (e != v && !down.get(v - 1).get(i1).contains(e)) {
                                                s_e_v[e - 1] += weights.get(i1);
                                            }
                                        }
                                    }
                                }
                            }
                            // STEP 2.4
                            int fnl = 1;
                            int fncl = 1;
                            for (int s = 0; s < s_e_v.length; s++) {
                                if (s != j) {
                                    if (s_e_v[s] >= s_v1_l) {
                                        fnl += 1;
                                        if (s_e_v[s] > s_v1_l) {
                                            fncl += 1;
                                        }
                                    }
                                }
                            }
                            if (fnl == s_e_v.length) {
                                flag_nl = false;
                            }
                            if (fncl == s_e_v.length) {
                                flag_ncl = false;
                            }
                            if (!flag_nl && !flag_ncl) {
                                flag_end = true;
                                break;
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            if (flag_nl) {
                NL.add(l);
            }
            if (flag_ncl) {
                NcL.add(l);
            }
            Long end = System.currentTimeMillis();
            System.out.println("NL的时间是" + (end - start) + "ms");
        }
    }

    public void getUp() {
        up = new ArrayList<List<List<Integer>>>();
        for (List<int[]> ints : vec_list) {
            ints = extern(ints);
        }
        for (String s : allCandidate) {
            List<List<Integer>> temp = new ArrayList<>();
            for (List<int[]> ints : vec_list) {
                temp.add(getOneUp(ints, Integer.parseInt(s)));
            }
            up.add(temp);
        }
    }

    public void getDown() {
        down = new ArrayList<List<List<Integer>>>();
        for (List<int[]> ints : vec_list) {
            ints = extern(ints);
        }
        for (String s : allCandidate) {
            List<List<Integer>> temp = new ArrayList<>();
            for (List<int[]> ints : vec_list) {
                temp.add(getOneDown(ints, Integer.parseInt(s)));
            }
            down.add(temp);
        }
    }

    public Object[] enter(List<List<int[]>> veclist,List<Float> ws, String[] candidates) {
        vec_list = veclist;
        allCandidate = candidates;
        weights = ws;
        getUp();
        Winner();
        getDown();
        Loser();
        String ref="";
        List sort = reference();
        for (int i = 0; i < sort.size(); i++) {
            Object o = sort.get(i);
            ref =ref +o.toString()+",";
        }
        ref = ref.substring(0, ref.length() - 1);
        Object[] re = {NW,NL,ref};
        return re;
    }

    public List reference() {
        int refer_sort[] = new int[allCandidate.length];
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < allCandidate.length; i++) {
            for (int j = 0; j < up.size(); j++) {
                if (j != i) {
                    for (List<Integer> integers : up.get(j)) {
                        if (integers.contains(i + 1)) {
                            refer_sort[i] += 1;
                        }
                    }
                }
            }
            map.put(i + 1, refer_sort[i]);
        }
        List<Map.Entry<Integer, Integer>> infoIds = new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<Integer, Integer>>() {
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return (o2.getValue() - o1.getValue());
            }
        });
        return infoIds;
    }

}
