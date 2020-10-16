import fr.shawiizz.shalibupdate.ShaLibUpdate;

import java.util.Timer;
import java.util.TimerTask;

public class TestUpdate {
  public static void main(String[] args) {
    new Thread(() -> new ShaLibUpdate("link", "path", ShaLibUpdate.SHOWMESSAGES).startUpdater()).start();
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
  }
}
