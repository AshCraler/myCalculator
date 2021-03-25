/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mycalculator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JButton;
import sun.net.www.content.audio.x_aiff;

/**
 *
 * @author AshCraler
 */
public class CalculatorObjects {
    
    
    
    public static class myStroke{
        public static BasicStroke axisStroke = new BasicStroke(2);
        public static BasicStroke lineStroke = new BasicStroke(2);
        public static BasicStroke unitStroke = new BasicStroke(1);
        public static BasicStroke netStroke = new BasicStroke(1);
        public static BasicStroke OxyStroke = new BasicStroke(1);
    }
    
    public static class myColor{
        public static Color color1 = Color.RED;
        public static Color color2 = Color.ORANGE;
        public static Color color3 = Color.YELLOW;
        public static Color color4 = Color.GREEN;
        public static Color color5 = Color.BLUE;
        public static Color color6 = Color.CYAN;
        public static Color color7 = Color.MAGENTA;
    }

    public enum MouseState{
        free,
        dragging;
        
    }
    
    public enum AppState{
        equationing, calculating, graphing
    }
    
    public enum EquationState{
        linear, quadra, doubleLinear, tripleLinear        
    }
    
    public enum UnitState{
        monoUnit, diUnit, pentaUnit
    }
    
    public enum solutionState{
        vonghiem, nghiemdon, vosonghiem
    }
    
    public static String convertToMultiline(String orig){
        return "<html>" + orig.replaceAll("\n", "<br>");
    }
    
    public static abstract class myEquation{
        protected float[] coeffitient = new float[3];
        protected float[] solution = new float[2];
        
        
        protected boolean isSolvable = true;
        
        
        
    }
    
    public static class linearEquation extends myEquation{
        public boolean isCountlessSolution = false;
        
        private linearEquation(){
            
        }
        
        public linearEquation(float a, float b){
            coeffitient[0] = a;
            coeffitient[1] = b;
            
            if(a == 0){
                if(b == 0)
                    isCountlessSolution = true;
                else
                    isSolvable = false;
            }
            else{
                solution[0] = (float)(-b)/a;
            }
        }
        
        public float getSolution(){
            return solution[0];
        }
        
    }
    
    public static class quadraticEquation extends myEquation{
        private quadraticEquation(){
            
        }

        public boolean isRightFormat;
        public boolean isSingleSolution = false;
        private float delta;
       
        public quadraticEquation(float a, float b, float c){
            coeffitient[0] = a;
            coeffitient[1] = b;
            coeffitient[2] = c;
            
            if(a == 0){
                isRightFormat = false;
                return;
            }
            
            isRightFormat = true;
            delta = b*b - 4*a*c;
            
            if(delta < 0)
                isSolvable = false;
            else{
                solution[0] = (float)(b-Math.sqrt(delta))/(2*a);
                solution[1] = (float)(b+Math.sqrt(delta))/(2*a);
                
                if(solution[0] == solution[1])
                    isSingleSolution = true;
            }
        }
        
        public float[] getSolutions(){
            return solution;
        }
    }
    
    public static class Operation{
    
        static ScriptEngineManager manager = new ScriptEngineManager();
        static ScriptEngine engine = manager.getEngineByName("js");

        public static String equal(String str){
            
            str = str.replaceAll("sin","Math.sin").replaceAll("cos", "Math.cos").replaceAll("tan", "Math.tan").replaceAll("pow", "Math.pow");
            str = str.replaceAll("√", "Math.sqrt").replaceAll("e", "Math.E").replaceAll("π", "Math.PI");
            str = str.replaceAll("\\)", ").toFixed(4)");
            String result = "";
            try {
                result = String.valueOf(engine.eval(str));
            } catch (Exception e) {
                result = "Math error";
                e.printStackTrace();
            }

            if(result == "Infinity")
                result = "Math error";
            return result;
        }
        
          
    }
 
    public static class doubleLinearEquation{
        private float a1, b1, c1, a2, b2, c2;
        private solutionState state;
        
        public solutionState getState(){
            return state;
        }
        
        private float X,Y;
        private doubleLinearEquation(){
            
        }
        
        public doubleLinearEquation(float a_1, float b_1, float c_1, float a_2, float b_2, float c_2){
            a1 = a_1; b1 = b_1; c1 = c_1;
            a2 = a_2; b2 = b_2; c2 = c_2;
            
            if(a1*b2 - b1*a2 != 0){
                state = solutionState.nghiemdon;
                X = (float)(c1*b2 - b1*c2)/(a1*b2 - b1*a2);
                Y = (float)(a1*c2 - a2*c1)/(a1*b2 - b1*a2);
            }
            else{
                if((float)a1/a2 == (float)c1/c2){
                    state = solutionState.vosonghiem;
                    
                }
                else{
                    state = solutionState.vonghiem;
                    
                }
                    
            }
                
        }
        
        public float getX(){
            return X;
        }
        
        public float getY(){
            return Y;
        }
    }
    
    public static enum FunctionType{
        linear, parabol, circle
    }
    
    public static abstract class myFunction{
        protected FunctionType type;
        
        public abstract FunctionType getType();
        public abstract float[] getInfo();
    }
    
    public static class linearFunction extends myFunction{
        
        private linearFunction(){
            type = FunctionType.linear;
        }
        
        private float a, b;
        
        public linearFunction(float _a, float _b){
            type = FunctionType.linear;
            a = _a;
            b = _b;
        }
        
        public float getA(){
            return a;
        }
        
        public float getB(){
            return b;
        }

        @Override
        public FunctionType getType() {
            return type;
        }

        @Override
        public float [] getInfo() {
            float[] result = {a, b};
            return result;
        }
    }
    
    public static class circleFunction extends myFunction{
        private circleFunction(){
            type = FunctionType.circle;
        }
        
        private float x, y, radius;
        
        public circleFunction( float _x, float _y, float _r){
            type = FunctionType.circle;
            x = _x;
            y = _y;
            radius = _r;
        }
        
        @Override
        public FunctionType getType(){
            return type;
        }

        @Override
        public float[] getInfo() {
            float[] result = {x, y, radius};
            return result;
        }
    }
    
    public static class parabolFunction extends myFunction{

        private float a, b, c;
        
        private parabolFunction(){
            
        }
        
        public parabolFunction(float _a, float _b, float _c){
            type = FunctionType.parabol;
            
            a = _a;
            b = _b; 
            c = _c;
        }
        
        public float getA(){
            return a;
        }
        
        public float getB(){
            return b;
        }
        
        public float getC(){
            return c;
        }
        
        @Override
        public FunctionType getType() {
            return type;
        }

        @Override
        public float[] getInfo() {
            float result[] = {a, b, c};
            return result;
        }
        
    }
}
