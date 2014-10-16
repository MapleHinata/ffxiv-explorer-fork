package ca.fraggergames.ffxivextract;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import ca.fraggergames.ffxivextract.gui.FileManagerWindow;
import ca.fraggergames.ffxivextract.helpers.EXD_Searcher;
import ca.fraggergames.ffxivextract.helpers.PathSearcher;
import ca.fraggergames.ffxivextract.models.SqPack_DatFile;
import ca.fraggergames.ffxivextract.models.SqPack_IndexFile;
import ca.fraggergames.ffxivextract.storage.HashDatabase;
import ca.fraggergames.ffxivextract.storage.PathHashList;

public class Main {

	public static void main(String[] args) {		
		
		//Set to windows UI
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Init the hash database
		try {
			File dbFile = new File("./" + Constants.DBFILE_NAME);
			if (dbFile.exists())
				HashDatabase.init();
			else
				JOptionPane.showMessageDialog(null,
						Constants.DBFILE_NAME + " is missing. No file or folder names will be shown... instead the file's hashes will be displayed.",
					    "Hash DB Load Error",
					    JOptionPane.ERROR_MESSAGE);		
		} catch (ClassNotFoundException e1) {			
			e1.printStackTrace();
		}
		
		EXD_Searcher.createEXDFiles("C:\\Users\\Filip\\Desktop\\blah.txt");
		

		try {
			HashDatabase.loadPathsFromTXT("C:\\Users\\Filip\\Desktop\\blah.txtout.txt");
			//return;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*
		SqPack_IndexFile index;
		try {
			index = new SqPack_IndexFile("E:\\Program Files (x86)\\SquareEnix\\FINAL FANTASY XIV - A Realm Reborn\\game\\sqpack\\ffxiv\\0a0000.win32.index");
		
		SqPack_DatFile dat = new SqPack_DatFile("E:\\Program Files (x86)\\SquareEnix\\FINAL FANTASY XIV - A Realm Reborn\\game\\sqpack\\ffxiv\\0a0000.win32.dat0");
		EXD_Searcher.saveEXDNames(index, dat);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		//Arguments
		if (args.length>0)
		{
			//DEBUG ON
			if (args[0].equals("-debug"))
			{
				Constants.DEBUG = true;
				System.out.println("Debug Mode ON");
			}
			
			//PATHSEARCH
			if (args[0].equals("-pathsearch"))
			{
				if (args.length < 2)
				{
					System.out.println("No path to the FFXIV folder or to an index file was given.");
					return;
				}
				
				System.out.println("Starting Path Searcher (this will take a while)");				
				
				if (args[1].endsWith(".index"))
				{
					try {
						PathSearcher.doPathSearch(args[1]);
					} catch (IOException e) {
						System.out.println("There was an error searching. Stacktrace: ");
						e.printStackTrace();
					}
				}
				else
				{
					File file = new File(args[1].replace("\"", "") + "/game/sqpack/ffxiv/");
					File fileList[] = file.listFiles();
					
					for (File f : fileList)
					{												
						if ((!f.getName().contains("02") && !f.getName().contains("04") && !f.getName().contains("07")) && f.getName().endsWith(".index"))
						{
							try {
								PathSearcher.doPathSearch(f.getAbsolutePath());
								System.gc();
							} catch (IOException e) {
								System.out.println("There was an error searching. Stacktrace: ");
								e.printStackTrace();
							}
						}					
					}
				}
				
				return;
			}
		}		
		
		//Open up the main window
		FileManagerWindow fileMan = new FileManagerWindow(Constants.APPNAME);
		fileMan.setVisible(true);								
		
	}
	
}
