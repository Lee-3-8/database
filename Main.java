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

            	  data[10] = "10,'정신의학과','김상준','남','010-0000-0010','김상준@gmail.com','레지던트'";
            	  data[11] = "11,'정신의학과','전수민','여','010-0000-0011','전수민@gmail.com','인턴'";
            	  data[12] = "12,'비뇨기과','남궁민수','남','010-0000-0012','12@gmail.com','과장'";
            	  data[13] = "13,'비뇨기과','갓상준','남','010-0000-0013','13@gmail.com','병원장'";
            	  data[14] = "14,'소아과','남궁상현','남','010-0000-0040','14@gmail.com','레지던트'";
            	  data[15] = "15,'산부인과','머상준','남','010-0000-0123','15@gmail.com','인턴'";
            	  data[16] = "16,'소아과','대상화','남','010-0000-0210','16@gmail.com','인턴'";
            	  data[17] = "17,'산부인과','남궁설','여','010-0000-0510','17@gmail.com','과장'";
            	  data[18] = "18,'흉부외과','감상준','남','010-0000-0710','18@gmail.com','과장'";
            	  data[19] = "19,'비뇨기과','상상준','여','010-0000-5689','19@gmail.com','레지던트'";
            	  
              state = con.createStatement();
              for(i=10;i<20;i++) {
                  state.executeUpdate(query+data[i]+")");
              }
              //Nurses
              query = "INSERT INTO Nurses Values(";
              String data1[] = new String [20];
              data1[10] = "10,'정신의학과','장원영','여','010-1234-0010','a10@gmail.com','수간호사'";
              data1[11] = "11,'정신의학과','장영실','남','010-1234-0011','a11@gmail.com','간호사'";
              data1[12] = "12,'정신의학과','김원희','여','010-1234-0012','a12@gmail.com','간호사'";
              data1[13] = "13,'정신의학과','박원삼','남','010-1234-0013','a13@gmail.com','간호사'";
              data1[14] = "14,'정신의학과','장원사','여','010-1234-0014','a14@gmail.com','간호사'";
              data1[15] = "15,'정신의학과','장원오','여','010-1234-0015','a15@gmail.com','간호사'";
              data1[16] = "16,'정신의학과','장원육','남','010-1234-0016','a16@gmail.com','간호사'";
              data1[17] = "17,'정신의학과','장원칠','여','010-1234-0017','a17@gmail.com','간호사'";
              data1[18] = "18,'정신의학과','장원팔','여','010-1234-0018','a18@gmail.com','간호사'";
              data1[19] = "19,'정신의학과','장원구','남','010-1234-0019','a19@gmail.com','간호사'";
              


              state = con.createStatement();
              for(i=10;i<20;i++) {
                  state.executeUpdate(query+data1[i]+")");
              }

              //Patients 입력
              query = "INSERT INTO Patients Values(";
              String data2[] = new String [20];
              data2[10] = "10,10,10,'탐캔치','남','970101-1063001','청계천','010-4130-0001','b10@gmail.com','챔피언'";

              
              state = con.createStatement();
              for(i=10;i<11;i++) {
                  state.executeUpdate(query+data2[i]+")");
              }
    
              //Teatments
              query = "INSERT INTO Teatments Values(";
              String data3[] = new String [20];
              data3[10] = "10,10,10,'아멀랑','2020-05-20'";
              state = con.createStatement();
              for(i=10;i<11;i++) {
                  state.executeUpdate(query+data3[i]+")");
              }

              System.out.println("asdfa");
              //Charts
              query = "INSERT INTO Charts Values(";
              String data4[] = new String [20];
              data4[10] = "10,10,10,10,10,'이상무'";

              state = con.createStatement();
              for(i=10;i<11;i++) {
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
	      String schema[] = {"------","Doctors" , "Nurses","Patients","Teatments","Charts"};
	      
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
	    	  String title[] = {"Doctors" , "Nurses","Patients","Teatments","Charts"};
	         if(select == null || select =="------") {
	            JOptionPane.showMessageDialog(input, "속성을 선택해주세요");
	         }
	         else{
	        	String schema[][] = {{"doc_id", "major_treat", "doc_name", "doc_gen", "doc_phone", "doc_email", "doc_position"},
	        			{"nur_id", "major_job", "nur_nam", "nur_gen", "nur_phone", "nur_email", "nur_position"}
	        	,{"pat_id", "nur_id", "doc_id", "pat_name", "pat_gen", "pat_jumin", "pat_addr", "pat_phone", "pat_emaiz", "pat_job"}
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