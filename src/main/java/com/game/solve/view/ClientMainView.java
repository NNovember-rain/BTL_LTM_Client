package com.game.solve.view;



import com.game.solve.socket.ManageSocket;
import com.game.solve.uitl.InitUtils;
import com.game.solve.model.Item;
import com.game.solve.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class ClientMainView extends JPanel {


    private final JPanel[] shelfPanels;
    private final JLabel[] floorSlots;
    private final JLabel[][] shelfSlots;
    private BufferedImage backgroundImage;
    private JLabel draggedLabel = null;
    private Point initialClick;
    private int originFloorSlotIndex;
    private int originShelfIndex;
    private final ArrayList<Item> items;
    private JButton showAssignmentButton;
    private final JFrame parentFrame;
    boolean checkSwapView;


    public static void main(String[] args) throws Exception {

        String HOST="localhost";
        int PORT=12345;
        Socket socketConnect=new Socket(HOST,PORT);
        ManageSocket socket=new ManageSocket(socketConnect);
        ArrayList<Item> items = InitUtils.getItemInGame();

        JFrame jframe = new JFrame("Xep Do");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setResizable(false); // Ngăn không cho thay đổi kích thước

        jframe.setSize(380, 850);
        jframe.setLocationRelativeTo(null);

        LoginView loginView=new LoginView(jframe,socket);
        jframe.setContentPane(loginView);

//        MenuGameView menuGameView = new MenuGameView(jframe);
//        jframe.setContentPane(menuGameView);
        jframe.setVisible(true);


    }

    public ClientMainView(User user, ArrayList<Item> items, JFrame parentFrame,ManageSocket socket) {
        this.parentFrame = parentFrame;
        this.items = items;
        checkSwapView=true;

        setLayout(null);

        //TODO: Trộn ngẫu nhiêm các item ban đầu
        ArrayList<Item> shuffledItems = new ArrayList<>(items);
        Collections.shuffle(shuffledItems);

        //TODO: Tạo bố cục ban đầu nơi để các item
        floorSlots = new JLabel[8];
        JPanel itemFloorPanel = createFloorItemPanel(shuffledItems);
        add(itemFloorPanel);

        //TODO: gọi màn hình AssignmentView khi run thì sẽ hiện ra
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showAssignmentView(items,socket,user);
            }
        });

        //TODO: Khởi tạo bố cục các kệ gồm : 4 cái , mỗi cái có 2 vị trí đặt
        shelfPanels = new JPanel[4];
        shelfSlots = new JLabel[4][2];
        int[] xPositions = {25, 215, 25, 210};
        int[] yPositions = {270, 270, 405, 405};

        for (int i = 0; i < shelfPanels.length; i++) {
            shelfPanels[i] = createShelfPanel(xPositions[i], yPositions[i], i); // khởi tạo panel shelf thứ i tại vị trí x,y
            add(shelfPanels[i]); // thêm panel vào giao diện chính
        }

        // Thêm nút "Show Assignment" gọi màn hình AssignmentView
        createShowAssignmentButton(items,socket,user);

        //Background
        InputStream background = getClass().getResourceAsStream("/images/bg3.png");
        backgroundImage = InitUtils.initBackgroundImage(background);

        //Tạo bộ đếm 10 giây
        countdownToEndGameView(parentFrame,socket,user);
    }


    // Phương thức tạo nút "Show Assignment"
    private void createShowAssignmentButton(ArrayList<Item> shuffledItems,ManageSocket socket,User user) {
        showAssignmentButton = new JButton();  // Tạo nút không có tên
        showAssignmentButton.setBounds(1, -11, 55, 70);
        String imagePath = "/images/btA2.png";
        ImageIcon icon = InitUtils.createResizedIconFromResource(imagePath, 600, 680);
        showAssignmentButton.setIcon(icon);

        // Làm trong suốt nút
        showAssignmentButton.setOpaque(false);
        showAssignmentButton.setContentAreaFilled(false);
        showAssignmentButton.setBorderPainted(false);

        // Thêm sự kiện hành động
        showAssignmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAssignmentView(shuffledItems,socket,user);
            }
        });

        add(showAssignmentButton);
    }


    // Phương thức để hiển thị AssignmentView trong 3 giây
    private void showAssignmentView(ArrayList<Item> shuffledItems,ManageSocket socket, User user) {
        AssignmentView assignmentView = new AssignmentView(shuffledItems,socket,user);
        parentFrame.setContentPane(assignmentView);
        parentFrame.revalidate();
        parentFrame.repaint();

        // Đặt thời gian chờ 3 giây, sau đó quay lại ClientView
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(checkSwapView==false) showEndGameView(parentFrame,socket,user);
                else parentFrame.setContentPane(ClientMainView.this);
                parentFrame.revalidate();
                parentFrame.repaint();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void countdownToEndGameView(JFrame parentFrame,ManageSocket socket,User user) {
        final int countdownTime = 34; // 24 giây (thời gian bạn mong muốn)
        JLabel countdownLabel = new JLabel("00:24", JLabel.CENTER); // Hiển thị bắt đầu từ 00:24
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 30));
        countdownLabel.setBounds(-10, 10, 380, 50);
        add(countdownLabel);
        repaint();

        Timer countdownTimer = new Timer(1000, new ActionListener() {
            int timeLeft = countdownTime;

            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;

                // Tính số phút và giây
                int minutes = timeLeft / 60;
                int seconds = timeLeft % 60;

                // Định dạng chuỗi thành "mm:ss"
                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                countdownLabel.setText(timeFormatted);

                if (timeLeft <= 0) {
                    ((Timer) e.getSource()).stop(); // Dừng bộ đếm
                    checkSwapView = false;
                    showEndGameView(parentFrame,socket,user); // Hiển thị EndGameView
                }
            }
        });
        countdownTimer.start();
    }



    // Phương thức để hiển thị EndGameView
    private void showEndGameView(JFrame parentFrame,ManageSocket socket,User user) {
        EndGameView EndGameView = new EndGameView(parentFrame,socket,user);
        parentFrame.setContentPane(EndGameView);
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    //TODO: Tạo bố cục nơi để các item ban đầu
    private JPanel createFloorItemPanel(ArrayList<Item> shuffledItems) {
        JPanel itemFloorPanel = new JPanel();
        itemFloorPanel.setBounds(57, 620, 250, 145); // tọa độ , độ rộng , chiều cao của jPanel
        itemFloorPanel.setLayout(new GridLayout(2, 4, 18, 42)); // tạo Grid 2 hàng , 3 cột , giãn giữa các cột 15px , các hàng 42px
        itemFloorPanel.setOpaque(false);

        // Duyệt và tạo từng slot cho Grid
        for (int i = 0; i < floorSlots.length; i++) {
            floorSlots[i] = createSlotLabel();
            itemFloorPanel.add(floorSlots[i]); // thêm 1 label vào grid
            if (i < shuffledItems.size()) {  //check đảm bảo các item đều có icon thì mơi thêm tiếp

                String imagePath = shuffledItems.get(i).getImagePath();
                ImageIcon icon = InitUtils.createResizedIconFromResource(imagePath,1100,1090);
                if (icon != null) {
                    floorSlots[i].setIcon(icon);
                    floorSlots[i].putClientProperty("item", shuffledItems.get(i));
                    addDragAndDropFunctionality(floorSlots[i], itemFloorPanel, i, -1);
                } else {
                    System.out.println("Error load image for item at position: " + i);
                }
            }
        }
        return itemFloorPanel;
    }

    //TODO: tạo các kệ và mỗi slot cho kệ và căn chỉnh vị trí
    private JPanel createShelfPanel(int x, int y, int shelfIndex) {
        JPanel shelfPanel = new JPanel();
        shelfPanel.setBounds(x, y, 130, 53); // tọa độ , độ rộng , chiều cao của jPanel
        shelfPanel.setLayout(new GridLayout(1, 2, 20, 5)); // tạo Grid 1 hàng , 2 cột , giãn giữa các cột 20px , các hàng 42px
        shelfPanel.setOpaque(false);

        for (int i = 0; i < shelfSlots[shelfIndex].length; i++) {
            shelfSlots[shelfIndex][i] = createSlotLabel();

            //TODO : Thêm sự kiện kéo thả
            addDragAndDropFunctionality(shelfSlots[shelfIndex][i], shelfPanel, i, shelfIndex);
            shelfPanel.add(shelfSlots[shelfIndex][i]);
        }

        return shelfPanel;
    }

    //TODO: gắn sự kiện kéo thả cho các Label, chuyển tọa độ của Label ở panel hiện tại sang tọa độ panel chính(ClientView) của màn hình game
    private void addDragAndDropFunctionality(JLabel label, JPanel sourcePanel, int slotIndex, int shelfIndex) {
        label.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) { // Sk nhấn chuột
                if (label.getIcon() != null) {

                    //TODO: khi kéo item(lebel) ta sẽ tạo một lebel khác để gán item hiện tại vào đó.
                    draggedLabel = createSlotLabel();
                    draggedLabel.setIcon(label.getIcon());
                    draggedLabel.putClientProperty("item", label.getClientProperty("item")); // Gắn item vào draggedLabel

                    initialClick = e.getPoint(); // lưu vị trí ban đầu của con trỏ

                    // Xóa icon và item khỏi label ban đầu
                    label.setIcon(null);
                    label.putClientProperty("item", null);

                    //chuyển tọa độ của item
                    Point startPoint = SwingUtilities.convertPoint(sourcePanel, label.getLocation(), ClientMainView.this);
                    add(draggedLabel); //thêm draggedLabel vào giao diện ClientView
                    draggedLabel.setLocation(startPoint);
                    draggedLabel.setSize(draggedLabel.getPreferredSize());
                    repaint(); //cập nhật giao diện sau một loạt thao tác xóa và tạo item hiển thị thay thay thế

                    // vị trí ban đầu
                    originFloorSlotIndex = slotIndex; //vị trí ban đầu của item trong danh sách
                    originShelfIndex = shelfIndex;
                }
            }
            //thêm sự kiện nhả chuột sau khi kéo một item. Lúc này, đoạn mã kiểm tra xem draggedLabel (item đang được kéo) sẽ được thả đúng vị trí hợp lệ không
            // và sau đó thực hiện cập nhật lại giao diện dựa trên vị trí thả.
            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggedLabel != null) {
                    boolean placed = false;
                    Item draggedItem = (Item) draggedLabel.getClientProperty("item");

                    for (int i = 0; i < shelfPanels.length; i++) {
                        for (int j = 0; j < shelfSlots[i].length; j++) {
                            if (isWithinSlot(e, shelfSlots[i][j])) { //isWithinSlot(e, shelfSlots[i][j]): check vị trí hiện tại của chuột (vị trí nhả) có nằm trong shelfSlots[i][j] không. Nếu đúng, điều này nghĩa là người dùng đã thả item vào slot đó.
                                placeItemInSlot(shelfSlots[i][j], draggedLabel, draggedItem); // Đổi chỗ giữa item ở sàn và kệ , ngược lại
                                placed = true;
                                break;
                            }
                        }
                        if (placed) break;
                    }

                    // nếu vị trí thả không nằm trong vị trí hợp lệ thì sẽ trở về vị trí ban đầu trước khi thả ( phân biệt thông qua shelfIndex )
                    if (!placed) {
                        for (JLabel floorSlot : floorSlots) {
                            if (isWithinSlot(e, floorSlot)) {
                                placeItemInSlot(floorSlot, draggedLabel, draggedItem);
                                placed = true;
                                break;
                            }
                        }
                    }

                    if (!placed) {
                        if (originShelfIndex == -1) {
                            floorSlots[originFloorSlotIndex].setIcon(draggedLabel.getIcon());
                            floorSlots[originFloorSlotIndex].putClientProperty("item", draggedItem);
                        } else {
                            shelfSlots[originShelfIndex][originFloorSlotIndex].setIcon(draggedLabel.getIcon());
                            shelfSlots[originShelfIndex][originFloorSlotIndex].putClientProperty("item", draggedItem);
                        }
                    }

                    remove(draggedLabel);
                    repaint();
                    draggedLabel = null;

                    checkIfAllShelvesFilled();
                }
            }
        });

        mountMotion(label);
    }
    // TODO: check item xem đã full hay chưa , nếu rồi check đúng sai
    private void checkIfAllShelvesFilled() {
        boolean allFilled = true;
        boolean result = false;

        for (JLabel[] slot : shelfSlots) {
            for (JLabel jLabel : slot) {
                if (jLabel.getIcon() == null) {
                    allFilled = false;
                    break;
                }
            }
            if (!allFilled) break;
        }

        if (allFilled) {
            ArrayList<Item> shelf = new ArrayList<>();
            for (JLabel[] shelfSlot : shelfSlots) {
                for (JLabel jLabel : shelfSlot) {
                    shelf.add((Item) jLabel.getClientProperty("item"));
                }
            }

            if (shelf.equals(this.items)) {
                System.out.println("All shelves filled");
            }
            else System.out.println("sai");

        }
    }

    // TODO: gắn sự kiện kéo giữ chuột vào label
    private void mountMotion(JLabel label) {
        label.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedLabel != null) {
                    //chuyển đổi tọa độ
                    Point point = SwingUtilities.convertPoint(label, e.getPoint(), ClientMainView.this);
                    //Sau khi chuyển đổi tọa độ, tính toán vị trí mới của draggedLabel bằng cách trừ đi vị trí ban đầu của con trỏ khi nhấn chuột (initialClick).
                    int x = point.x - initialClick.x;
                    int y = point.y - initialClick.y;
                    //Đặt lại vị trí của draggedLabel theo tọa độ đã tính toán, đảm bảo rằng item luôn di chuyển theo con trỏ chuột.
                    draggedLabel.setLocation(x,y);
                    repaint();
                }
            }
        });
    }


    // TODO: check xem có nằm trong khu vực hợp lệ không
    private boolean isWithinSlot(MouseEvent e, JLabel slot) {
        Rectangle slotBounds = SwingUtilities.convertRectangle(slot.getParent(), slot.getBounds(), ClientMainView.this);
        Point mouseLocation = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), ClientMainView.this);
        return slotBounds.contains(mouseLocation);
    }

    // TODO: xử lí việc đổi chỗ giữa 2 item
    private void placeItemInSlot(JLabel slot, JLabel itemLebel, Item item) {
        Icon currentIcon = slot.getIcon();
        Item currentItem = (Item) slot.getClientProperty("item");

        slot.setIcon(itemLebel.getIcon());
        slot.putClientProperty("item", item);

        if (currentIcon != null && currentItem != null) {
            if (originShelfIndex == -1) {
                floorSlots[originFloorSlotIndex].setIcon(currentIcon);
                floorSlots[originFloorSlotIndex].putClientProperty("item", currentItem);
            } else {
                shelfSlots[originShelfIndex][originFloorSlotIndex].setIcon(currentIcon);
                shelfSlots[originShelfIndex][originFloorSlotIndex].putClientProperty("item", currentItem);
            }
        }
    }


    // TODO: tạo slot
    private JLabel createSlotLabel() {
        JLabel slotLabel = new JLabel();
        slotLabel.setHorizontalAlignment(JLabel.CENTER);
        slotLabel.setVerticalAlignment(JLabel.CENTER);
        slotLabel.setPreferredSize(new Dimension(50, 50));
        return slotLabel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    
}