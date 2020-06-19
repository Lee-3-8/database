import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Main extends JFrame {
   Color mousein = new Color(50,188,223);
   Color backgroundC = new Color(70,70,70);
   Color ComponentC = new Color(150,150,150);
   JFrame UI = new JFrame();
   JPanel manager;
   JLabel a[];
   JTextArea manResult;
   JButton manInit,MBs[],MBsearch[];
   static Connection con; //���� connect
   Statement state = null;
   ResultSet rs = null;
   String Driver=""; 
    String url="jdbc:mysql://localhost:3306/madang?&serverTimezone=Asia/Seoul&useSSL=false"; 
    String userid="madang";
    String pwd="madang";
    MyMouseListener listener;
    
    Font font = new Font("Serit",Font.BOLD,18);
    Font font2 = new Font("Serit",Font.BOLD,13);
   GridBagConstraints gbM1;
   GridBagLayout layoutM,layoutU;
   
    public Main() {
       GridLayout ad = new GridLayout(1,2);
       UI.setLayout(ad);
       UI.setTitle("16011036/�̽���");
       UI.setSize(700,600);
       UI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       Manager();     
       UI.add(manager);
       UI.setVisible(true);
       addmouselistener();
       conDB(); //����
       InitDB();
       InsertTables();
       InsertData();
 
        
    }
    public void addmouselistener() {
       listener = new MyMouseListener();
       manInit.addMouseListener(listener); //�ʱ�ȭ 
        for(int i =0;i<5;i++) {
              MBs[i].addMouseListener(listener); // �Է�5��
        }
        for(int j = 0;j<3;j++) {
           MBsearch[j].addMouseListener(listener);// �˻� 3��
       }
    }
    
    public void Manager() {
       manager = new JPanel();
       manager.setBackground(backgroundC);
       manager.setSize(700,400);
       manager.setBorder(BorderFactory.createLineBorder(backgroundC,10));
       gbM1 = new GridBagConstraints();
       layoutM = new GridBagLayout();
       //��ư��//
       MBs = new JButton[5]; // MBs �Է¹�ư 5��;
       MBsearch = new JButton[3]; // MBs �Է¹�ư 5��;
       a = new JLabel[2];
       
       manager.setLayout(layoutM);
       gbM1.fill = GridBagConstraints.BOTH;
       gbM1.weightx = 1.0;
       gbM1.weighty = 1.0;
       MBInit();
       labeling();
       MBInsert(MBs);
       MBSearch();
       MBs[0].addActionListener(new ActionListenerDocInsert());
       MBs[1].addActionListener(new ActionListenerNurInsert());
       MBs[2].addActionListener(new ActionListenerPatInsert());
       MBs[3].addActionListener(new ActionListenerTeatInsert());
       MBs[4].addActionListener(new ActionListenerChartInsert());
       MBsearch[0].addActionListener(new ActionListenerMBS1()); 
       MBsearch[1].addActionListener(new ActionListenerMBS2()); 
       MBsearch[2].addActionListener(new ActionListenerMBS3()); 
     
    }
    public void labeling() {
    	String title[] = {"�Է�", " �˻�"};
    	for(int r=0;r<2;r++) {
    	a[r] = new JLabel(title[r]+"  >>");
        a[r].setBackground(ComponentC);
        a[r].setOpaque(true);
        a[r].setBorder(BorderFactory.createLineBorder(backgroundC,2));
        a[r].setFont(font);
        a[r].setHorizontalAlignment(SwingConstants.CENTER);
        makeM(a[r],0,r+1,1,1);
        manager.add(a[r]);
       }
    }
    public void MBSearch() {
       MBsearch = new JButton[3];
        String[] text = {"�˻�1","�˻�2 ","�˻�3"};

       for(int i =0;i<3;i++) {
          MBsearch[i] =  new JButton(text[i]);
          MBsearch[i].setFont(font2);
          makeM(MBsearch[i],i+1,2,1,1);
       
          manager.add(MBsearch[i]);
          MBsearch[i].setBorder(BorderFactory.createLineBorder(backgroundC,2));
          MBsearch[i].setBackground(ComponentC);
       }
    }

    public void MBInsert(JButton MB[]) {
       String[] text = {"Doctors","Nurses","Patients","Teatments","Charts"};
       
       for(int i =0;i<5;i++) {
          MB[i] =  new JButton(text[i]);
          MB[i].setFont(font);
          makeM(MB[i],i+1,1,1,1);   
          manager.add(MB[i]);
          MB[i].setBorder(BorderFactory.createLineBorder(backgroundC,2));
          MB[i].setBackground(ComponentC);
       }
    }
    public void MBInit() {
       manInit = new JButton("�����ͺ��̽� �ʱ�ȭ");
       manInit.setFont(font);
       manInit.setBackground(ComponentC);
       manInit.setBorder(BorderFactory.createLineBorder(backgroundC,2));
       manResult = new JTextArea();   
       manResult.setEditable(false);
       JScrollPane scrollPane = new JScrollPane(manResult);
       makeM(manInit,0,0,6,1);
       makeMScroll(scrollPane,0,3,6,6);
       scrollPane.setBorder(BorderFactory.createLineBorder(backgroundC,2));
       manager.add(scrollPane);
       manager.add(manInit);
       manInit.addActionListener(new ActionListenerInit());
    }
    public void makeM(JComponent c, int x, int y, int w, int h) {

        gbM1.gridx = x;
        gbM1.gridy = y;
        gbM1.gridwidth = w;
        gbM1.gridheight = h;
        layoutM.setConstraints(c, gbM1);

        // GridBagLayout�� GridBagConstraints�� set�ϴ� ���
 }

    public void makeMScroll(JComponent c, int x, int y, int w, int h) {

        gbM1.gridx = x;
        gbM1.gridy = y;
        gbM1.gridwidth = w;
        gbM1.gridheight = h;
        gbM1.weighty = 50;
        layoutM.setConstraints(c, gbM1);
        gbM1.weighty = 1;
        // GridBagLayout�� GridBagConstraints�� set�ϴ� ���
 }

    public void conDB(){
          try { /* ����̹��� ã�� ���� */
                 Class.forName("com.mysql.cj.jdbc.Driver");   
                 System.out.println("����̹� �ε� ����");
               } catch(ClassNotFoundException e) {
                   e.printStackTrace();
                }
          // Class.forName()���� ����̹��� �ε��Ѵ�. ����̹� �̸��� Class.forName�� �Է��Ѵ�.      
               try { /* �����ͺ��̽��� �����ϴ� ���� */
                 System.out.println("�����ͺ��̽� ���� �غ�...");   
                 
                 con=DriverManager.getConnection(url,userid,pwd);
                 
                 System.out.println("�����ͺ��̽� ���� ����");
                 manResult.setText("������ ���� ����\n");
                  
               } catch(SQLException e) {
                   e.printStackTrace();
                    manResult.setText("������ ���� ����\n"+e);
                 }
       }
  
    public void InsertData() {
        
        try {
             //Doctors �Է�
             int a=0;
             int i=0;
              String query = "INSERT INTO Doctors Values(";
              String data[] = new String [20];

            	  data[10] = "10,'�������а�','�����','��','010-0000-0010','�����@gmail.com','������Ʈ'";
            	  data[11] = "11,'�������а�','������','��','010-0000-0011','������@gmail.com','����'";
            	  data[12] = "12,'�񴢱��','���ùμ�','��','010-0000-0012','12@gmail.com','����'";
            	  data[13] = "13,'�񴢱��','������','��','010-0000-0013','13@gmail.com','������'";
            	  data[14] = "14,'�Ҿư�','���û���','��','010-0000-0040','14@gmail.com','������Ʈ'";
            	  data[15] = "15,'����ΰ�','�ӻ���','��','010-0000-0123','15@gmail.com','����'";
            	  data[16] = "16,'�Ҿư�','���ȭ','��','010-0000-0210','16@gmail.com','����'";
            	  data[17] = "17,'����ΰ�','���ü�','��','010-0000-0510','17@gmail.com','����'";
            	  data[18] = "18,'��οܰ�','������','��','010-0000-0710','18@gmail.com','����'";
            	  data[19] = "19,'�񴢱��','�����','��','010-0000-5689','19@gmail.com','������Ʈ'";
            	  
              state = con.createStatement();
              for(i=10;i<20;i++) {
                  state.executeUpdate(query+data[i]+")");
              }
              //Nurses
              query = "INSERT INTO Nurses Values(";
              String data1[] = new String [20];
              data1[10] = "10,'�������а�','�����','��','010-1234-0010','a10@gmail.com','����ȣ��'";
              data1[11] = "11,'�������а�','�念��','��','010-1234-0011','a11@gmail.com','��ȣ��'";
              data1[12] = "12,'�������а�','�����','��','010-1234-0012','a12@gmail.com','��ȣ��'";
              data1[13] = "13,'�������а�','�ڿ���','��','010-1234-0013','a13@gmail.com','��ȣ��'";
              data1[14] = "14,'�������а�','�����','��','010-1234-0014','a14@gmail.com','��ȣ��'";
              data1[15] = "15,'�������а�','�����','��','010-1234-0015','a15@gmail.com','��ȣ��'";
              data1[16] = "16,'�������а�','�����','��','010-1234-0016','a16@gmail.com','��ȣ��'";
              data1[17] = "17,'�������а�','���ĥ','��','010-1234-0017','a17@gmail.com','��ȣ��'";
              data1[18] = "18,'�������а�','�����','��','010-1234-0018','a18@gmail.com','��ȣ��'";
              data1[19] = "19,'�������а�','�����','��','010-1234-0019','a19@gmail.com','��ȣ��'";
              


              state = con.createStatement();
              for(i=10;i<20;i++) {
                  state.executeUpdate(query+data1[i]+")");
              }

              //Patients �Է�
              query = "INSERT INTO Patients Values(";
              String data2[] = new String [20];
              data2[10] = "10,10,10,'Žĵġ','��','970101-1063001','û��õ','010-4130-0001','b10@gmail.com','è�Ǿ�'";

              
              state = con.createStatement();
              for(i=10;i<11;i++) {
                  state.executeUpdate(query+data2[i]+")");
              }
    
              //Teatments
              query = "INSERT INTO Teatments Values(";
              String data3[] = new String [20];
              data3[10] = "10,10,10,'�Ƹֶ�','2020-05-20'";
              state = con.createStatement();
              for(i=10;i<11;i++) {
                  state.executeUpdate(query+data3[i]+")");
              }

              System.out.println("asdfa");
              //Charts
              query = "INSERT INTO Charts Values(";
              String data4[] = new String [20];
              data4[10] = "10,10,10,10,10,'�̻�'";

              state = con.createStatement();
              for(i=10;i<11;i++) {
                  state.executeUpdate(query+data4[i]+")");
              }

            System.out.println("������ �Է� ����");
            manResult.append("������ �Է� ����\n");
         }catch(Exception e4) {
            System.out.println("������ �Է� ����"+e4);
            manResult.setText("������ �Է� ����\n"+e4);
         }
       }
    public void InsertTables() {
        try {
             int cnt=0;
                //create tables
                String data[] = new String [5];
                data[0] = "CREATE TABLE IF NOT EXISTS `madang`.`Doctors` (\r\n" + 
                		"  `doc_id` INT NOT NULL,\r\n" + 
                		"  `major_treat` VARCHAR(25) NULL,\r\n" + 
                		"  `doc_name` VARCHAR(20) NULL,\r\n" + 
                		"  `doc_gen` CHAR(1) NULL,\r\n" + 
                		"  `doc_phone` VARCHAR(15) NULL,\r\n" + 
                		"  `doc_email` VARCHAR(50) NULL,\r\n" + 
                		"  `doc_position` VARCHAR(20) NULL,\r\n" + 
                		"  PRIMARY KEY (`doc_id`))\r\n" + 
                		"ENGINE = InnoDB";

                data[1] ="CREATE TABLE IF NOT EXISTS `madang`.`Nurses` (\r\n" + 
                		"  `nur_id` INT NOT NULL,\r\n" + 
                		"  `major_job` VARCHAR(25) NULL,\r\n" + 
                		"  `nur_name` VARCHAR(20) NULL,\r\n" + 
                		"  `nur_gen` CHAR(1) NULL,\r\n" + 
                		"  `nur_phone` VARCHAR(15) NULL,\r\n" + 
                		"  `nur_email` VARCHAR(50) NULL,\r\n" + 
                		"  `nur_position` VARCHAR(20) NULL,\r\n" + 
                		"  PRIMARY KEY (`nur_id`))\r\n" + 
                		"ENGINE = InnoDB;";
             
                data[2] ="CREATE TABLE IF NOT EXISTS `madang`.`Patients` (\r\n" + 
                		"  `pat_id` INT NOT NULL,\r\n" + 
                		"  `nur_id` INT NOT NULL,\r\n" + 
                		"  `doc_id` INT NOT NULL,\r\n" + 
                		"  `pat_name` VARCHAR(20) NULL,\r\n" + 
                		"  `pat_gen` CHAR(1) NULL,\r\n" + 
                		"  `pat_jumin` VARCHAR(14) NULL,\r\n" + 
                		"  `pat_addr` VARCHAR(100) NULL,\r\n" + 
                		"  `pat_phone` VARCHAR(15) NULL,\r\n" + 
                		"  `pat_email` VARCHAR(50) NULL,\r\n" + 
                		"  `pat_job` CHAR(20) NULL,\r\n" + 
                		"  PRIMARY KEY (`pat_id`),\r\n" + 
                		"  INDEX `fk_Patients_Doctors_idx` (`doc_id` ASC) VISIBLE,\r\n" + 
                		"  INDEX `fk_Patients_Nurses1_idx` (`nur_id` ASC) VISIBLE,\r\n" + 
                		"  CONSTRAINT `fk_Patients_Doctors`\r\n" + 
                		"    FOREIGN KEY (`doc_id`)\r\n" + 
                		"    REFERENCES `madang`.`Doctors` (`doc_id`)\r\n" + 
                		"    ON DELETE CASCADE\r\n" + 
                		"    ON UPDATE CASCADE,\r\n" + 
                		"  CONSTRAINT `fk_Patients_Nurses1`\r\n" + 
                		"    FOREIGN KEY (`nur_id`)\r\n" + 
                		"    REFERENCES `madang`.`Nurses` (`nur_id`)\r\n" + 
                		"    ON DELETE CASCADE\r\n" + 
                		"    ON UPDATE CASCADE)\r\n" + 
                		"ENGINE = InnoDB";
               
                data[3] ="CREATE TABLE IF NOT EXISTS `madang`.`Teatments` (\r\n" + 
                		"  `treat_id` INT NOT NULL,\r\n" + 
                		"  `pat_id` INT NOT NULL,\r\n" + 
                		"  `doc_id` INT NOT NULL,\r\n" + 
                		"  `treat_contents` VARCHAR(1000) NULL,\r\n" + 
                		"  `treat_date` DATE NULL,\r\n" + 
                		"  PRIMARY KEY (`treat_id`),\r\n" + 
                		"  INDEX `fk_Teatments_Patients1_idx` (`pat_id` ASC) VISIBLE,\r\n" + 
                		"  INDEX `fk_Teatments_Doctors1_idx` (`doc_id` ASC) VISIBLE,\r\n" + 
                		"  CONSTRAINT `fk_Teatments_Patients1`\r\n" + 
                		"    FOREIGN KEY (`pat_id`)\r\n" + 
                		"    REFERENCES `madang`.`Patients` (`pat_id`)\r\n" + 
                		"    ON DELETE CASCADE\r\n" + 
                		"    ON UPDATE CASCADE,\r\n" + 
                		"  CONSTRAINT `fk_Teatments_Doctors1`\r\n" + 
                		"    FOREIGN KEY (`doc_id`)\r\n" + 
                		"    REFERENCES `madang`.`Doctors` (`doc_id`)\r\n" + 
                		"    ON DELETE CASCADE\r\n" + 
                		"    ON UPDATE CASCADE)\r\n" + 
                		"ENGINE = InnoDB";
                
                data[4] ="CREATE TABLE IF NOT EXISTS `madang`.`Charts` (\r\n" + 
                		"  `chart_id` INT NOT NULL,\r\n" + 
                		"  `treat_id` INT NOT NULL,\r\n" + 
                		"  `doc_id` INT NOT NULL,\r\n" + 
                		"  `pat_id` INT NOT NULL,\r\n" + 
                		"  `nur_id` INT NOT NULL,\r\n" + 
                		"  `chart_contents` VARCHAR(1000) NULL,\r\n" + 
                		"  INDEX `fk_Charts_Teatments1_idx` (`treat_id` ASC) VISIBLE,\r\n" + 
                		"  INDEX `fk_Charts_Nurses1_idx` (`nur_id` ASC) VISIBLE,\r\n" + 
                		"  PRIMARY KEY (`chart_id`),\r\n" + 
                		"  INDEX `fk_Charts_Patients1_idx` (`pat_id` ASC) VISIBLE,\r\n" + 
                		"  INDEX `fk_Charts_Doctors1_idx` (`doc_id` ASC) VISIBLE,\r\n" + 
                		"  CONSTRAINT `fk_Charts_Teatments1`\r\n" + 
                		"    FOREIGN KEY (`treat_id`)\r\n" + 
                		"    REFERENCES `madang`.`Teatments` (`treat_id`)\r\n" + 
                		"    ON DELETE CASCADE\r\n" + 
                		"    ON UPDATE CASCADE,\r\n" + 
                		"  CONSTRAINT `fk_Charts_Nurses1`\r\n" + 
                		"    FOREIGN KEY (`nur_id`)\r\n" + 
                		"    REFERENCES `madang`.`Nurses` (`nur_id`)\r\n" + 
                		"    ON DELETE CASCADE\r\n" + 
                		"    ON UPDATE CASCADE,\r\n" + 
                		"  CONSTRAINT `fk_Charts_Patients1`\r\n" + 
                		"    FOREIGN KEY (`pat_id`)\r\n" + 
                		"    REFERENCES `madang`.`Patients` (`pat_id`)\r\n" + 
                		"    ON DELETE CASCADE\r\n" + 
                		"    ON UPDATE CASCADE,\r\n" + 
                		"  CONSTRAINT `fk_Charts_Doctors1`\r\n" + 
                		"    FOREIGN KEY (`doc_id`)\r\n" + 
                		"    REFERENCES `madang`.`Doctors` (`doc_id`)\r\n" + 
                		"    ON DELETE CASCADE\r\n" + 
                		"    ON UPDATE CASCADE)\r\n" + 
                		"ENGINE = InnoDB";
               
                state = con.createStatement();
                for(int i=0;i<5;i++) {
                   cnt = state.executeUpdate(data[i]);
                }
            System.out.println("���̺� �Է� ����");
            manResult.append("���̺� �Է� ����\n");
         }catch(Exception e4) {
            System.out.println("���̺� �Է� ����"+e4);
            manResult.setText("���̺� �Է� ����\n"+e4);
         }
       }
    public void InitDB() {
        try {
           state = con.createStatement();
           
           manResult.setText("DB�ʱ�ȭ , ���� madang database ����");
           String query = "DROP DATABASE IF EXISTS  madang";
           state.executeUpdate(query);
           
           manResult.setText("DB�ʱ�ȭ , madang database ����");
           query = "create database madang";
           state.executeUpdate(query); 
           query = "grant all privileges on madang.* to madang@localhost with grant option";
           state.executeUpdate(query);
           query = "USE `madang`";
           state.executeUpdate(query); 
           System.out.println("DB �ʱ�ȭ ����");
           manResult.setText("DB �ʱ�ȭ ����\n");
        }catch(Exception e4) {
           System.out.println("DB �ʱ�ȭ ����"+e4);
           manResult.setText("DB �ʱ�ȭ ����\n"+e4);
        }
     }         
   
   class MyMouseListener implements MouseListener{

       @Override
       public void mouseClicked(MouseEvent e) {
       }

       @Override
       public void mousePressed(MouseEvent e) {
       }

       @Override
       public void mouseReleased(MouseEvent e) {
       }

       @Override//���콺�� ��ư ������ ������ ���������� �ٲ�
       public void mouseEntered(MouseEvent e) {
           JButton b = (JButton)e.getSource();
           b.setBackground(mousein);
       }

       @Override//���콺�� ��ư ������ ������ ��������� �ٲ�
       public void mouseExited(MouseEvent e) {
           JButton b = (JButton)e.getSource();
           b.setBackground(ComponentC);
       }

   }

   //////////input â class//////
   public class Input implements ActionListener{
      int size;
      String table ="";
      String schema[];
      JFrame input = new JFrame();
      JButton In = new JButton("Ȯ��");
      JLabel[] labels;
      JTextArea[] textarea;
      JPanel[] pls;
      String[] results;
      

      public Input(String table,int size , String schema[]) {
         this.size = size;
         this.schema = schema;
         this.table = table;
         labels = new JLabel[size];
         textarea = new JTextArea[size];
         pls = new JPanel[size];
         results = new String[size];
         
         init();
         input.setVisible(true);
         input.setLayout(new FlowLayout());
         input.setBounds(700,100,800,200);

      }
      public void init() {
         
         input.setTitle("�Է��ϼ���");

         for(int i = 0;i<size;i++) {         
            labels[i] = new JLabel(schema[i]);
            textarea[i] = new JTextArea(1,5);
            pls[i] = new JPanel();
            pls[i].add(labels[i]);
            pls[i].add(textarea[i]);
            input.add(pls[i]);

         }

         In.addActionListener(this);
         input.add(In);
      }
      public String data() {
         String str = "";
         for(int i=0;i<size-1;i++) {
            str = str+"'"+results[i]+"'"+",";
         }
         str = str+"'"+results[size-1]+"'"+")";
         return str;
      }
      @Override
      public void actionPerformed(ActionEvent e) {
         for(int i =0;i<size;i++) {
            results[i] = textarea[i].getText();
         }
         try {
             System.out.println("DB �Է�");
             manResult.setText("DB �Է�");
             String query = "INSERT INTO " + table + " Values(";

             state = con.createStatement();
             state.executeUpdate(query+data());

             manResult.setText("DB �Է� ����\n");
             System.out.println("DB ����");
          }catch(Exception e4) {
             System.out.println("DB �Է� ���� "+e4);
             manResult.setText("DB �Է� ���� \n"+e4);
          }
         input.dispose();
      }
   }
   
   public class Search1 implements ActionListener{
	      int size;
	      JFrame input = new JFrame();
	      JButton In = new JButton("Ȯ��");
	      JLabel label;
	      JComboBox box;
	      JPanel pls;
	      String results,select;
	      String schema[] = {"------","Doctors" , "Nurses","Patients","Teatments","Charts"};
	      
	      public Search1() {
	         label = new JLabel("table ����");
	         pls = new JPanel();
	         results = new String();
	         box = new JComboBox(schema);
	         init();
	         input.setVisible(true);
	         input.setLayout(new FlowLayout());
	         input.setBounds(700,100,800,200);
	      }
	      public void init() {
	         
	         input.setTitle("�Է��ϼ���");
	         pls = new JPanel();
	         box.addActionListener(new ActionListener() {

	            @Override
	            public void actionPerformed(ActionEvent e) {
	               // TODO Auto-generated method stub
	               select = box.getSelectedItem().toString();
	            }
	         
	         });
	         pls.add(label);
	         pls.add(box);
	         input.add(pls);         
	         In.addActionListener(this);
	         input.add(In);
	      }

	      @Override
	      public void actionPerformed(ActionEvent e) {
	    	  int i=0;
	    	  String title[] = {"Doctors" , "Nurses","Patients","Teatments","Charts"};
	         if(select == null || select =="------") {
	            JOptionPane.showMessageDialog(input, "�Ӽ��� �������ּ���");
	         }
	         else{
	        	String schema[][] = {{"doc_id", "major_treat", "doc_name", "doc_gen", "doc_phone", "doc_email", "doc_position"},
	        			{"nur_id", "major_job", "nur_nam", "nur_gen", "nur_phone", "nur_email", "nur_position"}
	        	,{"pat_id", "nur_id", "doc_id", "pat_name", "pat_gen", "pat_jumin", "pat_addr", "pat_phone", "pat_emaiz", "pat_job"}
	        	,{"treat_id", "pat_id", "doc_id", "treat_contents", "treat_date"},
	        	{"chart_id", "treat_id", "doc_id", "pat_id", "nur_id", "chart_contents"}};
	        	String str;
	            try {   
	               System.out.println("DB ����");
	               manResult.setText("DB ����");
	               String query = "SELECT "+ "*" + " FROM " + select;
	               state = con.createStatement();
	               state.executeQuery(query);
	              //if������ table select �����ؼ� ���� ���ǹ�����
	               if(select =="Doctors") {
		               manResult.setText(schema[0][0] +"\t" +  schema[0][1] + "\t"+ schema[0][2] + "\t"+ schema[0][3] + "\t"+ schema[0][4] 
		                        + "\t"+ schema[0][5] + "\t"+ schema[0][6] +"\n");
		               rs = state.executeQuery(query);
		               while (rs.next()) {
		                       str = rs.getInt(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getString(4) 
		                       + "\t" + rs.getString(5) + "\t" + rs.getString(6)+ "\t"  + rs.getString(7)+ "\n";
		                       manResult.append(str);
		                       }
	               }
	               else if(select =="Nurses") {
	                   manResult.setText(schema[1][0] +"\t" +  schema[1][1] + "\t"+ schema[1][2] + "\t"+ schema[1][3] + "\t"+ schema[1][4] 
		                        + "\t"+ schema[1][5] + "\t"+ schema[1][6] +"\n");
		               rs = state.executeQuery(query);
		               while (rs.next()) {
		                       str = rs.getInt(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getString(4) 
		                       + "\t" + rs.getString(5) + "\t" + rs.getString(6)+ "\t"  + rs.getString(7)+ "\n";
		                       manResult.append(str);
		                       }
	               }
	               else if(select =="Patients") {
	                   manResult.setText(schema[2][0] +"\t" +  schema[2][1] + "\t"+ schema[2][2] + "\t"+ schema[2][3] + "\t"+ schema[2][4] 
		                        + "\t"+ schema[2][5] + "\t"+ schema[2][6] + "\t"+ schema[2][7]+ "\t"+ schema[2][8]+ "\t"+ schema[2][9]+"\n");
		               rs = state.executeQuery(query);
		               while (rs.next()) {
		                       str = rs.getInt(1) + "\t" + rs.getInt(2) + "\t" + rs.getInt(3) + "\t" + rs.getString(4) 
		                       + "\t" + rs.getString(5) + "\t" + rs.getString(6)+ "\t"  + rs.getString(7)+ "\t"  + rs.getString(7)
		                       + "\t"  + rs.getString(8)+ "\t"  + rs.getString(9)+ "\t"  + rs.getString(10)+ "\n";
		                       manResult.append(str);
		                       }
	               }
	               else if(select =="Teatments") {
	                   manResult.setText(schema[3][0] +"\t" +  schema[3][1] + "\t"+ schema[3][2] + "\t"+ schema[3][3] + "\t"+ schema[3][4]  +"\n");
		               rs = state.executeQuery(query);
		               while (rs.next()) {
		                       str = rs.getInt(1) + "\t" + rs.getInt(2) + "\t" + rs.getInt(3) + "\t" + rs.getString(4) 
		                       + "\t" + rs.getString(5) + "\n";
		                       manResult.append(str);
		                       }
	               }
	               else if(select =="Charts") {
	                   manResult.setText(schema[4][0] +"\t" +  schema[4][1] + "\t"+ schema[4][2] + "\t"+ schema[4][3] + "\t"+ schema[4][4] 
		                        + "\t"+ schema[4][5] +"\n");
		               rs = state.executeQuery(query);
		               while (rs.next()) {
		                       str = rs.getInt(1) + "\t" + rs.getInt(2) + "\t" + rs.getInt(3) + "\t" + rs.getInt(4) 
		                       + "\t" + rs.getInt(5) + "\t" + rs.getString(6)+ "\n";
		                       manResult.append(str);
		                       }
	               }

	               System.out.println("DB �˻� ���� ");
	               }catch(Exception e4) {
	               System.out.println("DB �˻� ���� "+e4);
	               manResult.setText("DB �˻� ���� \n"+e4);
	            }
	            input.dispose();
	         }
	         
	      }
	   }
   private class ActionListenerInit implements ActionListener {
      public void actionPerformed (ActionEvent e) {         
    	  String a = "";
    	  try {
    		  InitDB();
    		  InsertTables();
    		  InsertData();
  			System.out.println("DB �ʱ�ȭ ����");
  			manResult.setText("DB �ʱ�ȭ ����\n");
  			a = "����";  			
    	  }
    	  catch(Exception e4){
  			System.out.println("DB �ʱ�ȭ ����"+e4);
  			manResult.setText("DB �ʱ�ȭ ����\n"+e4);
  			a = "����";
    	  }
    	  finally {
    		  JOptionPane.showMessageDialog(manager, "DB �ʱ�ȭ " + a);
    	  }
    }       
   }

   private class ActionListenerDocInsert implements ActionListener { // doctor ���̺� insert ������
      public void actionPerformed (ActionEvent e) {
            String schema[] = {"doc_id","major_treat","doc_name","doc_gen","doc_phone","doc_email","doc_position"};
         Input Doc = new Input("Doctors",7,schema);
      }
    }
   private class ActionListenerNurInsert implements ActionListener {
      public void actionPerformed (ActionEvent e) {
            String schema[] = {"nur_id","major_job","nur_name","nur_gen","nur_phone","nur_email","nur_position"};
         Input Nur = new Input("Nurses",7,schema);
      }
    }
   private class ActionListenerPatInsert implements ActionListener {
      public void actionPerformed (ActionEvent e) {
            String schema[] = {"pat_id","nur_id","doc_id","pat_name","pat_gen","pat_jumin","pat_addr","pat_phone","pat_email","pat_job"};
         Input Pat = new Input("Patients",10,schema);
      }
    }

   private class ActionListenerTeatInsert implements ActionListener {
      public void actionPerformed (ActionEvent e) {
            String schema[] = {"treat_id","pat_id","doc_id","treat_contents","treat_date"};
         Input Teat = new Input("Teatments",5,schema);
      }
    }
   private class ActionListenerChartInsert implements ActionListener {
	      public void actionPerformed (ActionEvent e) {
	            String schema[] = {"chart_id","treat_id","doc_id","pat_id","nur_id","chart_contents"};
	         Input Chart = new Input("Charts",6,schema);
	      }
	    }
 
 
   private class ActionListenerMBS1 implements ActionListener {
       public void actionPerformed (ActionEvent e) {
    	   Search1 a = new Search1();
       }
    }
   private class ActionListenerMBS2 implements ActionListener {
       public void actionPerformed (ActionEvent e) {
          String input = JOptionPane.showInputDialog(manager,"��¥�Է� ex)2020-06-09");
          if(input != null) {
             try {
                 state = con.createStatement();          
                 String title[] = {"�뿩��ȣ","������Ϲ�ȣ","������","�����ּ�","������ȣ","���� email","�뿩������","�뿩������","�뿩���"};
                 String query1 ="set @num ="+"'" +  input + "'";
                 String query2 = "select a.listid'�뿩��ȣ', c.CustId'������Ϲ�ȣ', c.CustName'������', c.CustAddress'�����ּ�', c.CustPhonenum'������ȣ', c.CustEmail'����e-mail',\r\n" + 
                       "      a.RentDate as �뿩������,DATE_ADD(a.rentdate, INTERVAL a.rentperiod DAY) as �뿩������, (RentPeriod * Rental)'�뿩���'\r\n" + 
                       "      from AvailableCarList a, customer c\r\n" + 
                       "      where a.RentDate <= @num and a.custid = c.custid\r\n" + 
                       "      and   @num <= (SELECT DATE_ADD(a.rentdate, INTERVAL a.rentperiod DAY))";
                 manResult.setText(title[0] +"\t" +  title[1] + "\t"+ title[2] + "\t"+ title[3] + "\t"+ title[4] 
                       + "\t\t"+ title[5] + "\t\t"+ title[6] + "\t      "+ title[7] + "\t     "+ title[8] + "\n");
                 rs = state.executeQuery(query1);
                    rs = state.executeQuery(query2);
                    while (rs.next()) {
                       String str = rs.getInt(1) + "\t" + rs.getInt(2) + "\t" + rs.getString(3) + "\t" + rs.getString(4) 
                       + "\t" + rs.getString(5) + "\t\t" + rs.getString(6)+ "\t"  + rs.getString(7)+ "\t      "  + rs.getString(8)+ "\t      "  + rs.getInt(9)+ "\n";
                       manResult.append(str);
                    }
                    System.out.println("���ǹ����� ���� ");
              }catch(Exception e4) {
                 System.out.println("���� : ���ǹ����� ���� "+e4);
                 manResult.setText("���� : ���ǹ����� ���� \n"+e4);
              }
          }
       
       }
    }
   
   private class ActionListenerMBS3 implements ActionListener {
       public void actionPerformed (ActionEvent e) {
          String input = JOptionPane.showInputDialog(manager,"ȸ�� ID�Է�");
          if(input != null) {
             try {
                 state = con.createStatement();             
                 String title[] = {"�����ȣ" ,"ķ��ī���ID","ȸ��ID","����� ID","������̸�","����� �ּ�","����ҹ�ȣ","���������̸�","��������email","������Ϲ�ȣ","��������","������¥","������","������ ���α���"};
                 String query1 ="set @num ="+"'" +  input + "'";
                 String query2 = "select r.RepairId'�����ȣ', r.CarId'ķ��ī���ID', r.ComId'ȸ��ID', r.WorkShopId'�����ID', w.ShopName'������̸�',w.ShopAddress'������ּ�', w.ShopPhoneNum'����ҹ�ȣ', w.ShopManagerName'���������̸�', \r\n" + 
                       "      w.ShopManagerEmail'��������e-mail', r.CustId'������Ϲ�ȣ', r.RepairInfo'��������', r.RepairDate'������¥', r.RepairFee'������', r.RepairFeeDeadLine'������ ���α���'\r\n" + 
                       "      from Workshop w,RepairCar r\r\n" + 
                       "        where r.WorkShopId = w.WorkShopId and r.comid=@num";
                 manResult.setText(title[0] +"\t" +  title[1] + "\t"+ title[2] + "\t"+ title[3] + "\t"+ title[4] 
                       + "\t"+ title[5] + "\t\t"+ title[6]+ "\t\t" + title[7] + "\t\t"+ title[8]+ "\t"+ title[9]+ "\t"+ title[10]+ "\t"+ title[11]+ "\t"+ title[12]+ "\t"+ title[13]+  "\n");
                 rs = state.executeQuery(query1);
                    rs = state.executeQuery(query2);
                    while (rs.next()) {
                       String str = rs.getInt(1) + "\t" + rs.getInt(2) + "\t" + rs.getInt(3) + "\t" + rs.getInt(4) 
                                + "\t" + rs.getString(5) + "\t" + rs.getString(6)+ "\t\t"  + rs.getString(7)+"\t\t"  
                                + rs.getString(8)+ "\t\t"+ rs.getString(9)+"\t" + rs.getInt(10)+ "\t" + rs.getString(11)
                                + "\t" + rs.getString(12)+ "\t" + rs.getInt(13)+ "\t" + rs.getString(14)+ "\n";
                       manResult.append(str);
                    }
                    System.out.println("���ǹ����� ���� ");
              }catch(Exception e4) {
                 System.out.println("���� : ���ǹ����� ���� "+e4);
                 manResult.setText("���� : ���ǹ����� ���� \n"+e4);
              }
          }
          
       }
    }

   public static void main(String[] args) {
           Main BLS = new Main();
           
           BLS.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
           BLS.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           
           BLS.addWindowListener(new WindowAdapter() {
              public void windowClosing(WindowEvent we) {
               try {
                  con.close(); 
               } catch (Exception e4) {    }
               System.out.println("���α׷� ���� ����!");          
                System.exit(0);
              }
            });
        }

}