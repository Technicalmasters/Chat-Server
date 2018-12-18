package chat.server;

import java.net.*;
import java.io.*;
import java.util.*;
public final class ChatClient
{
  protected Scanner sc;
  protected String username;
  protected DataInputStream is;
  protected DataOutputStream os;
  protected Thread listener,writing;

  public ChatClient (InputStream i, OutputStream o)
  {
		is = new DataInputStream (new BufferedInputStream (i));
		os = new DataOutputStream (new BufferedOutputStream (o));
		sc = new Scanner(System.in);
		try 
		{ 
      			System.out.println("Enter Username : ");
			username=sc.nextLine();
                        //System.out.println("yes");
			os.writeUTF(username);	
                        os.flush();
                        
                        String response=is.readUTF();
                        //System.out.println("yes");
                        if("Congratulation Successful completion".equals(response))
                        {
                            this.connect();
                            //System.out.println(response);
                            System.out.println("Your Chatting goes here\n");
                        }
                        else
                        {
                            System.out.println(response);
                        }
                }
                catch(IOException E)
                {
                    E.printStackTrace();
                }

	}
        
        public void connect()
        {
            listener = new Thread (new Runnable()
		{
			//@Override
			public void run()
			{
				while (true) 
				{ 		
					try 
					{ 
  		        			String msg = is.readUTF(); 
                        			System.out.println(msg); 
                    			} 
					catch (IOException e) 
					{ 
                        			e.printStackTrace(); 
                    			}
				}
			}
		});
		writing = new Thread(new Runnable()
		{
			//@Override
			public void run()
			{
				while(true)
				{
					String msg = sc.nextLine(); 
					try
					{ 
						os.writeUTF(msg); 
						os.flush();
					} 
					catch (IOException e) 
					{ 
					} 
				}
			}
		});	
		listener.start();
		writing.start();
        }


    public static void main (String args[]) throws IOException {
        Socket s = new Socket ("localhost",9000);
      ChatClient chatClient = new ChatClient (s.getInputStream (), s.getOutputStream ());
    }
}
