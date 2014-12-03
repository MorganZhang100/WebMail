package cs601.webmail.manager;

import cs601.webmail.module.MailModule;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.SocketException;
import java.util.StringTokenizer;

import cs601.webmail.module.UserModule;
import sun.misc.BASE64Encoder;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SMTPManager {

    private boolean debug = true;
    BASE64Encoder encode = new BASE64Encoder();
    private SSLSocket s;

    public SMTPManager(UserModule user) {
        try{
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            s = (SSLSocket) factory.createSocket(user.getSMTPServer(), user.getSMTPPort());

        }catch(SocketException e){
            System.out.println(e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            System.out.println("Connection has been built!");
        }

    }

    //HELO command
    public void helo(String server,BufferedReader in,BufferedWriter out) throws IOException{
        int result;
        result = getResult(in);

        if(result!=220){
            throw new IOException("Fail to connect to SMTP server");
        }
        result = sendServer("HELO "+server,in,out);

        if(result!=250)
        {
            throw new IOException("Register to SMTP server faild!");
        }
    }

    private int sendServer(String str,BufferedReader in,BufferedWriter out) throws IOException{
        out.write(str);
        out.newLine();
        out.flush();
        if(debug)
        {
            System.out.println("Command has been sent:"+str);
        }
        return getResult(in);
    }

    public int getResult(BufferedReader in){
        String line="";
        try{
            line = in.readLine();
            if(debug){
                System.out.println("Server state:"+line);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        StringTokenizer st = new StringTokenizer(line," ");
        return Integer.parseInt(st.nextToken());
    }

    public void authLogin(UserModule user,BufferedReader in,BufferedWriter out) throws IOException{
        int result;
        result = sendServer("AUTH LOGIN",in,out);
        if(result!=334){
            throw new IOException("Fail to auth login!");
        }

        result = sendServer(encode.encode(user.getUserNameForRealEmailAccount().getBytes()),in,out);

        if(result!=334){
            throw new IOException("user id error!");
        }

        result=sendServer(encode.encode(user.getEmailPwd().getBytes()),in,out);

        if(result!=235){
            throw new IOException("auth login failed!");
        }
    }

    //MAIL FROM command
    public void mailfrom(String source,BufferedReader in,BufferedWriter out) throws IOException{
        int result;
        result=sendServer("MAIL FROM:<"+source+">",in,out);
        if(result!=250){
            throw new IOException("MAIL FROM ERROR");
        }
    }

    //RECT TO COMMAND
    public void rcpt(String touchman,BufferedReader in,BufferedWriter out) throws IOException{
        int result;
        result=sendServer("RCPT TO:<"+touchman+">",in,out);
        if(result!=250){
            throw new IOException("RECT TO ERROR");
        }
    }

    //DATA
    public void data(String nickName,String from,String to,String subject,String content,BufferedReader in,BufferedWriter out) throws IOException{
        int result;
        result = sendServer("DATA",in,out);

        if(result!=354){
            throw new IOException("CAN'T SEND DATA");
        }
        out.write("From: " + nickName + "<" + from + ">");
        out.newLine();
        out.write("To: " + to);
        out.newLine();
        out.write("Subject: " + subject);
        out.newLine();
        out.newLine();
        out.write(content);
        out.newLine();

        result = sendServer("\r\n.\r\n",in,out);

        System.out.println(result);
        if(result!=250)
        {
            throw new IOException("SEND DATA ERROR");
        }
    }

    //QUIT COMMAND
    public void quit(BufferedReader in,BufferedWriter out) throws IOException{
        int result;
        result=sendServer("QUIT",in,out);
        if(result!=221){
            throw new IOException("quit unsuccessfully");
        }
    }

    public boolean sendMail(MailModule mail,UserModule user){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            helo(user.getSMTPServer(),in,out);//HELO
            authLogin(user,in,out);//AUTH LOGIN
            mailfrom(mail.getFromAddress(),in,out);//MAIL FROM
            rcpt(mail.getToFirstAddress(),in,out);//RCPT
            data(user.getNickName(),mail.getFromAddress(),mail.getToFirstAddress(),mail.getSubject(),mail.getBody(),in,out);//DATA
            quit(in,out);//QUIT
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Send fail");
            return false;
        }
        System.out.println("Send success");
        return true;
    }

}