/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.hotel;


/**
 *
 * @author A00288569
 */
   

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
public class RestConfig extends Application{
    @Override
    public Set<Class<?>> getClasses(){
        final Set<Class<?>> classes = 
                new HashSet<Class<?>>();
        classes.add(HotelResource.class);
        return classes;
    }
}

    

