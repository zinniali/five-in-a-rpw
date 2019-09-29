import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class main extends JFrame implements MouseListener{

        private static final long serialVersionUID = 1L;
        //set background

        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;

        int x,y;  // coordinate for mouse
        int[][] allChess = new int[15][15];   // 0 for no, 1 for black chess piece, 2 for white
        boolean isblack = true;   //true: black; false: white
        boolean canPlay = true;   // whether the game is ended
        String message = "Black first";
        String blackMessage = "No time limitation";
        String whiteMessage = "No time limitations";

        //record every position for black and white
        int[] chessX = new int[255];
        int[] chessY = new int[255];
        int countX,countY;

        //No time limitation
        int maxTime = 0;   //maximum time
        int blackTime = 0;
        int whileTime = 0;   //the remaining time for black and white

        public main(){
            this.setTitle("Five-in-a-row 1.0");
            this.setSize(500,500);
            this.setLocation((width - 500) / 2 , (height - 500) / 2 );
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setResizable(false);  //fix the size of the window
            this.setVisible(true);

            this.repaint();  //
            this.addMouseListener(this);


        }

        //draw the background
        public void paint(Graphics g){

            BufferedImage buf = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
            Graphics g1 =  buf.createGraphics();  // create painting tool
            g1.setColor(new Color(0,169,158));
            g1.fill3DRect(43, 60, 375, 375, true);

            for (int i = 0; i <= 15; i++) {
                g1.setColor(Color.WHITE);
                g1.drawLine(43, 60+i*25, 375+43, 60+i*25);  //draw horizontal lines
                g1.drawLine(43+i*25, 60, 43+i*25, 375+60);  //draw vertical lines
            }

            g1.setFont(new Font("黑体",Font.BOLD,20));
            g1.drawString("Game information:"+message,50,50);

            g1.drawRect(30, 440, 180, 40);
            g1.drawRect(250, 440, 180, 40);   //draw boxes for time for black and white
            g1.setFont(new Font("宋体",0,12));

            g1.drawString("TIME FOR BLACK: "+blackMessage,40,465);
            g1.drawString("TIME FOR WHITE: "+whiteMessage,260,465);

            g1.drawRect(430,66,55,20);
            g1.drawString("RESTATRT",432,80); //restart button

            g1.drawRect(430,106,55,20);
            g1.drawString("SETTINGS",432,120); //set button

            g1.drawRect(430,146, 55, 20);
            g1.drawString("HELP", 432, 160); //button for help

            g1.drawRect(430, 186, 55, 20);
            g1.drawString("QUIT GAME", 432, 200);  // quit button

            g1.drawRect(430, 246, 55, 20);
            g1.drawString("Sorry, I want to tack back a move", 442, 260);  // take back a move

            g1.drawRect(430, 286, 55, 20);
            g1.drawString("I GIVE UP:(", 442, 300);  // give up


            for(int i=0; i<15; i++){
                for (int j = 0; j < 15; j++) {
                    //draw black pieces
                    if(allChess[i][j] == 1){
                        int tempX = i*25+47;
                        int tempY = j*25+64;
                        g1.setColor(Color.BLACK);
                        g1.fillOval(tempX, tempY, 16, 16);
                        g1.setColor(Color.BLACK);
                        g1.drawOval(tempX, tempY, 16, 16);
                    }

                    //draw white pieces
                    if(allChess[i][j] == 2){
                        int tempX = i*25+47;
                        int tempY = j*25+64;
                        g1.setColor(Color.WHITE);
                        g1.fillOval(tempX, tempY, 16, 16);
                        g1.setColor(Color.WHITE);
                        g1.drawOval(tempX, tempY, 16, 16);
                    }
                }
            }


            g.drawImage(buf, 0, 0,this);
        }



        public void mousePressed(MouseEvent e){
            if(canPlay){
                x=e.getX();
                y=e.getY();  // get the coordinate of mouse
                if(x>55 && x<= 405  && y>=72 && y<=420){
                    //keep the mouse within the board
                    if((x-55)%25>12){
                        x=(x-55)/25 + 1;
                    }else {
                        x = (x-55)/25;
                    }
                    if((y-72)%25>12){
                        y=(y-72)/25 + 1;
                    }else {
                        y=(y-72)/25;
                    }

                    //black/white can go now
                    if(allChess[x][y] == 0){
                        chessX[countX++] = x;
                        chessY[countY++] = y;
                        if(isblack){
                            allChess[x][y] = 1;
                            isblack = false;
                            message = "White's turn";
                        }else {
                            allChess[x][y] = 2;
                            isblack = true;
                            message = "Black's turn";
                        }
                        this.repaint();

                        if(this.isWin()){
                            if(allChess[x][y] == 1){
                                JOptionPane.showMessageDialog(this, "Game over. Black wins!");
                            }else {
                                JOptionPane.showMessageDialog(this, "Game over. White wins!");
                            }
                            this.canPlay = false;  //game ends
                        }


                    }
                }
            }

            //restart
            if(e.getX() >=430 && e.getY() <= (428+55)  && e.getY() >= 66
                    &&	e.getY() <= (66+20) ){
                int result = JOptionPane.showConfirmDialog(this, "Do you want to restart the game？");
                if(result == 0){
                    restarGame();
                }
            }


            //rules for the game
            if(e.getX() >= 430 && e.getY() <= (430+55)  && e.getY() >=146
                    &&	e.getY() <= (146+20) ){
                JOptionPane.showMessageDialog(this, "Rule:if there are five-in-a-row, then you win!");
            }

            //exit game
            if(e.getX() >=430 && e.getX() <= (430+55)  && e.getY() >=186
                    &&  e.getY() <= (186+20)){
                int result = JOptionPane.showConfirmDialog(this, "Do you want to exit the game？");
                if(result == 0){
                    System.exit(0);
                }
            }

            //take back
            if(e.getX() >= 430 && e.getX() <= (430+55) && e.getY() >= 246
                    &&  e.getY() <= (246+20)){
                int result = JOptionPane.showConfirmDialog(this,
                        (isblack == true ? "White wants to take back a move, do black agree？" :"Black wants to take back a move, do white agree？"));
                // result = 0 means take back the move
                if(result == 0){
                    allChess[chessX[--countX]][chessY[--countY]]=0;
                    if(isblack == true ){
                        isblack = false;
                    }else {
                        isblack = true;
                    }

                    this.repaint();  //redraw the board
                }
            }

            //give up
            if(e.getX()>=430 && e.getX()<=(428+55) && e.getY()>=286
                    && e.getY()<=(286+20)){
                int result=JOptionPane.showConfirmDialog(this, "Do you want to give up?");
                if(result==0){
                    JOptionPane.showMessageDialog(this,
                            "Game Over,"+(isblack==true ? "White wins!" : "Black wins!"));
                }
            }

        }

        public void restarGame(){
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    allChess[i][j] = 0;  //empty pieces on the board
                }

            }

            //empty all records for pieces coordinates.
            for (int i = 0; i < 15; i++) {
                chessX[i] = 0;
                chessY[i] = 0;

            }

            countX =0;
            countY =0;
            message = "Black goes first";
            blackMessage = "no limitations";
            whiteMessage = "no limitations";
            blackTime = maxTime;
            whileTime = maxTime;
            isblack = true;
            canPlay = true;
            this.repaint();

        }



        /**
         * rules for win and lose
         * @return
         */
        public boolean isWin(){
            boolean flag = false;
            int count = 1;  //how many pieces are connected, initial value=1
            int color = allChess[x][y];  //color = 1 (black) color = 2(white)

            //check horizontally if there is five-in-a-row(same y-coor, meaning in allChess[x][y],same y)
            count = this.checkCount(1,0,color);
            if(count >= 5){
                flag = true;
            }else {
                //check vertically
                count = this.checkCount(0,1,color);
                if(count >= 5){
                    flag = true;
                }else {
                    //check north-east, south-west
                    count = this.checkCount(1,-1,color);
                    if(count >= 5){
                        flag = true;
                    }else {
                        //check south-east,north-west
                        count = this.checkCount(1,1,color);
                        if(count >= 5){
                            flag =  true;
                        }
                    }
                }
            }

            return flag;
        }
        /**
         * check if there is five-in-a-row
         * @param xChange
         * @param yChenge
         * @param color
         * @return
         */
        public int checkCount(int xChange , int yChenge ,int color){
            int count = 1;
            int tempX = xChange;
            int tempy = yChenge;  //save original value

            //x,y is the original coordinate for mouse clicking
            //x,y range: 0-15 (search for same color pieces on the board)
            while(x + xChange >=0 && x+xChange <15  && y+yChenge >=0 &&
                    y+yChenge < 15 && color == allChess[x+xChange][y+yChenge]){

                count++;
                if(xChange != 0)  xChange++;
                if(yChenge != 0 ){
                    if(yChenge != 0){
                        if(yChenge > 0) {
                            yChenge++;
                        }else {
                            yChenge--;
                        }
                    }
                }

            }


            xChange = tempX;
            yChenge = tempy;   // set back to the original value


            while(x-xChange >=0 && x-xChange <15 && y-yChenge >=0 &&
                    y-yChenge <15 && color == allChess[x-xChange][y-yChenge]){
                count++;
                if(xChange != 0){
                    xChange++;
                }
                if(yChenge != 0){
                    if (yChenge > 0) {
                        yChenge++;
                    }else {
                        yChenge--;
                    }
                }
            }

            return count;
        }



        public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        public static void main(String[] args) {
            new main();
        }



}
