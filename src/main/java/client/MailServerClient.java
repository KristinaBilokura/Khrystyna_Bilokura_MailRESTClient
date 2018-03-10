package client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import model.Mail;
import model.NotValidBookException;
import org.testng.Reporter;

import javax.ws.rs.core.MediaType;

public class MailServerClient {
    private Client client;
    private WebResource webResource;
    private ClientResponse response;

    public  MailServerClient () {
        client = Client.create();
    }

    public String doGet(String url){
        Reporter.log("doGet method started");
        webResource = client.resource(url);
        Reporter.log("Setting accept 'application/json'");
        response = webResource.accept("application/json")
                .get(ClientResponse.class);
        Reporter.log("Response: "+response);
        return response.getEntity(String.class);
    }

    public String doPost(String url, Mail mail) throws NotValidBookException {
        Reporter.log("doPost method started");
        if(!isValid(mail)){
            Reporter.log("Mail is NOT VALID: "+ mail);
            throw new NotValidBookException();
        }
        webResource = client.resource(url);
        Reporter.log("Setting accept 'application/json'");
        response = webResource.accept("application/json")
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .post(ClientResponse.class,transformToUrlUncodedType(mail));
        Reporter.log("Response: "+response);
        return response.getEntity(String.class);
    }

    public String doDelete(String url){
        Reporter.log("doDelete method started");
        webResource = client.resource(url);
        Reporter.log("Setting accept 'application/json'");
        response = webResource.accept("application/json")
                .delete(ClientResponse.class);
        Reporter.log("Response: "+response);
        return response.getEntity(String.class);
    }

    private String transformToUrlUncodedType(Mail mail){
        return  "id="+mail.getId()+"email="	+mail.getEmail()+"&"+
                "subject="+mail.getSubject()+"&"+
                "body="+mail.getBody()+"&"
                ;
    }

    private boolean isValid(Mail book){
        boolean isValid;
        if(book.getId()>0 && book.getEmail() != null && book.getSubject() != null && book.getBody() != null){
            isValid = true;
        }else{
            isValid = false;
        }
        return isValid;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public WebResource getWebResource() {
        return webResource;
    }

    public void setWebResource(WebResource webResource) {
        this.webResource = webResource;
    }

    public ClientResponse getResponse() {
        return response;
    }

    public void setResponse(ClientResponse response) {
        this.response = response;
    }



}
