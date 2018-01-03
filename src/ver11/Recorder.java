package ver11;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
public class Recorder extends Thread implements ActionListener
{
    int counter=0;
    private Timer timer;
    private JButton jb1,jb2,jb3;
    private TargetDataLine line;
    private AudioInputStream audioInput;
    private AudioFileFormat.Type targetType = AudioFileFormat.Type.WAVE;
    private File file=null;
    public JFrame frame;
    public Recorder(String name)
    {
        frame=new JFrame();
        file=new File(name);
        frame.setLayout(new FlowLayout());
        frame.setSize(350,250);
        frame.setLocation(400,400);
        frame.setVisible(true);
        jb1=new JButton("Start");
        jb2=new JButton("Stop");
        jb3=new JButton("Finish");
        frame.add(jb1);
        frame.add(jb2);
        frame.add(jb3);
        jb1.addActionListener(this);
        jb2.addActionListener(this);
        jb3.addActionListener(this);
        
        try {
            AudioFormat audioFormat = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,    // Encoding technique
            44100.0F,16,2,4,44100.0F,false );                          
            DataLine.Info info = new DataLine.Info( TargetDataLine.class, audioFormat );
            this.line = ( TargetDataLine )AudioSystem.getLine( info );
            this.line.open( audioFormat );
            this.audioInput = new AudioInputStream( this.line );
            }
            catch (LineUnavailableException ex) {
            JFrame frame1=new JFrame();JOptionPane.showMessageDialog(frame1,ex,"Error message",JOptionPane.ERROR_MESSAGE);}}
        
            public void startRecording()
            {this.line.start();start();}
            public void stopRecording()
            {this.line.stop();this.line.close();}
            public void run()
            {
            try
            {
                AudioSystem.write( this.audioInput, this.targetType, this.file );
            }
            catch( Exception e )
            {
                 JFrame frame1=new JFrame();JOptionPane.showMessageDialog(frame1,e,"Error message",JOptionPane.ERROR_MESSAGE);
            }}
           
   
            
            
    public void actionPerformed(ActionEvent e)
    {
        //int counter=0;
        Object source=e.getSource();
        if(source==jb1)
        {
            System.out.println("Start");
            JFrame frame=new JFrame();
            JOptionPane.showMessageDialog(frame,"Record is started","Information window",JOptionPane.INFORMATION_MESSAGE);
            this.startRecording();
        }
        if(source==jb2)
        {
            System.out.println("Stop");
            this.stopRecording();
            JFrame frame=new JFrame();
            JOptionPane.showMessageDialog(frame,"Record is finished","Information window",JOptionPane.INFORMATION_MESSAGE);
            //file.delete();
        }}}