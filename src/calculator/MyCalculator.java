package calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Stack;
public class MyCalculator {

	JFrame fj;
	//一个面板存放按钮，一个面板存放文字区
	JPanel buttonPanel,fieldPanel;
	String str="";
	//主窗口的内容面板
	Container contentPane;
	JTextField field;
	JButton buttons[];
	String[] numbers = {
			"C","sqrt","1/x","x^2","<-",
			 "7","8","9","+",
			 "4","5","6","-",
			 "1","2","3","*",
			 ".","0","=","/",
			 "(",")","sin","cos"
			};
	//用于计算的数字和符号栈
	Stack<BigDecimal>  numberStack=null;
	Stack<Character>   symbolStack=null;
	
	MyCalculator()
	{
		fj=new JFrame("计算器");
		fj.setLocation(300,300);
		buttonPanel=new JPanel();
		fieldPanel=new JPanel();
		field=new JTextField(13);
		field.setFont(new Font("宋体", Font.PLAIN, 50));//设置字体格式
		//设置文本框为不可编辑状态
		field.setEditable(false);
		//设置文本框为右对齐方式
		field.setHorizontalAlignment(JTextField.RIGHT);
		//网格布局6行4列
		buttonPanel.setLayout(new GridLayout(6,4,0,0));
		//采用流布局，使清零按钮在文字框后
		fieldPanel.setLayout(new FlowLayout(2));
		
		
		contentPane=fj.getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		
		
		contentPane.add(buttonPanel,BorderLayout.CENTER);
		contentPane.add(fieldPanel,BorderLayout.NORTH);
		
		
		buttons=new JButton[numbers.length];
		//事件处理
		for(int i=0;i<numbers.length;i++)
		{
			buttons[i]=new JButton(numbers[i]);
			buttons[i].addActionListener(new ButtonAction());
			//背景白色
			buttons[i].setBackground(Color.white);
			//符号字符为红色
			if(!(numbers[i].charAt(0)<='9'&&numbers[i].charAt(0)>='0'&&numbers[i].length()==1))buttons[i].setForeground(Color.red);  //设置按键颜色
			buttonPanel.add(buttons[i]);
		}
		buttons[0].setPreferredSize(new Dimension(46, 65));
		buttons[0].setBackground(Color.yellow);
		
		fieldPanel.add(field);
		fieldPanel.add(buttons[0]);

		//放最后显示
		fj.setVisible(true);
		fj.setSize(400,400);
	
	}

	
	class ButtonAction implements ActionListener
	{

		
		
		public void actionPerformed(ActionEvent event)
		{
			String s=event.getActionCommand();
			str=field.getText();
			if(!s.equals("cos")&&!s.equals("C")&&!s.equals("=")&&!s.equals("复数")&&!s.equals("<-")&&!s.equals("sin")&&!s.equals("1/x")&&!s.equals("x^2")&&!s.equals("sqrt"))
			{
				str+=s;
				field.setText(str);
			}
			else if(s.equals("="))
			{
				if(!check()) field.setText("格式错误！");
				else
				{
					//field.setText(str+'=');
					BigDecimal ans=calculate();
					
					field.setText(ans.toString());
					
					
				}
			}
			//清零操作
			else if(s.equals("C"))
			{
				field.setText("");
			}
			else if(s.equals("sqrt"))
			{
				if(!check()) field.setText("格式错误！");
				else
				{
					BigDecimal ans=calculate();
					
					field.setText(String.format("%.6f",Math.sqrt(ans.doubleValue())));
					
					
				}
			}
			else if(s.equals("cos"))
			{
				if(!check()) field.setText("格式错误！");
				else
				{
					BigDecimal ans=calculate();
					
					field.setText(String.format("%.6f", Math.cos(ans.doubleValue())));
					
					
				}
			}
			else if(s.equals("sin"))
			{
				if(!check()) field.setText("格式错误！");
				else
				{
					BigDecimal ans=calculate();
					
					field.setText(String.format("%.6f", Math.sin(ans.doubleValue())));
					
					
				}
			}
			else if(s.equals("1/x"))
			{
				if(!check()) field.setText("格式错误！");
				else
				{
					BigDecimal ans=calculate();
					if(ans.equals(0))field.setText("格式错误！");
					else
					{
						field.setText(String.format("%.6f", 1/ans.doubleValue()));
						
					}
					
				}
			}
			else if(s.equals("x^2"))
			{
				if(!check()) field.setText("格式错误！");
				else
				{
					BigDecimal ans=calculate();
					if(ans.equals(0))field.setText("格式错误！");
					else
					{
						field.setText(ans.multiply(ans).toEngineeringString());
					}
					
				}
			}
			//回退操作
			else if(s.equals("<-"))
			{
				if(field.getText().length()==0)field.setText(Integer.toString(0));
				else
				{
					str=str.substring(0,str.length()-1);
					field.setText(str);
				}
			}
		}
	}
	
	BigDecimal calculate()
	{
		str=field.getText();
		str+='=';
		String num="";
	       // 初始化栈
        if (numberStack == null) {
            numberStack = new Stack<BigDecimal>();
        }
        numberStack.clear();
        if (symbolStack == null) {
            symbolStack = new Stack<Character>();
        }
        symbolStack.clear();
        
		for(int i=0;i<str.length();i++)
		{
			char t=str.charAt(i);
			if(t<='9'&&t>='0'||t=='.')
			{
				num+=t;
			}
			else
			{
				if(!num.isEmpty())	numberStack.add(new BigDecimal(num));
				num="";
				while(!compare(t)&&!symbolStack.empty())
				{
					BigDecimal b=numberStack.pop();
					BigDecimal a=numberStack.pop();
					
					char sec=symbolStack.pop();
					
					if(sec=='+')
					{
						numberStack.push(a.add(b));
					}
					else if(sec=='-')
					{
						numberStack.push(a.subtract(b));
					}
					else if(sec=='*')
					{
						numberStack.push(a.multiply(b));
					}
					else if(sec=='/')
					{
						numberStack.push(a.divide(b));
					}
					else break;
				}
				
				if(t!='=')
				{
					symbolStack.push(t);
					
					if(t==')')
					{
						symbolStack.pop();
						symbolStack.pop();
					}
					
				}
			}
			
		}
		return numberStack.pop();
	}
	
	boolean check()
	{
		
		str=field.getText();
		if(str.length()==0)return false;
		char begin=str.charAt(0);
		char end=str.charAt(str.length()-1);
		//开始和末尾是除括号外的符号错误
		
		if(!(begin<='9'&&begin>='0'||begin=='('))return false;
		if(!(end<='9'&&end>='0'||end==')'))return false;
		
		int numStackz=0;//左括号数量
		if(str.charAt(0)=='(')numStackz++;
			
		for(int i=1;i<str.length();i++)
		{
			char ch=str.charAt(i);
			char c=str.charAt(i-1);
			//给连续出现的符号报错
			if(!(ch<='9'&&ch>='0'||ch=='('||ch==')')&&!(c<='9'&&c>='0'||c=='('||c==')'))
			{
				return false;
			}
			
			if(ch==')')
			{
				if(numStackz<=0)return false;
				numStackz--;
			}
		}
		
		if(numStackz>0)return false;//左括号多于右括号
		
		return true;
	}
	//优先级的设定，当返回为true时代表此时的符号sym优先级高于栈顶，可以往后继续放入栈中，因为最后出栈时是后入先出，不改变优先级
	//而发现sym优先级弱与栈顶时返回false需将前面的先行计算
	boolean compare(char sym)
	{
		if(symbolStack.empty())return true;
		char top=symbolStack.peek();
		if(top=='(')return true;
		
		switch(sym)
		{
		//(优先级最高
		case '(':
			return true;
		//* / 优先级高于+ -
		case '*':
			if(top=='+'||top=='-') return true;
			else return false;
			
		case '/':
			if(top=='+'||top=='-') return true;
			else return false;
			
		}
    return false;
		
	}
	
	public static void main(String... args)
	{
		var ca=new MyCalculator();
		
	}
	
}
