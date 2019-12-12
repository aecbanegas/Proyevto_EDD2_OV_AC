package gestordearchivos;

import java.io.File;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLMaker {

    Document documento;
    String Direccion;
    
    public XMLMaker() throws ParserConfigurationException {
        // Creamos los objectos de creacion de Documentos XML
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder constructor = docFactory.newDocumentBuilder();
        
        // Creamos el documento XML
        documento = constructor.newDocument();
    }
    
    public void Operate(JTable table, Metadata metadata,String nuevo){
        
        try {
            XMLMaker creador = new XMLMaker();
            this.Direccion = nuevo;
            creador.crearDocumento(table,metadata,nuevo);
            creador.escribirArchivo();
            System.out.println( creador.convertirString() );

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLMaker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(XMLMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String convertirString() throws TransformerConfigurationException, TransformerException {
        // Creamos el objecto transformador
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        // Creamos el escritor a cadena de texto
        StringWriter writer = new StringWriter();
        // Fuente de datos, en este caso el documento XML
        DOMSource source = new DOMSource(documento);
        // Resultado, el cual se almacenara en el objecto writer
        StreamResult result = new StreamResult(writer);
        // Efectuamos la transformacion a lo que indica el objecto resultado, writer apuntara a el resultado
        transformer.transform(source, result);
        // Convertimos el buffer de writer en cadena de texto
        String output = writer.getBuffer().toString();

        return output;
    }

    public void escribirArchivo() throws TransformerConfigurationException, TransformerException {
        // Creamos el objecto transformador
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        // Archivo donde almacenaremos el XML
        File archivo = new File(Direccion+".xml");
        
        // Fuente de datos, en este caso el documento XML
        DOMSource source = new DOMSource(documento);
        // Resultado, el cual almacena en el archivo indicado
        StreamResult result = new StreamResult(archivo);
        // Transformamos de ña fuente DOM a el resultado, lo que almacena todo en el archivo
        transformer.transform(source, result);
    }
    
    public void crearDocumento(JTable table, Metadata metadata,String nuevo) {
        
        
        // Creamos el elemento principal
        ArrayList supertemporal = new ArrayList();
        try{
            for (int i = 0; i < metadata.getCampos().size(); i++) {
            String concat = "";
            concat += metadata.getCampos().get(i).toString();
            concat += "(";
            concat += metadata.getTipos().get(i).toString();
            concat += ")";
            supertemporal.add(concat);
        }
        }catch(Exception e){
            System.out.println("Problem found on Exporting XML");
        }
        
        
        Element entrada = documento.createElement("Tipos");
        // Hacemos el elemento entrada descender directo del nodo XML principal
        documento.appendChild(entrada); 
        
        for (int i = 0; i < table.getRowCount(); i++) {
            ArrayList temporal = new ArrayList();
            for (int j = 0; j < table.getColumnCount(); j++) {
                  temporal.add(table.getValueAt(i, j));
            }
            int xert = i;
            String xer = Integer.toString(i);
            xer+=temporal.toString();
            
            xer = xer.replaceAll(" ","");
            xer = xer.replaceAll(",","");
            //xer = xer.replaceAll("]","");
            xer = xer.replaceAll("[(&)]", "");
            Element registro = documento.createElement(xer);
            entrada.appendChild(registro);
        }
        /*// Creamos el Elemento de titulo
        Element titulo = documento.createElement("TITULO");
        // Establecemos el contenido del titulo
        titulo.setTextContent("Creacion de XML");
        // Indicamos que el elemento titulo desciende de entrada
        entrada.appendChild(titulo);




        //Creamos mas elementos
        Element autor = documento.createElement("AUTOR");
        autor.setTextContent("hashRaygoza");
        entrada.appendChild(autor);

        //Elemento fecha
        Element fecha = documento.createElement("FECHA");
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendario = Calendar.getInstance();
        Date date = new Date(calendario.getTimeInMillis());

        fecha.setTextContent(formato.format(date));
        entrada.appendChild(fecha);*/
    }
}
