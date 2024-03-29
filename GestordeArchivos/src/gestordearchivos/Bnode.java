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
public class Bnode implements Serializable{

    Registro[] key;  // An array of keys 
    int t;      // Minimum degree (defines the range for number of keys) 
    Bnode[] hijos; // An array of child pointers //C
    int n;     // Current number of keys 
    boolean leaf; // Is true when node is leaf. Otherwise false 

    public Bnode(int _t, boolean _leaf) {
        t = _t;
        leaf = _leaf;
        // Allocate memory for maximum number of possible keys 
        // and child pointers 
        key = new Registro[2 * t - 1];
        hijos = new Bnode[2 * t];

    }

    public Registro[] getKeys() {
        return key;
    }

    public void addregistro(Registro r, int pos) {
        key[pos] = r;
    }

    public void setKeys(Registro[] keys) {
        this.key = keys;
    }

    public Registro getKey(int pos) {
        return key[pos];
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public Bnode[] getHijos() {
        return hijos;
    }

    public void setHijos(Bnode[] hijos) {
        this.hijos = hijos;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    // A function to traverse all nodes in a subtree rooted with this node 
    void traverse() {
        // There are n keys and n+1 children, travers through n keys 
        // and first n children 
        int i;
        for (i = 0; i < n; i++) {
            // If this is not leaf, then before printing key[i], 
            // traverse the subtree rooted with child C[i]. 
            if (leaf == false) //C[i]->traverse(); 
            {
                hijos[i].traverse();
            }
            //cout << " " << keys[i]; 
            System.out.print(", " + key[i]);
        }

        // Print the subtree rooted with last child 
        if (leaf == false) {
            //C[i]->traverse();
            hijos[i].traverse();
        }
    }


    // A function to search a key in the subtree rooted with this node.     
    Bnode search(Registro k) {
        // Find the first key greater than or equal to k 
        int i = 0;
 

        for (i = 0; i < n; i++) {
            if (key[i].getKey() >= k.getKey()) {
                break;
            }
        }
        if (i < n && key[i].getKey() == k.getKey()) {
            return this;
        }
        if (leaf == true) {
            return null;
        }
        // Go to the appropriate child 
        //return C[i]->search(k);
        return hijos[i].search(k);
    }
    // A utility function to insert a new key in the subtree rooted with 
    // this node. The assumption is, the node must be non-full when this 
    // function is called 

    void insertNonFull(Registro k) {
        // Initialize index as index of rightmost element 
        int i = n - 1;
        // If this is a leaf node 
        if (leaf == true) {
            // The following loop does two things 
            // a) Finds the location of new key to be inserted 
            // b) Moves all greater keys to one place ahead 
            while (i >= 0 && key[i].getKey() > k.getKey()) {
                //keys[i+1] = keys[i];

                //Doubt about it but lets try
                key[i + 1] = key[i];
                i--;
            }

            // Insert the new key at found location 
            key[i + 1] = k;
            n = n + 1;
        } else // If this node is not leaf 
        {
            // Find the child which is going to have the new key 
            // while (i >= 0 && keys[i] > k) {
            while (i >= 0 && key[i].getKey() > k.getKey()) {
                i--;
            }

            // See if the found child is full 
            //if (C[i + 1]->n == 2 * t - 1) 
            if (hijos[i + 1].getN() == 2 * t - 1) {
                // If the child is full, then split it 
                // splitChild(i + 1, C[i + 1]);
                splitChild(i + 1, hijos[i + 1]);

                // After split, the middle key of C[i] goes up and 
                // C[i] is splitted into two.  See which of the two 
                // is going to have the new key 
                //if (keys[i + 1] < k) {
                if (key[i + 1].getKey() < k.getKey()) {
                    i++;
                }
            }
            // C[i + 1]->insertNonFull(k);
            hijos[i + 1].insertNonFull(k);
        }

    }

    // A utility function to split the child y of this node. i is index of y in 
    // child array C[].  The Child y must be full when this function is called 
// A utility function to split the child y of this node 
// Note that y must be full when this function is called 
    void splitChild(int i, Bnode y) {
        // Create a new node which is going to store (t-1) keys 
        // of y 
        // BTreeNode *z = new BTreeNode(y->t, y->leaf); 
        Bnode z = new Bnode(y.getT(), y.isLeaf());
        // z->n = t - 1; 
        z.setN(t - 1);

        // Copy the last (t-1) keys of y to z 
        for (int j = 0; j < t - 1; j++) //z->keys[j] = y->keys[j+t];
        {
            z.key[j] = y.key[j + t];
        }

        // Copy the last t children of y to z 
        if (y.leaf == false) {
            for (int j = 0; j < t; j++) // z->C[j] = y->C[j+t]; 
            {
                z.hijos[j] = y.hijos[j + t];
            }
        }

        // Reduce the number of keys in y 
        //y->n = t - 1; 
        y.n = t - 1;

        // Since this node is going to have a new child, 
        // create space of new child 
        for (int j = n; j >= i + 1; j--) //C[j+1] = C[j]; 
        {
            hijos[j + 1] = hijos[j];
        }

        // Link the new child to this node 
        hijos[i + 1] = z;

        // A key of y will move to this node. Find location of 
        // new key and move all greater keys one space ahead 
        for (int j = n - 1; j >= i; j--) //keys[j+1] = keys[j]; 
        {
            key[j + 1] = key[j];
        }

        // Copy the middle key of y to this node 
        // keys[i] = y->keys[t-1]; 
        key[i] = y.key[t - 1];

        // Increment count of keys in this node 
        n = n + 1;
    }

    public int findKey(Registro k) {
        int idx = 0;
        //Registro x = new Registro(idx);
        while ((idx < n) && (key[idx].getKey() < k.getKey())) {
            ++idx;
        }
       // System.out.println("Raro-----------------------");
        return idx;
    }

    void remove(Registro k) {
        int idx = findKey(k);

        // The key to be removed is present in this node 
        if (idx < n && key[idx].getKey() == k.getKey()) {

            // If the node is a leaf node - removeFromLeaf is called 
            // Otherwise, removeFromNonLeaf function is called 
            if (leaf) {
                removeFromLeaf(idx);
            } else {
                removeFromNonLeaf(idx);
            }
        } else {

            // If this node is a leaf node, then the key is not present in tree 
            if (leaf) {
                System.out.println("El Registro [ " + k.getKey() + " ] no existe en el Arbol");
                //cout << "The key "<< k <<" is does not exist in the tree\n"; 
                return;
            }

            // The key to be removed is present in the sub-tree rooted with this node 
            // The flag indicates whether the key is present in the sub-tree rooted 
            // with the last child of this node 
            boolean bandera = ((idx == n) ? true : false);

            // If the child where the key is supposed to exist has less that t keys, 
            // we fill that child 
            if (hijos[idx].getN() < t) {
                fill(idx);
            }

            // If the last child has been merged, it must have merged with the previous 
            // child and so we recurse on the (idx-1)th child. Else, we recurse on the 
            // (idx)th child which now has atleast t keys 
            if (bandera && idx > n) {
                hijos[idx - 1].remove(k);
            } else {
                hijos[idx].remove(k);
            }
        }
        return;
    }
    // A function to remove the idx-th key from this node - which is a leaf node 

    void removeFromLeaf(int idx) {
        // Move all the keys after the idx-th pos one place backward 
        for (int i = idx + 1; i < n; ++i) {
            //key[i - 1] = key[i];
            key[i - 1] = (key[i]);
        }
        // Reduce the count of keys 
        n--;

        return;
    }

    void removeFromNonLeaf(int idx) {
        Registro k = key[idx];

        // If the child that precedes k (C[idx]) has atleast t keys, 
        // find the predecessor 'pred' of k in the subtree rooted at 
        // C[idx]. Replace k by pred. Recursively delete pred 
        // in C[idx] 
        if (hijos[idx].getN() >= t) {
            //int pred = getPred(idx); 
            Registro pred = getPred(idx);
            key[idx] = pred;
            hijos[idx].remove(pred);
        } // If the child C[idx] has less that t keys, examine C[idx+1]. 
        // If C[idx+1] has atleast t keys, find the successor 'succ' of k in 
        // the subtree rooted at C[idx+1] 
        // Replace k by succ 
        // Recursively delete succ in C[idx+1] 
        else if (hijos[idx + 1].n >= t) {
            Registro succ = getSucc(idx);
            key[idx] = succ;
            hijos[idx + 1].remove(succ);
        } // If both C[idx] and C[idx+1] has less that t keys,merge k and all of C[idx+1] 
        // into C[idx] 
        // Now C[idx] contains 2t-1 keys 
        // Free C[idx+1] and recursively delete k from C[idx] 
        else {
            merge(idx);
            hijos[idx].remove(k);
        }
        return;
    }
    // A function to get predecessor of keys[idx] 

    Registro getPred(int idx) {
        // Keep moving to the right most node until we reach a leaf 
        Bnode cur = hijos[idx];
        while (!cur.isLeaf()) {
            cur = cur.hijos[cur.n];
        }

        // Return the last key of the leaf 
        return cur.key[cur.n - 1];
    }

    Registro getSucc(int idx) {

        // Keep moving the left most node starting from C[idx+1] until we reach a leaf 
        Bnode cur = hijos[idx + 1];
        while (!cur.leaf) {
            cur = cur.hijos[0];
        }

        // Return the first key of the leaf 
        return cur.key[0];
    }

// A function to fill child C[idx] which has less than t-1 keys 
    void fill(int idx) {

        // If the previous child(C[idx-1]) has more than t-1 keys, borrow a key 
        // from that child 
        if (idx != 0 && hijos[idx - 1].n >= t) {
            borrowFromPrev(idx);
        } // If the next child(C[idx+1]) has more than t-1 keys, borrow a key 
        // from that child 
        else if (idx != n && hijos[idx + 1].n >= t) {
            borrowFromNext(idx);
        } // Merge C[idx] with its sibling 
        // If C[idx] is the last child, merge it with with its previous sibling 
        // Otherwise merge it with its next sibling 
        else {
            if (idx != n) {
                merge(idx);
            } else {
                merge(idx - 1);
            }
        }
        return;
    }

// A function to borrow a key from C[idx-1] and insert it 
// into C[idx] 
    void borrowFromPrev(int idx) {

        Bnode child = hijos[idx];
        Bnode sibling = hijos[idx - 1];

        // The last key from C[idx-1] goes up to the parent and key[idx-1] 
        // from parent is inserted as the first key in C[idx]. Thus, the  loses 
        // sibling one key and child gains one key 
        // Moving all key in C[idx] one step ahead 
        for (int i = child.n - 1; i >= 0; --i) {
            child.key[i + 1] = child.key[i];
        }

        // If C[idx] is not a leaf, move all its child pointers one step ahead 
        if (!child.isLeaf()) {
            for (int i = child.getN(); i >= 0; --i) {
                child.hijos[i + 1] = child.hijos[i];
            }
        }

        // Setting child's first key equal to keys[idx-1] from the current node 
        child.key[0] = key[idx - 1];

        // Moving sibling's last child as C[idx]'s first child 
        if (!child.isLeaf()) {
            child.hijos[0] = sibling.hijos[sibling.n];
        }

        // Moving the key from the sibling to the parent 
        // This reduces the number of keys in the sibling 
        key[idx - 1] = sibling.key[sibling.n - 1];

        child.n += 1;
        sibling.n -= 1;

        return;
    }

    void borrowFromNext(int idx) {

        Bnode child = hijos[idx];
        Bnode sibling = hijos[idx + 1];

        // keys[idx] is inserted as the last key in C[idx] 
        child.key[(child.getN())] = key[idx];

        // Sibling's first child is inserted as the last child 
        // into C[idx] 
        if (!(child.isLeaf())) {
            child.hijos[(child.getN()) + 1] = sibling.hijos[0];
        }

        //The first key from sibling is inserted into keys[idx] 
        key[idx] = sibling.key[0];

        // Moving all keys in sibling one step behind 
        for (int i = 1; i < sibling.getN(); ++i) {
            sibling.key[i - 1] = sibling.key[i];
        }

        // Moving the child pointers one step behind 
        if (!sibling.isLeaf()) {
            for (int i = 1; i <= sibling.getN(); ++i) {
                sibling.hijos[i - 1] = sibling.hijos[i];
            }
        }

        // Increasing and decreasing the key count of C[idx] and C[idx+1] 
        // respectively 
        child.n += 1;
        sibling.n -= 1;

        return;
    }

// A function to merge C[idx] with C[idx+1] 
// C[idx+1] is freed after merging 
    void merge(int idx) {
        Bnode child = hijos[idx];
        Bnode sibling = hijos[idx + 1];

        // Pulling a key from the current node and inserting it into (t-1)th 
        // position of C[idx] 
        child.key[t - 1] = key[idx];

        // Copying the keys from C[idx+1] to C[idx] at the end 
        for (int i = 0; i < sibling.n; ++i) {
            child.key[i + t] = sibling.key[i];
        }

        // Copying the child pointers from C[idx+1] to C[idx] 
        if (!child.isLeaf()) {
            for (int i = 0; i <= sibling.n; ++i) {
                child.hijos[i + t] = sibling.hijos[i];
            }
        }

        // Moving all keys after idx in the current node one step before - 
        // to fill the gap created by moving keys[idx] to C[idx] 
        for (int i = idx + 1; i < n; ++i) {
            key[i - 1] = key[i];
        }

        // Moving the child pointers after (idx+1) in the current node one 
        // step before 
        for (int i = idx + 2; i <= n; ++i) {
            hijos[i - 1] = hijos[i];
        }

        // Updating the key count of child and the current node 
        child.n += sibling.n + 1;
        n--;

        // Freeing the memory occupied by sibling 
        //delete(sibling);
        sibling=null;
        return;
    }

// The main function that inserts a new key in this B-Tree 
    @Override
    public String toString() {
        String a = "";
        for (int i = 0; i < n; i++) {
            if (key[i] != null) {
                a += key[i].toString() + ",";
            }
        }
        return "Bnode{" + a + '}';
    }

}
