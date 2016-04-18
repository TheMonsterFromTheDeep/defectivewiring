package alfredo.sprite;

import java.util.Iterator;

/**
 * Provides a container for a set of Entities meant to share similar logic, such as a group
 * of projectiles that all need to be the same.
 * @author TheMonsterFromTheDeep
 * @param <T> The subclass of Entity that forms this EntitySet.
 */
public class EntitySet<T extends Entity> implements Iterable<T> {

    private class EntitySetIterator implements Iterator<T> {
        int index;
        int lastIndex; //Used by removeCurrent()
        
        public void begin() {
            index = 0;
            while(index < entities.length && entities[index] == null) { index++; }
        }
        
        @Override
        public boolean hasNext() {
            return index < entities.length;
        }

        @Override
        public T next() {
            lastIndex = index; //lastIndex is the index of *this* object (the one returned)
            T ret = (T)entities[index];
            ++index;
            while(index < entities.length && entities[index] == null) { index++; }
            return ret;
        }   
    }
    
    EntitySetIterator iterator;
    Entity[] entities;
    
    EntityPool pool;
    int nextWrite; //similar to EntityPool, the next place an Entity will be written
    
    public EntitySet(EntityPool pool, int size) {
        this.pool = pool;
        
        entities = new Entity[size];
        iterator = new EntitySetIterator();
        
        nextWrite = 0;
    }
    
    public EntitySet(EntityPool pool) {
        this(pool, 20); //Default size of 20
    }
    
    public void add(T t) {
        if(t != null) {
            if(nextWrite > entities.length) {
                Entity[] tmp = entities;
                entities = new Entity[(entities.length * 3) / 2]; //Increase memory by factor of 1.5
                System.arraycopy(tmp, 0, entities, 0, tmp.length); //Copy old values over
            }
            entities[nextWrite] = t; //Add new entity
            while(nextWrite < entities.length && entities[nextWrite] != null) { nextWrite++; }
            if(nextWrite == entities.length) { //No new null values!
                Entity[] tmp = entities;
                entities = new Entity[(entities.length * 3) / 2]; //Increase memory by factor of 1.5
                System.arraycopy(tmp, 0, entities, 0, tmp.length); //Copy old values over
            }
            pool.add(t);
        }
    }
    
    public void remove(T t) {
        if(t != null) {
            for(int i = 0; i < entities.length; i++) {
                if(entities[i] == t) {
                    entities[i] = null;
                    if(i < nextWrite) { nextWrite = i; }
                    break;
                }
            }
            pool.remove(t);
        }
    }
    
    /**
     * Remove the current Entity that is being iterated over. This is perfectly safe to do.
     * 
     * This is also several orders faster than the normal remove() method.
     * 
     * If an array-related exception is thrown by this method, then it is not being properly used.
     */
    public void removeCurrent() {
        //iterator.lastIndex refers to the current object returned by the iterator.next() function.
        pool.remove(entities[iterator.lastIndex]);
        entities[iterator.lastIndex] = null;
    }
    
    public void removeAll() {
        for(int i = 0; i < entities.length; i++) {
            pool.remove(entities[i]);
            entities[i] = null;
        }
    }
    
    @Override
    public Iterator<T> iterator() {
        iterator.begin();
        return iterator;
    }
    
    public int count() {
        int count = 0;
        for (Entity e : entities) {
            if (e != null) {
                count++;
            }
        }
        return count;
    }
}