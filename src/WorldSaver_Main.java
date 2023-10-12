import javax.swing.SwingUtilities;

public class WorldSaver_Main {
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WorldSaverUI();
            }
        });
    }
}