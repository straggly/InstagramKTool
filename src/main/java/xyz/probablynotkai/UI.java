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
    public Instagram4j instagram;
    public String username;
    public String password;

    public void runGUI(){
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("K Tool - Login");

        final JTextField usernameT = new JTextField();
        usernameT.setFont(Font.getFont(Font.SANS_SERIF));
        usernameT.setBounds(85, 75, 125, 27);

        final JPasswordField passwordT = new JPasswordField();
        passwordT.setBounds(85, 110, 125, 27);
        passwordT.setFont(Font.getFont(Font.SANS_SERIF));
        passwordT.setEchoChar('*');

        final JButton login = new JButton("Login");
        login.setFont(Font.getFont(Font.SANS_SERIF));
        login.setBounds(95, 150, 100, 40); // X, Y, Width, Height
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

    public boolean isSpammingMessage;
    private void runAdminGUI(final JFrame frame){
        frame.setVisible(false);

        final JFrame admin = new JFrame();
        admin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        admin.setTitle("K Tool - Admin Panel");

        // SPAM MODE
        final JTextField spamTarget = new JTextField();
        spamTarget.setBounds(200, 123, 100, 27);
        spamTarget.setFont(Font.getFont(Font.SANS_SERIF));
        spamTarget.setToolTipText("Target");

        final JTextField spamMessage = new JTextField();
        spamMessage.setBounds(200, 150, 100, 27);
        spamMessage.setFont(Font.getFont(Font.SANS_SERIF));
        spamMessage.setToolTipText("Message Text");

        final JTextField spamNumber = new JTextField();
        spamNumber.setBounds(200, 177, 100, 27);
        spamNumber.setToolTipText("Number Of Messages");
        spamNumber.setText("4");
        spamNumber.setFont(Font.getFont(Font.SANS_SERIF));

        // COMMENT MODE
        final JTextField commentTarget = new JTextField();
        commentTarget.setBounds(200, 123, 100, 27);
        commentTarget.setFont(Font.getFont(Font.SANS_SERIF));
        commentTarget.setToolTipText("Target");
        commentTarget.setVisible(false);

        final JTextField commentMessage = new JTextField();
        commentMessage.setBounds(200, 150, 100, 27);
        commentMessage.setFont(Font.getFont(Font.SANS_SERIF));
        commentMessage.setToolTipText("Comment Text");
        commentMessage.setVisible(false);

        final JTextField commentNumber = new JTextField();
        commentNumber.setBounds(200, 177, 100, 27);
        commentNumber.setToolTipText("Number Of Comments");
        commentNumber.setText("4");
        commentNumber.setFont(Font.getFont(Font.SANS_SERIF));
        commentNumber.setVisible(false);

        final JButton commentMode = new JButton("Comment Mode");
        commentMode.setEnabled(true);
        commentMode.setBounds(125, 10, 250, 40);
        commentMode.setFont(Font.getFont(Font.SANS_SERIF));

        final JButton spamMode = new JButton("Spam Message Mode");
        spamMode.setFont(Font.getFont(Font.SANS_SERIF));
        spamMode.setEnabled(false);
        spamMode.setBounds(125, 60, 250, 40);

        JButton logout = new JButton("Logout");
        logout.setFont(Font.getFont(Font.SANS_SERIF));
        logout.setBounds(125, 400, 250, 40);
        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                admin.setVisible(false);
                frame.setVisible(true);
            }
        });

        final JButton sendSpam = new JButton("Send Spam Message");
        sendSpam.setFont(Font.getFont(Font.SANS_SERIF));
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

                if (isSpammingMessage){
                    for (int i = 0; i < spam; i++) {
                        if (!instagram.isLoggedIn()) {
                            instagram = Instagram4j.builder().username(username).password(password).build();
                        }

                        if (instagram.sendRequest(InstagramDirectShareRequest.builder().recipients(recipients).shareType(InstagramDirectShareRequest.ShareType.MESSAGE).message(String.valueOf(spamMessage.getText())).build()).getStatus().equalsIgnoreCase("fail")) {
                            System.out.println(" ");
                            System.out.println("Sending message failed, retrying in: 3 minutes.");
                            System.out.println(" ");
                            Thread.sleep(180000);
                        }
                    }
                } else {
                    instagram.sendRequest(InstagramDirectShareRequest.builder().shareType(InstagramDirectShareRequest.ShareType.MESSAGE).recipients(recipients).message(spamMessage.getText()).build());
                }
            }
        });

        final JButton spamMessageToggle = new JButton("Toggle Spam: ON");
        spamMessageToggle.setFont(Font.getFont(Font.SANS_SERIF));
        isSpammingMessage = true;
        spamMessageToggle.setBounds(125, 210, 250, 40);
        spamMessageToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isSpammingMessage){
                    isSpammingMessage = false;
                    spamMessageToggle.setText("Toggle Spam: OFF");
                    spamNumber.setVisible(false);
                    spamMessageToggle.setBounds(125, 185, 250, 40);
                    sendSpam.setBounds(125, 230, 250, 40);
                    sendSpam.setText("Send Message");
                } else {
                    isSpammingMessage = true;
                    spamMessageToggle.setText("Toggle Spam: ON");
                    spamMessageToggle.setBounds(125, 210, 250, 40);
                    sendSpam.setBounds(125, 257, 250, 40);
                    spamNumber.setVisible(true);
                    sendSpam.setText("Send Spam Message");
                }
            }
        });

        final JButton sendComment = new JButton("Send Comments");
        sendComment.setBounds(125, 257, 250, 40);
        sendComment.setFont(Font.getFont(Font.SANS_SERIF));
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
                            System.out.println("Sending message failed, retrying in: 3 minutes.");
                            System.out.println(" ");
                            Thread.sleep(180000);
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
                spamMessageToggle.setVisible(false);
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
                spamMessageToggle.setVisible(true);
                commentNumber.setVisible(false);
                commentTarget.setVisible(false);
                sendComment.setVisible(false);
                commentMessage.setVisible(false);

                spamMessageToggle.setBounds(125, 210, 250, 40);
                sendSpam.setBounds(125, 257, 250, 40);
                spamNumber.setVisible(true);
            }
        });

        admin.add(logout);

        admin.add(sendSpam);
        admin.add(spamMode);
        admin.add(spamTarget);
        admin.add(spamNumber);
        admin.add(spamMessage);
        admin.add(spamMessageToggle);

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
