package cs601.webmail.manager;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SimpleStringTemplate {

    private static String rootDir = "./src/cs601/webmail/SST/";
    private String content = "";

    public SimpleStringTemplate(String fileName) throws IOException {
        File file = new File(rootDir + fileName);

        BufferedReader bf = new BufferedReader(new FileReader(file));

        String content = "";
        StringBuilder sb = new StringBuilder();

        while(content != null){
            content = bf.readLine();

            if(content == null){
                break;
            }

            sb.append(content.trim());
        }

        bf.close();
        this.content = sb.toString();
    }

    public void add(String oldString,String newString) {
        this.content = this.content.replaceAll("\\$"+oldString+"\\$", newString);
    }

    public String render() {
        return this.content;
    }
}