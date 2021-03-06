
package ij3d.shortcuts;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class ShortCutTable extends JTable {

	public ShortCutTable(final ShortCuts shortcuts) {
		super(new ShortcutTableModel(shortcuts));
		setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
		setDefaultEditor(String.class, new ShortcutTableEditor());
	}

	private static final class ShortcutTableEditor extends DefaultCellEditor {

		ShortcutTableEditor() {
			super(getEditingTextField());
		}

		public static JTextField getEditingTextField() {
			final JTextField tf = new JTextField();
			tf.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(final KeyEvent e) {
					if (e.getKeyCode() != KeyEvent.VK_ENTER) {
						final String stroke = KeyStroke.getKeyStrokeForEvent(e).toString();
						((JTextField) e.getComponent()).setText(stroke);
						e.consume();
					}
				}

				@Override
				public void keyTyped(final KeyEvent e) {
					e.consume();
				}

				@Override
				public void keyReleased(final KeyEvent e) {
					e.consume();
				}
			});
			return tf;
		}
	}

	private static final class ShortcutTableModel implements TableModel {

		final ShortCuts shortcuts;

		public ShortcutTableModel(final ShortCuts shortcuts) {
			this.shortcuts = shortcuts;
		}

		@Override
		public void addTableModelListener(final TableModelListener l) {}

		@Override
		public void removeTableModelListener(final TableModelListener l) {}

		@Override
		public Class<?> getColumnClass(final int columnIndex) {
			return String.class;
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public String getColumnName(final int col) {
			return col == 0 ? "Command" : "Shortcut";
		}

		@Override
		public int getRowCount() {
			return shortcuts.getNumberOfCommands();
		}

		@Override
		public Object getValueAt(final int row, final int col) {
			final String command = shortcuts.getCommand(row);
			return col == 0 ? command : shortcuts.getShortCut(command);
		}

		@Override
		public boolean isCellEditable(final int rowIndex, final int columnIndex) {
			return columnIndex == 1;
		}

		@Override
		public void setValueAt(final Object aValue, final int row, final int col) {
			if (col != 1) return;
			final String command = shortcuts.getCommand(row);
			shortcuts.setShortCut(command, (String) aValue);
		}
	}
}
