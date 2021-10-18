package pruebas;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Rota el contenido de un arreglo (lista) hacia a la izquierda, tantas veces como se especifique por parámetro.
 * @author Ariel Miglio
 * @date 9/9/2021
 */
public class RotLeft {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>(5);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        rotLeft2(list, 2);
    }

    public static List<Integer> rotLeft(List<Integer> a, int d) {
        int pos = 0;
        int aux;

        for(int r = 0; r  < d; r++){
            int prior = a.get(a.size() -1);
            for(int i = a.size() -1; i >= 0; i --){
                pos = i - 1;
                if(pos < 0){
                    pos = a.size()+pos;
                }
                aux = a.get(pos);
                a.set(pos, prior);
                prior = aux;
            }
        }

        System.out.println(a);
        return a;
    }

    public static List<Integer> rotLeft2(List<Integer> a, int d) {
        int pos = 0;
        int aux;

        Vector<Integer> temp = new Vector<>(5);
        temp.setSize(a.size());

        for(int i = a.size() -1; i >= 0; i --){
            pos = i - d;
            if(pos < 0){
                pos = a.size()+pos;
            }
            temp.set(pos, a.get(i));

        }
        System.out.println(temp);
        return temp;

    }
}
