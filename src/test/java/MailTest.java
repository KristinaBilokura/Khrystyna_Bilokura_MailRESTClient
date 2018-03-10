import client.MailServerClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Mail;
import model.NotValidBookException;
import org.apache.log4j.Logger;
import org.testng.Reporter;
import org.testng.annotations.Test;
import java.util.ArrayList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;



public class MailTest {
    private static Logger LOG = Logger.getLogger(MailTest.class);
    private static MailServerClient CLIENT = new MailServerClient();
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static String LIBRARY_URL = "http://localhost:8080/rest/service/mails";
    @Test
    public void getMailsTest(){
        LOG.info("getMailsTest started: Gets all mails from the service and checks it's size");
        ArrayList<Mail> resultList = GSON.fromJson(CLIENT.doGet(LIBRARY_URL), ArrayList.class);
        Reporter.log("Response is null: "+(resultList == null));
        assertNotNull(resultList);
    }
    @Test
    public void getUnexistingMail(){
        String actualResponse = CLIENT.doGet(LIBRARY_URL+"/16").replaceAll("\\s","");
        Reporter.log("Response is null: "+(actualResponse == null));
        String expextedResponse = "{\"Message\":\"Mailwasnotfound\",\"Title\":\"Failure\",\"Code\":\"204-NoContent\"}";
        assertEquals(actualResponse,expextedResponse);
    }
    @Test
    public void getMailById(){
        LOG.info("geMailById started: Gets mail by id from the service and checks it's value");
        Mail expected = new Mail(2, "hnatko2@gmail.com", "Subject2","content2");
        Mail actual = GSON.fromJson(CLIENT.doGet(LIBRARY_URL+"/2"), Mail.class);
        Reporter.log("Response is null: "+(actual == null));
        assertNotNull(actual);
        Reporter.log("Verification if equal. Actual=["+actual+"] expected["+expected+"] is = "+(actual.equals(expected)));
        assertEquals(actual,expected);
    }

    @Test
    public void getMailsByEmail(){
        LOG.info("getMailsByEmail started: Gets mail by email from the service and checks it's value");
        ArrayList<Mail> resultList = GSON.fromJson(CLIENT.doGet(LIBRARY_URL+"/paramsEmail?from=hnatko2@gmail.com"), ArrayList.class);
        Reporter.log("Response is null: "+(resultList == null));
        assertNotNull(resultList);
    }

    @Test
    public void getMailsBySubject(){
        LOG.info("getMailsBySubject started: Gets mail by subject from the service and checks it's size");
        ArrayList<Mail> resultList = GSON.fromJson(CLIENT.doGet(LIBRARY_URL+"/paramsSubject?subject=Subject2"), ArrayList.class);
        Reporter.log("Response is null: "+(resultList == null));
        assertNotNull(resultList);
    }

    @Test
    public void postMailWithExistId() throws NotValidBookException {
        LOG.info("postMailWithExistId started: update mail with aleready exist id in the service");
        Mail replaceMail = new Mail(1, "bilokura@gmail.com", "Subject1","content1");
        CLIENT.doPost(LIBRARY_URL, replaceMail);
        String actualMail = CLIENT.doGet(LIBRARY_URL+"/1");
        assertNotNull(actualMail);
    }

    @Test
    public void postMailWithNotExistId() throws NotValidBookException{
        LOG.info("postMailWithNotExistId started: add mail without already exist id in the service");
        Mail newBook = new Mail(102,"New Book", "Author", "Genre");
        CLIENT.doPost(LIBRARY_URL+"/102", newBook);
        Mail actualBook = GSON.fromJson(CLIENT.doGet(LIBRARY_URL+"/102"), Mail.class);
        Reporter.log("Response is null: "+(actualBook == null));
        assertNotNull(actualBook);
    }
    @Test
    public void deleteMailWithExistId() throws NotValidBookException{
        LOG.info("deleteMailWithExistId started: delete mail with aleready exist id in the service");
        Mail mail = new Mail(9, "test9@gmail.com", "Subject9","content9");
        String actualResponse = CLIENT.doDelete(LIBRARY_URL+"/9");
        String expextedResponse = "{\"Message\":\"Mail was deleted successfully\"}";

        Reporter.log("Response is null: "+(actualResponse == null));
        assertNotNull(actualResponse);
        Reporter.log("Verification if equal. Actual=["+actualResponse+"] expected["+expextedResponse+"] is = "
                +(actualResponse.equals(expextedResponse)));
        assertEquals(actualResponse,expextedResponse);
        Reporter.log("Adding deleted mail");
        CLIENT.doPost(LIBRARY_URL+"/9", mail);
    }

    @Test
    public void deleteMailWithNotExistId() throws NotValidBookException{
        LOG.info("deleteMailWithNotExistId started: delete mail without aleready exist id in the service");
        String actualResponse = CLIENT.doDelete(LIBRARY_URL+"/666").replaceAll("\\s","");
        String expextedResponse = "{\"Message\":\"Mailwasnotfound\",\"Title\":\"Failure\",\"Code\":\"204-NoContent\"}";
        Reporter.log("Response is null: "+(actualResponse == null));
        assertNotNull(actualResponse);
        Reporter.log("Verification if equal. Actual=["+actualResponse+"] expected["+expextedResponse+"] is = "
                +(actualResponse.equals(expextedResponse)));
        assertEquals(actualResponse,expextedResponse);
    }

    @Test
    public void getMailsWithoutRightParamTest(){
        LOG.info("getMailsWithoutRightParamTest started: Gets mail without email or subject from the service and checks it's size");
        String actualResponse = CLIENT.doGet(LIBRARY_URL+"//paramsSubject?").replaceAll("\\s","");
        String expextedResponse = "{\"Message\":\"HTTP404NotFound\""
                + ",\"Title\":\"Failure\"}";
        Reporter.log("Response is null: "+(actualResponse == null));
        assertNotNull(actualResponse);
        Reporter.log("Verification if equal. Actual=["+actualResponse+"] expected["+expextedResponse+"] is = "
                +(actualResponse.equals(expextedResponse)));
        assertEquals(actualResponse,expextedResponse);
    }
}
