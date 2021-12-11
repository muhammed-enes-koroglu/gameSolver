public abstract class Helper {

    
    public static <T> boolean arrayContains(T[] arr, T value){
        for(T elm: arr)
            if(elm.equals(value))
                return true;
        return false;
    }

    public static int countElement(int[] arr, int elm){
        int count = 0;
        for(int e: arr)
            if(e == elm)
                count++;
        return count;
    }

}
