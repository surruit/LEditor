package surruit.leditor;

import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DropMode;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.TransferHandler;
/**
 * Lamina que contiene un JEditorPane y gestiona su dragAndDrop
 * 
 * @author Surruit
 */
public class Lamina extends JPanel{
    /**
     * JEditorPane de esta lamina. Area donde se escribe el texto
     */
    JEditorPane texto;
    /**
     * Permite el scroll sobre el texto del JEditorPane
     * 
     * @see #texto
     */
    JScrollPane scroll;
    
    /**
     * Constructor de Lamina
     */
    public Lamina(){
        setLayout(new GridLayout(1, 1));
        texto = new JEditorPane();
        scroll = new JScrollPane(texto);
        add(scroll);
        
        texto.setDragEnabled(true);
        texto.setDropMode(DropMode.INSERT);
        
        texto.setTransferHandler(transferHandler);
    }
    
    /**
     * Devuelve el JEditorPane de esta lamina
     * @return JEditorPane principal
     */
    public JEditorPane getEditorPane(){ return texto;}
    
    /**
     * TransferHandler que permite el drag and drop de archivos de texto para cargarlo.
     * 
     * @see #texto
     */
    private TransferHandler transferHandler = new TransferHandler(){
        private File fileByTransfer(TransferSupport transferHandler){ try {
            //Solo el primer archivo
            List<File> files= (List<File>) transferHandler.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
            
            return files.get(0);
            } catch (UnsupportedFlavorException ex) {
                Logger.getLogger(Lamina.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Lamina.class.getName()).log(Level.SEVERE, null, ex);
            }
        return null;
        }
        @Override
        public boolean canImport(TransferHandler.TransferSupport support) { // permite recibir ficheros
            if (!support.getDataFlavors()[0].isFlavorJavaFileListType()) return false; //si no son ficheres no lo acepto
            
            return true;
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport support) {
            return LoaderHelper.load((JEditorPane)support.getComponent(), fileByTransfer(support).getAbsolutePath());
        }
    };
}
