/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestordearchivos;

import java.io.Serializable;

/**
 *
 * @author MBanegas
 */
public class Registro implements Serializable{
    int key;
    long RRN;
    public Registro() {
    }
    public Registro(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
    
    public long getRRN() {
        return RRN;
    }

    public void setRRN(long RRN) {
        this.RRN = RRN;
    }
    
    @Override
    public String toString() {
        return key +"-";
    }
}