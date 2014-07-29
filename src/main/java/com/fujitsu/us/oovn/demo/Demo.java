package com.fujitsu.us.oovn.demo;

import java.io.*;
import java.util.Map;

import com.fujitsu.us.oovn.controller.LearningSwitchController;
import com.fujitsu.us.oovn.core.Tenant;
import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.host.Host;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.exception.InvalidVNOOperationException;
import com.fujitsu.us.oovn.map.GlobalMap;
import com.fujitsu.us.oovn.verification.VerificationResult;

public class Demo
{
    private static Tenant _tenant;
    private static LearningSwitchController _controller;
    
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
            print("* 5 - Stop           *\n");
            print("* 6 - Decommission   *\n");
            print("* 0 - Exit the demo  *\n");
            print("**********************\n");
            print("Please choose your operation:\n");
            
            switch(Integer.valueOf(read()))
            {
            case 0:
                bye();
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
                start();
                break;
            case 5:
                stop();
                break;
            case 6:
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
        
        VerificationResult verficationResult = null;
        try {
            verficationResult = vno.verify();
        } catch (InvalidVNOOperationException e) {
            e.printStackTrace();
        }
        
        if(!verficationResult.isPassed())
        {
            print("VNO (ID = " + vno.getID() + ") failed the verification\n");
            print(verficationResult.toString() + "\n");
            return;
        }
        
        print("VNO (ID = " + vno.getID() + ") verified.\n");
            
        Map<Integer, Host> hosts = vno.getNetwork().getHosts();
        for(Map.Entry<Integer, Host> entry: hosts.entrySet())
        {
            Host host = entry.getValue();
            _controller.addToGroup(host.getIPAddress(), vno.getID());
        }
    }
    
    private static void start()
    {
        VNO vno = chooseVNO();
        if(vno == null)
            return;
        
        try {
            vno.start();
            print("VNO (ID = " + vno.getID() + ") activated.\n");
        } catch (InvalidVNOOperationException e) {
            e.printStackTrace();
        }
    }
    
    private static void stop()
    {
        VNO vno = chooseVNO();
        if(vno == null)
            return;
        
        try {
            vno.stop();
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
    
    private static void bye()
    {
        GlobalMap.getInstance().clear();
        print("Bye!");
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
    
    public static void main(String[] args) throws IOException
    {
        PhysicalNetwork.init("DemoP.json");
        GlobalMap.getInstance();
        _controller = new LearningSwitchController(6633);
        _tenant = new Tenant("Demo");
        menu();
    }

}
