package com.game.solve.view;

import com.game.solve.model.DataSending;
import com.game.solve.model.Item;
import com.game.solve.model.UserRequest;
import com.game.solve.socket.ManageSocket;
import com.game.solve.uitl.ImageCache;
import com.game.solve.uitl.InitUtils;
import com.game.solve.model.User;

import javax.swing.*;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.*;
import java.util.Timer;

public class MenuGameView extends JPanel {
    private BufferedImage backgroundImage;
    private User user;  // Đối tượng User sẽ được truyền vào sau khi đăng nhập


    public MenuGameView(User user,  ArrayList<Item> items,ClientMainView clientMainView, EndGameView endGameView, AssignmentView assignmentView,TutorialView tutorialView,RankView rankView,ListUserOnlineView listUserOnlineView) {
        this.user = user;  // Khởi tạo với đối tượng User nếu có
        setLayout(null);

        InputStream background = getClass().getResourceAsStream("/images/bgall.jpg");
        backgroundImage = InitUtils.initBackgroundImage(background);

        JLabel messageLabel = InitUtils.creatLabel("Menu Game", 33, 115, 380, 63, 50);
        add(messageLabel);

        // Nút "Play"
        JLabel messagePlayLabel = InitUtils.creatLabel("Play", 157, 262, 100, 50, 27);
        add(messagePlayLabel);
        JButton playButton = InitUtils.createButtonFunctionMenu(58, 250, 250, 80, 440, 390, "/images/greenbutton.png", messagePlayLabel);
        playButton.addActionListener(e -> playGame(clientMainView, endGameView, assignmentView,items));
        add(playButton);

        // Nút "PlayFriend"
        JLabel messagePlayVsFr = InitUtils.creatLabel("Play vs Friend", 88, 360, 230, 50, 26);
        add(messagePlayVsFr);
        JButton playFrButton = InitUtils.createButtonFunctionMenu(58, 347, 250, 80, 440, 390, "/images/buttonblue.png", messagePlayVsFr);
        playFrButton.addActionListener(e -> viewPlayFriend(listUserOnlineView));
        add(playFrButton);

        // Nút "Rank"
        JLabel messageRankLabel = InitUtils.creatLabel("Rank", 147, 463, 100, 50, 27);
        add(messageRankLabel);
        JButton rankButton = InitUtils.createButtonFunctionMenu(58, 450, 250, 80, 440, 390, "/images/purplebutton.png", messageRankLabel);
        rankButton.addActionListener(e -> showRankView(rankView));
        add(rankButton);


        //Tutorial
        JButton buttonShowTutorial=InitUtils.createButtonFunctionIcon(300,10,50,50,520,520,"/images/tutorialIcon.png");
        buttonShowTutorial.addActionListener(e -> showTutorial(tutorialView,this.getUser()));
        add(buttonShowTutorial);

        //Quit
        JLabel messageQuitLabel = InitUtils.creatLabel("Quit Game", 112, 563, 170, 50, 27);
        add(messageQuitLabel);
        JButton quitButton = InitUtils.createButtonFunctionMenu(58, 550, 250, 80, 440, 390, "/images/redbutton.png", messageQuitLabel);
        quitButton.addActionListener(e -> System.exit(0)); // Đóng ứng dụng
        add(quitButton);

    }

    // Phương thức để nhận đối tượng User sau khi đăng nhập
    public void setUser(User user) {
        this.user = user;  // Gán đối tượng User đã đăng nhập
    }

    public User getUser() {
        return this.user;  // Trả về đối tượng User đã đăng nhập
    }

    public void createUserLabel(){
        JLabel nameUser = InitUtils.creatLabel("Welcome "+this.user.getUsername(), 15, -10, 380, 63, 15);
        System.out.println("Welcome "+this.user.getUsername());
        add(nameUser);
    }

    // Phương thức để xử lý khi nhấn nút "Play"
    private void playGame(ClientMainView clientMainView, EndGameView endGameView, AssignmentView assignmentView,ArrayList<Item> items) {
        Container parent = this.getParent();
        CardLayout cardLayout = (CardLayout) parent.getLayout();
        cardLayout.show(parent, "AssignmentView");
        Collections.shuffle(items);
        assignmentView.setItems(items);

        // Chuyển sang ClientMainView sau 4 giây
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                clientMainView.setCurrentUser(user);
                clientMainView.setItems(items);
                // Chuyển sang ClientMainView sau 4 giây
                cardLayout.show(parent, "ClientView");
                clientMainView.startCountdown();
            }
        }, 4000);  // 4000ms = 4 giây

    }

    // Phương thức hiển thị bảng xếp hạng
    private void viewPlayFriend(ListUserOnlineView listUserOnlineView) {
        Container parent = this.getParent();
        CardLayout layout = (CardLayout) parent.getLayout();
        List<User> users= listUserOnline();
        listUserOnlineView.setOnlineFriends(users);
        layout.show(parent, "ListUserOnlineView");
        listUserOnlineView.revalidate();
        listUserOnlineView.repaint();
    }

    private void showTutorial(TutorialView tutorialView,User user) {
        Container parent = this.getParent();
        tutorialView.setUser(user);
        tutorialView.createUserLabel();
        CardLayout layout = (CardLayout) parent.getLayout();
        layout.show(parent, "TutorialView");
        tutorialView.revalidate();
        tutorialView.repaint();

    }

    private void showRankView(RankView rankView) {
        Container parent = this.getParent();
        CardLayout layout = (CardLayout) parent.getLayout();
        List<User> users= ListhandleRank();
        rankView.setUsers(users);
        layout.show(parent, "RankView");
        rankView.revalidate();
        rankView.repaint();

    }

    private  List<User> ListhandleRank(){
        try {
            ManageSocket socket = ManageSocket.getInstance(null);
            DataSending<UserRequest> dataSending = new DataSending<>();
            UserRequest userRequest=new UserRequest();
            String message = "GetUsers";
            dataSending.setData(userRequest);
            dataSending.setRequestType(message);
            socket.getWriter().writeObject(dataSending);
            socket.getWriter().flush();
            socket.getWriter().reset();

            DataSending<List<User>> dataSendingListUser = (DataSending<List<User>>) socket.getReader().readObject();
            List<User> users = dataSendingListUser.getData();
            if(users.size()>0 && users!=null){
                return users;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private List<User> listUserOnline(){
        try {
            ManageSocket socket = ManageSocket.getInstance(null);
            DataSending<UserRequest> dataSending = new DataSending<>();
            UserRequest userRequest=new UserRequest();
            String message = "UserOnline";
            dataSending.setData(userRequest);
            dataSending.setRequestType(message);
            socket.getWriter().writeObject(dataSending);
            socket.getWriter().flush();
            socket.getWriter().reset();

            DataSending<List<User>> dataSendingListUser = (DataSending<List<User>>) socket.getReader().readObject();
            List<User> users = dataSendingListUser.getData();
            if(users.size()>0 && users!=null){
                return users;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
