# Projet-FinsGourmets
Restaurant recommendation challenge


##Description rapide des classes :

**CsvFile.java**
Cette classe permet de charger les fichiers d'entrainement et de test (en ".cvs") facillement.


**PredictFile.java**
Cette classe permet de créer facilement les fichiers de sortie ".predict" puis de les compresser automatiquement.


**ValidationCroisee.java**
Cette classe permet d'éffectuer un test de performance sans avoir à faire de soumission. Pour cela il effectue k test sur des morceaux de l'ensemble d'entrainement et en renvoie la moyenne.


**SVD.java**
Cette classe effectue la SVD (sur une matrice déjà remplis, pas creuse). La première division avec decomposer(int k) est très lente (20 minutes environs) mais toutes les suivantes seront quasiement instantanées tant que la matrice d'initialisation n'est pas modifié.