package surruit.leditor;

import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.spi.FileTypeDetector;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DropMode;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.TransferHandler;

public class Lamina extends JPanel{
    JEditorPane texto;
    JScrollPane scroll;
    
    public Lamina(){
        setLayout(new GridLayout(1, 1));
        texto = new JEditorPane();
        scroll = new JScrollPane(texto);
        add(scroll);
        
        texto.setDragEnabled(true);
        texto.setDropMode(DropMode.INSERT);
        
        texto.setTransferHandler(transferHandler);
    }

    public JEditorPane getEditorPane(){ return texto;}
    
    TransferHandler transferHandler = new TransferHandler(){
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
