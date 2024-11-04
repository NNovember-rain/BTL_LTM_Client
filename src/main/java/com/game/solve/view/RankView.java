package com.game.solve.view;

import com.game.solve.model.User;
import com.game.solve.uitl.InitUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

public class RankView extends JPanel {
    private BufferedImage backgroundImage;
    private List<User> users;
    private JTable userTable;
    private Font customFont;

    public RankView() {
        setLayout(null);
        setOpaque(false); // Make the JPanel background transparent

        // Load custom font
        loadCustomFont();

        // Create "Rank" label with custom font
        JLabel rankLabel = createRankLabel();
        add(rankLabel);

        // Add a table title label above the table
        JLabel tableTitle = new JLabel("Top 10 Players");
        tableTitle.setForeground(new Color(147, 70, 44)); // Example: Red color
        tableTitle.setBounds(33, 195, 300, 30);
        tableTitle.setFont(customFont != null ? customFont.deriveFont(Font.BOLD,15f) : new Font("Arial", Font.BOLD, 24));
        tableTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(tableTitle);

        // Create "Menu" button
        JButton buttonGoHome = createMenuButton();
        add(buttonGoHome);

        // Set up user table with maximum 10 rows
        userTable = createTransparentUserTable();

        // Wrap the user table in a JScrollPane to display headers
        JScrollPane tableScrollPane = new JScrollPane(userTable);
        tableScrollPane.setBounds(42, 255, 300, 400); // Set position and size of the scroll pane
        tableScrollPane.setOpaque(false);
        tableScrollPane.getViewport().setOpaque(false);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(tableScrollPane);

        // Initialize background image
        backgroundImage = InitUtils.initBackgroundImage(getClass().getResourceAsStream("/images/bgrank.jpg"));
    }


    private JLabel createRankLabel() {
        JLabel rankLabel = new JLabel("Ranking");
        rankLabel.setBounds(78, 90, 300, 55);
        rankLabel.setOpaque(false);
        rankLabel.setFont(customFont != null ? customFont : new Font("Arial", Font.BOLD, 70));

        // Set your favorite color
        rankLabel.setForeground(new Color(207, 84, 32)); // Example: Red color

        return rankLabel;
    }



    private JTable createTransparentUserTable() {
        JTable table = new JTable();
        table.setRowHeight(30);
        table.setShowGrid(false); // Disable grid to keep the appearance clean
        table.setIntercellSpacing(new Dimension(0, 0)); // Remove spacing between cells
        table.setOpaque(false); // Make the table itself transparent

        // Center align text in columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setOpaque(false); // Make each cell renderer transparent
        table.setDefaultRenderer(Object.class, centerRenderer);

        // Set table font
        table.setFont(customFont != null ? customFont.deriveFont(14f) : new Font("Arial", Font.PLAIN, 14));

        // Customize table header
        JTableHeader header = table.getTableHeader();
        header.setOpaque(false); // Make the header component transparent
        header.setFont(customFont != null ? customFont.deriveFont(16f) : new Font("Arial", Font.BOLD, 16));
        header.setForeground(Color.BLACK); // Set header text color

        // Set custom transparent header renderer
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true); // Make header cell opaque to apply background color
                label.setBackground(new Color(113, 9, 124)); // Màu nền với alpha (0-255) cho độ trong suốt
                label.setForeground(Color.BLACK); // Màu chữ của tiêu đề
                return label;
            }
        });

        // Remove the viewport background in JScrollPane
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setOpaque(false);
        tableScrollPane.getViewport().setOpaque(false);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove border for clean transparency

        return table;
    }








    private void updateUserTable() {
        int displayCount = Math.min(users.size(), 10); // Limit to top 10 users
        String[] columnNames = {"Username", "Gender", "Points"};
        Object[][] data = new Object[displayCount][3];

        for (int i = 0; i < displayCount; i++) {
            User user = users.get(i);
            data[i][0] = user.getUsername();
            data[i][1] = user.getGender();
            data[i][2] = user.getPoints();
        }

        userTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void showMenu() {
        CardLayout layout = (CardLayout) this.getParent().getLayout();
        layout.show(this.getParent(), "MenuGameView");
    }

    public void setUsers(List<User> users) {
        this.users = users;
        updateUserTable();
    }

    private void loadCustomFont() {
        try (InputStream is = InitUtils.class.getResourceAsStream("/fonts/RubikGemstones-Regular.ttf")) {
            if (is != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(50f);
            }
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.BOLD, 50); // Fallback font
        }
    }
    private JButton createMenuButton() {
        JButton buttonGoHome = InitUtils.createButtonFunctionIcon(300, 10, 50, 50, 520, 510, "/images/menuIcon.png");
        buttonGoHome.setContentAreaFilled(false);
        buttonGoHome.setBorderPainted(false);
        buttonGoHome.addActionListener(e -> showMenu());
        return buttonGoHome;
    }

}
