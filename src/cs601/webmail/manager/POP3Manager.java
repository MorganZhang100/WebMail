package cs601.webmail.manager;

import cs601.webmail.module.MailModule;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class POP3Manager {

    //private Socket socket;
    private SSLSocket s;
    private boolean debug = true;

    public static void main(String[] args) throws IOException {

        String server = "pop.gmail.com";
        String user = "morgantest601";
        String password = "zfzztc114";
//        String server = "pop.gmail.com";
//        String user = "morganzhang100@gmail.com";
//        String password = "zfzxsd114";
        POP3Manager pop3Manager = new POP3Manager(server,995);
        pop3Manager.recieveMail(user,password);
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
    public String getMessagedetail(BufferedReader in,MailModule mail){
        String message = "";
        String line;
        boolean body = false;
        String bodyContent = "";

        try{
            line = in.readLine();
            searchForInformation(line,mail);

            while(!".".equalsIgnoreCase(line)){
                message = message + line;
                line = in.readLine();

                if(!body) searchForInformation(line,mail);
                else if(!line.equals(".")) bodyContent = bodyContent + line;
                if(line.equals("")) body = true;
            }

            mail.setBody(bodyContent);

        }catch(Exception e){
            e.printStackTrace();
        }
        return message;
        //return "";
    }

    //retr
    public void retr(int mailNum,BufferedReader in,BufferedWriter out) throws IOException, SQLException, ClassNotFoundException {
        String result;
        //for(int i=1;i<=mailNum;i++){
        for(int i=1;i<=5;i++){
            result = getResult(sendServer("retr "+i,in,out));
            if(!"+OK".equals(result)){
                throw new IOException("Error in receiving email");
            }
            MailModule mail = new MailModule();

            System.out.println("#"+i+" email");

            String raw = getMessagedetail(in, mail);
            mail.setRaw(raw);

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
    public boolean recieveMail(String user,String password){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            user(user,in,out);
            pass(password,in,out);
            int mailNum;
            mailNum = stat(in,out);
            retr(mailNum,in,out);
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

    void searchForInformation(String s, MailModule mail) {
        //for Content-Type
        Matcher m = resultOfRegExSearch(s, "(?<=Content-Type: )[\\S\\s]+(?=;)");
        if(m.find()) mail.setContentType(m.group());

        //for Message-ID
        m = resultOfRegExSearch(s, "(?<=Message-I[Dd]: <)[\\S\\s]+(?=>)");
        if(m.find()) mail.setMessageId(m.group());

        //for charset
        m = resultOfRegExSearch(s, "(?<=charset=)[\\S\\s]+");
        if(m.find()) {
            String charset = m.group().replaceAll("^\"", "").replaceAll("\"$", "");
            mail.setCharset(charset);
        }

        //for Subject
        m = resultOfRegExSearch(s, "(?<=Subject: )[\\S\\s]+");
        if(m.find()) mail.setSubject(m.group());

        //for Content-Transfer-Encoding
        m = resultOfRegExSearch(s, "(?<=Content-Transfer-Encoding: )[\\S\\s]+");
        if(m.find()) mail.setContentTransferEncoding(m.group());

        //for MIME-Version
        m = resultOfRegExSearch(s, "(?<=[MIMEMime]-Version: )[\\S\\s]+");
        if(m.find()) mail.setMimeVersion(m.group());

        //for DATE
        m = resultOfRegExSearch(s, "(?<=Date: )[\\S\\s]+");
        if(m.find()) mail.setDate(m.group());

        //for FromName
        m = resultOfRegExSearch(s, "(?<=From: )[\\S\\s]+(?=<)");
        if(m.find()) {
            mail.setFromName(m.group());

            //for FromAddress
            m = resultOfRegExSearch(s, "(?<=<)[\\S\\s]+(?=>)");
            if(m.find()) mail.setFromAddress(m.group());
        }

        //for ToName
        m = resultOfRegExSearch(s, "(?<=To: )[\\S\\s]+");
        if(m.find()) {
            String onlyAddress = m.group();
            Matcher m2 = resultOfRegExSearch(s, "(?<=To: )[\\S\\s]+(?=<)");
            if(m2.find()) {
                mail.setToName(m2.group());

                //for ToAddress
                m2 = resultOfRegExSearch(s, "(?<=<)[\\S\\s]+(?=>)");
                if(m2.find()) mail.setToAddress(m2.group());
            }
            else {
                mail.setToName("");
                mail.setToAddress(onlyAddress);
            }
        }
    }
}