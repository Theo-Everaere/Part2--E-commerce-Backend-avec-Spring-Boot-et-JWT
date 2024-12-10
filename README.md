# Part2--E-commerce-Backend-avec-Spring-Boot-et-JWT

# Description :

Ce projet implémente le backend d'une plateforme e-commerce en utilisant Spring Boot et Spring Security. Il offre des fonctionnalités essentielles telles que l'inscription et la connexion des utilisateurs via JWT (JSON Web Tokens), ainsi que la gestion de la sécurité avec l'authentification stateless. En plus de la gestion des utilisateurs, une fonctionnalité complète de gestion des produits a été ajoutée, permettant aux utilisateurs de créer, mettre à jour et supprimer des produits de la plateforme. L'authentification par token est utilisée pour sécuriser l'accès aux actions sensibles.

# Fonctionnalités :

Inscription et connexion des utilisateurs avec génération de tokens JWT.
Gestion des produits : création, mise à jour, suppression de produits.
Vérification du token pour les utilisateurs afin de garantir la sécurité de l'application.
Récupération des utilisateurs et des produits via des endpoints sécurisés.
Protection CSRF pour les requêtes sensibles.
Authentification stateless via Spring Security.

# Technologies utilisées :

Spring Boot & Spring Security
JWT (JSON Web Tokens)
BCryptPasswordEncoder pour la gestion sécurisée des mots de passe
H2 Database pour le stockage des utilisateurs et des produits (base de données en mémoire)
Java 17
