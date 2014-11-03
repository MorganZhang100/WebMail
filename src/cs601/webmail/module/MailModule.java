package cs601.webmail.module;

import cs601.webmail.manager.SQLQueryManager;

import java.sql.ResultSet;
import java.sql.SQLException;

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
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getContentTransferEncoding() {
        return contentTransferEncoding;
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

    public boolean isComplate() {
        if(fromName != null && fromAddress != null && toName != null && toAddress != null && subject != null && mimeVersion != null && contentType != null && charset != null && contentTransferEncoding != null && date != null && body != null && messageId != null && raw != null) this.complate = true;
        else this.complate = false;
        return this.complate;
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
        output += ">> Body: " + "\n" + this.body + "\n\n";
        output += ">> RAW: " + "\n" + this.raw + "\n----------------------\n";

        return output;
    }

    public void toStore() throws SQLException, ClassNotFoundException {
        SQLQueryManager sql = new SQLQueryManager("select * from MAIL where message_id = '" + this.messageId + "';");
        ResultSet rs = sql.query();

        if(rs.next()) {
            sql.close();
            return;
        }
        else {
            int int_isComplate = 0;
            if(isComplate()) int_isComplate = 1;
            long currentTime = System.currentTimeMillis();

            sql.newQuery("insert into MAIL(user_id,subject,from_name,from_address,to_name,to_address,body,content_type,charset,content_transfer_encoding,mime_version,raw,date_string,is_complete,message_id,add_time) " +
                    "values('0','" + subject + "','" + fromName + "','" + fromAddress + "','" + toName + "','" + toAddress + "','" + body + "','" + contentType + "','" + charset + "','" + contentTransferEncoding + "','" + mimeVersion + "','" + raw + "','" + date + "'," + int_isComplate + ",'" + messageId + "',"+ currentTime + " );");
            sql.execute();
            sql.close();

            return;
        }
    }
}
