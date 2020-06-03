package xyz.probablynotkai;

import javafx.stage.Screen;
import lombok.SneakyThrows;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramDirectShareRequest;
import org.brunocvcunha.instagram4j.requests.InstagramPostCommentRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UI
{
    public static Instagram4j instagram;
    public static String username;
    public static String password;

    public void runGUI(final File file){
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("K Tool - Login");

        final JTextField usernameT = new JTextField();
        usernameT.setSelectedTextColor(Color.RED);
        usernameT.setDisabledTextColor(Color.black);
        usernameT.setBounds(75, 75, 125, 27);

        final JPasswordField passwordT = new JPasswordField();
        passwordT.setSelectedTextColor(Color.RED);
        passwordT.setDisabledTextColor(Color.black);
        passwordT.setBounds(75, 110, 125, 27);
        passwordT.setEchoChar('*');

        final JButton login = new JButton("Login");
        login.setBounds(85, 150, 100, 40); // X, Y, Width, Height
        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ((username = usernameT.getText()) == null
                        || (password = passwordT.getText()) == null){
                    return;
                }

                instagram = Instagram4j.builder().username(username).password(password).build();
                instagram.setup();

                try{
                    if (instagram.login().getStatus().equalsIgnoreCase("fail")){
                        System.out.println("Incorrect username or password.");
                        passwordT.setText("");
                        return;
                    } else {
                        if (!Main.db.containsKey(usernameT.getText())
                        || !Main.db.containsValue(passwordT.getText())){
                            Main.db.put(usernameT.getText(), passwordT.getText());
                        }
                        Main.saveNewEntries(file);

                        runAdminGUI(frame);
                        usernameT.setText("");
                        passwordT.setText("");
                    }
                } catch (Exception e1){
                    System.out.println("Incorrect username or password.");
                    e1.printStackTrace();
                    System.exit(-1);
                }
            }
        });

        frame.add(usernameT);
        frame.add(passwordT);
        frame.add(login);

        frame.setSize(300, 300);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public void runAdminGUI(final JFrame frame){
        frame.setVisible(false);

        final JFrame admin = new JFrame();
        admin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        admin.setTitle("K Tool - Admin Panel");

        // SPAM MODE
        final JTextField spamTarget = new JTextField();
        spamTarget.setBounds(200, 123, 100, 27);
        spamTarget.setToolTipText("Target");

        final JTextField spamMessage = new JTextField();
        spamMessage.setBounds(200, 150, 100, 27);
        spamMessage.setToolTipText("Message Text");

        final JTextField spamNumber = new JTextField();
        spamNumber.setBounds(200, 177, 100, 27);
        spamNumber.setToolTipText("Number Of Messages");
        spamNumber.setText("4");

        // COMMENT MODE
        final JTextField commentTarget = new JTextField();
        commentTarget.setBounds(200, 123, 100, 27);
        commentTarget.setToolTipText("Target");
        commentTarget.setVisible(false);

        final JTextField commentMessage = new JTextField();
        commentMessage.setBounds(200, 150, 100, 27);
        commentMessage.setToolTipText("Comment Text");
        commentMessage.setVisible(false);

        final JTextField commentNumber = new JTextField();
        commentNumber.setBounds(200, 177, 100, 27);
        commentNumber.setToolTipText("Number Of Comments");
        commentNumber.setText("4");
        commentNumber.setVisible(false);

        final JButton commentMode = new JButton("Comment Mode");
        commentMode.setEnabled(true);
        commentMode.setBounds(125, 10, 250, 40);

        final JButton spamMode = new JButton("Spam Message Mode");
        spamMode.setEnabled(false);
        spamMode.setBounds(125, 60, 250, 40);

        JButton logout = new JButton("Logout");
        logout.setBounds(125, 210, 250, 40);
        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                admin.setVisible(false);
                frame.setVisible(true);
            }
        });

        final JButton sendSpam = new JButton("Send Spam Message");
        sendSpam.setBounds(125, 257, 250, 40);
        sendSpam.addActionListener(new ActionListener() {
            @SneakyThrows
            public void actionPerformed(ActionEvent e) {
                if (spamTarget.getText() == null
                || spamNumber.getText() == null
                || spamMessage.getText() == null){
                    return;
                }

                int spam = 0;
                try{
                    spam = Integer.parseInt(spamNumber.getText());
                } catch (Exception e1){
                    return;
                }

                List<String> recipients = new ArrayList<String>();
                InstagramSearchUsernameResult user = instagram.sendRequest(new InstagramSearchUsernameRequest(spamTarget.getText()));
                recipients.add(String.valueOf(user.getUser().getPk()));

                for (int i = 0; i < spam; i++){
                    if (!instagram.isLoggedIn()){
                        instagram = Instagram4j.builder().username(username).password(password).build();
                    }

                    if (instagram.sendRequest(InstagramDirectShareRequest.builder().recipients(recipients).shareType(InstagramDirectShareRequest.ShareType.MESSAGE).message(String.valueOf(spamMessage.getText())).build()).getStatus().equalsIgnoreCase("fail")){
                        System.out.println(" ");
                        System.out.println(" ");
                        System.out.println(" ");
                        System.out.println(" ");
                        System.out.println(" ");
                        System.out.println(" ");
                        System.out.println(" ");
                        System.out.println(" ");
                        System.out.println(" ");
                        System.out.println(" ");
                        System.out.println("CHANGING ACCOUNT DUE TO STATUS FAILURE");
                        System.out.println(" ");
                        System.out.println(" ");
                        System.out.println(" ");
                        System.out.println(" ");
                        System.out.println(" ");
                        System.out.println(" ");
                        System.out.println(" ");
                        System.out.println(" ");
                        System.out.println(" ");
                        System.out.println(" ");

                        String[] entry = Main.getRandomEntry(username, password).split(";");

                        username = entry[0];
                        password = entry[1];

                        instagram = Instagram4j.builder().username(username).password(password).build();
                        instagram.setup();

                        try{
                            instagram.login();
                            System.out.println("Account successfully switched...");
                        } catch (Exception e1){
                            System.out.println("Error while logging into new account...");
                        }
                    }
                }
            }
        });

        final JButton sendComment = new JButton("Send Comments");
        sendComment.setBounds(125, 257, 250, 40);
        sendComment.setVisible(false);
        sendComment.addActionListener(new ActionListener() {
            @SneakyThrows
            public void actionPerformed(ActionEvent e) {
                if (commentNumber.getText() == null
                        || commentTarget.getText() == null
                || commentMessage.getText() == null){
                    return;
                }

                int spam = 0;
                try{
                    spam = Integer.parseInt(commentNumber.getText());
                } catch (Exception e1){
                    return;
                }

                InstagramSearchUsernameResult user = instagram.sendRequest(new InstagramSearchUsernameRequest(commentTarget.getText()));
                InstagramFeedResult feed = instagram.sendRequest(new InstagramUserFeedRequest(user.getUser().getPk()));

                for (int i = 0; i < spam; i++){
                    for (InstagramFeedItem item : feed.getItems()){
                        if (!instagram.isLoggedIn()){
                            instagram = Instagram4j.builder().username(username).password(password).build();
                        }

                        if (instagram.sendRequest(new InstagramPostCommentRequest(item.getPk(), commentMessage.getText())).getStatus().equalsIgnoreCase("fail")){
                            System.out.println(" ");
                            System.out.println(" ");
                            System.out.println(" ");
                            System.out.println(" ");
                            System.out.println(" ");
                            System.out.println(" ");
                            System.out.println(" ");
                            System.out.println(" ");
                            System.out.println(" ");
                            System.out.println(" ");
                            System.out.println("CHANGING ACCOUNT DUE TO STATUS FAILURE");
                            System.out.println(" ");
                            System.out.println(" ");
                            System.out.println(" ");
                            System.out.println(" ");
                            System.out.println(" ");
                            System.out.println(" ");
                            System.out.println(" ");
                            System.out.println(" ");
                            System.out.println(" ");
                            System.out.println(" ");

                            String[] entry = Main.getRandomEntry(username, password).split(";");

                            username = entry[0];
                            password = entry[1];

                            instagram = Instagram4j.builder().username(username).password(password).build();
                            instagram.setup();

                            try{
                                instagram.login();
                                System.out.println("Account successfully switched...");
                            } catch (Exception e1){
                                System.out.println("Error while logging into new account...");
                            }
                        }
                    }
                }
            }
        });

        commentMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                spamMode.setEnabled(true);
                commentMode.setEnabled(false);
                sendSpam.setVisible(false);
                spamTarget.setVisible(false);
                spamMessage.setVisible(false);
                spamNumber.setVisible(false);
                commentNumber.setVisible(true);
                commentTarget.setVisible(true);
                sendComment.setVisible(true);
                commentMessage.setVisible(true);
            }
        });

        spamMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                commentMode.setEnabled(true);
                spamMode.setEnabled(false);
                sendSpam.setVisible(true);
                spamTarget.setVisible(true);
                spamMessage.setVisible(true);
                spamNumber.setVisible(true);
                commentNumber.setVisible(false);
                commentTarget.setVisible(false);
                sendComment.setVisible(false);
                commentMessage.setVisible(false);
            }
        });

        admin.add(logout);

        admin.add(sendSpam);
        admin.add(spamMode);
        admin.add(spamTarget);
        admin.add(spamNumber);
        admin.add(spamMessage);

        admin.add(commentMode);
        admin.add(commentNumber);
        admin.add(commentTarget);
        admin.add(commentMessage);
        admin.add(sendComment);

        admin.setSize(500, 500);
        admin.setLayout(null);
        admin.setVisible(true);
        admin.setResizable(false);
    }
}
