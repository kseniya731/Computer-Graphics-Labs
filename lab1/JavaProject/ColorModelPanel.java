import java.util.List;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.GridLayout;

public class ColorModelPanel extends JPanel {
    private final String title;
    private final List<ColorChannelPanel> channels;
    private Runnable onChange;
    
    public ColorModelPanel(String title, String[] names, int[] mins, int[] maxs) {
        this.title = title;
        this.channels = new ArrayList<>();
        if (names.length != mins.length || names.length != maxs.length) {
            throw new IllegalArgumentException("Array lengths must match");
        }
        for (int i = 0; i < names.length; i++) {
            ColorChannelPanel ch = new ColorChannelPanel(names[i], mins[i], maxs[i]);
            ch.setOnChange(this::fireOnChange);
            channels.add(ch);
        }
        buildUI();
    }

    private void buildUI() {
        setLayout(new GridLayout(channels.size(), 1, 5, 5));
        for (ColorChannelPanel ch : channels) {
            add(ch);
        }
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(title),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void fireOnChange() {
        if (onChange != null) {
            onChange.run();
        }
    }

    public int[] getValues() {
        int[] values = new int[channels.size()];
        for (int i = 0; i < channels.size(); i++) {
            values[i] = channels.get(i).getValue();
        }
        return values;
    }

    public void setValues(int[] values) {
        if (values.length != channels.size()) {
            throw new IllegalArgumentException(
                "Expected " + channels.size() + " values, got " + values.length);
        }
        for (int i = 0; i < channels.size(); i++) {
            channels.get(i).setValue(values[i]);
        }
    }

    public void setOnChange(Runnable r) {
        this.onChange = r;
    }

}
