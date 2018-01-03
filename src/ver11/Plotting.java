package ver11;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Plotting extends JPanel
{
    public int hight,weight;
    public AudioInfo audio=null;
    public ArrayList singleChannel,graphDat;
    public int[] tab;
    
    
    public void setAudioDisplay(AudioInputStream audioStream)
    {
        singleChannel= new ArrayList();
        audio=new AudioInfo(audioStream);
        for(int t=0;t<audio.getNumberOfChannels();t++)
        {
            SingleWaveformPanel waveformpanel=new SingleWaveformPanel(audio,t);
            singleChannel.add(waveformpanel);
            //add(createChannelDisplay(waveformpanel,t));
        }
    }
    
    
    public int[] getUnscaledAmplitude(byte[] eightBitByteArray)
{
    int[] toReturn = new int[eightBitByteArray.length];
    int index = 0;

    for (int audioByte = 0; audioByte < eightBitByteArray.length;)
    {
        
            // Do the byte to sample conversion.
            int low = (int) eightBitByteArray[audioByte];
            audioByte++;
            int high = (int) eightBitByteArray[audioByte];
            audioByte++;
            int sample = (high << 8) + (low & 0x00ff);
             // int sample=low;
            toReturn[index] = sample;
        
        index++;
    }

    return toReturn;
}
    public AudioInputStream audioInputStream=null;
    public byte[] bytes;
    public int hight1,wight1;
    private JLabel jl1,jl2;
    
    public Plotting(String adress) throws IOException 
    {
        
        try {
            this.setVisible(true);
            this.setLayout(new BorderLayout());
            hight=this.getHeight();
            weight=this.getWidth();
            audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(adress.toString())));
            bytes = new byte[(int) (audioInputStream.getFrameLength()) * (audioInputStream.getFormat().getFrameSize())];
            audioInputStream.read(bytes); 
            int[] tab=new int[bytes.length];
            tab=getUnscaledAmplitude(bytes);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Plotting.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Plotting.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Plotting.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                audioInputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(Plotting.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        wight1=10;
        hight1=500;
        tab = getUnscaledAmplitude(bytes);
       // XYDefaultCategoryDataset defaul=new DefaultCategoryDataset();
        XYSeries series=new XYSeries("First");
        //System.out.println(bytes.length);
        Integer[] integerArray = new Integer[tab.length];
        for(int i = 0; i < tab.length; i++){
            integerArray[i] = new Integer(tab[i]);
        }
        for(int i=10;i<bytes.length/10;i++)
        {
            
            series.add(i,integerArray[i]); 
 
                    
        }
        //JFreeChart chart=ChartFactory.createBarChart("","","",defaul,PlotOrientation.VERTICAL,false,false,false);
        
        XYSeriesCollection defaul=new XYSeriesCollection();
        JFreeChart chart=ChartFactory.createXYLineChart( 
                "Display"," ", "Amplytude",
                defaul,PlotOrientation.VERTICAL,true,true,false);
        defaul.addSeries(series);
        ChartUtilities.saveChartAsJPEG(new File("C:Users/Student/Desktop/chart.jpg"), chart, 1600, 1200);
        
        
        for(int i = 0; i < tab.length; i++){
            integerArray[i] = new Integer(tab[i]);
        }
        Arrays.sort(integerArray, Collections.reverseOrder());
        
        ChartPanel chartpanel=new ChartPanel(chart);
        this.add(chartpanel,BorderLayout.CENTER);
        JPanel pane=new JPanel();
        pane.add(new JLabel("F1_MAX: "+integerArray[1]),BorderLayout.PAGE_END);
        pane.add(new JLabel("F2_MAX: "+integerArray[2]),BorderLayout.PAGE_END);
        pane.add(new JLabel("F3_MAX: "+integerArray[3]),BorderLayout.PAGE_END);
        pane.setLayout(new GridLayout(1,3));
        this.add(pane,BorderLayout.PAGE_END);
    }
    
    public JComponent createChannelDisplay(SingleWaveformPanel fora,int index)
    {
        JPanel panel=new JPanel(new BorderLayout());
        panel.add(fora,BorderLayout.CENTER);
        panel.add(new JLabel("Channel:"+ ++index));
        
        return panel;
    }
}
