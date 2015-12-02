/**
 * Created by user on 01/12/2015.
 */
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class SocketClient
{
    public static void main(String [] args)
    {
        String serverName = args[0];
        int port = Integer.parseInt(args[1]);
        try
        {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);
            client.setSoTimeout(10000000);
            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            ObjectOutputStream outs = new ObjectOutputStream(outToServer);
            ObjectInputStream ins = new ObjectInputStream(inFromServer);
            System.out.println("Silakan Login terlebih dahulu");
            Scanner scanner = new Scanner(System.in);
            String name = scanner.nextLine();
            out.writeUTF(name);
            System.out.println(in.readUTF());
            System.out.println("Masukan Pilihan mu "+name);
            int pilihan = scanner.nextInt();
            //Harus pindah baris setelah scan int
            scanner.nextLine();
            out.writeInt(pilihan);
            //Pilihan 1 Create room
            if (pilihan == 1){
                String roomName = scanner.nextLine();
                out.writeUTF(name);
                out.writeUTF(roomName);
                int countPlayer = in.readInt();
                System.out.println("Jumlah orang room " + roomName + " = " + countPlayer);
                System.out.println("Wanna Play Right Now? (Y/N)");
                String play = scanner.nextLine();
                out.writeUTF(play);
                String statusplay = in.readUTF();
                System.out.println(statusplay);
                if (statusplay.equals("No")) {
                        System.out.println("Harap Tunggu Jumlah Pemain < 3");
                }
                else System.out.println("Selamat Bermain");
                //Game Start Here
            }
            //Pilihan 2 Join room
            else if (pilihan == 2){
                String roomName = scanner.nextLine();
                out.writeUTF(name);
                out.writeUTF(roomName);
                int countPlayer = in.readInt();
                System.out.println("Jumlah orang room " + roomName + " = " + countPlayer);
                System.out.println("Wanna Play Right Now? (Y/N)");
                String play = scanner.nextLine();
                out.writeUTF(play);
                String statusplay = in.readUTF();
                System.out.println(statusplay);
                if (statusplay.equals("No")) {
                    System.out.println("Harap Tunggu Jumlah Pemain < 3");
                }
                else System.out.println("Selamat Bermain");
            }

            //client.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
