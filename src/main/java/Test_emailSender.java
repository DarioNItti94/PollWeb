
import iw.framework.utils.EmailSender;
import iw.framework.utils.PasswordUtility;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dario
 */
public class Test_emailSender {

    public static void main(String[] args) {
        String email ="dario.nitti@outlook.it";
        String Fname = "Dario";
        String Lname = "Nitti";
       String mittente = "pollweb2020@gmail.com";
       String password = PasswordUtility.generateRandomPassword();
        String pass = "We_PollWeb_2020";
        String obj = "C'è un sonadggio per te";
        String url = "http://localhost:8080/PollWeb/login";
        String testo = "Ciao " + Fname + " " + Lname + "\n" +
         "Sei stato invitato ad un nuovo sondaggio le tue credenziali sono: \n\n" + "Email:  " + email + "\n" + "password:  " + password + "\n" + "clicca qui per accedere al sondaggio: " + url;
        EmailSender.send(mittente, pass, email, obj, testo);
     
        

    }

}
