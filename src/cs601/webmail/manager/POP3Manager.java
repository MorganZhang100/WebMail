package cs601.webmail.manager;


import cs601.webmail.module.MailModule;
import cs601.webmail.module.UserModule;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class POP3Manager {

    //private Socket socket;
    private SSLSocket s;
    private boolean debug = true;

    public POP3Manager(UserModule user) throws IOException {
        String popServer = user.getPOP3Server();
        int popPort = user.getPOP3Port();

        String userName = user.getUserNameForRealEmailAccount();
        String pwd = user.getEmailPwd();
        int userId = user.getUser_id();

        POP3Manager pop3Manager = new POP3Manager(popServer,popPort);
        pop3Manager.recieveMail(userName,pwd,userId);
    }

    public POP3Manager(String server, int port) throws IOException{
        try{
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory
                    .getDefault();
            s = (SSLSocket) factory.createSocket(server, port);
            //socket = new Socket(server,port);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            System.out.println("POP3 Client connected to Server");
        }
    }

    public String getReturn(BufferedReader in){
        String line = "";
        try{
            line = in.readLine();
            if(debug){
                System.out.println("Stats of Server: "+line);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return line;
    }

    //get Stats Token
    public String getResult(String line){
        StringTokenizer st = new StringTokenizer(line," ");
        return st.nextToken();
    }

    //Send Commend
    private String sendServer(String str,BufferedReader in,BufferedWriter out) throws IOException{
        out.write(str);
        out.newLine();
        out.flush();
        if(debug)
        {
            System.out.println("Commend Sent: "+str);
        }
        return getReturn(in);
    }

    //user
    public void user(String user,BufferedReader in,BufferedWriter out) throws IOException{
        String result;
        result = getResult(getReturn(in));
        if(!"+OK".equals(result)){
            throw new IOException("Server Connect Failed");
        }
        result = getResult(sendServer("user "+user,in,out));
        if(!"+OK".equals(result)){
            throw new IOException("Login ID incorrect");
        }
    }

    //pass
    public void pass(String password,BufferedReader in,BufferedWriter out) throws IOException{
        String result;
        result = getResult(sendServer("pass "+password,in,out));
        if(!"+OK".equals(result)){
            System.out.println(">>>>>>>" + result);
            throw new IOException("Pwd incorrect");
        }
    }
    //stat
    public int stat(BufferedReader in,BufferedWriter out) throws IOException{
        String result;
        String line;
        int mailNum;
        line = sendServer("stat",in,out);
        StringTokenizer st = new StringTokenizer(line," ");
        result = st.nextToken();
        if(st.hasMoreTokens())
            mailNum = Integer.parseInt(st.nextToken());
        else
            mailNum=0;
        if(!"+OK".equals(result)){
            throw new IOException("Error in STAT commend");
        }
        System.out.println("All have " + mailNum + " emails");
        return mailNum;
    }

    //detail in email
    public String getMessagedetail(BufferedReader in){
        String message = "";
        String line;

        try{
            line = in.readLine();

            while(!".".equalsIgnoreCase(line)){
                message = message + line + "\n";
                line = in.readLine();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return message;
        //return "";
    }

    //retr
    public void retr(int mailNum,BufferedReader in,BufferedWriter out,int userId) throws Exception {
        String result;
        for(int i=1;i<=mailNum;i++){
            result = getResult(sendServer("retr "+i,in,out));
            if(!"+OK".equals(result)){
                throw new IOException("Error in receiving email");
            }
            MailModule mail = new MailModule();

            System.out.println("#"+i+" email");

            String raw = getMessagedetail(in);
            mail.setRaw(raw);
            mail.setEmailByRaw();
            mail.setUserId(userId);

            if(!mail.isComplate()) System.out.println(mail.toString());
            mail.toStorePreparedStatement();
        }
    }

    //quit
    public void quit(BufferedReader in,BufferedWriter out) throws IOException{
        String result;
        result = getResult(sendServer("QUIT",in,out));
        if(!"+OK".equals(result)){
            throw new IOException("Error in quit");
        }
    }

    //receiving email
    public boolean recieveMail(String user,String password,int userId){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            user(user,in,out);
            pass(password,in,out);
            int mailNum;
            mailNum = stat(in,out);
            retr(mailNum,in,out,userId);
            quit(in,out);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    Matcher resultOfRegExSearch(String s, String regEx) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(s);
        return m;
    }

}