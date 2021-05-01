import java.util.LinkedList;

public class Cache<T> {
    LinkedList<T> cache;
    int cacheSize;
    int currentSize;
    public Cache(int cacheSize) {
        cache = new LinkedList<T>();
        this.cacheSize = cacheSize;
    }

    public void addObject(T newObject){
        if(cache.size()<cacheSize){
            cache.addFirst(newObject);
        }else{
            cache.removeLast();
            cache.addFirst(newObject);
        }
    }

    public void removeObject(T target){
        cache.remove(target);

    }

    public T getObject(T target) {
        T found = null;
                //if target is found, return target
                if (cache.contains(target)) {
                    found = target;
                    return found;
                }
        return found;
    }

    public void clearCache(){
            cache.clear();
    }


    //finish other methods..
}
