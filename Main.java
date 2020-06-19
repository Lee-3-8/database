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
       String[] text = {"Doctors","Nurses","Patients","treatments","Charts"};
       
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
              	  data[0] = "'980312','�Ҿư�','������','M','010-333-1340','ltj@hanbh.com','����'";
              	data[1] = "'000601','����','�ȼ���','M','011-222-0987','ask@hanbh.com','����'";
              	data[2] = "'001208','�ܰ�','�����','M','010-333-1340','kmj@hanbh.com','����'";
              	data[3] = "'020403','����','���¼�','M','010-333-1340','lts@hanbh.com','����'";
              	data[4] = "'050900','�Ǻΰ�','�迬��','F','010-333-1340','kya@hanbh.com','������'";
              	data[5] = "'050101','�Ҿư�','������','M','010-333-1340','cth@hanbh.com','������'";
              	data[6] = "'062019','����','������','F','010-333-1340','jjh@hanbh.com','������'";
              	data[7] = "'070576','�Ҿư�','ȫ�浿','M','010-333-1340','hgd@hanbh.com','������'";
              	data[8] = "'080543','�Ǻΰ�','���缮','M','010-333-1340','yjs@hanbh.com','����'";
              	data[9] = "'091001','��缱��','�躴��','M','010-333-1340','kbm@hanbh.com','������'";
            	  data[10] = "000010,'�������а�','�����','M','010-0000-0010','�����@gmail.com','������Ʈ'";
            	  data[11] = "000011,'�������а�','������','F','010-0000-0011','������@gmail.com','����'";
            	  data[12] = "000012,'����','���ùμ�','M','010-0000-0012','12@gmail.com','����'";
            	  data[13] = "000013,'����','������','M','010-0000-0013','13@gmail.com','������'";
            	  data[14] = "000014,'�Ҿư�','���û���','M','010-0000-0040','14@gmail.com','������Ʈ'";
            	  data[15] = "000015,'�Ҿư�','�ӻ���','M','010-0000-0123','15@gmail.com','����'";
            	  data[16] = "000016,'�Ҿư�','���ȭ','M','010-0000-0210','16@gmail.com','����'";
            	  data[17] = "000017,'��缱��','���ü�','F','010-0000-0510','17@gmail.com','����'";
            	  data[18] = "000018,'��缱��','������','M','010-0000-0710','18@gmail.com','����'";
            	  data[19] = "000019,'�ܰ�','�����','F','010-0000-5689','19@gmail.com','������Ʈ'";
            	  
              state = con.createStatement();
              for(i=0;i<20;i++) {
                  state.executeUpdate(query+data[i]+")");
              }
              //Nurses
              query = "INSERT INTO Nurses Values(";
              String data1[] = new String [20];
              data1[0] = "050302,'�Ҿư�','������','F','010-555-8751','key@gmail.com','����ȣ��'";
              data1[1] = "050021,'����','������','F','016-333-8745','ysa@gmail.com','����ȣ��'";
              data1[2] = "040089,'�Ǻΰ�','������','M','010-666-7646','sjw@gmail.com','����'";
              data1[3] = "070605,'��缱��','����ȭ','F','010-333-4588','yjh@gmail.com','����'";
              data1[4] = "070804,'����','���ϳ�','F','010-222-1340','nhn@gmail.com','����'";
              data1[5] = "071018,'�Ҿư�','��ȭ��','F','010-888-4116','khk@gmail.com','����'";
              data1[6] = "100356,'�Ҿư�','�̼���','M','010-777-1234','lsy@gmail.com','��ȣ��'";
              data1[7] = "104145,'�ܰ�','����','F','010-999-8520','kh@gmail.com','��ȣ��'";
              data1[8] = "120309,'�Ǻΰ�','�ڼ���','F','010-777-4996','psw@gmail.com','��ȣ��'";
              data1[9] = "130211,'�ܰ�','�̼���','M','010-222-3214','lsy2@gmail.com','��ȣ��'";
              
              data1[10] = "000010,'�������а�','�����','F','010-1234-0010','a10@gmail.com','����'";
              data1[11] = "000011,'�������а�','�念��','M','010-1234-0011','a11@gmail.com','����'";
              data1[12] = "000012,'��缱��','�����','F','010-1234-0012','a12@gmail.com','����'";
              data1[13] = "000013,'��缱��','�ڿ���','M','010-1234-0013','a13@gmail.com','����'";
              data1[14] = "000014,'�Ҿư�','�����','F','010-1234-0014','a14@gmail.com','����ȣ��'";
              data1[15] = "000015,'�Ҿư�','�����','F','010-1234-0015','a15@gmail.com','����ȣ��'";
              data1[16] = "000016,'�Ҿư�','�����','M','010-1234-0016','a16@gmail.com','��ȣ��'";
              data1[17] = "000017,'�ܰ�','���ĥ','F','010-1234-0017','a17@gmail.com','��ȣ��'";
              data1[18] = "000018,'�ܰ�','�����','F','010-1234-0018','a18@gmail.com','��ȣ��'";
              data1[19] = "000019,'����','�����','M','010-1234-0019','a19@gmail.com','��ȣ��'";
              


              state = con.createStatement();
              for(i=0;i<20;i++) {

                  state.executeUpdate(query+data1[i]+")");
                  
              }

              //Patients �Է�
              query = "INSERT INTO Patients Values(";
              String data2[] = new String [20];
              data2[0] = "2345,050302,980312,'�Ȼ��','M','232345','����','010-555-7845','ask@gmail.com','ȸ���'";
              data2[1] = "3545,040089,020403,'�輺��','M','543545','����','010-333-7812','ksh@gmail.com','�ڿ���'";
              data2[2] = "3424,070605,080543,'������','M','433424','�λ�','010-888-4859','ljj@gmail.com','ȸ���'";
              data2[3] = "7675,100356,050900,'�ֱ���','M','677675','����','010-222-4847','cks@gmail.com','ȸ���'";
              data2[4] = "4533,070804,000601,'���Ѱ�','M','744533','����','010-777-9630','jhk@gmail.com','����'";
              data2[5] = "5546,120309,070576,'������','M','765546','�뱸','010-777-0214','ywh@gmail.com','�ڿ���'";
              data2[6] = "4543,070804,050101,'������','M','454543','�λ�','010-555-4187','cjj@gmail.com','ȸ���'";
              data2[7] = "9768,130211,091001,'������','F',119768,'����','010-888-3675','ljh@gmail.com','����'";
              data2[8] = "4234,130211,091001,'������','F',234234,'����','010-999-6541','onm@gmail.com','�л�'";
              data2[9] = "7643,071018,062019,'�ۼ���','M',987643,'����','010-222-5874','ssm@gmail.com','�л�'";
              data2[10] = "0010,000010,000010,'������','M',000010,'����','010-1555-7845','ask1@gmail.com','�л�'";
              data2[11] = "0011,000010,000014,'�̽���','M',000011,'�λ�','010-2555-7845','ask2@gmail.com','�л�'";
              data2[12] = "0012,000011,000019,'�����','F',000012,'�λ�','010-3555-7845','ask3@gmail.com','�л�'";
              data2[13] = "0013,000011,000010,'�����','M',000013,'����','010-4555-7845','ask4@gmail.com','�л�'";
              data2[14] = "0014,000017,000010,'������','M',000014,'����','010-5555-7845','ask5@gmail.com','�л�'";
              data2[15] = "0015,000017,000014,'������','F',000015,'����','010-6555-7845','ask6@gmail.com','ȸ���'";
              data2[16] = "0016,000018,000014,'ĥ����','F',000016,'����','010-7555-7845','ask7@gmail.com','�ڿ���'";
              data2[17] = "0017,000018,000019,'�Ƚ���','F',000017,'����','010-8555-7845','ask8@gmail.com','ȸ���'";
              data2[18] = "0018,000019,000019,'������','F',000018,'����','010-9555-7845','ask9@gmail.com','�ڿ���'";
              data2[19] = "0019,000019,000019,'������','M',000019,'����','010-0555-7845','ask10@gmail.com','ȸ���'";

              
              state = con.createStatement();
              for(i=0;i<20;i++) {
                  state.executeUpdate(query+data2[i]+")");
              }
    
              //treatments
              query = "INSERT INTO treatments Values(";
              String data3[] = new String [20];
              data3[0] = "130516023,2345,980312,'����,����','2013-05-16'";
              data3[1] = "130628100,3545,020403,'�Ǻ� Ʈ���� ġ��','2013-06-28'";
              data3[2] = "131205056,3424,080543,'�� ��ũ��   MRI �Կ�','2013-12-05'";
              data3[3] = "131218024,7675,050900,'���̿�','2013-12-18'";
              data3[4] = "131224012,4533,000601,'�忰','2013-12-24'";
              data3[5] = "140103001,5546,070576,'���帧 ġ��','2014-01-03'";
              data3[6] = "140109026,4543,050101,'����','2014-01-09'";
              data3[7] = "140226102,9768,091001,'ȭ��ġ��','2014-02-26'";
              data3[8] = "140303003,4234,091001,'������ �ܻ�ġ��','2014-03-03'";
              data3[9] = "140308087,7643,062019,'�忰','2014-03-08'";
              data3[10] = "000000001,0010,000010,'����','2014-05-01'";
              data3[11] = "000000002,0011,000014,'����','2014-05-03'";
              data3[12] = "000000003,0012,000019,'����','2014-05-05'";
              data3[13] = "000000004,0013,000010,'����','2014-05-07'";
              data3[14] = "000000005,0014,000010,'����','2014-05-09'";
              data3[15] = "000000006,0015,000014,'����','2014-05-11'";
              data3[16] = "000000007,0016,000014,'����','2014-05-13'";
              data3[17] = "00000008,0017,000019,'����','2014-05-15'";
              data3[18] = "000000009,0018,000019,'����','2014-05-17'";
              data3[19] = "000000010,0019,000019,'����','2014-05-19'";
              state = con.createStatement();
              for(i=0;i<20;i++) {
                  state.executeUpdate(query+data3[i]+")");
              }

              //Charts
              query = "INSERT INTO Charts Values(";
              String data4[] = new String [20];
              data4[0] = "1,130516023,980312,2345,050302,'����,����'";
              data4[1] = "2,130628100,020403,3545,040089,'�Ǻ� Ʈ���� ġ��'";
              data4[2] = "3,131205056,080543,3424,070605,'�� ��ũ��   MRI �Կ�'";
              data4[3] = "4,131218024,050900,7675,100356,'���̿�'";
              data4[4] = "5,131224012,000601,4533,070804,'�忰'";
              data4[5] = "6,140103001,070576,5546,120309,'���帧 ġ��'";
              data4[6] = "7,140109026,050101,4543,070804,'����'";
              data4[7] = "8,140226102,091001,9768,130211,'ȭ��ġ��'";
              data4[8] = "9,140303003,091001,4234,130211,'������ �ܻ�ġ��'";
              data4[9] = "10,140308087,062019,7643,071018,'�忰'";
              data4[10] = "11,00000001,000010,0010,000010,'����'";
              data4[11] = "12,00000002,000014,0011,000010,'����'";
              data4[12] = "13,00000003,000019,0012,000011,'����'";
              data4[13] = "14,00000004,000010,0013,000011,'����'";
              data4[14] = "15,00000005,000010,0014,000017,'����'";
              data4[15] = "16,00000006,000014,0015,000017,'����'";
              data4[16] = "17,00000007,000014,0016,000018,'����'";
              data4[17] = "18,00000008,000019,0017,000018,'����'";
              data4[18] = "19,00000009,000019,0018,000019,'����'";
              data4[19] = "20,00000010,000019,0019,000019,'����'";

              state = con.createStatement();
              for(i=0;i<20;i++) {
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
                		"  `major_treat` VARCHAR(25) NOT NULL,\r\n" + 
                		"  `doc_name` VARCHAR(20) NOT NULL,\r\n" + 
                		"  `doc_gen` CHAR(1) NOT NULL,\r\n" + 
                		"  `doc_phone` VARCHAR(15) NULL,\r\n" + 
                		"  `doc_email` VARCHAR(50) NULL,\r\n" + 
                		"  `doc_position` VARCHAR(20) NOT NULL,\r\n" + 
                		"  PRIMARY KEY (`doc_id`))\r\n" + 
                		"ENGINE = InnoDB";

                data[1] ="CREATE TABLE IF NOT EXISTS `madang`.`Nurses` (\r\n" + 
                		"  `nur_id` INT NOT NULL,\r\n" + 
                		"  `major_job` VARCHAR(25) NOT NULL,\r\n" + 
                		"  `nur_name` VARCHAR(20) NOT NULL,\r\n" + 
                		"  `nur_gen` CHAR(1) NOT NULL,\r\n" + 
                		"  `nur_phone` VARCHAR(15) NULL,\r\n" + 
                		"  `nur_email` VARCHAR(50) NULL,\r\n" + 
                		"  `nur_position` VARCHAR(20) NOT NULL,\r\n" + 
                		"  PRIMARY KEY (`nur_id`))\r\n" + 
                		"ENGINE = InnoDB;";
             
                data[2] ="CREATE TABLE IF NOT EXISTS `madang`.`Patients` (\r\n" + 
                		"  `pat_id` INT NOT NULL,\r\n" + 
                		"  `nur_id` INT NOT NULL,\r\n" + 
                		"  `doc_id` INT NOT NULL,\r\n" + 
                		"  `pat_name` VARCHAR(20) NOT NULL,\r\n" + 
                		"  `pat_gen` CHAR(1) NOT NULL,\r\n" + 
                		"  `pat_jumin` VARCHAR(14) NOT NULL,\r\n" + 
                		"  `pat_addr` VARCHAR(100) NOT NULL,\r\n" + 
                		"  `pat_phone` VARCHAR(15) NULL,\r\n" + 
                		"  `pat_email` VARCHAR(50) NULL,\r\n" + 
                		"  `pat_job` CHAR(20) NOT NULL,\r\n" + 
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
               
                data[3] ="CREATE TABLE IF NOT EXISTS `madang`.`treatments` (\r\n" + 
                		"  `treat_id` INT NOT NULL,\r\n" + 
                		"  `pat_id` INT NOT NULL,\r\n" + 
                		"  `doc_id` INT NOT NULL,\r\n" + 
                		"  `treat_contents` VARCHAR(1000) NOT NULL,\r\n" + 
                		"  `treat_date` DATE NOT NULL,\r\n" + 
                		"  PRIMARY KEY (`treat_id`),\r\n" + 
                		"  INDEX `fk_treatments_Patients1_idx` (`pat_id` ASC) VISIBLE,\r\n" + 
                		"  INDEX `fk_treatments_Doctors1_idx` (`doc_id` ASC) VISIBLE,\r\n" + 
                		"  CONSTRAINT `fk_treatments_Patients1`\r\n" + 
                		"    FOREIGN KEY (`pat_id`)\r\n" + 
                		"    REFERENCES `madang`.`Patients` (`pat_id`)\r\n" + 
                		"    ON DELETE CASCADE\r\n" + 
                		"    ON UPDATE CASCADE,\r\n" + 
                		"  CONSTRAINT `fk_treatments_Doctors1`\r\n" + 
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
                		"  `chart_contents` VARCHAR(1000) NOT NULL,\r\n" + 
                		"  INDEX `fk_Charts_treatments1_idx` (`treat_id` ASC) VISIBLE,\r\n" + 
                		"  INDEX `fk_Charts_Nurses1_idx` (`nur_id` ASC) VISIBLE,\r\n" + 
                		"  PRIMARY KEY (`chart_id`),\r\n" + 
                		"  INDEX `fk_Charts_Patients1_idx` (`pat_id` ASC) VISIBLE,\r\n" + 
                		"  INDEX `fk_Charts_Doctors1_idx` (`doc_id` ASC) VISIBLE,\r\n" + 
                		"  CONSTRAINT `fk_Charts_treatments1`\r\n" + 
                		"    FOREIGN KEY (`treat_id`)\r\n" + 
                		"    REFERENCES `madang`.`treatments` (`treat_id`)\r\n" + 
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
	      String schema[] = {"------","Doctors" , "Nurses","Patients","treatments","Charts"};
	      
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
	    	  String title[] = {"Doctors" , "Nurses","Patients","treatments","Charts"};
	         if(select == null || select =="------") {
	            JOptionPane.showMessageDialog(input, "�Ӽ��� �������ּ���");
	         }
	         else{
	        	String schema[][] = {{"doc_id", "major_treat", "doc_name", "doc_gen", "doc_phone", "doc_email", "doc_position"},
	        			{"nur_id", "major_job", "nur_nam", "nur_gen", "nur_phone", "nur_email", "nur_position"}
	        	,{"pat_id", "nur_id", "doc_id", "pat_name", "pat_gen", "pat_jumin", "pat_addr", "pat_phone", "pat_email", "pat_job"}
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
		                        + "\t\t"+ schema[0][5] + "\t\t"+ schema[0][6] +"\n");
		               rs = state.executeQuery(query);
		               while (rs.next()) {
		                       str = rs.getInt(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getString(4) 
		                       + "\t" + rs.getString(5) + "\t\t" + rs.getString(6)+ "\t\t"  + rs.getString(7)+ "\n";
		                       manResult.append(str);
		                       }
	               }
	               else if(select =="Nurses") {
	                   manResult.setText(schema[1][0] +"\t" +  schema[1][1] + "\t"+ schema[1][2] + "\t"+ schema[1][3] + "\t"+ schema[1][4] 
		                        + "\t\t"+ schema[1][5] + "\t\t"+ schema[1][6] +"\n");
		               rs = state.executeQuery(query);
		               while (rs.next()) {
		                       str = rs.getInt(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getString(4) 
		                       + "\t" + rs.getString(5) + "\t\t" + rs.getString(6)+ "\t\t"  + rs.getString(7)+ "\n";
		                       manResult.append(str);
		                       }
	               }
	               else if(select =="Patients") {
	                   manResult.setText(schema[2][0] +"\t" +  schema[2][1] + "\t"+ schema[2][2] + "\t"+ schema[2][3] + "\t"+ schema[2][4] 
		                        + "\t"+ schema[2][5] + "\t"+ schema[2][6] + "\t"+ schema[2][7]+ "\t\t"+ schema[2][8]+ "\t\t"+ schema[2][9]+"\n");
		               rs = state.executeQuery(query);
		               while (rs.next()) {
		                       str = rs.getInt(1) + "\t" + rs.getInt(2) + "\t" + rs.getInt(3) + "\t" + rs.getString(4) 
		                       + "\t" + rs.getString(5) + "\t" + rs.getString(6)+ "\t"  + rs.getString(7)
		                       + "\t"  + rs.getString(8)+ "\t\t"  + rs.getString(9)+ "\t\t"  + rs.getString(10)+ "\n";
		                       manResult.append(str);
		                       }
	               }
	               else if(select =="treatments") {
	                   manResult.setText(schema[3][0] +"\t" +  schema[3][1] + "\t"+ schema[3][2] + "\t"+ schema[3][3] + "\t\t"+ schema[3][4]  +"\n");
		               rs = state.executeQuery(query);
		               while (rs.next()) {
		                       str = rs.getInt(1) + "\t" + rs.getInt(2) + "\t" + rs.getInt(3) + "\t" + rs.getString(4) 
		                       + "\t\t" + rs.getString(5) + "\n";
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
         Input Teat = new Input("treatments",5,schema);
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
                 String title[] = {"�뿩��ȣ","������Ϲ�ȣ","����","���ּ�","����ȣ","�� email","�뿩������","�뿩������","�뿩���"};
                 String query1 ="set @num ="+"'" +  input + "'";
                 String query2 = "select a.listid'�뿩��ȣ', c.CustId'������Ϲ�ȣ', c.CustName'����', c.CustAddress'���ּ�', c.CustPhonenum'����ȣ', c.CustEmail'��e-mail',\r\n" + 
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