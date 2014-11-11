package cs601.webmail.module;

import cs601.webmail.manager.DecodeManager;
import cs601.webmail.manager.SQLQueryManager;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MailModule {

    private String fromName = null;
    private String fromAddress = null;
    private String toName = null;
    private String toAddress = null;
    private String subject = null;
    private String mimeVersion = null;
    private String contentType = null;
    private String charset = null;
    private String contentTransferEncoding = null;
    private String date = null;
    private String body = null;
    private String messageId = null;
    private String raw = null;
    private boolean complate = false;
    private int mailId = -1;


    public MailModule(UserModule user,int mail_id) throws SQLException, UnsupportedEncodingException, ClassNotFoundException {
        SQLQueryManager sql = new SQLQueryManager("select mail_id,from_name,from_address,to_address,subject,body,content_transfer_encoding,charset from MAIL where user_id = " + 0 + " and mail_id = " + mail_id + " ; ");
        ResultSet rs = sql.query();

        if(rs.next()) {
            this.setMailId(rs.getInt("mail_id"));
            this.setFromName(DecodeManager.getUTF8FromSubject(rs.getString("from_name")));
            this.setFromAddress(rs.getString("from_address"));
            this.setToAddress(rs.getString("to_address"));
            this.setSubject(DecodeManager.getUTF8FromSubject(rs.getString("subject")));
            this.setCharset(rs.getString("charset"));

            this.setContentTransferEncoding(rs.getString("content_transfer_encoding"));

            String body = rs.getString("body");
            if(this.getContentTransferEncoding().equals("base64")) body = DecodeManager.Base64(body,this.getCharset());
            if(this.getContentTransferEncoding().equals("quoted-printable")) body = DecodeManager.DP(body,this.getCharset());
            this.setBody(body);
            sql.close();
        }
        //**此处应该处理rs.next()不存在的情况
    }

    public MailModule() {

    }

    public MailModule(String subject, String fromAddress, String toAddress, String body) {
        this.subject = subject;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.body = body;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMimeVersion() {
        return mimeVersion;
    }

    public void setMimeVersion(String mimeVersion) {
        this.mimeVersion = mimeVersion;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCharset() {
        if(this.charset != null) return charset;
        else return "";
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getContentTransferEncoding() {
        if(this.contentTransferEncoding != null) return contentTransferEncoding;
        else return "";
    }

    public void setContentTransferEncoding(String contentTransferEncoding) {
        this.contentTransferEncoding = contentTransferEncoding;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public int getMailId() {
        return mailId;
    }

    public void setMailId(int mailId) {
        this.mailId = mailId;
    }

    public boolean isComplate() {
        if(fromName != null && fromAddress != null && toName != null && toAddress != null && subject != null && mimeVersion != null && contentType != null && charset != null && contentTransferEncoding != null && date != null && body != null && messageId != null && raw != null) this.complate = true;
        else this.complate = false;
        return this.complate;
    }

    public int getIsComplateInt() {
        if(this.complate) return 1;
        else return 0;
    }

    public String toString() {
        String output = ">> Subject: " + this.subject + "\n";
        output += ">> From: " + this.fromName + "<" + this.fromAddress + ">" + "\n";
        output += ">> To: " + this.toName + "<" + this.toAddress + ">" + "\n";
        output += ">> Date: " + this.date + "\n";
        output += ">> Content-type: " + this.contentType + "\n";
        output += ">> charset: " + this.charset + "\n";
        output += ">> Content-Transfer-Encoding: " + this.contentTransferEncoding + "\n";
        output += ">> MIME-Version: " + this.mimeVersion + "\n";
        output += ">> Message-ID: " + this.messageId + "\n";
        //output += ">> Body: " + "\n" + this.body + "\n\n";
        output += ">> RAW: " + "\n" + this.raw + "\n----------------------\n";

        return output;
    }

    public String toStringBrief() {
        String output = ">> Subject: " + this.subject + "\n";
        output += ">> From: " + this.fromName + "<" + this.fromAddress + ">" + "\n";
        output += ">> To: " + this.toName + "<" + this.toAddress + ">" + "\n";
        output += ">> Date: " + this.date + "\n";
        output += ">> Content-type: " + this.contentType + "\n";
        output += ">> charset: " + this.charset + "\n";
        output += ">> Content-Transfer-Encoding: " + this.contentTransferEncoding + "\n";
        output += ">> MIME-Version: " + this.mimeVersion + "\n";
        output += ">> Message-ID: " + this.messageId + "\n\n";

        return output;
    }

    public void toStore() throws SQLException, ClassNotFoundException {
        //some bug here, this function is no longer in use
        SQLQueryManager sql = new SQLQueryManager("select * from MAIL where message_id = '" + this.messageId + "';");
        ResultSet rs = sql.query();

        if(rs.next()) {
            sql.close();
            return;
        }
        else {
            long currentTime = System.currentTimeMillis();

            String queryStr = "insert into MAIL(user_id,subject,from_name,from_address,to_name,to_address,body,content_type,charset,content_transfer_encoding,mime_version,raw,date_string,is_complete,message_id,add_time) " +
                    "values('0','" + subject + "','" + fromName + "','" + fromAddress + "','" + toName + "','" + toAddress + "','" + body + "','" + contentType + "','" + charset + "','" + contentTransferEncoding + "','" + mimeVersion + "','" + raw + "','" + date + "'," + this.getIsComplateInt() + ",'" + messageId + "',"+ currentTime + " );";

            System.out.println("8888 " +queryStr);
            sql.newQuery(queryStr);

            int queryLines = sql.execute();
            System.out.println(">>>> " + queryLines);
            sql.close();

            return;
        }
    }

    public void toStorePreparedStatement() throws SQLException, ClassNotFoundException {
        SQLQueryManager sql = new SQLQueryManager("select * from MAIL where message_id = '" + this.messageId + "';");
        ResultSet rs = sql.query();

        if(rs.next()) {
            sql.close();
            return;
        }
        else {
            sql.newEmail(this);

            sql.close();

            return;
        }
    }

    public ArrayList<MailModule> getBriefUserMails(UserModule user, int mailAmount) throws SQLException, ClassNotFoundException, UnsupportedEncodingException {
        //SQLQueryManager sql = new SQLQueryManager("select from_name,subject,body from MAIL where user_id = " + user.getUser_id() + " order by add_time desc limit 0," + mailAmount +"; ");
        SQLQueryManager sql = new SQLQueryManager("select mail_id,from_name,subject,body,content_transfer_encoding,charset from MAIL where user_id = " + 0 + " order by add_time desc limit 0," + mailAmount +"; ");
        ResultSet rs = sql.query();

        ArrayList<MailModule> arrayList = new ArrayList<MailModule>();

        while(rs.next()) {
            MailModule mail = new MailModule();
            mail.setMailId(rs.getInt("mail_id"));
            mail.setFromName(DecodeManager.getUTF8FromSubject(rs.getString("from_name")));
            mail.setSubject(DecodeManager.getUTF8FromSubject(rs.getString("subject")));

            String charset = rs.getString("charset");
            if(charset == null) charset = "";
            mail.setCharset(charset);

            String ContentTransferEncoding = rs.getString("content_transfer_encoding");
            if(ContentTransferEncoding == null) ContentTransferEncoding = "";
            mail.setContentTransferEncoding(ContentTransferEncoding);

            //mail.setContentTransferEncoding(rs.getString("content_transfer_encoding"));

            String body = rs.getString("body");
            if(mail.getContentTransferEncoding().equals("base64")) {
                body = DecodeManager.Base64(body,mail.getCharset());
            }

            if(mail.getContentTransferEncoding().equals("quoted-printable")) body = DecodeManager.DP(body,mail.getCharset());
            mail.setBody(body.substring(0, 5));
            arrayList.add(mail);
        }
        sql.close();
        return arrayList;
    }
}
