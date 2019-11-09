/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import javax.swing.JOptionPane;

/**
 *
 * @author MBanegas
 */
public class ManejoArchivo implements Serializable{
    private static final long SerialVersioUID = 608L;
    RandomAccessFile flujo=null;
    File archivo=null;
    int numeroRegistros;
    int tamañoRegistros;

    public ManejoArchivo(File archivo) throws IOException{
        this.archivo=archivo;
        flujo=new RandomAccessFile(this.archivo, "rw");
        
    } 
    
}
