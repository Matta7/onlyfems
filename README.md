# Projet d'architecture orienté service sous JEE : Onlyfems

<!-- TOC -->
- [Projet d'architecture orienté service sous JEE : Onlyfems](#projet-darchitecture-orienté-service-sous-jee--onlyfems)
  - [Groupe](#groupe)
  - [Lancement](#lancement)
<!-- TOC -->

## Groupe

Ce projet est réalisé en binôme avec :
- MERRE Alexandre
- LOVICOURT Léo
- VALLÉE Mathieu

## Lancement

Il faut tout d'abord avoir Docker Desktop, ou un moyen de faire docker compose.
Il faut ensuite lancer les commandes suivantes :

- docker compose build
- docker compose up

Cela lancera dans le même conteneur la base de données, le client et l'API.

Si cela ne fonctionne pas, il faudra alors faire la commande suivante à la racine de l'API :
- mvn clean install