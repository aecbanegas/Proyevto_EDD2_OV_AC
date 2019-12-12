/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestordearchivos;

import java.util.ArrayList;


/**
 *
 * @author MBanegas
 */
public class BTree{

    Bnode root; // Pointer to root node 
    int t = 3;  // Minimum degree

    // Constructor (Initializes tree as empty) 
    BTree() {
        root = null;

    }

    // function to traverse the tree 
    void traverse() {
        if (root != null) {
            root.traverse();
            System.out.println("");
        }
    }
    
    ArrayList<Registro> llaves(){
        ArrayList<Registro> regresa=new ArrayList();
        AllKeys(regresa,root);
        return regresa;
    }
    
    void AllKeys(ArrayList<Registro> registros, Bnode nodo){
        if (nodo.isLeaf()) {
            for (int i = 0; i < nodo.getN(); i++) {
                if (nodo.getKeys()[i]!=null) {
                    registros.add(nodo.getKeys()[i]);
                }
            }
        }else{
            for (int i = 0; i < nodo.getN(); i++) {
                if (nodo.getKeys()[i]!=null) {
                    registros.add(nodo.getKeys()[i]);
                }
            }
            for (int i = 0; i < nodo.getHijos().length; i++) {
                if (nodo.getHijos()[i]!=null) {
                    AllKeys(registros,nodo.getHijos()[i]);
                }                
            }
        }
    }
    
    void PrintLevels() {
        try{
            if (root != null) {
                for (int i = 0; i < root.n; i++) {
                    if (root.key[i] != null) {
                        System.out.print(root.key[i] + "  ");
                    }
                }

                System.out.println("");
                if (root.hijos != null) {
                    for (int i = 0; i < root.hijos.length; i++) {
                        if (root.hijos[i] != null) {
                            for (int j = 0; j < root.hijos[i].n; j++) {
                                if (root.hijos[i].key[j] != null) {
                                    System.out.print(root.hijos[i].key[j]);
                                }
                            }
                            System.out.print("|");
                        }

                    }
                    System.out.println("");
                    for (int i = 0; i < root.hijos.length; i++) {
                        if (root.hijos[i] != null) {
                            for (int j = 0; j < root.hijos[i].n; j++) {
                                if (root.hijos[i].hijos[j] != null) {
                                    for (int k = 0; k < root.hijos[i].hijos[j].n + 1; k++) {
                                        //System.out.println("j:"+j+" K:"+k);
                                        if (root.hijos[i].hijos[j].key[k] != null) {
                                            System.out.print(root.hijos[i].hijos[j].key[k]);

                                        }

                                    }
                                    System.out.print("|");
                                }

                            }
                            System.out.print("|");
                        }

                    }

                }
            }
       
            
            }catch(Exception x){
                
            }

    }

    // function to search a key in this tree 
    Bnode search(Registro k) {
        //return (root == NULL)? NULL : root->search(k); c++
        //return (root == null) ? null : root.search(k);
        if (root == null) {
            return null;
        } else {
            return root.search(k);
        }
    }

    void insert(Registro r) {
        // If tree is empty 
        if (root == null) {
            // Allocate memory for root 
            root = new Bnode(t, true);
            // root.keys[0] = k;  // Insert key 
            root.key[0] = r;
            //root->n = 1;  // Update number of keys in root 
            root.n = 1;
        } else // If tree is not empty 
        {
            // If root is full, then tree grows in height 
            // if (root->n == 2*t-1) 
            if (root.getN() == 2 * t - 1) {
                // Allocate memory for new root 
                // BTreeNode *s = new BTreeNode(t, false); 
                Bnode s = new Bnode(t, false);

                // Make old root as child of new root 
                //s->C[0] = root; 
                s.hijos[0] = root;

                // Split the old root and move 1 key to the new root 
                //s->splitChild(0, root); 
                s.splitChild(0, root);

                // New root has two children now.  Decide which of the 
                // two children is going to have new key 
                int i = 0;
                //if (s->keys[0] < k) 
                if (s.getKey(0).key < r.getKey()) {
                    i++;
                }
                //s->C[i]->insertNonFull(k); 
                s.getHijos()[i].insertNonFull(r);
                // Change root 
                root = s;
            } else // If root is not full, call insertNonFull for root 
            //root->insertNonFull(k); 
            {
                root.insertNonFull(r);
            }
        }
    }

    void remove(Registro k) {
        if (root == null) {
            System.out.println("El Arbol esta vacio");

            return;
        }

        // Call the remove function for root 
        root.remove(k);

        // If the root node has 0 keys, make its first child as the new root 
        //  if it has a child, otherwise set root as NULL 
        if (root.n == 0) {
            Bnode tmp = root;
            if (root.isLeaf()) {
                root = null;
            } else {
                root = root.hijos[0];
            }

            // Free the old root 
            //delete tmp;
            tmp = null;
        }
        return;
    }

}
