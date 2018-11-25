package leetcode;

public class TestMain {
    public static void main(String[] args) {


        System.out.println(Integer.MAX_VALUE);
    }
    public static int reachNumber(int target){
        return 0;
    }

    public int hammingWeight(int n) {
        String a = Integer.toBinaryString(n);
        int res = 0;
        for (char i : a.toCharArray() )
            if(i == '1') res ++;
        return  res;
    }
    public static int[] plusOne0(int[] digits) {
        double a = 0;
        for(int i = 0 ;i< digits.length ;i ++){
            a =a+ Math.pow(10,(digits.length-i-1))*digits[i];
        }
        a++;
        long b= (long) a;
        char[] cres = Long.toString(b).toCharArray();
        int res [] = new int[cres.length];
        for(int i = 0;i< cres.length ;i++){
            res[i] = Integer.parseInt(String.valueOf(cres[i]));
        }
        System.exit(0);
        return res;
    }

    public static int[] plusOne(int[] digits) {
        int[] arr = new int[digits.length+1];
        for(int j = digits.length-1;j >= 0;j --)
            arr[j+1] = digits[j];
        arr[arr.length-1] = arr[arr.length-1]+1;
        for(int i = arr.length-1;i > 0;i --){
            if(arr[i] == 10){
                arr[i]=0;
                arr[i-1] = arr[i-1] +1;
            }else{
                break;
            }
        }
        if(arr[0] == 0){
            int res[] = new int[arr.length-1];
            for(int i = 0 ;i<arr.length -1 ;i++)
                res[i] = arr[i+1];
            return res;
        }
        return arr;
    }





}
