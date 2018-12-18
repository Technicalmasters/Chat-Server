package chat.server;
//importing networking and input/output libraries
import java.net.*;
import java.io.*;
import java.sql.*;
//import java.util.concurrent.TimeUnit;



//creating ChatServer Class
public class ChatServer 
{
    DataInputStream input;
    DataOutputStream output;
    int auth;
    public Connection con;
    public Statement stmt;
    public  ResultSet rs;
    Socket Client_accept;    
     public void connect() 
    {
        try 
        {
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/chatserver?useSSL=false","root","Shivam@2209");  
            stmt=con.createStatement();
        } 
        catch (ClassNotFoundException | SQLException ex) 
        {
            System.out.println("Error in mysql class : "+ex);
        }
    }
    
    
    public int authenticate() 
    {
        try 
        {  
            String username;
            username = input.readUTF();
            rs=stmt.executeQuery("Select username from users where username='"+username+"' limit 1");
            if(rs.next()==false)
            {
                System.out.println("User does not Exist");
                return 0;
            }
            else
            {
                System.out.println("Sucessfull Login");
                return 1;
            }
        } 
        catch (SQLException | IOException ex) 
        {
            System.out.println("Exception arises in Authentication method "+ex);
        }
                   // System.out.println("4");

        return -1;
    }
        

        
    //creating function to start listening on the socket
    public void start_listening()
    {   
        try
        {  
            ServerSocket server=new ServerSocket(9000);
            
            while(true)
            {   
                System.out.println("Waiting for client");
                Client_accept=server.accept();
                
                input=new DataInputStream(new BufferedInputStream(Client_accept.getInputStream()));
                output=new DataOutputStream(new BufferedOutputStream(Client_accept.getOutputStream())); 
                
                auth=authenticate();
                
                switch (auth) {
                    case 1:
                        output.writeUTF("Congratulation Successful completion");
                        output.flush();             
                        ChatHandler client=new ChatHandler(Client_accept);
                        client.start();
                        break;
                    case 0:
                        output.writeUTF("Sorry You need to Register first");
                        output.flush();
                        break;
                    default:
                        System.out.println("Error in authentication");
                        break;
                }
            }
            
        }
        catch(IOException e)
        {
            System.out.println("Exception arises in start_listening:"+ e);
        }
    }
    
    public static void main(String[] args) throws InterruptedException
    {   
        
        ChatServer server=new ChatServer();
        server.connect();
        server.start_listening();
    }  
}





