/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mycalculator;

import java.awt.Graphics;
import java.awt.Paint;
import static mycalculator.CalculatorObjects.FunctionType.linear;
import mycalculator.CalculatorObjects.myFunction;

/**
 *
 * @author AshCraler
 */
public class FunctionPanel extends javax.swing.JPanel {

    /**
     * Creates new form FunctionPanel
     */
    private FunctionPanel() {
        initComponents();
    }
    
    myFunction mFunction;
    public int index;
    GraphicPanel graphicPanel;
    
    public myFunction getFunction(){
        return mFunction;
    }
    
    public GraphicPanel getGraphicPanel(){
        return graphicPanel;
    }
    
    public FunctionPanel(myFunction mF, int i, GraphicPanel gp){
        initComponents();
        mFunction = mF;
        index = i;
        graphicPanel = gp;
        
    }
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
        switch(mFunction.getType()){
            case linear:
                titleLabel.setText("Line");
                functionLabel.setText("d: y = " + mFunction.getInfo()[0] + "x + " + mFunction.getInfo()[1]);
                break;
                
            case circle:
                titleLabel.setText("Circle");
                functionLabel.setText("(x; y) = (" + mFunction.getInfo()[0] + "; " + mFunction.getInfo()[1] + ");          r = " + mFunction.getInfo()[2]);
                break;
                
            case parabol:
                titleLabel.setText("Parabol");
                functionLabel.setText("p: y = " + mFunction.getInfo()[0] + "x² + " + mFunction.getInfo()[1] + "x + " + mFunction.getInfo()[2]);
                break;
                
            default:
                break;
        }
    }
    
    public void changeTo(FunctionPanel destination){
        index = destination.index;
        mFunction = destination.getFunction();
        graphicPanel = destination.getGraphicPanel();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        functionLabel = new javax.swing.JLabel();

        setLayout(null);

        titleLabel.setFont(new java.awt.Font("Lucida Grande", 0, 20)); // NOI18N
        titleLabel.setText("Line");
        add(titleLabel);
        titleLabel.setBounds(31, 6, 80, 25);
        add(functionLabel);
        functionLabel.setBounds(16, 43, 260, 30);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel functionLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
