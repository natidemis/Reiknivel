/**
 * Tilviksbreyturnar í Calculator eru notaðar til þess að
 * geyma upplýsingar um reiknisegða og segðir sem voru reiknaðir og birtingu þeirra í viðmót.
 * Forritið er byggt til þess að geta reiknað segða eins og "3 + 2 * 3". 
 * Forritið skoðar forgang reiknisegða með því að setja gildi á reikni segðana. "*" og "/"
 * hafa gildi 1. "+" og "-" hafa gildi 0 þannig útkoma reiknisegðarinnar að ofan yrði 9, ekki 15.
 * Keyra skal forritið með Calculator c = new Calculator();
 * @author Natanel Demissew Ketema, ndk1
 */
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.DimensionUIResource;
import net.miginfocom.swing.MigLayout;
import java.beans.*;
import java.io.IOException;
import java.awt.event.*;
import java.util.*;
import edu.princeton.cs.algs4.*;

public class Calculator extends JFrame{
    private JLabel ans = new JLabel();// ný reiknað gildi
    private JLabel output = new JLabel();// reiknisegð sem notandi slær inn
    private String[] values;// notað til að reikna upp úr
    private MaxPQ<Operator> pq = new MaxPQ<Operator>(); //leita "MaxPQ algs4 javadoc" fyrir skilin
    private String temp = "";//notað til að setja saman tölur og reiknisegða.
    public Calculator(){
        setLayout(new MigLayout("wrap 4", "[grow, fill]"));
        ans.setText("ans");
        output.setText(" ");
        output.setBackground(Color.BLACK);
        add(ans,"span,growx,push");
        add(output,"span,growx,push");
        initButtions();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Reiknivel");
        setPreferredSize(new DimensionUIResource(300,300));
        pack();
        setVisible(true);
    }
    /**
     * Fastayrðing gagna:
     * JLabel ans - breytan er eingöngu notað til að birta ný reiknað gildi
     * og einnig reikni segð t.d. "ans + 2"
     * JLabel output - breytan birtir það sem notandi slær inn á viðmót, einnig birtir það "ans" 
     * breytuna ef hún er ekki tóm.
     * String[] values - notað til að setja gildi út frá temp.split(" ") aðgerðini
     * auðveldar vinnu að leita að tölum og reiknisegðum í fylki.
     * MaxPQ<Operator> pq - ForgangsBiðröð af tagi Operator sem er innri klasi í Calculator.
     * MaxPQ er klasi sem er hannað frá höfundum bókarinar "algorithms 4"
     * og skilin má nálgast hér: https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/MaxPQ.html
     * 
     */

     /**
      * Operator - innri klasi í Calculator, notað til þess að raða reikniaðferðum
      * eftir gildi þeirra. "*" og "/" hafa gildi 1 og "+","-" hafa gildi 0.
      */
    private class Operator implements Comparable<Operator>{
        private String op;//Reikniaðgerð
        private Integer val;//gildi reiknisegðarinnar
        private Integer id;//vísi hennar í String[] values.
        /**
         * 
         * @param op Reikniaðgerð
         * @param id vísi hennar í fylkinu values
         */
        public Operator(String op, Integer id){
            this.op = op;
            this.val = operatorPrecedence(op);
            this.id = id;
        }
        /**
         * Notað til þess að taka inn reikniaðgerð og gefa henni gildi
         * svo hægt sé að bera hana saman við önnur reikniaðgerð
         * @param op Reikniaðgerðin sem við viljum gefa gildi
         * @return gildi reiknisegðarinnar.
         */
        private int operatorPrecedence(String op){
            if(op.equals("*") || op.equals("/")) return 1;
            return 0;
        }
        @Override
        /**
         * @param op Operator týpa sem við viðjum bera við
         */
        public int compareTo(Operator op){
            if(val.compareTo(op.val)== 1) return 1;
            if(val.compareTo(op.val) == -1) return -1;
            return 0;
        }
    }
    /**
     * Býr til takkana í viðmótinu,
     * setur hlustara og texti á takkana.
     */
    private void initButtions(){
        int c = 9;
        int s = 1;
        while (c > 0){
            for(int i = 0; i<3; i++)
        {
            JButton btn = new JButton(""+c--);
            btnListener(btn);
            add(btn,"grow, push");
        }
        String str = sign(s++,0);
        JButton btn =  new JButton(str);
        btnListener(btn);
        add(btn,"grow,push");
        }
        for(int i = 0; i < 4; i++){
            String str = sign(i+1,1);
            JButton btn = new JButton(str);
            btnListener(btn);
            add(btn,"grow,push");
        }
        JButton clrBtn = new JButton("clear");
        btnListener(clrBtn);
        add(clrBtn,"span,grow,push");        
    }
    /**
     * Notað til að sækja Streng fyrir nafn takkans
     * t.d. "0", "=";"." og reikniaðgerðana
     * @param n heiltala, notað til að itera í gegnum switch setninguna
     * @param m heiltala, 0 skal sækja reiknisegð(nema "+"), 1 annars
     * @return Strengin sem við viljum setja sem nafn takkans.
     */
    private String sign(int n,int m){
        String nm = n+""+m;
        String s ="";
        switch(nm){
            case "10":
                s = "*";
                break;
            case "11":
                s = ".";
                break;
            case "20": 
                s= "/";
                break;
            case "21":
                s ="0";
                break;
            case "30": 
                s = "-";
                break;
            case "31":
                s = "=";
                break;
            case "41":
                s = "+";
                break;
        }
        return s;
    }
    /**
     * notað til að setja hlustara á takkana í initButtons().
     * þegar smellt er á takka þá athugum við hverskonar takki smellt var á 
     * og ákvöðum hvaða aðgerð skal framkvæma.
     * @param btn JButton sem við viljum hlusta eftir
     */
    private void btnListener(JButton btn){
        btn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String input = btn.getText();
                if(isNumber(input)|| input.equals(".")){
                    if(input.equals(".")){
                        Character c = temp.charAt(temp.length()-1);
                        if(Character.isDigit(c)){
                            temp +=input;
                            output.setText(temp);
                        }
                        return;
                    }
                    temp += input;
                    output.setText(temp);
                    return;
                }else{
                    if(input.equals("=")){
                        compute();
                        temp = ans.getText().split(" ")[2];
                        return;
                    }else if (input.equals("clear")){
                        clear();
                        return;
                    }
                    Character c = temp.charAt(temp.length()-1);
                    if(Character.isDigit(c)){
                        temp += " " + input + " ";
                        output.setText(temp);
                    }
                }
            }
        });
    }
    /**
     * Keyrt þegar smellt er á "clear" hnappanum.
     * endurstillum öllum breytum sem við vinnum með þegar við reiknum segðum
     */
    private void clear(){
        temp = "";
        ans.setText("ans");
        output.setText(" ");
    }
    /**
     * isNumber(string) // notað í if-setningar
     * Athugar hvort gefinn strengur sé hægt að breyta yfir í tölu
     * @param str strengur sem við viljum athuga hvort hægt sé að breyta í tölu
     * @return true ef hægt er að breyta streng í tölu, false annars
     */
    public static boolean isNumber(String str) {
        boolean flag = false;
        try {
            double v = Double.parseDouble(str);
            flag = true;
        } catch (NumberFormatException nfe) {
        }
        return flag;
    }
    /**
     * framkvæmd þegar smellt er á "=" hnappanum.
     * framkvæmir compute ef síðasta gildið í s[] er tala 
     * strengurinn í temp er skiptur í fylki með split(" ") aðgerðini
     * Setjum öllum reikniaðgerðum í forgangsbiðröð með aðstoð innri klassans "Operator"
     * sækjum reikniaðgerð með hæstu forgang og leitum að einni tölu
     * sem kemur á undan honum í values listanum og annari tölu sem kemur eftir honum í values.
     * Reiknum upp úr því og setjum útkomuna í sæti reikniaðgerðarinnar og null stillum tölurnar 
     * sem voru notaðar.
     */
    private void compute(){
        String[] s = temp.split(" ");
        if(!isNumber(s[s.length-1])) return;
        if(s.length % 2 == 0){
            values = new String[s.length + 1];
            values[0] = ans.getText().split(" ")[2];
            for(int i = 1; i < values.length; i++){
                values[i] = s[i-1];
            }
        }else{
            values = s;
        }
        for(int i = 0; i < s.length;i++){
            if(!isNumber(values[i])) pq.insert(new Operator(values[i], i));
        }
        while(!pq.isEmpty()){
            Operator oper = pq.delMax();
            String[] val = getValues(oper.id-1, oper.id+1);
            double result = calculate(val[0], oper.op, val[1]);
            values[oper.id] = Double.toString(result);
        }
        for(int i = 0; i < values.length;i++){
            if(values[i] != null){
                ans.setText("ans = " + values[i]);
                output.setText(values[i]);
            }
        }
    }
    /**
     * String[] values = getValues(Operator.id-1, Operator.id+1);
     * sækjum fylki af stærð 2 sem inniheldur tölur sem koma á undan og eftir reikniaðgerðini.
     * @param i vísi til að leita aftur á bak fyrir tölu sem kemur á undan reikniaðgerðini
     * @param j vísi sem leitar áfram að næstu tölu sem kemur eftir reikniaðgerðini
     * @return fylki af tölum af tagi "String" sæti 0 er gildi sem kemur á undan reikniaðgerðini
     * og sæti 1 er gildi sem kemur á eftir reikniaðgerðini
     */
    private String[] getValues(int i, int j){
        String[] val = new String[2];
        while(val[0] == null){
            if(values[i] != null && isNumber(values[i])) val[0] = values[i];
            if(val[0] == null) i--;
        }
        while(val[1] == null){
            if(values[j] != null && isNumber(values[j])) val[1] = values[j];
            if(val[1] == null) j++;
        }
        values[i] = null;
        values[j] = null;
        return val;
    }
    /**
     * double value = calculate(string[0],Operator.op, string[1]);
     * reiknar upp úr gildunum sem getValues() skilar.
     * @param num1 fyrri talan sem getValues fann
     * @param op Reikniaðgerðin sem hafði hæsta forgang í forgangsbiðröðini
     * @param num2 seinni talan sem getValues fann
     * @return útkoma reiknisegðarinar, double.NEGATIVE_INFINITY(ef eitthvað klikkar)
     */
    private double calculate(String num1, String op, String num2){
        double n1 = Double.parseDouble(num1);
        double n2 = Double.parseDouble(num2);
        switch(op){
            case "+":
                return n1 + n2;
            case "*":
                return n1 * n2;
            case "/":
                return n1 / n2;
            case "-":
                return n1 - n2;
        }
        return Double.NEGATIVE_INFINITY;
    }
    public static void main(String[] args) {
        Calculator c = new Calculator();
    }
}