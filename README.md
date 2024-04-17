![test badge](https://github.com/BastienLevasseur/DevOps_BiblioJava/actions/workflows/maven.yml/badge.svg)

Coverage : 
![jacoco badge](.github/badges/jacoco.svg)
![jacoco badge branch](.github/badges/branches.svg)

Documentation à partir de la branche develop


# DevOps_BiblioJava 

Le package dataFrame vise à traiter simplement et efficacement un ensemble de données. Il permet de créer, afficher et
manipuler des dataFrames tout comme les dataFrames de la bibliothèque Pandas de Python tout en fournissant des analyses
statistiques.

Le but de ce projet est de fournir en Java une partie des fonctionnalités offertes par la bibliothèque Pandas.

## Features

Nous fournissons plusieurs fonctionnalités dans notre bibliothèque; ci-dessus la liste de ces fonctionnalités.

* Création d'un dataFrame
* 
Nous fournissons deux méthodes pour créer un nouveau DataFrame :

    -DataFrame(List<K> index, List<L> label, List<List<V>> values) : construit un Dataframe à partir de listes d'index, de label et de liste de valeurs.
    -TO DO DATAFRAME CSV

* Affichage d'un dataFrame


Nous fournissons plusieurs méthodes permettant d'afficher un DataFrame :

    -toString : Affiche tout le contenu d'un DataFrame
    -toStringFirstXElements(int nbLinesToWrite) : Affiche seulement les nbLinesToWrite premières lignes.
    -toStringLastXElements(int nbLinesToWrite) : Affiche seulement les nbLinesToWrite dernières lignes.

* Séléction dans un dataFrame

Nous fournissons plusieurs méthodes permettant de construire un nouveau DataFrame en sélectionnant des lignes et colonnes :

    - constructNewDataFrameWithSelectingRows(List<K> indexList) : crée un nouveau Dataframe en sélectionnant uniquement les lignes ayant leur index dans la liste d'index passé en paramétre.
    - constructNewDataFrameWithSelectingColumns(List<K> labelList) : crée un nouveau Dataframe en sélectionnant uniquement les colonnes ayant leur label dans la liste de label passé en paramétre.
    - constructNewDataFrameWithSelectingValuesOfColumns(L label, double min, double max) : crée un nouveau DataFrame en sélectionnant uniquement les lignes où la valeur de la colonne passé en paramètre est dans l'intervalle des paramètres [min; max].

* Statistiques sur un dataFrame

Nous fournissons plusieurs méthodes pour effectuer des statistiques sur une colonne d'un Dataframe :

Les méthodes suivantes s'appliquent uniquement à des colonnes comportants des nombres

    - Trouver le minimum : getMin(L label)
    - Trouver le maximum : getMax(L label)
    - Calculer la moyenne : getAverage(L label)
    - Calcler la somme : getSum(L label)
    - Calculer la somme en valeur absolue : getAbsolute(L label)
    - Calculer le produit : getProduct(L label)

La méthode suivante s'applique à toutes les colonnes

    - Compter le nombre de cases non vide : getCount(L label)




## Choix des outils

Comme indiqué dans l'introduction, nous avons développé notre bibliothèque en Java et utilisé la version 4 de JUnit
pour les tests unitaires. Nous avons également utilisé Maven afin de construire les différentes phases de notre projet
ainsi que Jacoco pour l'évaluation de la couverture du code.

## Description du workflow Git

(TODO)
