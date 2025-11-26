package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class RiftDialog extends JDialog {

    private int selection = -1;

    private final Color BG_COLOR = new Color(15, 15, 20);
    private final Color BORDER_COLOR = new Color(0, 255, 255); // Neon Cyan
    private final Font MSG_FONT = new Font("Consolas", Font.BOLD, 18);
    private final Font TITLE_FONT = new Font("Impact", Font.PLAIN, 24);

    public RiftDialog(Frame owner, String title, String message) {
        this(owner, title, message, new String[]{"YES", "NO"});
    }

    public RiftDialog(Frame owner, String title, String message, Object[] options) {
        super(owner, true);
        setUndecorated(true);
        setLayout(new BorderLayout());

        // Main Container
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG_COLOR);
        contentPanel.setBorder(new LineBorder(BORDER_COLOR, 3)); // Thicker border
        add(contentPanel);

        // Title
        JLabel titleLabel = new JLabel(title.toUpperCase(), SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setBorder(new EmptyBorder(15, 10, 5, 10));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        // Message Area
        JTextPane msgPane = new JTextPane();
        msgPane.setText(message);
        msgPane.setFont(MSG_FONT);
        msgPane.setForeground(Color.WHITE);
        msgPane.setBackground(BG_COLOR);
        msgPane.setEditable(false);
        msgPane.setFocusable(false);

        // Center Text
        StyledDocument doc = msgPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        JPanel textWrapper = new JPanel(new BorderLayout());
        textWrapper.setBackground(BG_COLOR);

        textWrapper.setBorder(new EmptyBorder(5, 30, 0, 30));

        textWrapper.add(msgPane, BorderLayout.CENTER);
        contentPanel.add(textWrapper, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 0, 10, 0));
        buttonPanel.setBackground(BG_COLOR);

        buttonPanel.setBorder(new EmptyBorder(5, 15, 20, 15));

        for (int i = 0; i < options.length; i++) {
            String label = options[i].toString();
            RiftButton btn = new RiftButton(label);

            btn.setPreferredSize(new Dimension(130, 40));

            int finalIndex = i;
            btn.addActionListener(e -> {
                if (label.equalsIgnoreCase("YES")) selection = JOptionPane.YES_OPTION;
                else if (label.equalsIgnoreCase("NO")) selection = JOptionPane.NO_OPTION;
                else selection = finalIndex;
                dispose();
            });
            buttonPanel.add(btn);
        }

        JPanel btnContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnContainer.setBackground(BG_COLOR);
        btnContainer.add(buttonPanel);

        contentPanel.add(btnContainer, BorderLayout.SOUTH);

        int calculatedWidth = Math.max(520, (options.length * 150) + 50);

        contentPanel.setPreferredSize(new Dimension(calculatedWidth, 260));

        pack();
        setLocationRelativeTo(owner);
    }

    public static int showConfirmDialog(Component parent, String message, String title) {
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(parent);
        RiftDialog dialog = new RiftDialog(owner, title, message);
        dialog.setVisible(true);
        return dialog.selection;
    }

    public static int showOptionDialog(Component parent, String message, String title, Object[] options) {
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(parent);
        RiftDialog dialog = new RiftDialog(owner, title, message, options);
        dialog.setVisible(true);
        return dialog.selection;
    }
}