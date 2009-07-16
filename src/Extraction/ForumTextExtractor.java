package Extraction;

import java.io.*;
import java.util.ArrayList;

public class ForumTextExtractor {		
	public static ArrayList<String> extractForumText (File forum_text_file) {
		File file = forum_text_file;
		String content = getFileContent(file);
		return processFileContent(content);
	}
	
	public static ArrayList<String> extractForumText (String content) {
		return processFileContent(content);
	}
		
	private static String getFileContent (File file) {
		StringBuilder contents = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			
			while ((line = br.readLine()) != null)
				contents.append(line);
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contents.toString();
	}
	
	private static ArrayList<String> processFileContent (String content) {
		ArrayList<String> chunks = new ArrayList<String>();
		content = content.replaceAll("(<br>|<br />|<li>|<li />|</li>|<ul>|</ul>|<i>|</i>|<b>|</b>)", " ");
		String[] tmp = content.split("<");
		for (String s : tmp) {
			int rindex = s.indexOf(">");
			if (rindex == -1) continue;
			else
				s = s.substring(rindex + 1, s.length());
			while (s.indexOf("<") != -1 && s.indexOf(">") != -1) {
				int fi = s.indexOf("<");
				int li = s.indexOf(">");
				s = s.substring(0,fi) + s.substring(li+1, s.length());
			}
			s = s.trim();
			if (s.length() != 0)
				chunks.add(s);
		}
		return chunks;
	}
	
	public static void main (String[] args) {
		ArrayList<String> asdf = ForumTextExtractor.extractForumText(new File("showthread.php.htm"));
		for (String s : asdf) {
			System.out.println(s);
		}
	}
}
