import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

class PaintovalPane extends JPanel {
	public void paintComponent(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;

		ArrayList input = new ArrayList();

		try {
			input = readFile("/Users/Jia/Desktop/dataset/r15_Improvedresult.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList result = stringtopoint(input);
		
		Color darkblue = new Color(7,54,112);
		Color purple = new Color(116,44,126);
		Color brown = new Color(121,77,49);
		Color darkgreen = new Color(34,85,35);
		Color maroon = new Color(158,35,60);
		
		Color c[] = {Color.black, Color.pink,Color.green,Color.yellow,Color.blue,Color.orange,Color.red,Color.cyan,Color.magenta,Color.gray,
				     darkblue,purple,brown,darkgreen,maroon}; //15 colors
        double ymax = 700; 
		
		for (int n = 1; n <= 15; n++) {        // n is the number of clusters
			for (int i = 0; i < result.size(); i++) {
				Point temp = (Point) result.get(i);
				if (temp.getId() == n) {
					g2.setColor(c[n-1]);
					Ellipse2D circle = new Ellipse2D.Double();
					circle.setFrameFromCenter(temp.getX(), ymax - temp.getY(),
							temp.getX() + 3, ymax - temp.getY() + 3);
					g2.fill(circle);

				}
				
			}
		}

	}

	private static ArrayList readFile(String fileName) throws IOException {

		ArrayList input = new ArrayList();

		File file = new File(fileName);

		BufferedReader bf = new BufferedReader(new FileReader(file));

		String content = "";
		StringBuilder sb = new StringBuilder();

		while (content != null) {
			content = bf.readLine();

			if (content == null) {
				break;
			}

			sb.append(content.trim());
		}

		bf.close();

		String s = sb.toString();
		String[] stringarray = s.split(",");

		for (int i = 0; i < stringarray.length; i = i + 3) {

			String x = stringarray[i];
			String y = stringarray[i + 1];
			String id = stringarray[i + 2];

			String one = id + ","+ x + "," + y;

			input.add(one);
		}

		return input;
	}

	public static ArrayList stringtopoint(ArrayList inputlist) {

		ArrayList doublepoint = new ArrayList();
		for (int i = 0; i < inputlist.size(); i++) {
			String inputstring = inputlist.get(i).toString();
			String[] inputpoint = inputstring.split(",");
			String id = inputpoint[0];
			String x = inputpoint[1];
			String y = inputpoint[2];

			Point point = new Point();
			double id0 = Double.parseDouble(id);
			point.setId(id0);

			double x0 = Double.parseDouble(x);
			point.setX(x0*30);
			double y0 = Double.parseDouble(y);
			point.setY(y0*30);

			doublepoint.add(point);

		}
		return doublepoint;
	}
}

class PaintovalFrame extends JFrame {
	public PaintovalFrame() {
        setBackground(Color.WHITE);
		setTitle("S1_hier");
		setSize(700, 700);
		addWindowListener(new WindowAdapter() {
			public void WindowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		Container contentPane = getContentPane();
		contentPane.add(new PaintovalPane());
	}
}

public class Display {
	public static void main(String[] args) {
		JFrame f = new PaintovalFrame();
		f.show();

	}

}