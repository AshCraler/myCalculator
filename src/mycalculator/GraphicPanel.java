/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mycalculator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javafx.scene.shape.Line;
import mycalculator.CalculatorObjects;

/**
 *
 * @author AshCraler
 */
public class GraphicPanel extends javax.swing.JPanel {

    public class MovingAdapter extends MouseAdapter{
        private int x;
        private int y;
        
        @Override
        public void mousePressed(MouseEvent e){
            x = e.getX();
            y = e.getY();
            
        }
        
        @Override 
        public void mouseDragged(MouseEvent e){
            doMove(e);
        }
        
        private void doMove(MouseEvent e){
            int dx = e.getX() - x;
            int dy = e.getY() - y;
            
            //cap nhat toa do goc O
            x0 += dx;
            y0 += dy;
            
            //cap nhat vi tri cua Adapter
            x += dx;
            y += dy;
            
            repaint();
        }
    }
    
    public class WheelAdapter implements MouseWheelListener{

        int x, y;
        private CalculatorObjects.UnitState state;
        int amount = 1;
        int firstDimension, lastDimension;//su dung de tinh ti le phong hinh
        float zoomRatio;
        
        public WheelAdapter() {
            state = CalculatorObjects.UnitState.monoUnit;
        }
        
        
        
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            doScale(e);
        }
        
        private void doScale(MouseWheelEvent e){
            x = e.getX();
            y = e.getY();
            firstDimension = netDistance;
            lastDimension = netDistance;
            
            if(e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL){
                amount = e.getWheelRotation();
                if(e.getWheelRotation() > 0){//neu dang phong to
                    if(netDistance + amount*2 < 40)
                        netDistance += amount * 2;
                    else{//phan chia thien ha
                        switch(state){
                            case monoUnit:
                                unitDistance *= 2;
                                state = CalculatorObjects.UnitState.pentaUnit;
                                netDistance = 20;
                                break;
                                
                            case diUnit:
                                unitDistance *= 2;
                                state = CalculatorObjects.UnitState.monoUnit;
                                netDistance = 20;
                                break;
                                
                            case pentaUnit:
                                unitDistance *= 2.5;
                                state = CalculatorObjects.UnitState.diUnit;
                                netDistance = 16;
                                break;
                                
                            default:
                                break;
                        }
//                        unitDistance *= 2;
                        ratio = (float)unitDistance/graphDistance;
                        
                    }
                }
                else{//dang thu nho
                    if(netDistance + amount*2 >= 16)
                        netDistance += amount * 2;
                    else{//thong nhat thien ha
                        switch(state){
                            case monoUnit:
                                unitDistance /= 2;
                                state = CalculatorObjects.UnitState.diUnit;
                                netDistance = 32;
                                break;
                                
                            case diUnit:
                                unitDistance /= 2.5;
                                state = CalculatorObjects.UnitState.pentaUnit;
                                netDistance = 38;
                                break;
                                
                            case pentaUnit:
                                unitDistance /= 2;
                                state = CalculatorObjects.UnitState.monoUnit;
                                netDistance = 32;
                                break;
                                
                            default:
                                break;
                        }
//                        unitDistance /= 2;
                        ratio = (float)unitDistance/graphDistance;
//                        netDistance = 38;
                    }
                }
                
                graphDistance = 5*netDistance;
                unitDistance = (float)graphDistance * ratio;
            }

            lastDimension += amount*2;
            zoomRatio = (float)lastDimension/firstDimension;
            x0 -= (zoomRatio - 1) * (x - x0);
            y0 -= (zoomRatio -1) * (y - y0);
            repaint();
        }
        
    }
    public GraphicPanel(int width, int height) {
        initComponents();
        
        this.setSize(width,height);
        netDistance = 20;
        unitDistance = 100;
        graphDistance = 100;
    
        MovingAdapter ma = new MovingAdapter();
        addMouseMotionListener(ma);
        addMouseListener(ma);
        
        WheelAdapter wa = new WheelAdapter();
        addMouseWheelListener(wa);
        
        
        max_x = this.getWidth();
        max_y = this.getHeight();
        
        x0 = max_x/2;
        y0 = max_y/2;
        
        functions = new CalculatorObjects.myFunction[20];
        functionCount = 0;
        
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        setBackground(Color.white);
        drawAll(g);
//        drawLinear(g, CalculatorObjects.myColor.color6, 1, 1);
//        drawParabol(g,CalculatorObjects.myColor.color1, 1, 1, 1);
    }
    //Declaration
    Graphics2D g2D;
    
    int max_x, max_y;
    int x0, y0; //toa do goc O
    int netDistance;
    float unitDistance;
    int graphDistance;
    float ratio = (float) 1;
    
    CalculatorObjects.myFunction[] functions;
    int functionCount = 0;
    public int getFunctionCount(){
        return functionCount;
    }
    
    Point delta = new Point(0, 0);
    Point currPoint;
    
    public void addFunction(CalculatorObjects.myFunction f){
        functions[functionCount] = f;
        functionCount++;
    }
    
    public void removeFunction(int index){
        for(int i = index; i < functionCount; i++){
            functions[i] = functions[i+1];
        }
        
        functions[functionCount] = null;
        functionCount--;
    }
    
    private void drawAll(Graphics g){
        drawAxis(g);
        drawFunctionGraphs(g);
    }
    private void drawAxis(Graphics g){
        g2D = (Graphics2D) g;
        int index;
        
        //Ve luoi
        g2D.setStroke(CalculatorObjects.myStroke.netStroke);
        g2D.setColor(Color.lightGray);
        
        for(index = 1;x0 + index*netDistance < max_x; index++)
            if( index%5 !=0)
                g2D.drawLine(x0 + index*netDistance, 1, x0 + index*netDistance, max_y);
        for(index = 1; x0 - index*netDistance > 0; index++)
            if(index %5 != 0)
                g2D.drawLine(x0 - index*netDistance, 1, x0 - index*netDistance, max_y);
        
        for(index = 1; y0 + index*netDistance < max_y; index++)
            if(index%5 != 0)
                g2D.drawLine(1, y0 + index*netDistance, max_x, y0 + index*netDistance);
        for(index = 1; y0 - index*netDistance > 0; index++)
            if(index%5 != 0)
                g2D.drawLine(1, y0 - index*netDistance, max_x, y0 - index*netDistance);
        
        
        //ve truc don vi
        g2D.setStroke(CalculatorObjects.myStroke.unitStroke);
        g2D.setColor(Color.black);
        
        
        for(index = 1; x0 + index*graphDistance <max_x; index++){
            g2D.drawLine(x0 + index*graphDistance, 1, x0 + index*graphDistance, max_y);
            
            if(unitDistance > 100)
                g2D.drawString(String.valueOf((float)index/ratio), x0 + index*graphDistance + 10, y0 - 10);
            else
                g2D.drawString(String.valueOf((int)(index/ratio)), x0 + index*graphDistance + 10, y0 - 10);
        }

        for(index = 1; x0 - index*graphDistance > 0; index++){
            g2D.drawLine(x0 - index*graphDistance, 1, x0 - index*graphDistance, max_y);
            
            if(unitDistance > 100)
                g2D.drawString(String.valueOf(-(float)index/ratio), x0 - index*graphDistance + 10, y0 - 10);
            else
                g2D.drawString(String.valueOf(-(int)(index/ratio)), x0 - index*graphDistance + 10, y0 - 10);
        }
        
        for(index = 1; y0 + index*graphDistance < max_y; index++){
            g2D.drawLine(1,y0 + index*graphDistance,max_x,y0 + index*graphDistance);
            
            if(unitDistance > 100)
                g2D.drawString(String.valueOf(-(float)index/ratio), x0 + 10, y0 + index*graphDistance + 10);
            else
                g2D.drawString(String.valueOf(-(int)(index/ratio)), x0 + 10, y0 + index*graphDistance + 10);
        }
        for(index = 1; y0 - index*graphDistance > 0; index++){
            g2D.drawLine(1, y0 - index*graphDistance, max_x, y0 - index*graphDistance);
            
            if(unitDistance > 100)
                g2D.drawString(String.valueOf((float)index/ratio), x0 + 10, y0 - index*graphDistance + 10);
            else
                g2D.drawString(String.valueOf((int)(index/ratio)), x0 + 10, y0 - index*graphDistance + 10);
        }
        
        //ve truc Oxy
        g2D.setStroke(CalculatorObjects.myStroke.axisStroke);
        g2D.drawLine(1, y0, max_x, y0); //ve truc Ox
        g2D.drawLine(x0, 1, x0, max_y); //ve truc Oy
        
        g2D.setStroke(CalculatorObjects.myStroke.OxyStroke);
        g2D.drawString("O", x0 + 10, y0 + 20);
        g2D.drawString("x", max_x - 20, y0 - 10);
        g2D.drawString("y", x0 + 10, max_y - 20);
        
    }
    
    private void drawFunctionGraphs(Graphics g){
        for(int i = 0; i < functionCount; i++){
            switch(functions[i].getType()){
                case linear:
                    drawLinear(g, Color.red, functions[i].getInfo()[0], functions[i].getInfo()[1]);
                    break;
                    
                case circle:
                    drawCircle(g, Color.red, functions[i].getInfo()[0] , functions[i].getInfo()[1], functions[i].getInfo()[2]);
                    break;
                    
                case parabol:
                    drawParabol(g, Color.red, functions[i].getInfo()[0] , functions[i].getInfo()[1], functions[i].getInfo()[2]);
                    break;
                    
                default:
                    break;
            }
        }
    }
            
    private void drawCircle(Graphics g, Color col, float x, float y, float r){
        g2D = (Graphics2D)g;
        g2D.setStroke(CalculatorObjects.myStroke.lineStroke);
        
        g2D.setColor(col);
        int X = (int)(x*unitDistance + x0);
        int Y = (int)(-y*unitDistance + y0);
        int R = (int)(r*unitDistance);
        g2D.drawOval((int)(X - R), (int)(Y - R), 2*(int)R, 2*(int)R);
    }
    
    private void drawLinear(Graphics g, Color col, float a, float b){
        g2D = (Graphics2D)g;
        g2D.setStroke(CalculatorObjects.myStroke.lineStroke);
        
        g2D.setColor(col);
        
        Point firstPoint, secondPoint;
        
        int tempX = (int)((y0 - max_y - b*unitDistance) / a) + x0; //(tempX, max_y) => bottom
        int tempY = -(int)(-x0*a+b*unitDistance) + y0; //(0, tempY) => left
        
        
        if(tempY >= 0 && tempY <= max_y){ //ton tai mot diem thuoc bien trai
            firstPoint = new Point(0, tempY);
            
            if(tempX > 0 && tempX < max_x){ //ton tai mot diem thuoc bien duoi
                secondPoint = new Point(tempX, max_y);
            }else{ //khong co diem thuoc bien duoi
                tempX = (int)((y0 - b*unitDistance) / a) + x0; //(tempX, 0) => top
                tempY = -(int)((max_x-x0)*a + b*unitDistance) + y0; //(max_x, tempY) => right
                
                if(tempY >= 0 && tempY <= max_y) //ton tai mot diem thuoc bien phai
                    secondPoint = new Point(max_x, tempY);
                else //khong co diem thuoc bien phai, chung to co ton tai diem thuoc bien tren
                    secondPoint = new Point(tempX, 0);
            }
            
        }else{ //khong ton tai diem thuoc bien trai
            if(tempX > 0 && tempX < max_x){ //ton tai diem thuoc bien duoi
                firstPoint = new Point(tempX, max_y);
                tempX = (int)((y0 - b*unitDistance) / a) + x0; //(tempX, 0) => top
                tempY = -(int)((max_x-x0)*a + b*unitDistance) + y0; //(max_x, tempY) => right
                
                if(tempY >= 0 && tempY <= max_y) //ton tai mot diem thuoc bien phai
                    secondPoint = new Point(max_x, tempY);
                else //khong co diem thuoc bien phai, chung to co ton tai diem thuoc bien tren
                    secondPoint = new Point(tempX, 0);
                
            }else{ //khong ton tai diem thuoc bien duoi
                tempX = (int)((y0 - b*unitDistance) / a) + x0; //(tempX, 0) => top
                tempY = -(int)((max_x-x0)*a + b*unitDistance) + y0; //(max_x, tempY) => right
                
                if(tempY >= 0 && tempY <= max_y){ //ton tai mot diem thuoc bien phai, suy ra chac chan diem thu hai phai thuoc bien tren
                    firstPoint = new Point(max_x, tempY);
                    secondPoint = new Point(tempX, 0);
                    
                }else //khong ton tai diem thuoc bien phai suy ra duong thang khong hien thi trong khung nhin
                    return;
            }
        }
        
        if(firstPoint != null && secondPoint != null){
            
            g2D.drawLine(firstPoint.x, firstPoint.y, secondPoint.x, secondPoint.y);
        }
            
    }
    
    private float func2(float a, float b, float c, float x){
        float relativeX = (float)(x - x0)/unitDistance;
        
        return y0 - (a*relativeX*relativeX + b*relativeX + c)*unitDistance;
    }
    
    private void drawParabol(Graphics g, Color col, float a, float b, float c){
        g2D = (Graphics2D)g;
        
        g2D.setStroke(CalculatorObjects.myStroke.lineStroke);
        g2D.setColor(col);
        
        for(int i = 0; i < max_x; i++){
            g2D.drawString(".", i, func2(a, b, c, i));
        }
            
//        Point firstPoint, secondPoint, thirdPoint, forthPoint;
//        
//        
//        float tempB = b*unitDistance -2*a*x0;
//        float tempC = a*x0*x0 + b*x0*unitDistance + (c + y0)*unitDistance*unitDistance;
//        CalculatorObjects.quadraticEquation equationTop = new CalculatorObjects.quadraticEquation(a,tempB ,tempC);
//        
//        
//        
//        tempC -= max_y*unitDistance*unitDistance;
//        CalculatorObjects.quadraticEquation equationBot = new CalculatorObjects.quadraticEquation(a, tempB, tempC);
//        
//        if(a > 0){ //do thi huong len tren
//            if(equationBot.isSolvable && !equationBot.isSingleSolution){ //day cua do thi nam duoi so voi bien duoi
//                float x21 = equationBot.solution[0];
//                float x22 = equationBot.solution[1];
//                float x11 = equationTop.solution[0];
//                float x12 = equationTop.solution[1];
//                
//                if(x11 > max_x || x12 < 0) //do thi nam ngoai vung nhin thay
//                    return;
//                else{ //do thi co hien thi mot phan trong vung nhin thay
//                    if(x11 < max_x && x11 > 0){ //x11 thuoc bien tren
//                        firstPoint = new Point((int)x11, 0);
//                        
//                        if(x21 > max_x)//x21 khong thuoc bien duoi
//                            secondPoint = new Point(max_x, (int)func2(a, b, c, max_x));
//                        else //x21 thuoc bien duoi
//                            secondPoint = new Point((int)x21, max_y);
//                        
//                        if(x12 < max_x){ //x12 thuoc bien tren
//                                thirdPoint = new Point((int)x22, max_y);
//                                forthPoint = new Point((int)x12, 0);
//                        }else //x12 khong thuoc bien tren
//                            if(x22 < max_x){ //x22 thuoc bien duoi
//                                thirdPoint = new Point((int)x22, max_y);
//                                forthPoint = new Point(max_x, (int)func2(a, b, c, max_x));
//                            }else{ //neu x22 khong thuoc bien duoi thi khong lam gi
//                                thirdPoint = new Point(0, 0);
//                                forthPoint = new Point(0, 0);
//                            }
//                                
//                    }else{ //x11 khong thuoc bien tren
//                        if(x21 > 0){ //x21 thuoc bien duoi
//                            firstPoint = new Point(0, (int)func2(a, b, c, 0));
//                            secondPoint = new Point((int)x21, max_y);
//                            
//                            
//                        }else{ //neu x21 khong thuoc bien duoi 
//                            firstPoint = new Point((int)x12, 0);
//                        
//                        if(x22 < 0) //x22 khong thuoc bien duoi
//                            secondPoint = new Point(0, (int)func2(a, b, c, 0));
//                        else //x22 thuoc bien duoi
//                            secondPoint = new Point((int)x22, max_y);
//                        
//                        thirdPoint = new Point(0, 0);
//                        forthPoint = new Point(0, 0);
//                        }
//                        
//                        thirdPoint = new Point((int)x12, 0);
//                        
//                        if(x22 < 0) //x22 khong thuoc bien duoi
//                            forthPoint = new Point(0, (int)func2(a, b, c, 0));
//                        else //x22 thuoc bien duoi
//                            forthPoint = new Point((int)x22, max_y);
//                    }
//                    
//                }
//                
//                
//            }else{ //day cua do thi nam tren so voi bien duoi
//                if(equationTop.isSolvable && !equationTop.isSingleSolution){ //day cua do thi thuoc vung nhin thay
//                    firstPoint = new Point((int)equationTop.solution[0], 0);
//                    secondPoint = new Point((int)equationTop.solution[1], 0);
//                    
//                    thirdPoint = new Point(0, 0);
//                    forthPoint = new Point(0, 0);
//                }else //day cua do thi nam ngoai vung nhin thay
//                    return;
//            }
//            
//            
//            
//        }else{ //do thi huong xuong duoi
//            if(equationTop.isSolvable && !equationTop.isSingleSolution){ //day cua do thi nam tren so voi bien tren
//                float x21 = equationBot.solution[0];
//                float x22 = equationBot.solution[1];
//                float x11 = equationTop.solution[0];
//                float x12 = equationTop.solution[1];
//                
//                if(x21 > max_x || x22 < 0) //do thi nam ngoai vung nhin thay
//                    return;
//                else{ //do thi co hien thi mot phan trong vung nhin thay
//                    if(x21 < max_x && x21 > 0){ //x21 thuoc bien duoi
//                        firstPoint = new Point((int)x21, max_y);
//                        
//                        if(x11 > max_x) //x11 khong thuoc bien tren
//                            secondPoint = new Point(max_x, (int)func2(a, b, c, max_x));
//                        else //x11 thuoc bien tren
//                            secondPoint = new Point((int)x11, 0);
//                        
//                        if(x22 < max_x){ //x22 thuoc bien duoi
//                            thirdPoint = new Point((int)x12, 0);
//                            forthPoint = new Point((int)x22, max_y);
//                        }else //x22 khong thuoc bien duoi
//                            if(x12 < max_x){ //x12 thuoc bien tren
//                                thirdPoint = new Point((int)x12, 0);
//                                forthPoint = new Point(max_x, (int)func2(a, b, c, max_x));
//                            }else{ //neu x12 khong thuoc bien tren thi khong lam gi
//                                thirdPoint = new Point(0, 0);
//                                forthPoint = new Point(0, 0);
//                            }
//                                
//                            
//                    }else{ //x21 khong thuoc bien duoi
//                        if(x11 > 0){ //x11 thuoc bien duoi
//                            firstPoint = new Point(0, (int)func2(a, b, c, 0));
//                            secondPoint = new Point((int)x11, 0);
//                        }else{ //x11 khong thuoc bien duoi 
//                            firstPoint = new Point((int)x22, max_y);
//                        
//                        if(x12 < 0) //x12 khong thuoc bien tren
//                            secondPoint = new Point(0, (int)func2(a, b, c, 0));
//                        else //x12 thuoc bien tren
//                            secondPoint = new Point((int)x12, 0);
//                        
//                        thirdPoint = new Point(0, 0);
//                        forthPoint = new Point(0, 0);
//                        }
//                        
//                        thirdPoint = new Point((int)x22, max_y);
//                        
//                        if(x12 < 0) //x12 khong thuoc bien tren
//                            forthPoint = new Point(0, (int)func2(a, b, c, 0));
//                        else //x12 thuoc bien tren
//                            forthPoint = new Point((int)x12, 0);
//                    }
//                }
//                    
//                
//            }else{ //day cua do thi nam duoi so voi bien tren
//                if(equationBot.isSolvable && !equationBot.isSingleSolution){ //day cua do thi thuoc vung nhin thay
//                    firstPoint = new Point((int)equationBot.solution[0], max_y);
//                    secondPoint = new Point((int)equationBot.solution[1], max_y);
//                    
//                    thirdPoint = new Point(0, 0);
//                    forthPoint = new Point(0, 0);
//                }else //day cua do thi nam ngoai vung nhin thay
//                    return;
//            } 
//        }
//        
//        if(firstPoint != null && secondPoint != null)
//            for(int i = firstPoint.x; i <= secondPoint.x; i++)
//                g2D.drawString(".", i, func2(a, b, c, i));
//        
//        if(thirdPoint != null && forthPoint != null)
//            for(int i = firstPoint.x; i <= secondPoint.x; i++)
//                g2D.drawString(".", i, func2(a, b, c, i));
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
