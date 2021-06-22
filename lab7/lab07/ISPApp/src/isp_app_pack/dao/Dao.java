package isp_app_pack.dao;

import java.util.List;

public interface Dao<T> {
    
    T get(long id);
     
    List<T> getAll();
     
    boolean add(T t);
     
    boolean update(T t);
    
    boolean delete(long id);
    
    Object[] getColumnsIdentifiers();
    
    Object[] instanceToTable(T t);

    T tableToInstance(Object[] p);
    
    long getNextId();
}
