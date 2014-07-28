package com.fujitsu.us.oovn.demo;

import java.io.*;

import com.fujitsu.us.oovn.core.Tenant;
import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.exception.InvalidVNOOperationException;

public class Demo
{
    private static Tenant _tenant;
    
    public static void menu()
    {
        while(true)
        {
            print("**********************\n");
            print("*      SPN Demo      *\n");
            print("**********************\n");
            print("* 1 - Create         *\n");
            print("* 2 - List           *\n");
            print("* 3 - Verify         *\n");
            print("* 4 - Start          *\n");
            print("* 5 - Pause          *\n");
            print("* 6 - Stop           *\n");
            print("* 7 - Decommission   *\n");
            print("* 0 - Exit the demo  *\n");
            print("**********************\n");
            print("Please choose your operation:\n");
            
            switch(Integer.valueOf(read()))
            {
            case 0:
                print("Bye!");
                return;
            case 1:
                create();
                break;
            case 2:
                list();
                break;
            case 3:
                verify();
                break;
            case 4:
                activate();
                break;
            case 5:
                break;
            case 6:
                deactivate();
                break;
            case 7:
                decommission();
                break;
            default:
                print("Wrong choice!\n");
                break;
            }
        }
    }
    
    private static void create()
    {
        print("Input the configuration file name: ");
        String fileName = read();
        
        VNO vno = new VNO(_tenant);
        try {
            vno.init(fileName);
            _tenant.registerVNO(vno);
            print("VNO (ID = " + vno.getID() + ") created.\n");
        } catch (InvalidVNOOperationException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void list() {
        print("Existing VNOs: \n" + _tenant.getVNOs().keySet().toString() + "\n");
    }
    
    private static void verify()
    {
        VNO vno = chooseVNO();
        if(vno == null)
            return;
        
        try {
            vno.verify();
            print("VNO (ID = " + vno.getID() + ") verified.\n");
        } catch (InvalidVNOOperationException e) {
            e.printStackTrace();
        }
    }
    
    private static void activate()
    {
        VNO vno = chooseVNO();
        if(vno == null)
            return;
        
        try {
            vno.activate();
            print("VNO (ID = " + vno.getID() + ") activated.\n");
        } catch (InvalidVNOOperationException e) {
            e.printStackTrace();
        }
    }
    
    private static void deactivate()
    {
        VNO vno = chooseVNO();
        if(vno == null)
            return;
        
        try {
            vno.deactivate();
            print("VNO (ID = " + vno.getID() + ") deactivated.\n");
        } catch (InvalidVNOOperationException e) {
            e.printStackTrace();
        }
    }
    
    private static void decommission()
    {
        VNO vno = chooseVNO();
        if(vno == null)
            return;
        
        try {
            vno.decommission();
            print("VNO (ID = " + vno.getID() + ") decommissioned.\n");
        } catch (InvalidVNOOperationException e) {
            e.printStackTrace();
        }
    }
    
    private static VNO chooseVNO()
    {
        print("VNO ID = ");
        VNO vno = _tenant.getVNO(Integer.valueOf(read()));
        if(vno == null)
        {
            print("No such VNO\n");
            return null;
        }
        return vno;
    }
    
    private static void print(String string) {
        System.out.print(string);
    }
    
    private static String read()
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            return br.readLine();
        } catch (IOException e) {
            return new String();
        }
    }
    
//    public static void main(String[] args)
//    {
//        PhysicalNetwork.init("PhysicalConfigDemo.json");
//        _tenant = new Tenant("Demo");
//        menu();
//    }

}
