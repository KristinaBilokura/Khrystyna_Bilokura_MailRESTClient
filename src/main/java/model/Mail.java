package model;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "message")
public class Mail {
    private String email;

    private String subject, body;
    private Integer id;

    public Mail(Integer id, String email,String subject,String body) {
        this.id = id;
        this.email = email;
        this.subject = subject;
        this.body = body;
    }

    public Mail() {}


    public String getEmail() {
        return email;
    }

    public void setEmail(String from) {
        this.email = from;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        Mail mail = (Mail) obj;
        if(this.id == mail.id || this.email.equals(mail.email)){
            return true;
        }
        return false;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }



}