
package ver11;
import java.awt.GridLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;




public class Ver11 extends JFrame implements ActionListener
{
    private JButton jb1,jb2,jb3,jb4,jb5;
    private JPanel jp1;
    public JTextField jt1,jt2,jt3;
    public JTextField up, down, diferent, strong, size;
    public JLabel jup,jdown,jdiferent,jstrong, jsize;
    public JLabel jl1,jl2,jl3;
    public JMenuBar jmb1;
    public JMenu file,edit;
    public JMenuItem save,New,SaveAss,Close,Open;
    public JFrame frame;
    public String RECORD_TIME =null,nameFile;  // 1 minute
    public File fi = null;
    public AudioInputStream audioInput=null;
    public ArrayList arl;
    public int i=0,x=0,m=0;
    public JTable table;
    private int[] tab;
    public DefaultTableModel model;
    private Vector row;
    private JProgressBar prog;
    private NumberFormat amountFormat;
    private JFrame frame1;
    private TargetDataLine line;
    
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
    
    
    public Ver11()
    {
        super("Audio- rejestr");
        this.setSize(300,600);
        setLayout(new BorderLayout());
        
        jmb1=new JMenuBar();
        file=new JMenu("Fille");
        edit=new JMenu("Edit");
        jmb1.add(file);
        jmb1.add(edit);  
        save=new JMenuItem("Save");
        New=new JMenuItem("New fille");
        SaveAss=new JMenuItem("Save ass");
        Close=new JMenuItem("Close programm");
        Open=new JMenuItem("Open fille");
        jb1 = new JButton("Recording");
        jb2 = new JButton("Convert");
        jb3=new JButton("Plot");
        jt1 = new JTextField();
        jt2 = new JTextField();
        jt3 = new JTextField();
        jl1=new JLabel("Frequently");
        jl2=new JLabel("Amplitude");
        jl3=new JLabel("Time");
        jb4=new JButton("Compare");

//adding element to file menu       
        file.add(New);
        file.add(Open);
        file.add(save);
        file.add(SaveAss);
        file.add(Close);
        setJMenuBar(jmb1);
        
        Close.addActionListener(this);
        Open.addActionListener(this);
        SaveAss.addActionListener(this);
        
        //Create the text fields and set them up.
        
        
        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createTitledBorder("Button panel"));
        pane.setLayout(new GridLayout(1,4));
        pane.add(jb1);
        pane.add(jb2);
        pane.add(jb3);
        pane.add(jb4);
        add(pane,BorderLayout.PAGE_END);
        
        JPanel pane1 = new JPanel();
        pane1.setBorder(BorderFactory.createTitledBorder("TextPanel"));
        pane1.setLayout(new GridLayout(3,2));
        pane1.add(jl1);
        pane1.add(jt1);
        pane1.add(jl2);
        pane1.add(jt2);
        pane1.add(jl3);
        pane1.add(jt3);
        //add(pane1,BorderLayout.CENTER);
        
//adding element and declarations to panel 4        
        JPanel pane4 = new JPanel();
        pane4.setBorder(BorderFactory.createTitledBorder("Parameters of bow"));
        pane4.setLayout(new GridLayout(5,2));
        jup=new JLabel("Up tiler");jdown=new JLabel("Down tiler");jdiferent=new JLabel("Diferent");jstrong=new JLabel("Strong of bow");jsize=new JLabel("Different in a middle:");  
    //rozstaw elementów w panelu z parametrwami łuku    
        pane4.add(jup);
        up = new JTextField();
        pane4.add(up);
        down = new JTextField();
        pane4.add(jdown);
        diferent = new JTextField();
        pane4.add(down);
        strong = new JTextField();
        pane4.add(jdiferent);
        size= new JTextField();
        pane4.add(diferent);
        pane4.add(jstrong);
        pane4.add(strong);
        pane4.add(jsize);
        pane4.add(size);
//adding ellement to panel 5 
        this.add(pane4,BorderLayout.NORTH);
        arl=new ArrayList();
//adding actions listeners
        jb1.addActionListener(this);
        jb2.addActionListener(this);
        jb3.addActionListener(this);
        jb4.addActionListener(this);
        New.addActionListener(this);
//adding element to panel 2
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // pane3.add(new GraphingData());
        this.setSize(500,400);
        this.setLocation(200,200);
        //audioInput=null;
        
        model=new DefaultTableModel();
        table=new JTable(model);
 
        model.addColumn("L.p.");
        model.addColumn("Up tiler");
        model.addColumn("Down tiler");
        model.addColumn("Diferent");
        model.addColumn("Strong");
        model.addColumn("Midle");
        model.addColumn("Frequently");
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel panel6=new JPanel();
        panel6.add(scrollPane);
        panel6.setLayout(new GridLayout(1,1));
        panel6.setBorder(BorderFactory.createTitledBorder("Specification"));
        this.add(panel6,BorderLayout.CENTER);
        
        strong.addKeyListener(new KeyAdapter() {
         public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) ||(c == KeyEvent.VK_DELETE))) {
        if (!(c == '0' || c == '1' || c == '2' || c == '3' ||c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' || c==','))
        {
          e.consume();
        }}}});
        up.addKeyListener(new KeyAdapter() {
        public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) ||(c == KeyEvent.VK_DELETE))) {
        if (!(c == '0' || c == '1' || c == '2' || c == '3' ||c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' || c==','))
        {
          e.consume();
        }}}});
        down.addKeyListener(new KeyAdapter() {
        public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!(Character.isDigit(c) ||(c == KeyEvent.VK_BACK_SPACE) ||(c == KeyEvent.VK_DELETE))) {
        if (!(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' || c==','))
        { 
          e.consume();
        }}}});
        size.addKeyListener(new KeyAdapter() {
        public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!(Character.isDigit(c) ||(c == KeyEvent.VK_BACK_SPACE) ||(c == KeyEvent.VK_DELETE))) {
        if (!(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' || c==',')){
               e.consume();
        }}}});
        diferent.addKeyListener(new KeyAdapter() {
        public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!(Character.isDigit(c) ||(c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
        if (!(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' || c==','))
        {
          e.consume();
        }}}});}
        
    public void actionPerformed(ActionEvent e) 
    {
          Object source=e.getSource();
          if(source==jb1)
          {
              Recorder r;
              r=new Recorder(nameFile);
              
          }
          
          if(source==New)
          {
            JFileChooser fc=new JFileChooser();
            //String ur1=null;
            if(fc.showSaveDialog(this)==JFileChooser.APPROVE_OPTION)
            {
                
                arl.add(fc.getSelectedFile().getPath().toString());
                fi=new File(fc.getSelectedFile().getPath());
                nameFile=fc.getSelectedFile().getPath().toString();
            }
                else
                {
                      dispose();  
                }
           }
          if(source==Open)
          {
              JFileChooser fc=new JFileChooser();
              if(fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION)
              {
                 arl.add(fc.getSelectedFile().getPath().toString());
                 //nameFile=fc.getSelectedFile().getPath().toString();
                 fi=new File(fc.getSelectedFile().getPath());
                 nameFile=fc.getSelectedFile().getPath().toString();
              }
              else
              {
                  dispose();  
              }
          }
          if(source==Close)
          {
              System.exit(0);
          }
          if(source==jb2)
          {
              frame1 = new JFrame();
              String result = JOptionPane.showInputDialog(frame1, "How many arrows you shoot:");
              
              Integer[] integerArray = null ;
              try 
              {
                  //audioInput=new AudioInputStream();
                  audioInput=AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(fi)));
// audioInput = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(adress.toString())));
                  byte[] bytes = new byte[(int) (audioInput.getFrameLength()) * (audioInput.getFormat().getFrameSize())];
                  audioInput.read(bytes); 
                  tab=new int[bytes.length];
                  tab=getUnscaledAmplitude(bytes);
                  
                  integerArray = new Integer[tab.length];
                  for(int i = 0; i < tab.length; i++){
                        integerArray[i] = new Integer(tab[i]);
                  }
                  Arrays.sort(integerArray, Collections.reverseOrder());
                  
                  //System.out.println(integerArray[1]);
                  //jt1.setText(integerArray[1].toString());
                  
                  //audioInput.close();
              } catch (IOException ex) {
                  frame1=new JFrame();
                  JOptionPane.showMessageDialog(frame1,ex,"Information window",JOptionPane.ERROR_MESSAGE);
              } catch (UnsupportedAudioFileException ex) {
                  frame1=new JFrame();
                  JOptionPane.showMessageDialog(frame1,ex,"Information window",JOptionPane.ERROR_MESSAGE);
              }
              long arrows=Long.parseLong(result);
              long wynik=0;
              long sume = 0;
              for(int k=1;k<=arrows;k++)
              {
                  sume=sume+integerArray[k];
              }
              wynik=sume/arrows;
              jt1.setText(Long.toString(wynik));
              row = new Vector();
              row.add(m);
              row.add(up.getText());
              row.add(down.getText());
              row.add(diferent.getText());
              row.add(strong.getText());
              row.add(size.getText());
              row.add(wynik);
              model.addRow(row);
              
             up.setText(null);
             down.setText(null);
             diferent.setText(null);
             strong.setText(null);
             size.setText(null);
             m++;
             repaint(); 
          }
          if(source==jb3)
          {
              frame=new JFrame();
              Plotting panel = null;
              try {
                  panel = new Plotting(arl.get(i).toString());
              } catch (IOException ex) {
                  frame1=new JFrame();
                  JOptionPane.showMessageDialog(frame1,ex,"Information window",JOptionPane.ERROR_MESSAGE);
                  
              }
              JButton jb11=new JButton("Close");
              panel.setAudioDisplay(audioInput);
              frame.getContentPane().setLayout(new BorderLayout());
              frame.setVisible(true);
              jb11.addActionListener(new ActionListener() 
              {
                  public void actionPerformed(ActionEvent e) 
                  {
                      frame.setVisible(false);
                  }
              } );
              
              frame.add(panel,BorderLayout.CENTER);
              frame.add(jb11,BorderLayout.PAGE_END);
              frame.show();
              frame.validate();
              frame.repaint();
              frame.setTitle("Plotting");
              frame.setSize(300,400);
              frame.setLocation(600,100);
              try {
                  audioInput.reset();
              } catch (IOException ex) {
                  frame1=new JFrame();
                  JOptionPane.showMessageDialog(frame1,ex,"Information window",JOptionPane.ERROR_MESSAGE);
              }
              
                //x++;
            }
                    if(source==SaveAss)
          {
              try {
                  FileWriter writer=null;
                  String ur = null;
                  JFileChooser fc=new JFileChooser();
                  if(fc.showSaveDialog(this)==JFileChooser.APPROVE_OPTION)
                  {
                      ur=fc.getSelectedFile().getPath().toString();
                  }
                  else
                  {
                      dispose();
                  }
                  writer = new FileWriter(ur);
                  for(int u=0;u<7;u++)
                  {
                    writer.append((CharSequence)model.getColumnName(u).toString());
                    writer.append(';');
                  }
                  writer.append("\n");
                  for(int j=0;j<m;j++)
                  {
                      
                      for(int u=0;u<7;u++)
                      {
                          writer.append((CharSequence)model.getValueAt(j, u).toString());
                          writer.append(';');
                      }
                      writer.append("\n");
                  }
                  
                  writer.close();
                frame1=new JFrame();
                JOptionPane.showMessageDialog(frame1,"Success","Save operation",JOptionPane.INFORMATION_MESSAGE);
              } catch (IOException ex) {
                  frame1=new JFrame();
                  JOptionPane.showMessageDialog(frame1,ex,"Information window",JOptionPane.ERROR_MESSAGE);
              }}
                    if(source==jb4)
                    {
                        float[] lista=new float[m];
                        for(int i=0;i<m;i++)
                        {
                            lista[i]=(float)model.getValueAt(i, 6);
                        }
                    }}  
    public static void main(String[] args)
    {
        Ver11 ver11=new Ver11();
    }}
