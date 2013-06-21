/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package poliz;

import java.util.Scanner;
import java.util.ArrayList;

public class Poliz {

    public static void main(String[] args) {
        
        System.out.println("Enter expression with operators +, -, /, * or %");
        
        Scanner scan = new Scanner(System.in);
        String input;
        input = scan.nextLine();
        
        RPN rpn = new RPN(input);
        
        System.out.println("In RPN form: " + rpn.getRPN());
        System.out.println( "Answer: " + rpn.RPNCulc());
    }
}

class RPN {
    
    String _rpn_form = "";
    String _cur_str = "";
    
    RPN (String str) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> operators = new ArrayList<String>();
        
        _cur_str = str;
        
        for (;;) {
            
            String sub = nextLex();
            
            if (sub.isEmpty()) 
                continue;
            
            char cur_char = sub.charAt(0);
            if (isNumeric(sub)) {                                
                //int i = Integer.parseInt(sub);
                result.add(sub);
                
            } else if (sub.equals("(")) {
                operators.add(sub);
                
            } else if (sub.equals("+") || sub.equals("-") ) {
                int i = operators.size() - 1;
                while (!operators.isEmpty() && !operators.get(i).equals("(")) {
                    result.add(operators.get(i));
                    operators.remove(i);
                    i--;
                }
                operators.add(sub);
                
            } else if(sub.equals("*") || sub.equals("%")) {
                int i = operators.size() - 1;
                while (!operators.isEmpty() && !operators.get(i).equals("(") && (operators.get(i).equals("*") || operators.get(i).equals("%") || operators.get(i).equals("/"))) {
                    result.add(operators.get(i));
                    operators.remove(i);
                    i--;
                }
                operators.add(sub);
                
            } else if (sub.equals(")")) {
                for (int i = operators.size() - 1; i >= 0; i-- ) {
                    if (operators.get(i).equals("(")) {
                        operators.remove(i);
                        break;
                    } else {
                        result.add(operators.get(i));
                        operators.remove(i);
                    }
                    if (i == 0 ) {
                        System.out.println("Wrong brackets order");
                        System.exit(0);
                    }
                }
            }
            
            if (_cur_str.isEmpty()) {
                break;
            }
        }
        
        for (int i = operators.size() - 1; i >= 0; i--) {
            result.add(operators.get(i));
        }
        
        String res_str = "";
        
        for (String s : result) {
            if (s.equals("(")) {
                System.out.println("Wrong brackets order");
                System.exit(0);
            }
            res_str += s + " ";
        }
        
        _rpn_form = res_str;
    }
    
    private boolean isNumeric(String str) {
        boolean ans = true;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i)))
                ans = false;
        }
        return ans;
    }
    
    private void checkArg(ArrayList vec) {
        if (vec.size() < 2) {
            System.out.println("Not enough numbers");
            System.exit(0);
        }
    }
    
    private String nextLex() {
        
        int space = _cur_str.indexOf(' ');
        String sub;
            
        if (space == -1) {
            space = 0;
            sub = _cur_str;
            _cur_str = "";
        } else {
            sub = _cur_str.substring(0, space);
            _cur_str = _cur_str.substring(space + 1, _cur_str.length());                
        } 
        
        return sub;
    }
    
    public String getRPN() {
        
        if (_rpn_form.equals("")) 
            System.out.println("PRN form is empty");
        
        return _rpn_form;
    }    
    
    int RPNCulc() {
        
        if (_rpn_form.equals("")) 
            System.exit(0);
            
        ArrayList<Integer> vec = new ArrayList<Integer>();  
        _cur_str = _rpn_form;
        
        for (;;) {
            String sub = nextLex();
            
            if (sub.isEmpty()) 
                continue;
            
            if (isNumeric(sub)) {                                
                int i = Integer.parseInt(sub);
                vec.add(i);
            }
            else switch (sub.charAt(0)) {
                case '+' :
                    checkArg(vec);
                    vec.set(vec.size() - 2, vec.get(vec.size() - 2) + vec.get(vec.size() - 1));
                    vec.remove(vec.size() - 1);
                    break;
                case '-' :
                    checkArg(vec);
                    vec.set(vec.size() - 2, vec.get(vec.size() - 2) - vec.get(vec.size() - 1));
                    vec.remove(vec.size() - 1);
                    break;
                case '*' :
                    checkArg(vec);
                    vec.set(vec.size() - 2, vec.get(vec.size() - 2) * vec.get(vec.size() - 1));
                    vec.remove(vec.size() - 1);
                    break;
                case '/' :
                    checkArg(vec);
                    vec.set(vec.size() - 2, vec.get(vec.size() - 2) / vec.get(vec.size() - 1));
                    vec.remove(vec.size() - 1);
                    break;
                case '%' :
                    checkArg(vec);
                    vec.set(vec.size() - 2, vec.get(vec.size() - 2) % vec.get(vec.size() - 1));
                    vec.remove(vec.size() - 1);
                    break;
                default :
                    System.out.println("Wrong symbol in input" + sub.charAt(0));
                    System.exit(0);
            }
            
            if (_cur_str.isEmpty()) {
                if (vec.size() > 1)
                    System.out.println("Not enough operators");
                break;
            }
        }
        
        return vec.get(0);
    }
}