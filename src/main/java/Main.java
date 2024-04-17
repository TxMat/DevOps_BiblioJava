package main.java;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("\n\t Début du programme de démonstration :\n");



        System.out.println("*****************\n");

        System.out.println("PARTIE CREATION :\n");


        List<String> index = List.of("ligne1", "ligne2");
        List<String> label = List.of("colonne1", "colonne2", "colonne3", "colonne4");

        /* Chaque sous-listes represente les valeurs d'une colonne */
        List<List<Object>> values = List.of(
                List.of(-1, 2),
                List.of(3.1, 4.2),
                List.of("Hello World", "Goodbye World"),
                List.of('a', 'b'));


        System.out.println("Création d'un premier DataFrame :");
        System.out.println("Liste des index : " +index);
        System.out.println("Liste des labels : " +label);
        System.out.println("Liste des valeurs: " + values);

        System.out.println("\n*****************\n");




        System.out.println("PARTIE AFFICHAGE :\n");


        DataFrame<String, String, Object> df = new DataFrame<>(index, label, values);

        System.out.println("Affichage du DataFrame créé :");
        System.out.println(df.toString());

        System.out.println("Affichage de la première ligne uniquement :");
        System.out.println(df.toStringFirstXElements(1));

        System.out.println("Affichage de la dernière ligne uniquement :");
        System.out.println(df.toStringLastXElements(1));


        System.out.println("\n*****************\n");





        System.out.println("PARTIE STATISTIQUE :\n");

        System.out.println("Statistique sur des colonnes ne contenant pas de nombre :");

        System.out.println("GetMin : " + df.getMin("colonne3"));
        System.out.println("GetMax : " + df.getMax("colonne3"));
        System.out.println("GetAverage : " + df.getAverage("colonne3"));
        System.out.println("GetCount : " + df.getCount("colonne3"));
        System.out.println("GetSum : " + df.getSum("colonne3"));
        System.out.println("GetAbsolute : " + df.getAbsolute("colonne3"));
        System.out.println("GetProduct : " + df.getProduct("colonne3"));


        System.out.println("\nStatistique sur des colonnes  contenant des nombres :");

        System.out.println("GetMin : " + df.getMin("colonne1"));
        System.out.println("GetMax : " + df.getMax("colonne1"));
        System.out.println("GetAverage : " + df.getAverage("colonne1"));
        System.out.println("GetCount : " + df.getCount("colonne1"));
        System.out.println("GetSum : " + df.getSum("colonne1"));
        System.out.println("GetAbsolute : " + df.getAbsolute("colonne1"));
        System.out.println("GetProduct : " + df.getProduct("colonne1"));

        System.out.println("\n*****************\n");






        System.out.println("PARTIE SELECTION :\n");

        System.out.println("Sélection des colonnes 1 et 4 :");
        List<String> label2 = List.of("colonne1", "colonne4");
        DataFrame<String, String, Object> df2 = df.constructNewDataFrameWithSelectingColumns(label2);
        System.out.println(df2.toString());

        System.out.println("Sélection de la ligne 2 :");
        List<String> index2 = List.of("ligne2");
        DataFrame<String, String, Object> df3 = df.constructNewDataFrameWithSelectingRows(index2);
        System.out.println(df3.toString());

        System.out.println("Sélection des lignes de la colonne 2 dans l'intervalle [2.5;4.1] :");
        DataFrame<String, String, Object> df4 = df.constructNewDataFrameWithSelectingValuesOfColumns("colonne2", 2.5, 4.1);
        System.out.println(df4.toString());


        System.out.println("\n*****************\n");

        System.out.println("\t Fin du programme de démonstration.\n");

    }
}
