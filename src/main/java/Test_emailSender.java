
import iw.framework.utils.EmailSender;
import iw.framework.utils.PasswordUtility;
import iw.pollweb.model.PollWebDataLayer;
import iw.pollweb.model.dto.Participant;
import java.util.Arrays;
import java.util.List;

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
        String email ="dario.nitti@outlook.it, nitti8@gmail.com";
        String password = PasswordUtility.generateRandomPassword();
        List<String> emails = Arrays.asList(email.split(","));
        System.out.println(emails.get(1));
     
        

    }

}
