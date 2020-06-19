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
   static Connection con; //연결 connect
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
       UI.setTitle("16011036/이신필");
       UI.setSize(700,600);
       UI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       Manager();     
       UI.add(manager);
       UI.setVisible(true);
       addmouselistener();
       conDB(); //연결
       InitDB();
       InsertTables();
       InsertData();
 
        
    }
    public void addmouselistener() {
       listener = new MyMouseListener();
       manInit.addMouseListener(listener); //초기화 
        for(int i =0;i<5;i++) {
              MBs[i].addMouseListener(listener); // 입력5개
        }
        for(int j = 0;j<3;j++) {
           MBsearch[j].addMouseListener(listener);// 검색 3개
       }
    }
    
    public void Manager() {
       manager = new JPanel();
       manager.setBackground(backgroundC);
       manager.setSize(700,400);
       manager.setBorder(BorderFactory.createLineBorder(backgroundC,10));
       gbM1 = new GridBagConstraints();
       layoutM = new GridBagLayout();
       //버튼들//
       MBs = new JButton[5]; // MBs 입력버튼 5개;
       MBsearch = new JButton[3]; // MBs 입력버튼 5개;
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
    	String title[] = {"입력", " 검색"};
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
        String[] text = {"검색1","검색2 ","검색3"};

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
       manInit = new JButton("데이터베이스 초기화");
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

        // GridBagLayout의 GridBagConstraints의 set하는 방법
 }

    public void makeMScroll(JComponent c, int x, int y, int w, int h) {

        gbM1.gridx = x;
        gbM1.gridy = y;
        gbM1.gridwidth = w;
        gbM1.gridheight = h;
        gbM1.weighty = 50;
        layoutM.setConstraints(c, gbM1);
        gbM1.weighty = 1;
        // GridBagLayout의 GridBagConstraints의 set하는 방법
 }

    public void conDB(){
          try { /* 드라이버를 찾는 과정 */
                 Class.forName("com.mysql.cj.jdbc.Driver");   
                 System.out.println("드라이버 로드 성공");
               } catch(ClassNotFoundException e) {
                   e.printStackTrace();
                }
          // Class.forName()으로 드라이버를 로딩한다. 드라이버 이름을 Class.forName에 입력한다.      
               try { /* 데이터베이스를 연결하는 과정 */
                 System.out.println("데이터베이스 연결 준비...");   
                 
                 con=DriverManager.getConnection(url,userid,pwd);
                 
                 System.out.println("데이터베이스 연결 성공");
                 manResult.setText("관리자 연결 성공\n");
                  
               } catch(SQLException e) {
                   e.printStackTrace();
                    manResult.setText("관리자 연결 실패\n"+e);
                 }
       }
  
    public void InsertData() {
        
        try {
             //Doctors 입력
             int a=0;
             int i=0;
              String query = "INSERT INTO Doctors Values(";
              String data[] = new String [20];
              	  data[0] = "'980312','소아과','이태정','M','010-333-1340','ltj@hanbh.com','과장'";
              	data[1] = "'000601','내과','안성기','M','011-222-0987','ask@hanbh.com','과장'";
              	data[2] = "'001208','외과','김민종','M','010-333-1340','kmj@hanbh.com','과장'";
              	data[3] = "'020403','내과','이태서','M','010-333-1340','lts@hanbh.com','과장'";
              	data[4] = "'050900','피부과','김연아','F','010-333-1340','kya@hanbh.com','전문의'";
              	data[5] = "'050101','소아과','차태현','M','010-333-1340','cth@hanbh.com','전문의'";
              	data[6] = "'062019','내과','전지현','F','010-333-1340','jjh@hanbh.com','전문의'";
              	data[7] = "'070576','소아과','홍길동','M','010-333-1340','hgd@hanbh.com','전문의'";
              	data[8] = "'080543','피부과','유재석','M','010-333-1340','yjs@hanbh.com','과장'";
              	data[9] = "'091001','방사선과','김병만','M','010-333-1340','kbm@hanbh.com','전문의'";
            	  data[10] = "000010,'정신의학과','김상준','M','010-0000-0010','김상준@gmail.com','레지던트'";
            	  data[11] = "000011,'정신의학과','전수민','F','010-0000-0011','전수민@gmail.com','인턴'";
            	  data[12] = "000012,'내과','남궁민수','M','010-0000-0012','12@gmail.com','과장'";
            	  data[13] = "000013,'내과','갓상준','M','010-0000-0013','13@gmail.com','병원장'";
            	  data[14] = "000014,'소아과','남궁상현','M','010-0000-0040','14@gmail.com','레지던트'";
            	  data[15] = "000015,'소아과','머상준','M','010-0000-0123','15@gmail.com','인턴'";
            	  data[16] = "000016,'소아과','대상화','M','010-0000-0210','16@gmail.com','인턴'";
            	  data[17] = "000017,'방사선과','남궁설','F','010-0000-0510','17@gmail.com','과장'";
            	  data[18] = "000018,'방사선과','감상준','M','010-0000-0710','18@gmail.com','과장'";
            	  data[19] = "000019,'외과','상상준','F','010-0000-5689','19@gmail.com','레지던트'";
            	  
              state = con.createStatement();
              for(i=0;i<20;i++) {
                  state.executeUpdate(query+data[i]+")");
              }
              //Nurses
              query = "INSERT INTO Nurses Values(";
              String data1[] = new String [20];
              data1[0] = "050302,'소아과','김은영','F','010-555-8751','key@gmail.com','수간호사'";
              data1[1] = "050021,'내과','윤성애','F','016-333-8745','ysa@gmail.com','수간호사'";
              data1[2] = "040089,'피부과','신지원','M','010-666-7646','sjw@gmail.com','주임'";
              data1[3] = "070605,'방사선과','유정화','F','010-333-4588','yjh@gmail.com','주임'";
              data1[4] = "070804,'내과','라하나','F','010-222-1340','nhn@gmail.com','주임'";
              data1[5] = "071018,'소아과','김화경','F','010-888-4116','khk@gmail.com','주임'";
              data1[6] = "100356,'소아과','이선용','M','010-777-1234','lsy@gmail.com','간호사'";
              data1[7] = "104145,'외과','김현','F','010-999-8520','kh@gmail.com','간호사'";
              data1[8] = "120309,'피부과','박성완','F','010-777-4996','psw@gmail.com','간호사'";
              data1[9] = "130211,'외과','이서연','M','010-222-3214','lsy2@gmail.com','간호사'";
              
              data1[10] = "000010,'정신의학과','장원영','F','010-1234-0010','a10@gmail.com','주임'";
              data1[11] = "000011,'정신의학과','장영실','M','010-1234-0011','a11@gmail.com','주임'";
              data1[12] = "000012,'방사선과','김원희','F','010-1234-0012','a12@gmail.com','주임'";
              data1[13] = "000013,'방사선과','박원삼','M','010-1234-0013','a13@gmail.com','주임'";
              data1[14] = "000014,'소아과','장원사','F','010-1234-0014','a14@gmail.com','수간호사'";
              data1[15] = "000015,'소아과','장원오','F','010-1234-0015','a15@gmail.com','수간호사'";
              data1[16] = "000016,'소아과','장원육','M','010-1234-0016','a16@gmail.com','간호사'";
              data1[17] = "000017,'외과','장원칠','F','010-1234-0017','a17@gmail.com','간호사'";
              data1[18] = "000018,'외과','장원팔','F','010-1234-0018','a18@gmail.com','간호사'";
              data1[19] = "000019,'내과','장원구','M','010-1234-0019','a19@gmail.com','간호사'";
              


              state = con.createStatement();
              for(i=0;i<20;i++) {

                  state.executeUpdate(query+data1[i]+")");
                  
              }

              //Patients 입력
              query = "INSERT INTO Patients Values(";
              String data2[] = new String [20];
              data2[0] = "2345,050302,980312,'안상건','M','232345','서울','010-555-7845','ask@gmail.com','회사원'";
              data2[1] = "3545,040089,020403,'김성룡','M','543545','서울','010-333-7812','ksh@gmail.com','자영업'";
              data2[2] = "3424,070605,080543,'이종진','M','433424','부산','010-888-4859','ljj@gmail.com','회사원'";
              data2[3] = "7675,100356,050900,'최광석','M','677675','당진','010-222-4847','cks@gmail.com','회사원'";
              data2[4] = "4533,070804,000601,'정한경','M','744533','강릉','010-777-9630','jhk@gmail.com','교수'";
              data2[5] = "5546,120309,070576,'유원현','M','765546','대구','010-777-0214','ywh@gmail.com','자영업'";
              data2[6] = "4543,070804,050101,'최재정','M','454543','부산','010-555-4187','cjj@gmail.com','회사원'";
              data2[7] = "9768,130211,091001,'이진희','F',119768,'서울','010-888-3675','ljh@gmail.com','교수'";
              data2[8] = "4234,130211,091001,'오나미','F',234234,'속초','010-999-6541','onm@gmail.com','학생'";
              data2[9] = "7643,071018,062019,'송성묵','M',987643,'서울','010-222-5874','ssm@gmail.com','학생'";
              data2[10] = "0010,000010,000010,'영신필','M',000010,'서울','010-1555-7845','ask1@gmail.com','학생'";
              data2[11] = "0011,000010,000014,'이신필','M',000011,'부산','010-2555-7845','ask2@gmail.com','학생'";
              data2[12] = "0012,000011,000019,'삼신필','F',000012,'부산','010-3555-7845','ask3@gmail.com','학생'";
              data2[13] = "0013,000011,000010,'사신필','M',000013,'대전','010-4555-7845','ask4@gmail.com','학생'";
              data2[14] = "0014,000017,000010,'오신필','M',000014,'대전','010-5555-7845','ask5@gmail.com','학생'";
              data2[15] = "0015,000017,000014,'육신필','F',000015,'서울','010-6555-7845','ask6@gmail.com','회사원'";
              data2[16] = "0016,000018,000014,'칠신필','F',000016,'서울','010-7555-7845','ask7@gmail.com','자영업'";
              data2[17] = "0017,000018,000019,'팔신필','F',000017,'대전','010-8555-7845','ask8@gmail.com','회사원'";
              data2[18] = "0018,000019,000019,'구신필','F',000018,'서울','010-9555-7845','ask9@gmail.com','자영업'";
              data2[19] = "0019,000019,000019,'열신필','M',000019,'서울','010-0555-7845','ask10@gmail.com','회사원'";

              
              state = con.createStatement();
              for(i=0;i<20;i++) {
                  state.executeUpdate(query+data2[i]+")");
              }
    
              //treatments
              query = "INSERT INTO treatments Values(";
              String data3[] = new String [20];
              data3[0] = "130516023,2345,980312,'감기,몸살','2013-05-16'";
              data3[1] = "130628100,3545,020403,'피부 트러블 치료','2013-06-28'";
              data3[2] = "131205056,3424,080543,'목 디스크로   MRI 촬영','2013-12-05'";
              data3[3] = "131218024,7675,050900,'중이염','2013-12-18'";
              data3[4] = "131224012,4533,000601,'장염','2013-12-24'";
              data3[5] = "140103001,5546,070576,'여드름 치료','2014-01-03'";
              data3[6] = "140109026,4543,050101,'위염','2014-01-09'";
              data3[7] = "140226102,9768,091001,'화상치료','2014-02-26'";
              data3[8] = "140303003,4234,091001,'교통사고 외상치료','2014-03-03'";
              data3[9] = "140308087,7643,062019,'장염','2014-03-08'";
              data3[10] = "000000001,0010,000010,'감기','2014-05-01'";
              data3[11] = "000000002,0011,000014,'감기','2014-05-03'";
              data3[12] = "000000003,0012,000019,'감기','2014-05-05'";
              data3[13] = "000000004,0013,000010,'감기','2014-05-07'";
              data3[14] = "000000005,0014,000010,'감기','2014-05-09'";
              data3[15] = "000000006,0015,000014,'감기','2014-05-11'";
              data3[16] = "000000007,0016,000014,'감기','2014-05-13'";
              data3[17] = "00000008,0017,000019,'감기','2014-05-15'";
              data3[18] = "000000009,0018,000019,'감기','2014-05-17'";
              data3[19] = "000000010,0019,000019,'감기','2014-05-19'";
              state = con.createStatement();
              for(i=0;i<20;i++) {
                  state.executeUpdate(query+data3[i]+")");
              }

              //Charts
              query = "INSERT INTO Charts Values(";
              String data4[] = new String [20];
              data4[0] = "1,130516023,980312,2345,050302,'감기,몸살'";
              data4[1] = "2,130628100,020403,3545,040089,'피부 트러블 치료'";
              data4[2] = "3,131205056,080543,3424,070605,'목 디스크로   MRI 촬영'";
              data4[3] = "4,131218024,050900,7675,100356,'중이염'";
              data4[4] = "5,131224012,000601,4533,070804,'장염'";
              data4[5] = "6,140103001,070576,5546,120309,'여드름 치료'";
              data4[6] = "7,140109026,050101,4543,070804,'위염'";
              data4[7] = "8,140226102,091001,9768,130211,'화상치료'";
              data4[8] = "9,140303003,091001,4234,130211,'교통사고 외상치료'";
              data4[9] = "10,140308087,062019,7643,071018,'장염'";
              data4[10] = "11,00000001,000010,0010,000010,'감기'";
              data4[11] = "12,00000002,000014,0011,000010,'감기'";
              data4[12] = "13,00000003,000019,0012,000011,'감기'";
              data4[13] = "14,00000004,000010,0013,000011,'감기'";
              data4[14] = "15,00000005,000010,0014,000017,'감기'";
              data4[15] = "16,00000006,000014,0015,000017,'감기'";
              data4[16] = "17,00000007,000014,0016,000018,'감기'";
              data4[17] = "18,00000008,000019,0017,000018,'감기'";
              data4[18] = "19,00000009,000019,0018,000019,'감기'";
              data4[19] = "20,00000010,000019,0019,000019,'감기'";

              state = con.createStatement();
              for(i=0;i<20;i++) {
                  state.executeUpdate(query+data4[i]+")");
              }
            System.out.println("데이터 입력 성공");
            manResult.append("데이터 입력 성공\n");
         }catch(Exception e4) {
            System.out.println("데이터 입력 실패"+e4);
            manResult.setText("데이터 입력 실패\n"+e4);
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
            System.out.println("테이블 입력 성공");
            manResult.append("테이블 입력 성공\n");
         }catch(Exception e4) {
            System.out.println("테이블 입력 실패"+e4);
            manResult.setText("테이블 입력 실패\n"+e4);
         }
       }
    public void InitDB() {
        try {
           state = con.createStatement();
           
           manResult.setText("DB초기화 , 기존 madang database 삭제");
           String query = "DROP DATABASE IF EXISTS  madang";
           state.executeUpdate(query);
           
           manResult.setText("DB초기화 , madang database 생성");
           query = "create database madang";
           state.executeUpdate(query); 
           query = "grant all privileges on madang.* to madang@localhost with grant option";
           state.executeUpdate(query);
           query = "USE `madang`";
           state.executeUpdate(query); 
           System.out.println("DB 초기화 성공");
           manResult.setText("DB 초기화 성공\n");
        }catch(Exception e4) {
           System.out.println("DB 초기화 실패"+e4);
           manResult.setText("DB 초기화 실패\n"+e4);
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

       @Override//마우스가 버튼 안으로 들어오면 빨간색으로 바뀜
       public void mouseEntered(MouseEvent e) {
           JButton b = (JButton)e.getSource();
           b.setBackground(mousein);
       }

       @Override//마우스가 버튼 밖으로 나가면 노란색으로 바뀜
       public void mouseExited(MouseEvent e) {
           JButton b = (JButton)e.getSource();
           b.setBackground(ComponentC);
       }

   }

   //////////input 창 class//////
   public class Input implements ActionListener{
      int size;
      String table ="";
      String schema[];
      JFrame input = new JFrame();
      JButton In = new JButton("확인");
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
         
         input.setTitle("입력하세요");

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
             System.out.println("DB 입력");
             manResult.setText("DB 입력");
             String query = "INSERT INTO " + table + " Values(";

             state = con.createStatement();
             state.executeUpdate(query+data());

             manResult.setText("DB 입력 성공\n");
             System.out.println("DB 성공");
          }catch(Exception e4) {
             System.out.println("DB 입력 실패 "+e4);
             manResult.setText("DB 입력 실패 \n"+e4);
          }
         input.dispose();
      }
   }
   
   public class Search1 implements ActionListener{
	      int size;
	      JFrame input = new JFrame();
	      JButton In = new JButton("확인");
	      JLabel label;
	      JComboBox box;
	      JPanel pls;
	      String results,select;
	      String schema[] = {"------","Doctors" , "Nurses","Patients","treatments","Charts"};
	      
	      public Search1() {
	         label = new JLabel("table 선택");
	         pls = new JPanel();
	         results = new String();
	         box = new JComboBox(schema);
	         init();
	         input.setVisible(true);
	         input.setLayout(new FlowLayout());
	         input.setBounds(700,100,800,200);
	      }
	      public void init() {
	         
	         input.setTitle("입력하세요");
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
	            JOptionPane.showMessageDialog(input, "속성을 선택해주세요");
	         }
	         else{
	        	String schema[][] = {{"doc_id", "major_treat", "doc_name", "doc_gen", "doc_phone", "doc_email", "doc_position"},
	        			{"nur_id", "major_job", "nur_nam", "nur_gen", "nur_phone", "nur_email", "nur_position"}
	        	,{"pat_id", "nur_id", "doc_id", "pat_name", "pat_gen", "pat_jumin", "pat_addr", "pat_phone", "pat_email", "pat_job"}
	        	,{"treat_id", "pat_id", "doc_id", "treat_contents", "treat_date"},
	        	{"chart_id", "treat_id", "doc_id", "pat_id", "nur_id", "chart_contents"}};
	        	String str;
	            try {   
	               System.out.println("DB 변경");
	               manResult.setText("DB 변경");
	               String query = "SELECT "+ "*" + " FROM " + select;
	               state = con.createStatement();
	               state.executeQuery(query);
	              //if문으로 table select 구분해서 각각 질의문실행
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

	               System.out.println("DB 검색 성공 ");
	               }catch(Exception e4) {
	               System.out.println("DB 검색 실패 "+e4);
	               manResult.setText("DB 검색 실패 \n"+e4);
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
  			System.out.println("DB 초기화 성공");
  			manResult.setText("DB 초기화 성공\n");
  			a = "성공";  			
    	  }
    	  catch(Exception e4){
  			System.out.println("DB 초기화 실패"+e4);
  			manResult.setText("DB 초기화 실패\n"+e4);
  			a = "실패";
    	  }
    	  finally {
    		  JOptionPane.showMessageDialog(manager, "DB 초기화 " + a);
    	  }
    }       
   }

   private class ActionListenerDocInsert implements ActionListener { // doctor 테이블 insert 리스너
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
          String input = JOptionPane.showInputDialog(manager,"날짜입력 ex)2020-06-09");
          if(input != null) {
             try {
                 state = con.createStatement();          
                 String title[] = {"대여번호","운전등록번호","고객명","고객주소","고객번호","고객 email","대여시작일","대여마감일","대여비용"};
                 String query1 ="set @num ="+"'" +  input + "'";
                 String query2 = "select a.listid'대여번호', c.CustId'운전등록번호', c.CustName'고객명', c.CustAddress'고객주소', c.CustPhonenum'고객번호', c.CustEmail'고객e-mail',\r\n" + 
                       "      a.RentDate as 대여시작일,DATE_ADD(a.rentdate, INTERVAL a.rentperiod DAY) as 대여마감일, (RentPeriod * Rental)'대여비용'\r\n" + 
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
                    System.out.println("질의문실행 성공 ");
              }catch(Exception e4) {
                 System.out.println("오류 : 질의문실행 실패 "+e4);
                 manResult.setText("오류 : 질의문실행 실패 \n"+e4);
              }
          }
       
       }
    }
   
   private class ActionListenerMBS3 implements ActionListener {
       public void actionPerformed (ActionEvent e) {
          String input = JOptionPane.showInputDialog(manager,"회사 ID입력");
          if(input != null) {
             try {
                 state = con.createStatement();             
                 String title[] = {"정비번호" ,"캠핑카등록ID","회사ID","정비소 ID","정비소이름","정비소 주소","정비소번호","정비담당자이름","정비담당자email","운전등록번호","수리내용","수리날짜","수리비","수리비 납부기한"};
                 String query1 ="set @num ="+"'" +  input + "'";
                 String query2 = "select r.RepairId'정비번호', r.CarId'캠핑카등록ID', r.ComId'회사ID', r.WorkShopId'정비소ID', w.ShopName'정비소이름',w.ShopAddress'정비소주소', w.ShopPhoneNum'정비소번호', w.ShopManagerName'정비담당자이름', \r\n" + 
                       "      w.ShopManagerEmail'정비담당자e-mail', r.CustId'운전등록번호', r.RepairInfo'수리내용', r.RepairDate'수리날짜', r.RepairFee'수리비', r.RepairFeeDeadLine'수리비 납부기한'\r\n" + 
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
                    System.out.println("질의문실행 성공 ");
              }catch(Exception e4) {
                 System.out.println("오류 : 질의문실행 실패 "+e4);
                 manResult.setText("오류 : 질의문실행 실패 \n"+e4);
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
               System.out.println("프로그램 완전 종료!");          
                System.exit(0);
              }
            });
        }

}