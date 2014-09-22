package LoggFiles;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.mail.internet.NewsAddress;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import LoggFiles.StaticSubject;
import LoggFiles.DynamicSubject;


public class AddSubject extends JFrame implements ActionListener{

	private JPanel contentPane, fileEndings = new JPanel();
	private JPanel[] fileEndingPanels;
	private JComboBox actionList, typeList;
	private GridLayout mainGrid = new GridLayout(1,2,5,0);
	private GridLayout subGrid = new GridLayout(0,1,0,15);
	private JTextField subName, ipName, fileEnding;
	private JLabel subNameLabel, ipNameLabel, numberOfFilesLabel, filePathLabel;
	private JButton saveButton, storeButton, removeButton, browse, loadSubs;
	private JPanel[][] panelHolder = new JPanel[8][2];
	private JPanel[][] panelDivider = new JPanel[2][3];
	private JList<String> subjectList, ipList, fileEndingList;
	DefaultListModel<String> fileEndingListModel;
	private boolean editableFields=false, staticSubject = false;
	private JFileChooser fileChooser = new JFileChooser();
	private String filePath;
	private JOptionPane frame;
	ListSelectionListener listSelectionListener = new ListSelectionListener() {
	      public void valueChanged(ListSelectionEvent listSelectionEvent) {
	        boolean adjust = listSelectionEvent.getValueIsAdjusting();
	        if (!adjust) {
	          JList list = (JList) listSelectionEvent.getSource();
	          int selections[] = list.getSelectedIndices();
	          Object selectionValues[] = list.getSelectedValues();
	          for (int i = 0, n = selections.length; i < n; i++) {
	        	  if(!editableFields && list.equals(subjectList)){
	        		  String selectedSubject = (String)selectionValues[i];
	        		  subName.setText(selectedSubject);
	        		  try {
	        			  StaticSubject tempSub = new StaticSubject();
	        			  ipList.setListData(getIP(selectedSubject));
	        			  fileEndingListModel = new DefaultListModel<String>();
	        			  getFileEndings(selectedSubject);
	        			  fileEndingList.setModel(fileEndingListModel);
						if(tempSub.isStatic(selectedSubject, filePath)){
							ipName.enable();
							typeList.setSelectedItem("Static IP");
						}else{
							ipName.disable();
							typeList.setSelectedItem("Dynamic IP");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
	        	  }
	        	  else if(!editableFields && list.equals(ipList))
	        		  ipName.setText((String)selectionValues[i]);
	        	  else if(!editableFields && list.equals(fileEndingList))
	        		  fileEnding.setText((String)selectionValues[i]);
	          }
	        }
	      }
	    };

	/**
	 * Launch the application.
	 * 
	 * Titta pa att skapa tva JPanel varav den ena innehaller flera rader och alla falt som ska fyllas i
	 * Medan den andra bara har 3 rader, Subject, IP, FileEndings. 
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddSubject frame = new AddSubject();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AddSubject() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 300, 700, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(mainGrid);
		
		panelDivider[1][1] = new JPanel();
		panelDivider[1][1].setLayout(subGrid);
		add(panelDivider[1][1]);
		panelDivider[1][2]=new JPanel();
		panelDivider[1][2].setLayout(subGrid);
		add(panelDivider[1][2]);
		
		setTitle("Manage Subjects");
		for(int i=0;i<8;i++){
			for(int k=0;k<1;k++){
				panelHolder[i][k] = new JPanel();
			    panelDivider[1][1].add(panelHolder[i][k]);
			}
			
		}
		for(int i=0;i<3;i++){
			for(int k=1;k<2;k++){
				panelHolder[i][k] = new JPanel();
			    panelDivider[1][2].add(panelHolder[i][k]);
			}
			
		}
		createDropdownToDo();
		createDropdownSubType();
		//panelHolder[0][0].setBorder(BorderFactory.createRaisedBevelBorder());
		panelHolder[0][0].setLayout(new GridLayout(0, 1,0,2));
		browse = new JButton("Browse");
		browse.addActionListener(this);
		panelHolder[0][0].add(browse);
		filePathLabel = new JLabel();
		panelHolder[0][0].add(filePathLabel);
		loadSubs = new JButton("Load Path");
		loadSubs.addActionListener(this);
		panelHolder[0][0].add(loadSubs);
		panelHolder[1][0].setLayout(new GridLayout(0, 1,0,2));
		panelHolder[1][0].add(new JLabel("What do you want to do:"));
		panelHolder[1][0].add(actionList);
		panelHolder[2][0].setLayout(new GridLayout(0, 1,0,2));
		panelHolder[2][0].add(new JLabel("What kind of subject:"));
		panelHolder[2][0].add(typeList);
		
		createLabels();
		panelHolder[3][0].setBorder(BorderFactory.createRaisedBevelBorder());
		panelHolder[3][0].setLayout(new GridLayout(2, 1,0,2));
		panelHolder[3][0].add(subNameLabel);
		subName = new JTextField(20);
		subName.disable();
		panelHolder[3][0].add(subName);
		
		panelHolder[4][0].setBorder(BorderFactory.createRaisedBevelBorder());
		panelHolder[4][0].setLayout(new GridLayout(2, 1,0,2));
		panelHolder[4][0].add(ipNameLabel);
		ipName = new JTextField(20);
		panelHolder[4][0].add(ipName);
		
		panelHolder[5][0].setBorder(BorderFactory.createRaisedBevelBorder());
		panelHolder[5][0].setLayout(new GridLayout(2, 1,0,2));
		panelHolder[5][0].add(numberOfFilesLabel);
		fileEnding = new JTextField(20);
		panelHolder[5][0].add(fileEnding);
		
		panelHolder[6][0].setLayout(new GridLayout(1,2));
		storeButton = new JButton("Store Ending");
		storeButton.addActionListener(this);
		removeButton = new JButton("Remove Ending");
		removeButton.addActionListener(this);
		panelHolder[6][0].add(storeButton);
		panelHolder[6][0].add(removeButton);
		
		saveButton = new JButton("Save Subject");
		saveButton.addActionListener(this);
		panelHolder[7][0].add(saveButton);
		
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		subjectList = new JList<String>(listModel); //data has type Object[]
		fileEndingList = new JList<String>(listModel); //data has type Object[]
		ipList = new JList<String>(listModel); //data has type Object[]
		panelHolder[0][1].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		panelHolder[0][1].setLayout(subGrid);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(subjectList);
		panelHolder[0][1].add(scrollPane);
		panelHolder[1][1].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		panelHolder[1][1].setLayout(subGrid);
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(ipList);
		panelHolder[1][1].add(scrollPane);
		panelHolder[2][1].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		panelHolder[2][1].setLayout(subGrid);
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(fileEndingList);
		panelHolder[2][1].add(scrollPane);
		//contentPane.setPreferredSize(contentPane.getPreferredSize());
		ipName.disable();
		subName.disable();
		fileEnding.disable();
		typeList.disable();
	}
	
	private void createDropdownToDo(){
		String[] actions = { "New Subject", "Update Subject", "Remove Subject", "--Select--" };

		actionList = new JComboBox(actions);
		actionList.setSelectedIndex(3);
		actionList.addActionListener(this);
	}
	
	private void createLabels(){
		subNameLabel = new JLabel("Subject Name:");
		ipNameLabel = new JLabel("IP Number:"); 
		numberOfFilesLabel = new JLabel("Add/Remove File Endings:");
	}
	
	private void createDropdownSubType(){
		String[] types = { "Dynamic IP", "Static IP", "--Select--" };

		typeList = new JComboBox(types);
		typeList.setSelectedIndex(2);
		typeList.addActionListener(this);
	}
	
	private void createSubjectsList() throws IOException{
		StaticSubject tempStat = new StaticSubject();
		String [] StatSubs = tempStat.getStaticSubs(filePath);
		DynamicSubject tempDyn = new DynamicSubject();
		String [] DynSubs = tempDyn.getDynamicSubs(filePath);
		int arrayLength = StatSubs.length+DynSubs.length;
		String[] both = new String[arrayLength];
		for(int i=0;i<StatSubs.length;i++){
			both[i]=StatSubs[i];
		}
		for(int i=StatSubs.length;i<arrayLength;i++){
			both[i]=DynSubs[i-StatSubs.length];
		}
		
		subjectList.setListData(both);
		subjectList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		subjectList.setLayoutOrientation(JList.VERTICAL);
		subjectList.setVisibleRowCount(-1);
		subjectList.addListSelectionListener(listSelectionListener);
		
	}
	
	private void createIPList(){
		
		ipList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		ipList.setLayoutOrientation(JList.VERTICAL);
		ipList.setVisibleRowCount(-1);
		ipList.addListSelectionListener(listSelectionListener);
	}
	private void createFileEndingList(){

		fileEndingList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		fileEndingList.setLayoutOrientation(JList.VERTICAL);
		fileEndingList.setVisibleRowCount(-1);
		fileEndingList.addListSelectionListener(listSelectionListener);
}
		
	private void handleNewSubject(){
		subName.enable(); 
		subName.setText("");
		typeList.enable();
		ipName.setText("");
		ipName.enable();
		fileEnding.setText("");
		fileEnding.enable();
		String[] s = {};
		fileEndingList.setListData(s);
		editableFields=true;
		fileEndingListModel = new DefaultListModel<String>();
	}
	
	private void handleUpdateSubject(){
		subName.enable(); 
		typeList.enable();
		editableFields=false;
	}

	private void handleRemoveSubject(){
		subName.disable(); 
		editableFields=false;
	}
	
	private void handleStaticIP(){
		staticSubject=true;
		ipName.enable(); 
		subName.enable();
		fileEnding.enable();
	}
	private void handleDynamicIP(){
		staticSubject=false; 
		subName.enable();
		ipName.disable(); 
		fileEnding.enable();
	}
	
	private String[] getIP(String subName) throws IOException{
		subName = subName.replaceAll("\\s","_");
		BufferedReader br = new BufferedReader(new FileReader(filePath+"/"+subName+"/IP.txt"));
		Scanner sc = new Scanner(br);
		String[] subIP = new String[fileCount(subName+"/IP.txt")];
		int i = 0;
		while(sc.hasNext()){
			subIP[i] = sc.nextLine();
			i++;
		}
		return subIP;
		
	}
	
	private void getFileEndings(String subName) throws IOException{
		subName = subName.replaceAll("\\s","_");
		BufferedReader br = new BufferedReader(new FileReader(filePath+"/"+subName+"/file_endings.txt"));
		Scanner sc = new Scanner(br);
		while(sc.hasNext()){
			fileEndingListModel.addElement(sc.nextLine());
		}
		
	}
	
	@SuppressWarnings("resource")
	private int fileCount(String s) throws IOException{
		int i=0;
		String path = filePath+"/"+s;
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = br.readLine();
		while(line != null){
			i=i+1;
			line = br.readLine();
		}
		return i;
	}
	private boolean checkFields(boolean subType){
		boolean retBool = false;
		if(subType)
			if(!(ipName.getText()=="") && !(subName.getText()==""))
				retBool = true;
		else
			if(!(subName.getText()==""))
				retBool = true;
		return retBool;
	}
	private void createNewStatic(){
		StaticSubject statSub = new StaticSubject(subName.getText(), filePath);
		statSub.handleIPStatic(subName.getText(), ipName.getText(), filePath);
		statSub.addFileEnding(subName.getText(), fileEndingListModel.elements(), filePath);
	}
	private void updateStatic(){
		StaticSubject statSub = new StaticSubject(subName.getText(), filePath);
		statSub.handleIPStatic(subName.getText(), ipName.getText(), filePath);
		statSub.addFileEnding(subName.getText(), fileEndingListModel.elements(), filePath);
	}
	private void removeStatic(){
		//Coming soon
	}
	
	private void createNewDynamic(){
		DynamicSubject dynSub = new DynamicSubject(subName.getText(), filePath);
		dynSub.addFileEnding(subName.getText(), fileEndingListModel.elements(), filePath);
		
	}
	private void updateDynamic(){
		DynamicSubject dynSub = new DynamicSubject(subName.getText(), filePath);
		dynSub.addFileEnding(subName.getText(), fileEndingListModel.elements(), filePath);
	}
	private void removeDynamic(){
		//Coming soon
	}
	private void handleStoreEnding(){
		fileEndingList.setModel(fileEndingListModel);
		int index = fileEndingList.getSelectedIndex(); //get selected index
	    if (index == -1) { //no selection, so insert at beginning
	        index = 0;
	    } else {           //add after the selected item
	        index++;
	    }
	    fileEndingListModel.addElement(fileEnding.getText());
	    //Reset the text field.
	    fileEnding.requestFocusInWindow();
	    fileEnding.setText("");

	    //Select the new item and make it visible.
	    fileEndingList.setSelectedIndex(index);
	    fileEndingList.ensureIndexIsVisible(index);
	}
	
	private void handleRemoveEnding(){
		int index = fileEndingList.getSelectedIndex();
	    fileEndingListModel.remove(index);

	    int size = fileEndingListModel.getSize();

	    if (size == 0) { //Nobody's left, disable firing.
	        removeButton.setEnabled(false);

	    } else { //Select an index.
	        if (index == fileEndingListModel.getSize()) {
	            //removed item in last position
	            index--;
	        }

	        fileEndingList.setSelectedIndex(index);
	        fileEndingList.ensureIndexIsVisible(index);
	    }
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == saveButton){
			if(staticSubject && checkFields(staticSubject))
				switch(actionList.getSelectedIndex()){
				case 0: createNewStatic(); break;
				case 1: updateStatic(); break;
				case 2: removeStatic(); break;
				}
			else if(!staticSubject && checkFields(!staticSubject)){
				switch(actionList.getSelectedIndex()){
				case 0: createNewDynamic(); break;
				case 1: updateDynamic(); break;
				case 2: removeDynamic(); break;
				}
			}
			else{
				JOptionPane.showMessageDialog(frame, "Fill out all fields.");	
			}
			// actionList.setSelectedItem("Remove Subject"); // Fungerar!
		}else if(e.getSource().equals(actionList)){
			JComboBox cb = (JComboBox)e.getSource();
	        String action = (String)cb.getSelectedItem();
			switch(action){
			case "New Subject":  handleNewSubject(); break;
			case "Update Subject":  handleUpdateSubject(); break;
			case "Remove Subject": handleRemoveSubject(); ;break;
			case "--Select--": typeList.disable(); break;
			}
		}
		else if(e.getSource().equals(typeList)){
			JComboBox cb = (JComboBox)e.getSource();
		    String action = (String)cb.getSelectedItem();
			switch(action){
			case "Static IP": handleStaticIP(); break;
			case "Dynamic IP": handleDynamicIP(); break;
			case "--Select--": ipName.disable();subName.disable(); fileEnding.disable(); break;
			}
		}
		else if(e.getSource()== browse){
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fileChooser.showDialog(AddSubject.this, "Choose");
			File browsedFile = fileChooser.getSelectedFile();
			filePath = browsedFile.toString();
			filePathLabel.setText(filePath);
			
		}
		else if(e.getSource() == loadSubs){
			try {
				createSubjectsList();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			createIPList();
			createFileEndingList();
		}
		else if(e.getSource()==storeButton){
			handleStoreEnding();
		}
		else if(e.getSource()==removeButton){
			handleRemoveEnding();
		}
			
		}
		
	}

