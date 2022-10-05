# TPNoteIHM

This work is a school exercice about creating a graphical interface using Java and SceneBuilder.

________________________________________________________________________________________________________________________________________________________________
J'ai répondu aux cinq questions du TP. J'ai conçu l'interface graphique sur scene builder. J'ai pris en compte la gestion des figures qui débordent du canevas et ai choisi comme feedback lors de la sélection d'un objet un contour bleuté plus épais pour les ellipses et rectangles, et un trait plus épais pour la ligne.
Je précise que les deux comptes apparaissant comme contributeurs m'appartiennent. Je ne sais pas pourquoi le compte StudioHarfang s'est réveillé tout seul.

Réponse à la question bonus;
Expliquez comment réaliser une architecture MVC de cette application. Quels sont vos Modèles, Vues et Controlleurs ? Expliquez en détail comment vous structureriez l’application? (L’implémentation du code n’est pas demandée).

Les controlleurs sont:
-les radio Button (select/move, Ellipse, Rectangle, Line)
-les buttons (delete, clone)
-le colorPicker
-le canvas lui-même pour ce qui est des sélections et drag and drop
Une classe controller lirait le fichier XML et gèrerait les événements, répertoriés chacun en une classe.

Les vues sont:
-les différentes figures dessinées et leur couleur
-l'interface des widgets
Chaque figure aurait une classe qui la représenterait, gérant son affichage en fonction des données du modèle.

Les modèles sont:
-la liste des figures et de leurs données
Une classe modèle stockerait les données des figures et des widgets.
________________________________________________________________________________________________________________________________________________________________
