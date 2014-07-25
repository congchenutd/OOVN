package com.fujitsu.us.oovn.factory;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;

//public abstract class Factory<T>
//{
//    private static final HashMap<Class  <? extends Persistable>,
//                                 Factory<? extends Persistable>> _factories
//                             = new HashMap<Class  <? extends Persistable>, 
//                                           Factory<? extends Persistable>>();
//    
//    protected final Class<T> type;
//    
//    public Factory(Class<T> type) {
//        this.type = type;
//    }
//    
//    public static void register(Class  <? extends Persistable> type, 
//                                Factory<? extends Persistable> factory)
//    {
//        _factories.put(type, factory);
//    }
//        
//    public static Factory<? extends Persistable> getInstance(
//                                    Class<? extends Persistable> type)
//            throws InstantiationException, IllegalAccessException
//    {
//        return _factories.get(type);
//    }
//    
//    public abstract T create(Node node);
//    
//    public static void main(String[] argvs) throws InstantiationException, IllegalAccessException
//    {
//        PhysicalSwitch sw = Factory.getInstance(PhysicalSwitch.class)
//                                .create(null);
//    }
//}
//
//class PhysicalSwitchFactory2 extends Factory<PhysicalSwitch>
//{
//
//    public PhysicalSwitchFactory2() {
//        super(PhysicalSwitch.class);
//    }
//    
//    @Override
//    public PhysicalSwitch create(Node node)
//    {
//        return new PhysicalSwitch(null, null);
//    }
//}

/*
class Communication {}

class EmailMessage extends Communication {
    public EmailMessage() {
        System.out.println("Email");
    }
}

public class Factory {
    private static final Map<Class<? extends Communication>, 
                             Class<? extends Communication>> IMPLEMENTATIONS 
                             = new HashMap<Class<? extends Communication>, 
                                           Class<? extends Communication>>();

    public static <T extends Communication> T make(Class<T> type)
            throws InstantiationException, IllegalAccessException {
        return (T) IMPLEMENTATIONS.get(type).newInstance();
    }
    
    public static void register(Class<? extends Communication> type, 
                                Class<? extends Communication> factory) {
        IMPLEMENTATIONS.put(type, factory);
    }
    
    public static void main(String[] argvs)
    {
        Factory.register(EmailMessage.class, EmailMessage.class);
        try {
            EmailMessage emailMessage = Factory.make(EmailMessage.class);
        } catch (Exception e) {
        }
    }
}*/

class Product {}
class ProductA extends Product {}
class ProductB extends Product {}

public abstract class Factory
{
    private static final HashMap<Class  <? extends Product>,
                                 Factory> _factories
                             = new HashMap<Class  <? extends Product>, 
                                           Factory>();
    
    public static void register(Class  <? extends Product> type, 
                                Factory factory)
    {
        _factories.put(type, factory);
    }
        
    public static Factory getInstance(
                                    Class<? extends Product> type)
            throws InstantiationException, IllegalAccessException
    {
        return _factories.get(type);
    }
    
    public static <T extends Product> T create2(Class<T> type)
    {
        try {
            return (T) Factory.getInstance(type).create();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }
    
    public abstract Product create();
    
    public static void main(String[] argvs) throws InstantiationException, IllegalAccessException
    {
        ProductA p = Factory.create2(ProductA.class);
    }
}

class FactoryA extends Factory
{

    @Override
    public Product create() {
        return new ProductA();
    }
}