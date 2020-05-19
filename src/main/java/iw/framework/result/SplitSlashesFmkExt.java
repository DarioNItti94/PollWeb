/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iw.framework.result;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import iw.framework.security.SecurityLayer;

import java.util.List;

/**
 *
 * @author Giuseppe Della Penna
 */
public class SplitSlashesFmkExt implements TemplateMethodModelEx {

    @Override
    public Object exec(List list) throws TemplateModelException {
        //la lista contiene i parametri passati alla funzione nel template
        //the list contains the parameters passed to the function in the template
        if (!list.isEmpty()) {
            return SecurityLayer.stripSlashes(list.get(0).toString());
        } else {
            //e' possibile ritornare qualsiasi tipo che sia gestibile da Freemarker (scalari, hash, liste)
            //it is possible tor eturn any data type recognized by Freemarker (scalars, hashes, lists)
            return "";
        }
    }
}
