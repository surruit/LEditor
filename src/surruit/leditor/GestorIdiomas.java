/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package surruit.leditor;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;

/**
 *
 * @author Larry
 */
public class GestorIdiomas {
    Map<JComponent, String> valores;
    ResourceBundle bundle;

    public GestorIdiomas() {
        valores = new HashMap<>();
    }
    
    public void add(JComponent c, String resourceKey){
        valores.put(c, resourceKey);
    }
    
    public void updateLocale(Locale newLocale){
        bundle = java.util.ResourceBundle.getBundle("surruit.leditor/idioma", newLocale);
        for(JComponent c :valores.keySet()){
            if (c instanceof AbstractButton) ((AbstractButton)c).setText(bundle.getString(valores.get(c)));
            if (c instanceof JLabel) ((JLabel)c).setText(bundle.getString(valores.get(c)));
            if (c instanceof JMenu) ((JMenu)c).setText(bundle.getString(valores.get(c)));
        }
    }
    
    public String getString(String key){return bundle.getString(key);}
}
