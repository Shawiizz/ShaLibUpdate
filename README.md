<div align="center">
  <h1>ShaLibUpdate 0.1</h1>

### An easy java updater to download files easily from webserver, including some cool features !
_Note that this updater is in beta and it's my first java library, so it can have bugs.
If you have a bug, you can report it creating an issue on this repository_
</div>


**What are the features ?**
------
This java library include file downloader, file checker, a feature to not download certain files / directories and another feature to not delete file / directories when file checker is checking files.


**How to use this ?**
------
It's simple ! Follow these steps.
* Download
  * You need to download the [library](https://github.com/Shawiizz/ShaLibUpdate/blob/master/ShaLibUpdate-0.1.jar)
  `link`
  * You need to download [webserver zip file](https://github.com/Shawiizz/ShaLibUpdate/blob/master/Webserver.zip)
  `link`
* Install
  * Import the library into your project
  * Extract the content of your webserver zip file into a folder on your webserver or WAMP :
  `Then your folder should contains these things : IgnoreDownload.cfg, IngoreDelete.cfg, and in the files folder you should have index.php file`
* Code
  * You will need to write some code now to make the lib work :)

**Code**
------
This code permit you to launch the updater :
> new ShaLibUpdate("link", "path", ShaLogger.SHOWMESSAGES).startUpdater();
<br>

I advice you to make a it in a new thread to update the percentage :
> new Thread(() -> new ShaLibUpdate("link", "path", ShaLogger.SHOWMESSAGES).startUpdater()).start();

Now, explainations. Ok so the first field of this function ("link") is the link or your webserver, where you uploaded the content of webserver.zip file. The second field ("path") is where the updater will download your file and the third field is to show or not show the messages in the console. I advice it to show the messages if you have a problem, but if you don't have any problem you can hide the messages.

**Additional Code**
------
I think you will want to display the percentage, here is the code i advice you to use (Timer). I already calculate the percentage in the lib but you can do it yourself. Also see this [class](https://github.com/Shawiizz/ShaLibUpdate/blob/master/src/TestUpdate.java) which contains the code. Code :
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
