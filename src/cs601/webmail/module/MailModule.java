package cs601.webmail.module;

import cs601.webmail.manager.DBManager;
import cs601.webmail.manager.DecodeManager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;


public class MailModule {

    private String fromName = null;
    private String fromAddress = null;
    private String toName = null;
    private String toFirstAddress = null;
    private String toAddresses = null;
    private String ccAddresses = null;
    private String bccAddresses = null;
    private String subject = null;
    private String body = null;
    private String messageId = null;
    private String raw = null;
    private boolean complate = false;
    private int mailId = -1;
    public MimeMessage msg = null;
    private String sentDate = null;
    private int mailState = -1; //-1是初始值，0是正常，1是删除了（在垃圾箱），2是彻底删除了（垃圾箱也没有了）
    private int readFlag = 0; // 0:unRead, 1:read
    private int folderId = 0;
    private int userId = 0;

    //create mail from sent mails
    public MailModule(int sentIntMailId, UserModule user) throws SQLException, ClassNotFoundException {
        DBManager sql = new DBManager("select id,from_address,to_address,subject,body from SENT where user_id = " + user.getUser_id() + " and id = " + sentIntMailId + " ; ");
        ResultSet rs = sql.query();

        if(rs.next()) {
            this.setMailId(rs.getInt("id"));
            this.setFromName(rs.getString("from_address"));
            this.setToFirstAddress(rs.getString("to_address"));
            this.setSubject(rs.getString("subject"));
            this.setBody(rs.getString("body"));

            sql.close();
        }
    }

    public int getMailState() {
        return mailState;
    }

    public void setMailState(int mailState) {
        this.mailState = mailState;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public String getToAddresses() {
        return toAddresses;
    }

    public void setToAddresses(String toAddresses) {
        this.toAddresses = toAddresses;
    }

    public String getCcAddresses() {
        return ccAddresses;
    }

    public void setCcAddresses(String ccAddresses) {
        this.ccAddresses = ccAddresses;
    }

    public String getBccAddresses() {
        return bccAddresses;
    }

    public void setBccAddresses(String bccAddresses) {
        this.bccAddresses = bccAddresses;
    }

    public int getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(int readFlag) {
        this.readFlag = readFlag;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public MailModule(UserModule user,int mail_id) throws SQLException, UnsupportedEncodingException, ClassNotFoundException {
        DBManager sql = new DBManager("select mail_id,from_name,from_address,to_address,subject,body,to_addresses,cc_addresses,bcc_addresses,sent_date_string,mail_state,read_flag,folder_id,message_id from MAIL where user_id = " + user.getUser_id() + " and mail_id = " + mail_id + " ; ");
        ResultSet rs = sql.query();

        if(rs.next()) {
            this.setMailId(rs.getInt("mail_id"));
            this.setFromName(rs.getString("from_name"));
            this.setFromAddress(rs.getString("from_address"));
            this.setToFirstAddress(rs.getString("to_address"));
            this.setSubject(rs.getString("subject"));
            this.setBody(rs.getString("body"));
            this.toAddresses = rs.getString("to_addresses");
            this.ccAddresses = rs.getString("cc_addresses");
            this.bccAddresses = rs.getString("bcc_addresses");
            this.mailState = rs.getInt("mail_state");
            this.readFlag = rs.getInt("read_flag");
            this.folderId = rs.getInt("folder_id");
            this.messageId = rs.getString("message_id");

            if(this.readFlag == 0) {
                sql.newQuery("update MAIL set read_flag = 1 where mail_id = " + mail_id + ";");
                sql.execute();
            }
            sql.close();
        }
        //**此处应该处理rs.next()不存在的情况
    }

    public MailModule() {

    }

    public MailModule(String subject, String fromAddress, String toAddress, String body) {
        this.subject = subject;
        this.fromAddress = fromAddress;
        this.toFirstAddress = toAddress;
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

    public String getToFirstAddress() {
        return toFirstAddress;
    }

    public void setToFirstAddress(String toFirstAddress) {
        this.toFirstAddress = toFirstAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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
        if(fromName != null && fromAddress != null && toName != null && toFirstAddress != null && subject != null && sentDate != null && body != null && messageId != null && raw != null) this.complate = true;
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
        output += ">> FirstTo: " + this.toName + "<" + this.toFirstAddress + ">" + "\n";
        output += ">> ToAddresses: " + this.toAddresses + "\n";
        output += ">> ccAddresses: " + this.ccAddresses + "\n";
        output += ">> bccAddresses: " + this.bccAddresses + "\n";
        output += ">> sentDate: " + this.sentDate + "\n";
        output += ">> Message-ID: " + this.messageId + "\n";
        output += ">> Body: " + "\n" + this.body + "\n----------------------\n";

        return output;
    }

    public void toStorePreparedStatement() throws SQLException, ClassNotFoundException {
        DBManager sql = new DBManager("select * from MAIL where message_id = '" + this.messageId + "';");
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

    public ArrayList<MailModule> getBriefUserMails(UserModule user, int pageNumber, int tempMailState,int tempFolderId) throws SQLException, ClassNotFoundException, UnsupportedEncodingException {
        String sqlString = "select mail_id,from_name,subject,body,read_flag,sent_date_string from MAIL where user_id = " + user.getUser_id() + " and mail_state = " + tempMailState ;
        if(tempFolderId != 0) sqlString = sqlString + " and folder_id = " + tempFolderId;
        sqlString = sqlString + " order by add_time desc limit " + pageNumber * 5 + "," + (pageNumber + 1) * 5 + ";";

        DBManager sql = new DBManager(sqlString);
        ResultSet rs = sql.query();

        ArrayList<MailModule> arrayList = new ArrayList<MailModule>();

        while(rs.next()) {
            MailModule mail = new MailModule();
            mail.setMailId(rs.getInt("mail_id"));
            mail.setFromName(DecodeManager.getSafeHTMLOutputFromString(rs.getString("from_name")));
            mail.setSubject(DecodeManager.getSafeHTMLOutputFromString(rs.getString("subject")));
            mail.setReadFlag(rs.getInt("read_flag"));

            String body = rs.getString("body");
            if(body.length()>8) mail.setBody(DecodeManager.getSafeHTMLOutputFromString(body.substring(0, 8)));
            else mail.setBody(DecodeManager.getSafeHTMLOutputFromString(body));

            mail.setSentDate(rs.getString("sent_date_string"));

            arrayList.add(mail);
        }
        sql.close();
        return arrayList;
    }

    public ArrayList<MailModule> getSortedBriefUserMails(UserModule user, int tempMailState,String type) throws SQLException, ClassNotFoundException, UnsupportedEncodingException {
        String sqlString = "select mail_id,from_name,subject,body,read_flag,sent_date_string from MAIL where user_id = " + user.getUser_id() + " and mail_state = " + tempMailState + " order by " + type + " desc ;";

        DBManager sql = new DBManager(sqlString);
        ResultSet rs = sql.query();

        ArrayList<MailModule> arrayList = new ArrayList<MailModule>();

        while(rs.next()) {
            MailModule mail = new MailModule();
            mail.setMailId(rs.getInt("mail_id"));
            mail.setFromName(DecodeManager.getSafeHTMLOutputFromString(rs.getString("from_name")));
            mail.setSubject(DecodeManager.getSafeHTMLOutputFromString(rs.getString("subject")));
            mail.setReadFlag(rs.getInt("read_flag"));

            String body = rs.getString("body");
            if(body.length() > 8) mail.setBody(DecodeManager.getSafeHTMLOutputFromString(body.substring(0, 8)));
            else mail.setBody(DecodeManager.getSafeHTMLOutputFromString(body));
            mail.setSentDate(rs.getString("sent_date_string"));

            arrayList.add(mail);
        }
        sql.close();
        return arrayList;
    }


    /**
     * 获得发件人的地址和姓名
     */
    public String getFromAddressByRaw(MimeMessage msg) throws Exception {
        InternetAddress address[] = (InternetAddress[]) msg.getFrom();
        String fromAddress = address[0].getAddress();
        if (fromAddress == null) fromAddress = "";

        return fromAddress;
    }

    public String getFromNameByRaw(MimeMessage msg) throws Exception {
        InternetAddress address[] = (InternetAddress[]) msg.getFrom();
        String personal = address[0].getPersonal();
        if (personal == null)
            personal = "";
        return personal;
    }

    /**
     * 获得邮件的一个收件人地址
     */
    public String getFirstToAddressByRaw(MimeMessage msg) throws Exception {
        String mailaddr = "";
        InternetAddress[] address = null;

        address = (InternetAddress[]) msg.getRecipients(Message.RecipientType.TO);

        if (address != null) {
            String email = address[0].getAddress();
            if (email == null)
                email = "";
            else {
                email = MimeUtility.decodeText(email);
            }
            String compositeto = email;

            mailaddr = compositeto.substring(1);
        }

        return mailaddr;
    }

    /**
     * 获得邮件的收件人，抄送，和密送的地址和姓名，根据所传递的参数的不同 "to"----收件人 "cc"---抄送人地址 "bcc"---密送人地址
     */
    public String getMailAddressByRaw(String type,MimeMessage msg) throws Exception {
        String mailaddr = "";
        String addtype = type.toUpperCase();
        InternetAddress[] address = null;
        if (addtype.equals("TO") || addtype.equals("CC")|| addtype.equals("BCC")) {
            if (addtype.equals("TO")) {
                address = (InternetAddress[]) msg.getRecipients(Message.RecipientType.TO);
            } else if (addtype.equals("CC")) {
                address = (InternetAddress[]) msg.getRecipients(Message.RecipientType.CC);
            } else {
                address = (InternetAddress[]) msg.getRecipients(Message.RecipientType.BCC);
            }
            if (address != null) {
                for (int i = 0; i < address.length; i++) {
                    String email = address[i].getAddress();
                    if (email == null)
                        email = "";
                    else {
                        email = MimeUtility.decodeText(email);
                    }
                    String personal = address[i].getPersonal();
                    if (personal == null)
                        personal = "";
                    else {
                        personal = MimeUtility.decodeText(personal);
                    }
                    String compositeto = personal + "<" + email + ">";
                    mailaddr += "," + compositeto;
                }
                mailaddr = mailaddr.substring(1);
            }
        } else {
            throw new Exception("Error emailaddr type!");
        }
        return mailaddr;
    }

    /**
     * 获得邮件主题
     */
    public String getSubjectByRaw(MimeMessage msg) throws MessagingException {
        String subject = "";
        try {
            subject = MimeUtility.decodeText(msg.getSubject());
            if (subject == null)
                subject = "";
        } catch (Exception exce) {}
        return subject;
    }

    /**
     * 获得邮件发送日期
     */
    public String getSentDateByRaw(MimeMessage msg) throws Exception {
        Date sentdate = msg.getSentDate();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        return format.format(sentdate);
    }

    /**
     * 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，解析邮件 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析
     */
    public StringBuffer getMailContentByRaw(Part part, StringBuffer body) throws Exception {
        String contenttype = part.getContentType();
        int nameindex = contenttype.indexOf("name");
        boolean conname = false;
        if (nameindex != -1)
            conname = true;
        System.out.println("CONTENTTYPE: " + contenttype);
        if (part.isMimeType("text/plain") && !conname) {
            body.append((String) part.getContent());
        } else if (part.isMimeType("text/html") && !conname) {
            body.append((String) part.getContent());
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
                getMailContentByRaw(multipart.getBodyPart(i), body);
            }
        } else if (part.isMimeType("message/rfc822")) {
            getMailContentByRaw((Part) part.getContent(), body);
        } else {}
        return body;
    }

    /**
     * 判断此邮件是否需要回执，如果需要回执返回"true",否则返回"false"
     */
    public boolean getReplySignByRaw(MimeMessage msg) throws MessagingException {
        boolean replysign = false;
        String needreply[] = msg.getHeader("Disposition-Notification-To");
        if (needreply != null) {
            replysign = true;
        }
        return replysign;
    }

    /**
     * 获得此邮件的Message-ID
     */
    public String getMessageIdByRaw(MimeMessage msg) throws MessagingException {
        return msg.getMessageID();
    }

    /**
     * 【判断此邮件是否已读，如果未读返回返回false,反之返回true】
     */
    public boolean isNewByRaw(MimeMessage msg) throws MessagingException {
        boolean isnew = false;
        Flags flags = ((Message) msg).getFlags();
        Flags.Flag[] flag = flags.getSystemFlags();
        System.out.println("flags's length: " + flag.length);
        for (int i = 0; i < flag.length; i++) {
            if (flag[i] == Flags.Flag.SEEN) {
                isnew = true;
                System.out.println("seen Message.......");
                break;
            }
        }
        return isnew;
    }

    /**
     * 判断此邮件是否包含附件
     */
    public boolean isContainAttachByRaw(Part part) throws Exception {
        boolean attachflag = false;
        String contentType = part.getContentType();
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart mpart = mp.getBodyPart(i);
                String disposition = mpart.getDisposition();
                if ((disposition != null)
                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition
                        .equals(Part.INLINE))))
                    attachflag = true;
                else if (mpart.isMimeType("multipart/*")) {
                    attachflag = isContainAttachByRaw((Part) mpart);
                } else {
                    String contype = mpart.getContentType();
                    if (contype.toLowerCase().indexOf("application") != -1)
                        attachflag = true;
                    if (contype.toLowerCase().indexOf("name") != -1)
                        attachflag = true;
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            attachflag = isContainAttachByRaw((Part) part.getContent());
        }
        return attachflag;
    }

    public void setEmailByRaw() throws Exception {
        Session emptySession = null;

        MimeMessage mimeMessage = new MimeMessage(emptySession, new ByteArrayInputStream(this.raw.getBytes()));

        this.fromAddress = getFromAddressByRaw(mimeMessage);
        this.fromName = getFromNameByRaw(mimeMessage);
        this.messageId = getMessageIdByRaw(mimeMessage);
        this.body = getMailContentByRaw((Part)mimeMessage,new StringBuffer()).toString();
        this.subject = getSubjectByRaw(mimeMessage);
        this.sentDate = getSentDateByRaw(mimeMessage);
        this.toFirstAddress = getFirstToAddressByRaw(mimeMessage);
        this.toAddresses = getMailAddressByRaw("to", mimeMessage);
        this.ccAddresses = getMailAddressByRaw("cc", mimeMessage);
        this.bccAddresses = getMailAddressByRaw("bcc",mimeMessage);
        this.mailState = 0;
        getAttachmentByRaw((Part)mimeMessage);
    }

    public void getAttachmentByRaw(Part part) throws Exception {
        String fileName = "";
        String cid = "";

        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart mpart = mp.getBodyPart(i);
                MimeBodyPart mimeBodyPart = (MimeBodyPart)mpart;
                cid = mimeBodyPart.getContentID();
                if(cid == null) cid = "";

                String disposition = mpart.getDisposition();
                if ((disposition != null) && ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE)))) {
                    fileName = mpart.getFileName();

                    if (fileName.toLowerCase().indexOf("gb2312") != -1) {
                        fileName = MimeUtility.decodeText(fileName);
                    }
                    storeAttachment(fileName, mpart.getInputStream(),cid);
                } else if (mpart.isMimeType("multipart/*")) {
                    getAttachmentByRaw(mpart);
                } else {
                    fileName = mpart.getFileName();
                    if ((fileName != null) && (fileName.toLowerCase().indexOf("GB2312") != -1)) {
                        fileName = MimeUtility.decodeText(fileName);
                        storeAttachment(fileName, mpart.getInputStream(),cid);
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            getAttachmentByRaw((Part) part.getContent());
        }
    }

    private void storeAttachment(String fileName, InputStream in, String cid) throws Exception {
        DBManager sql = new DBManager();
        int attaId = sql.newAttachment(fileName,in,this.messageId,this.userId,cid);
        sql.close();

        String srcCid = cid.replace("<","").replace(">","");
        srcCid = "\"cid:" + srcCid + "\"";

        this.body = this.body.replaceAll(srcCid,"\"/Public/tem/" + attaId + "\"");
    }

    public void unRead() throws SQLException, ClassNotFoundException {
        DBManager sql = new DBManager("update MAIL set read_flag = 0 where mail_id = " + this.mailId + ";");
        sql.execute();
    }

    public void deleteToTrash() throws SQLException, ClassNotFoundException {
        DBManager sql = new DBManager("update MAIL set mail_state = 1 where mail_id = " + this.mailId + ";");
        sql.execute();
    }

    public void changeFolder(UserModule user,int mailId, int folderId) throws SQLException, ClassNotFoundException {
        DBManager sql = new DBManager("update MAIL set folder_id = " + folderId + " where mail_id = " + mailId + " and user_id = " + user.getUser_id() + " ;");
        sql.execute();
    }

    public void saveToSentFolder(int userId, String subject, String body, String toAddresses, String fromAddress) throws SQLException, ClassNotFoundException {
        DBManager sql = new DBManager("insert into Sent (user_id,subject,body,to_address,from_address) values( " + userId + ", '" + subject + "', '" + body + "', '" + toAddresses + "', '" + fromAddress + "')");
        sql.execute();
    }

    public ArrayList<MailModule> getBriefUserSentMails(int userId) throws SQLException, ClassNotFoundException {
        String sqlString = "select id,from_address,subject,body,to_address from SENT where user_id = " + userId + "  order by id desc ;";

        DBManager sql = new DBManager(sqlString);
        ResultSet rs = sql.query();

        ArrayList<MailModule> arrayList = new ArrayList<MailModule>();

        while(rs.next()) {
            MailModule mail = new MailModule();
            mail.setMailId(rs.getInt("id"));
            mail.setFromName(DecodeManager.getSafeHTMLOutputFromString(rs.getString("from_address")));
            mail.setToFirstAddress(DecodeManager.getSafeHTMLOutputFromString(rs.getString("to_address")));
            mail.setSubject(DecodeManager.getSafeHTMLOutputFromString(rs.getString("subject")));
            mail.setReadFlag(0);

            String body = rs.getString("body");
            if(body.length()>8) mail.setBody(DecodeManager.getSafeHTMLOutputFromString(body.substring(0, 8)));
            else mail.setBody(DecodeManager.getSafeHTMLOutputFromString(body));

            arrayList.add(mail);
        }
        sql.close();
        return arrayList;
    }
}
