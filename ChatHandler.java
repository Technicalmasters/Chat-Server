package chat.server;
import java.io.*;
import java.net.*;
import java.util.*;


public class ChatHandler extends Thread
{
    Socket client_accept;
    DataInputStream input;
    DataOutputStream output;
    String Username;
    static Vector list=new Vector();
    
    
    public ChatHandler(Socket client_accept)
    {
                   
        try 
        {
            this.client_accept=client_accept;
            input=new DataInputStream(new BufferedInputStream(client_accept.getInputStream()));
            output=new DataOutputStream(new BufferedOutputStream(client_accept.getOutputStream()));
        } catch (IOException ex) {
            System.out.println("Constructor exception :"+ex);
        }
    }   
    
        
   
    @Override
    public void run()
    {    
        try
        {   //System.out.println("s");
            list.addElement(this);
            //System.out.println("s1");
            while(true)
            {
               // input=new DataInputStream(new BufferedInputStream(client_accept.getInputStream()));
                String message=input.readUTF();
               // System.out.println("s3");
                brodcast(message,Username,this);
            }
        }
        catch(IOException e)
        {
            System.out.println("Exception in run of chathandler :"+e);
        }
        finally
        {   
            list.removeElement(this);
           try
            {   
               client_accept.close();
            }
            catch(IOException e)
            {
              System.out.println("Exception in run finally :"+e);
            }
        }
    }
    
    
    
   static void brodcast(String Message,String username,ChatHandler x)
   {
       synchronized(list)
       {
           Enumeration e=list.elements();
           while(e.hasMoreElements())
           {
               ChatHandler c=(ChatHandler)e.nextElement();
               
               if(c!=x)
               {
                 try
                 {   
                   //write the output to the output stream of the client
                   synchronized(c.output)
                   {
                       c.output.writeUTF(username+" :"+Message);
                   }
                   //flush the stream
                   c.output.flush();
                 }
                 catch(IOException ex)
                 {  System.out.println("Exception caught at brodcast :"+ex);
                     c.stop();
                 }
               }
               
           }
          
       }
   }
    
}
