import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.SwingConstants;

public class ColorChannelPanel extends JPanel {
    private final String name;
    private final int min;
    private final int max;

    private JLabel nameLabel;
    private JSlider slider;
    private JTextField textField;

    private Runnable onChange;
    private boolean updating = false;

    public ColorChannelPanel(String name, int min, int max) {
        this.name = name;
        this.min = min;
        this.max = max;
        buildUI();
        wireListeners();
    }

    private void buildUI() {
        setLayout(new BorderLayout(5, 0));

        nameLabel = new JLabel(name);
        nameLabel.setPreferredSize(new Dimension(20, 20));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        slider = new JSlider(min, max, min);
        textField = new JTextField(5);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setText(String.valueOf(min));

        add(nameLabel, BorderLayout.WEST);
        add(slider, BorderLayout.CENTER);
        add(textField, BorderLayout.EAST);
    }

    private void wireListeners() {
        slider.addChangeListener(e -> handleSliderChange());
        textField.addActionListener(e -> handleTextFieldChange());
    }

    private void handleSliderChange() {
        if (updating) return;
        int v = slider.getValue();
        textField.setText(String.valueOf(v));
        fireOnChange();
    }

    private void handleTextFieldChange() {
        if (updating) return;
        try {
            int v = Integer.parseInt(textField.getText().trim());
            v = Math.max(min, Math.min(max, v));
            updating = true;
            try {
                slider.setValue(v);
                textField.setText(String.valueOf(v));
            } finally {
                updating = false;
            }
            fireOnChange();
        } catch (NumberFormatException ex) {
            updating = true;
            textField.setText(String.valueOf(slider.getValue()));
            updating = false;
        }        
    }

    private void fireOnChange() {
        if (onChange != null) {
            onChange.run();
        }
    }

    public void setValue(int value) {
        value = Math.max(min, Math.min(max, value));
        updating = true;
        try {
            slider.setValue(value);
            textField.setText(String.valueOf(value));
        } finally {
            updating = false;
        }
    }

    public int getValue() {
        return slider.getValue();
    }

    public void setOnChange(Runnable r) {
        this.onChange = r;
    }
}
