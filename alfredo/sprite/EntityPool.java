package alfredo.sprite;

import java.util.Iterator;

/**
 * The EntityPool is a specialized container class built for storing entities.
 * 
 * Essentially, it is meant to manage the memory for entities and provide thread locking mechanisms
 * to some extent.
 * @author TheMonsterFromTheDeep
 */
public class EntityPool implements Iterable<Entity> {
    //should it maybe just use the World?
    //or does it even matter?
    private final Entity dummy;
    
    Entity[] entities;
    
    int nextWrite; //The position to write the next added entity to; the next null object in the entities array
    
    private class EntityPoolIterator implements Iterator<Entity> {

        int index;
        
        void begin() { index = 0; }
        
        @Override
        public boolean hasNext() {
            return index < entities.length;
        }

        @Override
        public Entity next() {
            return entities[index++]; //TODO: Should this only give non-null Entities? Maybe implement some dummy Entity?
        }
        
    }
    
    EntityPoolIterator iterator;
    
    private void populate(int begin) {
        for (int i = begin; i < entities.length; i++) {
            if(entities[i] == null) {
                entities[i] = dummy;
            }
        }
    }
    
    public EntityPool() {
        entities = new Entity[20]; //Allocate space for 20 entities
        nextWrite = 0;
        
        iterator = new EntityPoolIterator();
        dummy = new Entity(); //Used to prevent nullness
        
        populate(0);
    }
    
    /**
     * Adds the specified Entity to the pool. If there is no room for the new Entity, then new
     * room is allocated and the old values are copied over.
     * @param e The Entity object to add to the pool.
     */
    public void add(Entity e) {
        if(e != null) { //Don't bother writing if entity is null
            entities[nextWrite] = e; //Add new entity
            while(nextWrite < entities.length && entities[nextWrite] != dummy) { nextWrite++; }
            if(nextWrite == entities.length) { //No new null values!
                Entity[] tmp = entities;
                entities = new Entity[(entities.length * 3) / 2]; //Increase memory by factor of 1.5
                System.arraycopy(tmp, 0, entities, 0, tmp.length); //Copy old values over
                populate(tmp.length);
            }
        }
    }
    
    /**
     * Removes the specified Entity from the pool. However, only removes one instance of the Entity -
     * if the Entity was added multiple times it is not removed. Call removeAll() in order to do that -
     * Entities should not be added more than once as a general principle.
     * 
     * This method requires looping through every Entity in the pool. It is much faster to call remove()
     * with an integer index value if possible.
     * @param e The Entity to remove from the pool.
     */
    public void remove(Entity e) {
        for(int i = 0; i < entities.length; i++) {
            if(entities[i] == e) {
                entities[i] = dummy;
                if(i < nextWrite) { nextWrite = i; }
                break;
            }
        }
    }
    
    /**
     * Removes all copies of the specified Entity from the pool. This will make sure the pool contains
     * no references to the specified Entity. However, as a general rule, Entities should not be added 
     * more than once.
     * 
     * This method requires looping through every Entity in the pool.
     * @param e The Entity to remove from the pool.
     */
    public void removeAll(Entity e) {
        for(int i = 0; i < entities.length; i++) {
            if(entities[i] == e) {
                entities[i] = dummy;
                if(i < nextWrite) { nextWrite = i; }
            }
        }
    }
    
    /**
     * Removes the Entity from the pool at the specified index. This is done by setting the Entity
     * stored in the pool to a dummy Entity, effectively releasing a hold on it.
     * @param index The index of the Entity to remove.
     */
    public void remove(int index) {
        entities[index] = dummy;
        if(index < nextWrite) { nextWrite = index; }
    }

    @Override
    public Iterator<Entity> iterator() {
        iterator.begin();
        return iterator;
    }
}