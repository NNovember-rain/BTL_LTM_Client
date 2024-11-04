package com.game.solve.view;

import com.game.solve.model.DataSending;
import com.game.solve.model.UserRequest;
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
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class ClientMainView extends JPanel {

    private JPanel mainPanel;
    private JPanel[] shelfPanels;
    private JLabel[] floorSlots;
    private JLabel[][] shelfSlots;
    private BufferedImage backgroundImage;
    private JLabel draggedLabel = null;
    private Point initialClick;
    private int originFloorSlotIndex;
    private int originShelfIndex;
    private ArrayList<Item> items;
    private User currentUser;
    private CardLayout cardLayout;
    private JLabel countdownLabel; // Nhãn hiển thị bộ đếm
    private Timer countdownTimer; // Lưu trữ tham chiếu tới Timer
    boolean checkSwapView=false;
    int count = 30;

    public static void main(String[] args) throws Exception {
        String HOST = "localhost";
        int PORT = 12345;
        Socket socketConnect = new Socket(HOST, PORT);
        ManageSocket socket = ManageSocket.getInstance(socketConnect);
        ArrayList<Item> items = InitUtils.getItemInGame();

        JFrame jframe = new JFrame("ITEM ORDER");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setResizable(false); // Ngăn không cho thay đổi kích thước

        jframe.setSize(380, 850);
        jframe.setLocationRelativeTo(null);

        User user = new User();
        ClientMainView clientMainView = new ClientMainView(items);
        jframe.setContentPane(clientMainView.getMainPanel()); // Đặt mainPanel chứa các view

        jframe.setVisible(true);
    }

    public ClientMainView(ArrayList<Item> items) {
        this.items = items;
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        //Khởi tạo ClientMainView và AssignmentView

        JPanel clientMainView = createClientView(items, new User());
        AssignmentView assignmentView = new AssignmentView();
        EndGameView endGameView = new EndGameView(this, assignmentView, items);
        TutorialView tutorialView = new TutorialView();
        RankView rankView = new RankView();
        ListUserOnlineView listUserOnlineView =new ListUserOnlineView();
        MenuGameView menuGameView = new MenuGameView(new User(), this.items, this, endGameView, assignmentView, tutorialView,rankView,listUserOnlineView);
        LoginView loginView = new LoginView(menuGameView);
        RegisterView registerView = new RegisterView(menuGameView);
        WinView winView = new WinView();
        LoseView loseView = new LoseView();

        mainPanel.add(loginView, "LoginView");
        mainPanel.add(menuGameView, "MenuGameView");
        mainPanel.add(clientMainView, "ClientView");
        mainPanel.add(assignmentView, "AssignmentView");
        mainPanel.add(endGameView, "EndGameView");
        mainPanel.add(registerView, "RegisterView");
        mainPanel.add(tutorialView, "TutorialView");
        mainPanel.add(rankView, "RankView");
        mainPanel.add(listUserOnlineView, "ListUserOnlineView");
        mainPanel.add(winView, "WinView");
        mainPanel.add(loseView, "LoseView");


        // Khởi tạo bộ đếm trong ClientMainView
        countdownLabel = new JLabel("10", JLabel.CENTER);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 30));
        countdownLabel.setBounds(140, 10, 100, 50);
        clientMainView.add(countdownLabel);
    }

    // Sửa lại phương thức createClientView để trả về ClientMainView thay vì JPanel mới
    private JPanel createClientView(ArrayList<Item> items, User currentUser) {
        this.setLayout(null);

        // Nạp hình nền vào ClientMainView
        InputStream background = getClass().getResourceAsStream("/images/bg3.png");
        backgroundImage = InitUtils.initBackgroundImage(background);

        // TODO: Trộn ngẫu nhiên các item ban đầu
        ArrayList<Item> shuffledItems = new ArrayList<>(this.items);
        Collections.shuffle(shuffledItems);

        // TODO: Tạo bố cục ban đầu nơi để các item
        floorSlots = new JLabel[8];
        JPanel itemFloorPanel = createFloorItemPanel(shuffledItems);
        this.add(itemFloorPanel); // Thêm vào chính ClientMainView

        // TODO: Khởi tạo bố cục các kệ gồm: 4 cái, mỗi cái có 2 vị trí đặt
        shelfPanels = new JPanel[4];
        shelfSlots = new JLabel[4][2];
        int[] xPositions = {25, 215, 25, 210};
        int[] yPositions = {270, 270, 405, 405};

        for (int i = 0; i < shelfPanels.length; i++) {
            shelfPanels[i] = createShelfPanel(xPositions[i], yPositions[i], i); // khởi tạo panel shelf thứ i tại vị trí x,y
            this.add(shelfPanels[i]); // Thêm vào chính ClientMainView
        }

        JButton buttonShowAssigntmentView=InitUtils.createButtonFunctionIcon(300,10,50,50,520,520,"/images/assignmenIcon.png");
        buttonShowAssigntmentView.addActionListener(e -> showAssignmentView()); // Đóng ứng dụng
        add(buttonShowAssigntmentView);

        return this; // Trả về chính ClientMainView
    }

    public void showAssignmentView() {
        Container parent = getParent();
        if (parent instanceof JPanel) {
            CardLayout cardLayout = (CardLayout) parent.getLayout();

            // Chuyển sang AssignmentView
            cardLayout.show(parent, "AssignmentView");

            // Tạo một Timer để đợi 4 giây trước khi quay lại ClientMainView
            Timer delayTimer = new Timer(4000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!checkSwapView) cardLayout.show(parent, "ClientView");
                }
            });
            delayTimer.setRepeats(false); // Chỉ chạy một lần
            delayTimer.start(); // Bắt đầu bộ đếm thời gian
        }
    }



    public JPanel getMainPanel() {
        return mainPanel;
    }

    // Phương thức để nhận đối tượng User sau khi đăng nhập
    public void setCurrentUser(User user) {
        this.currentUser = user;  // Gán đối tượng User đã đăng nhập
    }

    public User getCurrentUser() {
        return this.currentUser;  // Trả về đối tượng User đã đăng nhập
    }

    public void setItems(ArrayList<Item> items) {
        this.items=items;
    }




    public void startCountdown() {
        countdownLabel.setText("00:35"); // Bắt đầu với 35 giây
        countdownTimer = new Timer(1000, new ActionListener() { // Tạo một bộ đếm thời gian mỗi 1 giây
            int countdown = 35; // Bắt đầu từ 35 giây

            @Override
            public void actionPerformed(ActionEvent e) {
                countdown--; // Giảm số giây
                int minutes = countdown / 60;
                int seconds = countdown % 60;
                countdownLabel.setText(String.format("%02d:%02d", minutes, seconds)); // Cập nhật nhãn hiển thị với định dạng mm:ss

                if (countdown == 0) {
                    checkSwapView=true;
                    countdownTimer.stop(); // Dừng bộ đếm khi hết thời gian
                    Container parent = getParent();
                    if (parent instanceof JPanel) {
                        CardLayout cardLayout = (CardLayout) parent.getLayout();
                        cardLayout.show(parent, "LoseView"); // Chuyển sang LoseView

                        // Tạo một Timer để đợi 5 giây trước khi chuyển sang EndGameView
                        Timer delayTimer = new Timer(5000, ev -> {
                            // Chuyển sang EndGameView sau 4 giây
                            cardLayout.show(parent, "EndGameView");
                            checkSwapView=false;
                        });
                        delayTimer.setRepeats(false); // Chỉ chạy một lần
                        delayTimer.start(); // Bắt đầu bộ đếm thời gian
                    }
                }
            }
        });

        // Load custom font
        try (InputStream is = InitUtils.class.getResourceAsStream("/fonts/RubikGemstones-Regular.ttf")) {
            if (is != null) {
                Font rubikGemstones = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(24f); // Adjust size as needed
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(rubikGemstones);
                countdownLabel.setFont(rubikGemstones); // Set the font
            } else {
                System.err.println("Font not found!");
            }
        } catch (FontFormatException | IOException ex) {
            ex.printStackTrace();
        }

        countdownTimer.start(); // Bắt đầu bộ đếm
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
                try {
                    ManageSocket socket = ManageSocket.getInstance(null);
                    DataSending<UserRequest> dataSending = new DataSending<>();
                    Integer id =this.getCurrentUser().getId();
                    UserRequest userRequest=new UserRequest();
                    userRequest.setId(id);
                    String message = "points";
                    dataSending.setData(userRequest);
                    dataSending.setRequestType(message);
                    socket.getWriter().writeObject(dataSending);
                    socket.getWriter().flush();
                    socket.getWriter().reset();
                    DataSending<String> dataSendingListUser = (DataSending<String>) socket.getReader().readObject();
                }catch (Exception e){
                    System.out.println("Khong cap nhat diem thanh cong");
                }
                // Dừng countdown timer nếu nó đang chạy
                if (countdownTimer != null && countdownTimer.isRunning()) {
                    countdownTimer.stop(); // Dừng bộ đếm thời gian
                }

                // Chuyển sang WinView
                Container parent = getParent();
                if (parent instanceof JPanel) {
                    CardLayout cardLayout = (CardLayout) parent.getLayout();
                    cardLayout.show(parent, "WinView"); // Chuyển sang WinView

                    // Tạo một Timer để đợi 4 giây trước khi chuyển sang EndGameView
                    Timer delayTimer = new Timer(5000, e -> {
                        // Chuyển sang EndGameView sau 4 giây
                        cardLayout.show(parent, "EndGameView");
                    });
                    delayTimer.setRepeats(false); // Chỉ chạy một lần
                    delayTimer.start(); // Bắt đầu bộ đếm thời gian
                }
            } else {
                System.out.println("Sai rồi!");
            }
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

    public void resetItems() {
        // Tạo một SwingWorker để xử lý việc reset trong background
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                // 1. Reset các item trên kệ (xóa icon và giá trị item)
                for (int i = 0; i < shelfSlots.length; i++) {
                    for (int j = 0; j < shelfSlots[i].length; j++) {
                        shelfSlots[i][j].setIcon(null); // Xóa icon
                        shelfSlots[i][j].putClientProperty("item", null); // Xóa item
                    }
                }

                // 2. Trộn ngẫu nhiên các item
                ArrayList<Item> shuffledItems = new ArrayList<>(items);  // Sao chép danh sách item gốc
                Collections.shuffle(shuffledItems);  // Trộn ngẫu nhiên các item

                // 3. Đặt lại các item vào sàn (floorSlots)
                for (int i = 0; i < floorSlots.length; i++) {
                    if (i < shuffledItems.size()) {  // Đảm bảo rằng không có slot nào bị trống
                        Item item = shuffledItems.get(i);  // Lấy item từ danh sách trộn
                        String imagePath = item.getImagePath();
                        ImageIcon icon = InitUtils.createResizedIconFromResource(imagePath, 1100, 1090);

                        if (icon != null) {
                            floorSlots[i].setIcon(icon);  // Đặt lại icon cho slot
                            floorSlots[i].putClientProperty("item", item);  // Gán lại item cho slot
                            addDragAndDropFunctionality(floorSlots[i], ClientMainView.this, i, -1);  // Thêm sự kiện kéo thả
                        }
                    } else {
                        // Nếu không còn item nào để đặt vào slot, thì làm trống nó
                        floorSlots[i].setIcon(null);
                        floorSlots[i].putClientProperty("item", null);
                    }
                }

                return null;
            }

            @Override
            protected void done() {
                // Cập nhật giao diện sau khi reset (được thực hiện trên EDT)
                repaint();
            }
        };

        // Bắt đầu thực hiện SwingWorker
        worker.execute();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    
}