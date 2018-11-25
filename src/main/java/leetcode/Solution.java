package leetcode;

import jdk.nashorn.internal.codegen.Label;

import java.util.*;

public class Solution {
    public String minWindow(String s, String t) {
        return  null;
    }

    public static void main(String[] args) {
        String s = "fbhdsjkahldgfbihsga";
        String t = "abs";
        int[] a = new int[s.length()];
        char[] chars = s.toCharArray();
        for (int i = 0; i < s.length() ; i++) {
           if(t.contains(String.valueOf(chars[i])))
               a[i] = 1;
        }
        for (int i = 0; i <a.length ; i++) {

            Set<Character> set = new HashSet<>();
            int beginindex;
            if(a[i]==0 ) continue;
            else {
                beginindex = i;
                for(int j = beginindex ; j < a.length ; j++){
                    if(a[j]==0 ) continue;
                    else{
                        set.add(chars[j]);
                        if(set.size() == t.length()){
                            a[beginindex] = j - beginindex;
                            break;
                        }
                    }
                }
            }
        }
        for (int i = 0; i <a.length ; i++) {
           int index = 0;
        }
    }

}
