package br.ufmt.lalg.ide.view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MouseInputListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;

public class JCodeEditor extends JScrollPane implements MouseInputListener, DocumentListener, FocusListener{

	private JTextArea lines;
	private Highlighter highlighter;
	private JTextArea jta;
	
	public JCodeEditor(){
		super();
		
		jta = new JTextArea();
		jta.getDocument().addDocumentListener(this);
		jta.setTabSize(1);		
		highlighter = jta.getHighlighter();
		 
		/* Set up line numbers */
		lines = new JTextArea("2");
		lines.setBackground(Color.LIGHT_GRAY);
		lines.setEditable(false);
		lines.addMouseListener(this);
		lines.addFocusListener(this);
 
		/* Set up scroll pane */
		this.setBackground(Color.LIGHT_GRAY);
		this.getViewport().add(jta);
		this.setRowHeaderView(lines);
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
				
	}
	
	/* Document Listener Events */
	public void changedUpdate(DocumentEvent de) {
		lines.setText(getText());
	}
	public void insertUpdate(DocumentEvent de) {
		lines.setText(getText());
	}
	public void removeUpdate(DocumentEvent de) {
		lines.setText(getText());
	}
	
	public String getConteudo(){
	
		return jta.getText();
	}
	
	private String getText(){
		int caretPosition = jta.getDocument().getLength();
		Element root = jta.getDocument().getDefaultRootElement();
		String text = "1\n";
		for(int i = 2; i < root.getElementIndex( caretPosition ) + 2; i++)
			text += i + "\n";
		return text;
	}
 
	public JTextArea getTextArea() {
		return jta;
	}


	/* Mouse Listener Events */
	public void mouseClicked(MouseEvent me) {
		if(me.getClickCount() == 2){
			try {
				int caretPos = lines.getCaretPosition();
				int lineOffset = lines.getLineOfOffset(caretPos);
				if(lines.getText().charAt(caretPos-1) == '\n')
					lineOffset--;
				
				highlighter.addHighlight(jta.getLineStartOffset(lineOffset),
										 jta.getLineEndOffset(lineOffset), 
										 new MyHighlighter(Color.cyan));
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}
	public void mouseEntered(MouseEvent me) {}
	public void mouseExited(MouseEvent me) {}
	public void mousePressed(MouseEvent me) {}
	public void mouseReleased(MouseEvent me) {}
	public void mouseDragged(MouseEvent me) {}
	public void mouseMoved(MouseEvent me) {}
 
	/* Focus Listener Events for line numbers*/
	public void focusGained(FocusEvent fe) {}
	public void focusLost(FocusEvent fe) {
		highlighter.removeAllHighlights();
	}
	
	public class MyHighlighter extends DefaultHighlighter.DefaultHighlightPainter {
		public MyHighlighter(Color c) {
			super(c);
		}
	}

}

