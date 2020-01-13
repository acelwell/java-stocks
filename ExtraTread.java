import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class ExtraTread implements Runnable {

    private String sym;
    private int buySell;
    private JFrame parent = new JFrame();
    private volatile boolean exit = false;

    public ExtraTread (String sym, int buySell){
        this.buySell = buySell;
        this.sym = sym;
    }

    public void run()
    {
        if(buySell == 0)
        {
            for(int i = 0; i < 10; i++)
            {
                Toolkit.getDefaultToolkit().beep();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            System.out.println("Buy " + this.sym + " idiot!!");
        }
        else if(this.buySell == 1)
        {
            //Toolkit.getDefaultToolkit().beep();
//            try
//            {
//                SoundUtils.tone(100,1000);
//            }
//            catch (LineUnavailableException e)
//            {
//                e.printStackTrace();
//            }
            System.out.println("Maybe time to Sell " + this.sym + " idiot!!");
        }
        //JOptionPane.showMessageDialog(this.parent, "Buy " + this.sym + " idiot!!");
        //this.parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
//        parent.setVisible(false);
//        parent.dispose();
//        JOptionPane.getFrameForComponent(parent).setVisible(false);

        stop();
    }

    public void stop(){
        exit = true;
    }
}


class SoundUtils {

    public static float SAMPLE_RATE = 8000f;

    public static void tone(int hz, int msecs)
            throws LineUnavailableException
    {
        tone(hz, msecs, 1.0);
    }

    public static void tone(int hz, int msecs, double vol)
            throws LineUnavailableException
    {
        byte[] buf = new byte[1];
        AudioFormat af =
                new AudioFormat(
                        SAMPLE_RATE, // sampleRate
                        8,           // sampleSizeInBits
                        1,           // channels
                        true,        // signed
                        false);      // bigEndian
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
        for (int i=0; i < msecs*8; i++) {
            double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
            buf[0] = (byte)(Math.sin(angle) * 127.0 * vol);
            sdl.write(buf,0,1);
        }
        sdl.drain();
        sdl.stop();
        sdl.close();
    }


}
