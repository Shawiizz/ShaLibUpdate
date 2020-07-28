<div align="center">
  <h1>ShaLibUpdate 0.1</h1>

### Un système de mise à jour facile d'utilisation codé en java pour télécharger des fichiers depuis un serveur web en ayant des fonctionnalités intéréssantes !
_Ce système de mise à jour est en bêta et c'est aussi ma première librarie en java, donc il peut y avovir des bugs. Si tu as un bug, tu peux le report dans la section issues sur ce repo._

Langue du readme : <a href="https://github.com/Shawiizz/ShaLibUpdate/blob/master/README.md">English</a>
</div>


**Quelles sont les fonctionnalités ?**
------
Cette librarie contient le système pour télécharger les fichiers, un système pour vérifier les fichiers, une fonctionnalité qui permet de ne pas télécharger certains fichiers / dossiers et une autre pour ne pas supprimer certains fichiers / dossiers quand le système de vérification de fichiers est en marche.


**Comment l'utiliser ?**
------
C'est simple ! Il te suffit de suivre ces étapes.
* Télécharger
  * Tu as besoin de télécharger la [librarie](https://github.com/Shawiizz/ShaLibUpdate/blob/master/ShaLibUpdate-0.1.jar) et le fichier [webserver.zip](https://github.com/Shawiizz/ShaLibUpdate/blob/master/Webserver.zip)
* Install
  * Importe la librarie dans ton projet
  * Extrait le contenu de webserver.zip dans le dossier sur votre hébergement web ou local (WAMP par exemple) :
  `Ensuite ton dossier sur ton serveur web devrait contenir ceci : IgnoreDownload.cfg, IngoreDelete.cfg, et dans le dossier files du devrait avoir un fichier index.php.`
* Code
  * Tu vas devoir écrire un peu de code pour faire fonctionner cette lib :)

**Code**
------
Ce code te permet de lancer l'updater :
> new ShaLibUpdate("link", "path", ShaLogger.SHOWMESSAGES).startUpdater();
<br>

Je te conseilles de le mettre dans un autre thread pour afficher le pourcentage :
> new Thread(() -> new ShaLibUpdate("link", "path", ShaLogger.SHOWMESSAGES).startUpdater()).start();

Maintenat, expliquations. Ok donc le paramètre "link" est le lien vers ton serveur web, là ou tu as extrait le contenu de webserver.zip. Le second paramètre ("path") est la où tous tes fichiers vont être téléchargés, puis le 3ème paramètre est tout simplement pour affiche ou masquer les messages dans la console. Si tu as un problème avec la lib je te conseilles de mettre en ShaLogger.SHOWMESSAGES pour montrer les messages.

**Code Additionnel**
------
Je pense que tu voudras afficher le pourcentage, voici le code que je te conseilles d'utiliser (j'utilise un Timer). Tu peux aussi regarder cette [classe](https://github.com/Shawiizz/ShaLibUpdate/blob/master/src/TestUpdate.java) qui contient le code. Code :
```java
  Timer t = new Timer();
    t.schedule(new TimerTask() {
      @Override
      public void run() {
        if(ShaLibUpdate.updateStatus)
          System.out.println(ShaLibUpdate.percentage+"%");
        else
          t.cancel();
      }
    }, 0, 20);
```
