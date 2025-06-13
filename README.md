# Projet : Simulateur DHCP en Java

## Description générale
Ce projet implémente un serveur DHCP simplifié et un client DHCP en Java.  
Le serveur attribue des adresses IP dynamiques à des clients sur une plage définie, gère les baux et les expire automatiquement. Le client communique avec le serveur via sockets pour obtenir une adresse IP.

---

## Fonctionnalités développées

| Fonctionnalité                          | État                  | Description                                                                                 |
|---------------------------------------|-----------------------|---------------------------------------------------------------------------------------------|
| Serveur DHCP                          | Totalement fonctionnelle | Gère une plage d’adresses IP, attribue, renouvelle et expire les baux automatiquement.      |
| Client DHCP                          | Totalement fonctionnel   | Envoie DISCOVER, reçoit OFFER, envoie REQUEST et reçoit ACK, puis affiche l’IP attribuée.  |
| Gestion des baux (lease)              | Totalement fonctionnelle | Le serveur libère automatiquement les IP expirées toutes les 10 secondes.                   |
| Interface console serveur             | Totalement fonctionnelle | Permet de consulter la liste des IP disponibles, les baux en cours, et d’arrêter le serveur.|
| Logging des actions serveur et client| Totalement fonctionnel   | Les événements sont enregistrés dans un fichier `dhcp.log`.                                 |

---

## Structure des fichiers

- `code/Client.java` — programme client DHCP  
- `code/Serveur.java` — programme serveur DHCP avec interface console  
- `code/IPPool.java` — gestion de la plage IP et des baux  
- `code/ThreadComm.java` — thread de communication serveur/client  
- `code/DHCPEnum.java` — énumération des types de messages DHCP  
- `code/DHCPMessage.java` — classe de message DHCP sérialisable  
- `code/DHCPLogger.java` — gestion des logs dans fichier  

---

## Compilation

Place-toi dans le répertoire parent de `code/` et exécute :

```bash
javac code/*.java

## Exécution

### Démarrer le serveur DHCP :

```bash
java code.Serveur

